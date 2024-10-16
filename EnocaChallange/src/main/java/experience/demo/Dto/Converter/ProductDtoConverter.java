package experience.demo.Dto.Converter;

import experience.demo.Dto.ProductDto;
import experience.demo.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDtoConverter {

    public static ProductDto convertToProductDto(Product from){
        return ProductDto.builder()
                .id(from.getId())
                .creationTimestamp(from.getCreationTimestamp())
                .updateTimestamp(from.getUpdateTimestamp())
                .name(from.getName())
                .stock(from.getStock())
                .price(from.getPrice())
                .build();
    }
}
