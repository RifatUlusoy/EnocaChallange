package experience.demo.Model;


import experience.demo.Dto.OrderDto;
import experience.demo.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

    private Long price;
    private String address;
    private OrderStatus orderStatus;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<Cart> carts;

    public OrderDto getOrderDto(){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(new Cart().getId());
        orderDto.setOrderStatus(orderStatus);
        orderDto.setPrice(price);
        orderDto.setAddress(address);
        orderDto.setEmail(customer.getEmail());
        return orderDto;
    }


}
