package org.ahroo.nexus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahroo.nexus.dto.notification.Notification;
import org.ahroo.nexus.dto.notification.NotificationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final KafkaTemplate<String, Notification> producer;

    private final ApplicationEventPublisher eventPublisher;

    public void send(Notification notification) {
        producer.sendDefault(notification)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Error producing notification: {}", notification, exception);
                    } else if (result != null) {
                        var metaData = result.getRecordMetadata();
                        log.trace("Produced to: {}-{}@{}. Notification: {}", metaData.topic(), metaData.partition(), metaData.offset(), notification);
                    }
                });
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", batch = "true", containerFactory = "notificationKafkaListenerContainerFactory")
    public void listen(
            @Payload List<Notification> notifications,
            @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {

        assert notifications.size() == topics.size();
        assert notifications.size() == partitions.size();
        assert notifications.size() == offsets.size();

        for (int index = 0; index < notifications.size(); index++) {

            var notification = notifications.get(index);
            var id = String.format("%s-%s@%s", topics.get(index), partitions.get(index), offsets.get(index));

            log.trace("Received from {} -> notification: {}", id, notification);

            // publish this notification
            NotificationEvent event = new NotificationEvent(this, id, notification);
            eventPublisher.publishEvent(event);
        }
    }

}
