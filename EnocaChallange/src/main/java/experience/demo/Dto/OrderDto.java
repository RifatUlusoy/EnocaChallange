package experience.demo.Dto;

import experience.demo.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {

    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private Long price;
    private String address;
    private String email;
    private OrderStatus orderStatus;
    private List<CartDto> cartDtoList;
}
