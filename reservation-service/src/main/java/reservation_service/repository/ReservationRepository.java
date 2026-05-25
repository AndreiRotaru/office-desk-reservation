package reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reservation_service.entity.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUser_IdAndReservationDate(Long userId, LocalDate date);

    boolean existsByWorkstation_IdAndReservationDate(UUID workstationId, LocalDate date);

    List<Reservation> findByUser_Id(Long userId);

    List<Reservation> findByReservationDate(LocalDate date);
}
