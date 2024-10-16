package experience.demo.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductDto {

    private Long id;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
    private String name;
    private int stock;
    private Long price;
}
