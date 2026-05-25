package reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reservation_service.entity.Workstation;
import java.util.UUID;
public interface WorkstationRepository extends JpaRepository<Workstation, UUID> {
}
