package reservation_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reservation_service.entity.Reservation;
import reservation_service.entity.ReservationCancelledEvent;
import reservation_service.entity.User;
import reservation_service.entity.Workstation;
import reservation_service.exception.InvalidReservationDateException;
import reservation_service.exception.ReservationException;
import reservation_service.exception.UserCustomException;
import reservation_service.repository.ReservationRepository;
import reservation_service.repository.UserRepository;
import reservation_service.repository.WorkstationRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WorkstationRepository workstationRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Reservation createReservation(String username, UUID workstationId, LocalDate date) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserCustomException("User not found"));

        if (reservationRepository.existsByUser_IdAndReservationDate(user.getId(), date)) {
            throw new UserCustomException("User already has reservation for this day");
        }

        if (reservationRepository.existsByWorkstation_IdAndReservationDate(workstationId, date)) {
            throw new ReservationException("Workstation already reserved");
        }

        Workstation workstation = workstationRepository.findById(workstationId)
                .orElseThrow(() -> new ReservationException("Workstation not found"));

        Reservation reservation = Reservation.builder()
                .user(user)
                .workstation(workstation)
                .reservationDate(date)
                .status("ACTIVE")
                .build();

        UUID eventId = UUID.randomUUID();
        String convertFromUUIDToString = String.valueOf(eventId);

        System.out.println("A intrat in metoda de create Reservation");
        rabbitTemplate.convertAndSend(
                "reservation.exchange",
                "reservation.notifications",
                "Reservation has been created. The event id is " + convertFromUUIDToString);
        System.out.println("Dupa rabbit send");
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation cancelReservationService(Long reservationId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserCustomException("User not found"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new UserCustomException("You cannot cancel another user's reservation");
        }

        if (reservation.getStatus().equals("CANCELLED")) {
            throw new ReservationException("Reservation already cancelled");
        }
        System.out.println("LINIA 116");
        reservation.setStatus("CANCELLED");
        reservationRepository.save(reservation);
        System.out.println("LINIA 119");
        ReservationCancelledEvent event = ReservationCancelledEvent.builder()
                .username(user.getUsername())
                .message("Reservation cancelled")
                .reservationId(reservationId)
                .date(reservation.getReservationDate())
                .build();

        System.out.println("LINIA 127");

        rabbitTemplate.convertAndSend(
                "reservation.exchange",
                "reservation.notifications",
                "Reservation has been cancelled!");
        System.out.println("LINIA 133");
        return reservation;
    }

    public List<Reservation> getMyReservations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserCustomException("User not found!"));
        return reservationRepository.findByUser_Id(user.getId());
    }

    public List<Workstation> getAvailableWorkstations(LocalDate date) {
        if (date.isBefore(LocalDate.now())){
            throw new InvalidReservationDateException("Reservation date cannot be in the past!");
        }
        // toate rezervările din ziua respectivă
        List<UUID> reservedIds = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findByReservationDate(date);
        if (reservations.size() > 0) {
            // extragem ID-urile ocupate
            reservedIds = reservations.stream().map(r -> r.getWorkstation().getId()).toList();
        }

        // returnăm doar workstation-urile libere
        List<UUID> finalReservedIds = reservedIds;
        return workstationRepository.findAll().stream().filter(w -> !finalReservedIds.contains(w.getId())).toList();
    }
}