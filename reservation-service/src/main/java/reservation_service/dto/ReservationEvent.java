package reservation_service.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEvent {

    private String username;
    private String message;


}
