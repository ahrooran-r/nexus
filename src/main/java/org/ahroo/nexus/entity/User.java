package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.configuration.entity.ReadOnlyEnforcer;
import org.ahroo.nexus.entity.common.AuditableEntity;

@Entity
@EntityListeners(ReadOnlyEnforcer.class)
@Table(name = "users", schema = "nexus")
@Getter
@Setter
public class User extends AuditableEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "description", nullable = false, length = 600)
    private String description;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;
}
