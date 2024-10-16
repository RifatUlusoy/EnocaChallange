package experience.demo.Repository;

import experience.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Boolean existsByName(String name);
    List<Product> findAllByNameContaining(String name);
}
