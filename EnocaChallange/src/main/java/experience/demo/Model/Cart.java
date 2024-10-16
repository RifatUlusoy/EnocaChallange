package experience.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import experience.demo.Dto.CartDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{


    private Long price;
    private Long quantity;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public  CartDto getCartDto(){
        CartDto cartDto = new CartDto();
        cartDto.setId(new Cart().getId());
        cartDto.setProductId(product.getId());
        cartDto.setPrice(price);
        cartDto.setCustomerId(customer.getId());
        cartDto.setQuantity(quantity);
        cartDto.setCreationTimestamp(new Cart().getCreationTimestamp());
        cartDto.setUpdateTimestamp(new Cart().getUpdateTimestamp());
        return cartDto;
    }
}
