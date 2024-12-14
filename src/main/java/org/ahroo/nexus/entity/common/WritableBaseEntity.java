package org.ahroo.nexus.entity.common;

import java.io.Serializable;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects
 * needing this property: <a href="https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java">source</a>
 * <p>
 * Makes writing to id public.
 */
public abstract class WritableBaseEntity extends BaseEntity implements Serializable {

    public void setId(Integer id) {
        super.setId(id);
    }

}
