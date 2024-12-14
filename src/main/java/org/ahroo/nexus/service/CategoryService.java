package org.ahroo.nexus.service;

import lombok.AllArgsConstructor;
import org.ahroo.nexus.entity.Category;
import org.ahroo.nexus.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.persist(category);
    }

    public Set<Category> saveAll(Set<Category> categories) {
        return categoryRepository.persistAll(categories);
    }

    public int vacuum() {
        return categoryRepository.vacuum();
    }

    public Set<String> getSubscribedCategoriesForUser(Integer userId) {
        return categoryRepository.findNameByUserId(userId);
    }

}
