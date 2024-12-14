package org.ahroo.nexus.entity.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property: <a href="https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java">source</a>
 * <p>
 * This is readable only.
 */
@MappedSuperclass
@Getter
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    protected Integer id;

    public boolean isNew() {
        return this.id == null;
    }
}
