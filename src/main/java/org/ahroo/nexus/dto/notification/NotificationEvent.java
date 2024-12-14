package org.ahroo.nexus.dto.notification;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final Notification notification;

    private final String id;

    public NotificationEvent(Object source, String id, Notification notification) {
        super(source);
        this.notification = notification;
        this.id = id;
    }
}
