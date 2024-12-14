package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ahroo.nexus.entity.common.AuditableEntity;

import java.util.Set;

@Entity
@Table(name = "users", schema = "nexus")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends AuditableEntity {

    public User(Integer id) {
        setId(id);
    }

    public User(Integer id, String email, String password, boolean isDeleted, boolean isEnabled) {
        setId(id);
        super.setIsDeleted(isDeleted);
        this.email = email;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * One of business decisions is to keep email immutable. Other parameters can be updated.
     */
    @Column(name = "email", nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "description", nullable = false, length = 600)
    private String description;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = false;

    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_categories", schema = "nexus", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
}
