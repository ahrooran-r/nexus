package org.ahroo.nexus.repository;

import org.ahroo.nexus.entity.Article;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.repository.common.SoftDeleteAwareJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleRepository extends SoftDeleteAwareJpaRepository<Article, Integer> {

    /**
     * Ideally article content would be huge.
     * So this query just returns basic fields for verification and pre-processing.
     */
    @Query(value = "select new Article(a.id, u.id, a.isDeleted) from Article a join fetch User u on a.user.id = u.id where a.id =:articleId")
    Optional<Article> headById(Integer articleId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "Article.eagerFetch.categories")
    Page<Article> findAllByUserAndIsDeletedFalse(User user, Pageable pageable);

    // TODO: THis query is not optimized for handling large volumes
    @Query(value = "select a from Article a join a.categories ac where ac.name=:categoryName")
    Page<Article> findAllByCategoryName(String categoryName, Pageable pageable);
}
