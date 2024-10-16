package experience.demo.Service;



import experience.demo.Dto.*;
import experience.demo.Dto.Converter.CustomerDtoConverter;
import experience.demo.Dto.Converter.ProductDtoConverter;
import experience.demo.Model.Cart;
import experience.demo.Model.Customer;
import experience.demo.Model.Order;
import experience.demo.Model.Product;
import experience.demo.OrderStatus;
import experience.demo.Repository.CartRepository;
import experience.demo.Repository.CustomerRepository;

import experience.demo.Repository.OrderRepository;
import experience.demo.Repository.ProductRepository;
import experience.demo.UserRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService  {

    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter converter;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @PostConstruct
    public void createAdminAccount(){
        Customer adminAccount = customerRepository.findByUserRole(UserRole.ADMIN);
        if(adminAccount == null){
            Customer customer = new Customer();
            customer.setUserRole(UserRole.ADMIN);
            customer.setEmail("admin@test.com");
            customer.setPassword(new BCryptPasswordEncoder().encode("admin123"));
            customerRepository.save(customer);

        }
    }
    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDtoConverter::convertToProductDto).collect(Collectors.toList());
    }
    public List<ProductDto> getProduct(String name){
        List<Product> product = productRepository.findAllByNameContaining(name);
        return product.stream().map(ProductDtoConverter::convertToProductDto).collect(Collectors.toList());
    }
    public ResponseEntity<?> addProductToCart(AddProductInCardDto addProductInCardDto){
        Order pendingOrder2 = orderRepository.findByCustomerIdAndOrderStatus(addProductInCardDto.getCustomerId(), OrderStatus.PENDING);
        Optional<Cart> cart = cartRepository.findByCustomerIdAndProductIdAndOrderId(addProductInCardDto.getCustomerId(),
                addProductInCardDto.getProductId(),pendingOrder2.getId() );

        if(cart.isPresent()){
            CartDto existsInCart = new CartDto();
            existsInCart.setProductId(null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existsInCart);
        }else{
            Optional<Product> product = productRepository.findById(addProductInCardDto.getProductId());
            Optional<Customer> customer = customerRepository.findById(addProductInCardDto.getCustomerId());
            if(product.isPresent() && customer.isPresent()){

                Cart cart1 = new Cart();
                cart1.setProduct(product.get());
                cart1.setCustomer(customer.get());
                cart1.setQuantity(1L);
                cart1.setOrder(pendingOrder2);
                cart1.setPrice(product.get().getPrice());
                Cart updatedCart = cartRepository.save(cart1);
                pendingOrder2.setPrice(pendingOrder2.getPrice()+ cart1.getPrice());
                pendingOrder2.getCarts().add(cart1);
                orderRepository.save(pendingOrder2);
                CartDto updatedCartDto = new CartDto();
                updatedCartDto.setId(updatedCart.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(cart1);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer or product not found");
            }
        }
    }
    public OrderDto removeProductFromCart(Long customerId, Long productId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        Optional<Product> product = productRepository.findById(productId);
        Optional<Cart> cart = cartRepository.findByCustomerIdAndProductIdAndOrderId(customerId,productId,pendingOrder.getId());
        if(cart.isPresent() && product.isPresent()){
            Cart cart1 = cart.get();
            cart1.setQuantity(cart.get().getQuantity()-1);
            pendingOrder.setPrice(pendingOrder.getPrice()+product.get().getPrice());
            cartRepository.save(cart1);
            orderRepository.save(pendingOrder);
            return pendingOrder.getOrderDto();
        }
        return null;
    }
    public OrderDto addProductCart(Long customerId, Long productId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        Optional<Product> product = productRepository.findById(productId);
        Optional<Cart> cart = cartRepository.findByCustomerIdAndProductIdAndOrderId(customerId,productId,pendingOrder.getId());
        if(cart.isPresent() && product.isPresent()){
            Cart cart1 = cart.get();
            cart1.setQuantity(cart.get().getQuantity()+1);
            pendingOrder.setPrice(pendingOrder.getPrice()+product.get().getPrice());
            cartRepository.save(cart1);
            orderRepository.save(pendingOrder);
            return pendingOrder.getOrderDto();
        }
        return null;
    }
    public OrderDto getCartByCustomerId(Long customerId){
        Order pendingOrder = orderRepository.findByCustomerIdAndOrderStatus(customerId,OrderStatus.PENDING);
        List<CartDto> cartDtoList = pendingOrder.getCarts().stream().map(Cart::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setCartDtoList(cartDtoList);
        orderDto.setPrice(pendingOrder.getPrice());
        orderDto.setId(pendingOrder.getId());
        orderDto.setOrderStatus(pendingOrder.getOrderStatus());
        return orderDto;
    }
    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order existingOrder = orderRepository.findByCustomerIdAndOrderStatus(placeOrderDto.getCustomerId(), OrderStatus.PENDING);
        Optional<Customer> user = customerRepository.findById(placeOrderDto.getCustomerId());
        if(user.isPresent()){
            existingOrder.setOrderStatus(OrderStatus.SUBMITTED);
            existingOrder.setAddress(placeOrderDto.getAddress());
            existingOrder.setCreationTimestamp(new Order().getCreationTimestamp());
            existingOrder.setPrice(existingOrder.getPrice());

            orderRepository.save(existingOrder);
            Order order = new Order();
            order.setOrderStatus(OrderStatus.SUBMITTED);
            order.setCustomer(user.get());
            order.setPrice(0L);
            orderRepository.save(order);
            return order.getOrderDto();
        }
        return null;
    }
    public List<OrderDto> getOrderForCode(Long customerId){
        return orderRepository.findAllByCustomerIdAndOrderStatus(customerId,OrderStatus.SUBMITTED).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }
    public Customer saveRepository(Customer customer){
        return customerRepository.save(customer);
    }
    public Boolean existsByEmail(String email){
        return customerRepository.existsByEmail(email);
    }


    public Customer findCustomerByEmail(String email){
        return customerRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
    public CustomerDto getCustomerDtoByEmail(String email){
       return converter.convertToCustomerDto(findCustomerByEmail(email));
    }
    public Customer getCustomerInTokenContext(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findCustomerByEmail(userDetails.getUsername());
    }
}
