package org.ahroo.nexus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.ahroo.nexus.entity.common.WritableBaseEntity;

import java.util.Set;

@Entity
@Table(name = "categories", schema = "nexus")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Category extends WritableBaseEntity {

    public Category(String name) {
        this.name = name;
    }

    @Column(name = "name", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<User> users;
}
