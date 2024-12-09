package org.ahroo.nexus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.entity.common.WritableBaseEntity;

@Entity
@Table(name = "categories", schema = "nexus")
@Getter
@Setter
public class Category extends WritableBaseEntity {

    @Column(name = "category", unique = true, nullable = false)
    private String category;
}
