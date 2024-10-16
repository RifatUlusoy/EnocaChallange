package experience.demo.Service;


import experience.demo.Dto.Converter.ProductDtoConverter;
import experience.demo.Dto.OrderDto;
import experience.demo.Dto.ProductDto;
import experience.demo.Dto.Request.CreateProductRequest;
import experience.demo.Dto.Request.UpdateProductRequest;
import experience.demo.Model.Order;
import experience.demo.Model.Product;
import experience.demo.OrderStatus;
import experience.demo.Repository.OrderRepository;
import experience.demo.Repository.ProductRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    public ProductDto createProduct(CreateProductRequest createProductRequest){
        var isAlreadyExists = productRepository.existsByName(createProductRequest.getName());
        if(isAlreadyExists){
            throw new EntityExistsException(createProductRequest.getName()+" is already exists");
        }
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setStock(createProductRequest.getStock());

        return ProductDtoConverter.convertToProductDto(productRepository.save(product));
    }
    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductDtoConverter::convertToProductDto).collect(Collectors.toList());
    }
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));
        productRepository.delete(product);
    }
    public ProductDto updateProduct(Long id, UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        product.setStock(updateProductRequest.getStock());

        return ProductDtoConverter.convertToProductDto(productRepository.save(product));
    }
    public List<OrderDto> GetAllOrdersForCustomer(){
        return orderRepository.findAllByOrderStatus(OrderStatus.SUBMITTED).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }


}
