package reservation_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reservation_service.entity.Reservation;
import reservation_service.entity.Workstation;
import reservation_service.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public Reservation createReservation(@RequestParam UUID workstationId, @RequestParam LocalDate date, Authentication authentication) {
        String username = authentication.getName();
        return reservationService.createReservation(username, workstationId, date);
    }

    @GetMapping("/myReservation")
    public List<Reservation> myReservations(Authentication authentication) {
        System.out.println("Username din authentication " + authentication.getName());
        return reservationService.getMyReservations(authentication.getName());
    }

    @DeleteMapping("/cancelReservation/{id}")
    public Reservation cancelReservation(@PathVariable Long id, Authentication authentication) {
        return reservationService.cancelReservationService(id, authentication.getName());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Workstation>> getAvailable(@RequestParam LocalDate date) {
        return ResponseEntity.ok(reservationService.getAvailableWorkstations(date));
    }

}
