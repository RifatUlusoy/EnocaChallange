package experience.demo.Model;


import experience.demo.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@ToString
@Table(name="customers")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Column(unique = true)
    private String email;
    private String password;
    private UserRole userRole;

}
