package reservation_service.entity;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public class ReservationCancelledEvent {
    private String username;
    private String message;
    private Long reservationId;
    private LocalDate date;
}
