package notification_service.consumer;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import notification_service.dto.ReservationEvent;
import notification_service.entity.Notification;
import notification_service.entity.ProcessedEvent;
import notification_service.repository.NotificationRepository;
import notification_service.repository.ProcessedEventRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationConsumer {
    private final NotificationRepository notificationRepository;

    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    @RabbitListener(queues = "reservation.notifications")
    public void consume(String message, String eventId) {
        if (processedEventRepository.existsById(eventId)) {
            return;
        } else {
            ProcessedEvent processedEvent = new ProcessedEvent(eventId, LocalDateTime.now());
            processedEventRepository.save(processedEvent);
        }
        System.out.println("Inainte de SAVE");
        Notification notification = Notification.builder()
                .username("Andrei")
                .message("The event has been processed!")
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        System.out.println(message);
    }


}
