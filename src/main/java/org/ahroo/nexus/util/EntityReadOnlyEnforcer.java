package org.ahroo.nexus.util;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

/**
 * Enforces Read-Only behaviour in entities: <a href="https://stackoverflow.com/a/32207163/10582056">source</a>
 */
public class EntityReadOnlyEnforcer {

    @PrePersist
    public void onPrePersist(Object o) {
        throw new IllegalStateException("JPA is trying to persist an entity of type " + (o == null ? "null" : o.getClass()));
    }

    @PreUpdate
    public void onPreUpdate(Object o) {
        throw new IllegalStateException("JPA is trying to update an entity of type " + (o == null ? "null" : o.getClass()));
    }

    @PreRemove
    public void onPreRemove(Object o) {
        throw new IllegalStateException("JPA is trying to remove an entity of type " + (o == null ? "null" : o.getClass()));
    }
}
