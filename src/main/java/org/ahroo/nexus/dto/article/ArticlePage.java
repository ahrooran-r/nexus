package org.ahroo.nexus.dto.article;

import lombok.Builder;
import org.ahroo.nexus.entity.Article;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record ArticlePage(
        int page,
        int totalPages,
        int numFound,
        List<ArticleDTO> articleDTOs
) {

    public static ArticlePage fromArticles(Page<Article> page) {
        var articleDTOs = page.map(ArticleDTO::fromArticle).stream().toList();
        return ArticlePage.builder()
                .page(page.getNumber())
                .totalPages(page.getTotalPages())
                .numFound(page.getNumberOfElements())
                .articleDTOs(articleDTOs)
                .build();
    }

}
