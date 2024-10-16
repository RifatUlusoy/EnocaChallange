package experience.demo.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class CartDto {

    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private Long price;
    private Long quantity;
    private Long productId;
    private Long customerId;
    private Long orderId;
}
