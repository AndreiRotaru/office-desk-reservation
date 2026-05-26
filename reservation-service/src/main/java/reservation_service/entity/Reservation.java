package reservation_service.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservations", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "reservationDate"}),
                             @UniqueConstraint(columnNames = {"workstation_id", "reservationDate"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "workstation_id", nullable = false)
    private Workstation workstation;

    @Column(nullable = false)
    private LocalDate reservationDate;

    private String status; // ACTIVE, CANCELLED
}