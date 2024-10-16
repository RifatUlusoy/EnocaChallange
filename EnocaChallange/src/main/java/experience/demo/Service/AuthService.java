package experience.demo.Service;


import experience.demo.Dto.Converter.CustomerDtoConverter;
import experience.demo.Dto.CustomerDto;
import experience.demo.Dto.LoginRequest;
import experience.demo.Dto.SingUpRequest;
import experience.demo.Dto.TokenResponseDto;
import experience.demo.Model.Customer;
import experience.demo.Model.Order;
import experience.demo.OrderStatus;
import experience.demo.Repository.OrderRepository;
import experience.demo.UserRole;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerService customerService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomerDtoConverter dtoConverter;
    private final OrderRepository orderRepository;
    @Transactional
    public CustomerDto singUp(SingUpRequest singUpRequest) {
        var isAlreadyExists = customerService.existsByEmail(singUpRequest.getEmail());

        if (isAlreadyExists) {
            throw new EntityExistsException("Is already exists");
        }
        var customer = Customer.builder()
                .email(singUpRequest.getEmail())
                .userRole(UserRole.USER)
                .password(bCryptPasswordEncoder.encode(singUpRequest.getPassword()))
                .build();
        Order order = new Order();
        order.setPrice(0L);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        return dtoConverter.convertToCustomerDto(customerService.saveRepository(customer));

    }

    public TokenResponseDto login(LoginRequest loginRequest) {

        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (auth.isAuthenticated()) {
            return TokenResponseDto
                    .builder()
                    .accessToken(jwtService.generateToken(loginRequest.getEmail()))
                    .build();
        } else {
            throw new UsernameNotFoundException("user not found");
        }


    }
}