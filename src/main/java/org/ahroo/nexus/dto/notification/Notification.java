package org.ahroo.nexus.dto.notification;

import lombok.Builder;
import org.ahroo.nexus.controller.ArticleController;
import org.ahroo.nexus.entity.Article;
import org.ahroo.nexus.entity.Category;
import org.ahroo.nexus.util.ControllerUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record Notification(
        String author,
        String title,
        String link,
        Set<String> categories,
        LocalDateTime lastUpdated,
        ArticleAction action
) {

    public enum ArticleAction {
        PUBLISHED, UPDATED
    }

    public static Notification fromArticle(Article article, ArticleAction action) {
        return Notification.builder()
                .action(action)
                .author(article.getUser().getName())
                .title(article.getTitle())
                .categories(article.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                .lastUpdated(article.getLastUpdatedOn())
                .link(ControllerUtils.getActionUrl(ArticleController.class, "/" + article.getId()))
                .build();
    }

}
