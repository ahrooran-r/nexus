package org.ahroo.nexus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import org.ahroo.nexus.util.EntityReadOnlyEnforcer;
import org.ahroo.nexus.entity.common.BaseEntity;

/**
 * This entity is read only. Only updated at DB level.
 */
@Entity
@EntityListeners(EntityReadOnlyEnforcer.class)
@Table(name = "countries", schema = "nexus")
@Getter
public class Country extends BaseEntity {

    /**
     * No need for additional params since this is read-only
     */
    @Column(name = "name")
    private String name;
}
