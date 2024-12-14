package org.ahroo.nexus.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.ahroo.nexus.entity.Category;
import org.intellij.lang.annotations.Language;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Transactional
@Slf4j
public class CategoryRepository {

    @PersistenceContext
    private final EntityManager em;

    public CategoryRepository(EntityManager em) {
        this.em = em;
    }

    public Category persist(Category category) {
        var existingCategory = findByName(category.getName());
        if (null != existingCategory) {
            return existingCategory;
        }

        em.persist(category);
        return category;
    }

    public Set<Category> persistAll(Set<Category> categories) {
        return categories.stream().map(this::persist).collect(Collectors.toSet());
    }

    public Category findByName(String category) {
        @Language("HQL") final String queryString = "select c from Category c where c.name = :category";
        var query = em.createQuery(queryString, Category.class);
        query.setParameter("category", category);

        var categories = query.getResultList();
        if (categories.isEmpty()) return null;
        else if (categories.size() > 1) throw new IllegalStateException("More than one categories found");
        else return categories.getFirst();
    }

    /**
     * Returns set of category names for this user.
     */
    public Set<String> findNameByUserId(Integer userId) {
        @Language("HQL") final String queryString = "select distinct c.name from Category c join c.users uc where uc.id=:userId";
        var query = em.createQuery(queryString, String.class);
        query.setParameter("userId", userId);

        //noinspection UnnecessaryLocalVariable
        var categories = query.getResultStream().collect(Collectors.toSet());
        return categories;
    }

    /**
     * Categories are immutable. We can only add them.
     * Although User and Article can mutate their respective relationships via intermediate tables.
     * This method finds categories that are neither mapped to user nor article and removes them
     */
    public int vacuum() {
        @Language("HQL") final String queryString = """
                delete from Category c where
                c.id not in (select uc.id from User u join u.categories uc)
                and c.id not in (select ac.id from Article a join a.categories ac)""";

        var query = em.createQuery(queryString);
        int deletedRecords = query.executeUpdate();

        if (deletedRecords > 0) {
            log.info("Removed {} records during vacuuming", deletedRecords);
        }

        return deletedRecords;
    }

}

