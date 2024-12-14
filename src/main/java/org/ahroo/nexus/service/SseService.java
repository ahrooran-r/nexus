package org.ahroo.nexus.service;

import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.dto.user.Principal;
import org.ahroo.nexus.dto.notification.Notification;
import org.ahroo.nexus.dto.notification.NotificationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://lorenzomiscoli.com/server-sent-events-in-spring-boot/">source</a>
 */
@Component
@RequiredArgsConstructor
public class SseService {

    // TODO: Possible bug -> What happens if User is soft deleted?
    private final Map<Principal, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final AuthenticationService authenticationService;

    private final CategoryService categoryService;

    public SseEmitter newEmitter() {

        Principal principal = authenticationService.getPrincipal();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(principal, emitter);

        emitter.onCompletion(() -> emitters.remove(principal));
        emitter.onTimeout(() -> emitters.remove(principal));
        emitter.onError((e) -> emitters.remove(principal));

        return emitter;
    }

    public void send(Principal principal, SseEmitter.SseEventBuilder event) {

        SseEmitter emitter = emitters.get(principal);
        if (emitter == null) {
            throw new IllegalStateException("No SseEmitter found for user " + principal.getEmail());
        }

        try {
            emitter.send(event);
        } catch (IOException ioException) {
            emitter.completeWithError(ioException);
            emitters.remove(principal);
        }
    }

    public boolean isUserEligibleForNotification(Principal principal, Notification notification) {
        Set<String> subscriptions = categoryService.getSubscribedCategoriesForUser(principal.getId());
        Set<String> provided = notification.categories();

        return provided.stream().anyMatch(subscriptions::contains);
    }

    @EventListener
    public void onNotification(NotificationEvent notificationEvent) {
        Notification notification = notificationEvent.getNotification();

        emitters.forEach((user, emitter) -> {
            boolean eligible = isUserEligibleForNotification(user, notification);
            if (eligible) {
                SseEmitter.SseEventBuilder builder = SseEmitter.event();
                builder.id(notificationEvent.getId());
                builder.data(notification, MediaType.APPLICATION_JSON);
                send(user, builder);
            }
        });
    }

    /**
     * Ping every 5 seconds
     */
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void emitPing() {
        emitters.forEach((user, emitter) -> {
            SseEmitter.SseEventBuilder builder = SseEmitter.event();
            builder.id("PING");
            builder.data(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            send(user, builder);
        });
    }
}
