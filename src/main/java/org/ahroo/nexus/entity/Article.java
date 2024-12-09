package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.configuration.entity.ReadOnlyEnforcer;
import org.ahroo.nexus.entity.common.AuditableEntity;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@EntityListeners(ReadOnlyEnforcer.class)
@Table(name = "articles", schema = "nexus")
@Getter
@Setter
public class Article extends AuditableEntity {

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "summary", length = 350)
    private String summary;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "articles_categories", schema = "nexus", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @OrderBy("category ASC")
    private Set<Category> categories = new LinkedHashSet<>();
}
