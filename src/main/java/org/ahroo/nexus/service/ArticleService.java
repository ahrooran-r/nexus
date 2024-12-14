package org.ahroo.nexus.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahroo.nexus.dto.article.ArticleDTO;
import org.ahroo.nexus.dto.notification.Notification;
import org.ahroo.nexus.entity.Article;
import org.ahroo.nexus.entity.User;
import org.ahroo.nexus.exception.ArticleNotFoundException;
import org.ahroo.nexus.exception.common.IllegalRequestException;
import org.ahroo.nexus.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ArticleService {

    private final CategoryService categoryService;

    private final ArticleRepository articleRepository;

    private final AuthenticationService authenticationService;

    private final NotificationService notificationService;

    public Article create(ArticleDTO articleDTO) {

        // 1. check valid user
        var principal = authenticationService.getPrincipal();

        var article = articleDTO.toArticle();

        // 2. try to save all categories
        var categories = article.getCategories();
        categories = categoryService.saveAll(categories);

        // 3. try to save article
        article.setUser(principal.user());
        article.setCategories(categories);

        article = articleRepository.save(article);

        // 4. send notification to kafka
        Notification notification = Notification.fromArticle(article, Notification.ArticleAction.PUBLISHED);
        notificationService.send(notification);

        return article;
    }

    public Article update(Integer articleId, ArticleDTO articleDTO) {

        // 1. check valid article
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        // 2. check valid user
        var principal = authenticationService.getPrincipal();
        if (!principal.getId().equals(article.getUser().getId())) {
            // user should be the owner of Article
            throw new IllegalRequestException(principal);
        }

        if (article.getIsDeleted()) {
            throw new ArticleNotFoundException(articleId);
        }

        // 3. save categories
        var newCategories = articleDTO.toArticle().getCategories();
        newCategories = categoryService.saveAll(newCategories);

        // 4. check for parameter updates -> only delta is received. So we have to check fields one by one
        if (null != articleDTO.getTitle()) article.setTitle(articleDTO.getTitle());
        if (null != articleDTO.getContent()) article.setContent(articleDTO.getContent());
        if (null != articleDTO.getSummary()) article.setSummary(articleDTO.getSummary());
        article.setCategories(newCategories);

        article = articleRepository.save(article);

        // 4. send notification to kafka
        Notification notification = Notification.fromArticle(article, Notification.ArticleAction.UPDATED);
        notificationService.send(notification);

        return article;
    }

    public Article softDelete(Integer articleId) {

        // 1. check valid Article
        Article article = articleRepository.headById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        // 2. check valid user
        var principal = authenticationService.getPrincipal();
        if (!principal.getId().equals(article.getUser().getId())) {
            // user should be the owner of Article
            throw new IllegalRequestException(principal);
        }

        if (article.getIsDeleted()) {
            throw new ArticleNotFoundException(articleId);
        }

        article.setIsDeleted(true);
        articleRepository.softDelete(article.getId());

        return article;
    }

    public Article findById(Integer articleId) {

        // 1. check valid Article
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));
        boolean isDeleted = null == article.getIsDeleted() || article.getIsDeleted();

        if (isDeleted) {
            // only owner can see deleted article
            throw new ArticleNotFoundException(articleId);
        }

        return article;
    }

    public Page<Article> findAllByAuthor(Integer userId, Integer page, Integer size) {
        User author = new User(userId);

        Sort orderBy = Sort.by("lastUpdatedOn").descending();
        Pageable pageable = PageRequest.of(page, size, orderBy);
        //noinspection UnnecessaryLocalVariable
        var articles = articleRepository.findAllByUserAndIsDeletedFalse(author, pageable);
        return articles;
    }
}
