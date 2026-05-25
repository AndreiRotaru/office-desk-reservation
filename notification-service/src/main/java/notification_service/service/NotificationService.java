package notification_service.service;

import lombok.RequiredArgsConstructor;
import notification_service.entity.Notification;
import notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    public List<Notification> getMyNotifications(String username) {
        return notificationRepository.findByUsername(username);
    }
}
