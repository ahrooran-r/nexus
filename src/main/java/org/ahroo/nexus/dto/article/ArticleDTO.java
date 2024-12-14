package org.ahroo.nexus.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.ahroo.nexus.dto.common.AuditableDTO;
import org.ahroo.nexus.entity.Article;
import org.ahroo.nexus.entity.Category;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
@Setter
public class ArticleDTO extends AuditableDTO {

    @NotBlank(message = "title required")
    private String title;

    @NotBlank(message = "summary required")
    private String summary;

    @NotBlank(message = "content required")
    private String content;

    @NotNull(message = "at least one categories must be selected")
    @Size(min = 1, max = 5, message = "more categories than allowed")
    private Set<@NotBlank(message = "invalid categories") String> categories;

    public Article toArticle() {
        var article = new Article();
        article.setTitle(this.getTitle());
        article.setContent(this.getContent());
        article.setSummary(this.getSummary());
        article.setId(this.getId());

        var categoryEntities = categories.stream()
                .map(_category -> {
                    var category = new Category();
                    category.setName(_category);
                    return category;
                })
                .collect(Collectors.toSet());
        article.setCategories(categoryEntities);

        return article;
    }

    public static ArticleDTO fromArticle(Article article) {
        var dto = new ArticleDTO();
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setSummary(article.getSummary());
        dto.setCreatedOn(article.getCreatedOn());
        dto.setLastUpdatedOn(article.getLastUpdatedOn());
        dto.setId(article.getId());

        var categories = article.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toCollection(TreeSet::new));
        dto.setCategories(categories);

        if (null != article.getIsDeleted() && article.getIsDeleted()) {
            dto.setIsDeleted(true);
        }

        return dto;
    }
}
