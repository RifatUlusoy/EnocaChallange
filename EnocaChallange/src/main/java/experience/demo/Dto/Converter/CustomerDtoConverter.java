package experience.demo.Dto.Converter;

import experience.demo.Dto.CustomerDto;
import experience.demo.Model.Customer;
import experience.demo.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerDtoConverter {

    public CustomerDto convertToCustomerDto(Customer from){

        return CustomerDto.builder()
                .id(from.getId())
                .creationDateTime(from.getCreationTimestamp())
                .updateDateTime(from.getUpdateTimestamp())
                .email(from.getEmail())
                .password(from.getPassword())
                .userRole(UserRole.USER)
                .build();
    }
}
