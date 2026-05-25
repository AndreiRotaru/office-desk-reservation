package reservation_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "workstations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workstation {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false)
    private String code;
    private String location;
    private Boolean active;


}
