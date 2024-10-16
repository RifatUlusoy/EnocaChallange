package experience.demo.Repository;

import experience.demo.Model.Order;
import experience.demo.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


    Order findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
}
