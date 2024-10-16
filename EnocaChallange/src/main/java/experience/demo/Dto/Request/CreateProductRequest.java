package experience.demo.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateProductRequest {

    private String name;
    private int stock;
    private Long price;
}