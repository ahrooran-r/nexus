package org.ahroo.nexus.entity.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property: <a href="https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java">source</a>
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AuditableEntity extends WritableBaseEntity implements Serializable {

    @Column(name = "created_on", nullable = false, updatable = false)
    protected LocalDateTime createdOn;

    @Column(name = "last_updated_on", nullable = false)
    protected LocalDateTime lastUpdatedOn;

    @Column(name = "is_deleted", nullable = false)
    protected Boolean isDeleted = false;

    @PrePersist
    public void create() {
        createdOn = LocalDateTime.now();
        lastUpdatedOn = LocalDateTime.now();
    }

    @PreUpdate
    public void update() {
        lastUpdatedOn = LocalDateTime.now();
    }

    @PreRemove
    public void remove() {
        isDeleted = true;
    }
}
