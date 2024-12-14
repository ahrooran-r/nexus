package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ahroo.nexus.entity.common.AuditableEntity;

import java.util.Set;

@Entity
@Table(name = "articles", schema = "nexus")
@NamedEntityGraph(name = "Article.eagerFetch.categories", attributeNodes = {
        @NamedAttributeNode("categories")
})
@Getter
@Setter
@NoArgsConstructor
public class Article extends AuditableEntity {

    public Article(Integer id, Integer userId, Boolean isDeleted) {
        setId(id);
        if (null != userId) {
            User user = new User(userId);
            setUser(user);
        }
        setIsDeleted(isDeleted);
    }

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "summary", length = 350)
    private String summary;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * Only few categories can be added to Article.
     * This is a business limitation. So no problem to eager fetch.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "articles_categories", schema = "nexus", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;
}
