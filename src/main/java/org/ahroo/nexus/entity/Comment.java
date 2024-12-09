package org.ahroo.nexus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.configuration.entity.ReadOnlyEnforcer;
import org.ahroo.nexus.entity.common.AuditableEntity;

import java.util.List;

@Entity
@EntityListeners(ReadOnlyEnforcer.class)
@Table(name = "comments", schema = "nexus")
@Getter
@Setter
public class Comment extends AuditableEntity {

    @Column(name = "comment", nullable = false, length = 100)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("createdOn asc")
    private List<Comment> children;

}
