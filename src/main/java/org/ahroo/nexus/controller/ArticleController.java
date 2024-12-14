package org.ahroo.nexus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.dto.article.ArticleDTO;
import org.ahroo.nexus.dto.article.ArticlePage;
import org.ahroo.nexus.service.ArticleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> post(
            @RequestBody @Valid final ArticleDTO request
    ) {
        var article = articleService.create(request);
        var articleDTO = ArticleDTO.fromArticle(article);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(articleDTO.getId()).toUri();

        return ResponseEntity
                .created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .body(articleDTO);
    }

    @PutMapping("/{article-id}")
    public ResponseEntity<ArticleDTO> put(
            @PathVariable("article-id") final Integer articleId,
            @RequestBody final ArticleDTO deltaRequest
    ) {
        var article = articleService.update(articleId, deltaRequest);
        var articleDTO = ArticleDTO.fromArticle(article);
        return ResponseEntity.ok(articleDTO);
    }

    @DeleteMapping("/{article-id}")
    public ResponseEntity<ArticleDTO> softDelete(
            @PathVariable("article-id") final Integer articleId
    ) {
        var article = articleService.softDelete(articleId);
        var articleDTO = ArticleDTO.fromArticle(article);
        return ResponseEntity.ok(articleDTO);
    }

    @GetMapping("/{article-id}")
    public ResponseEntity<ArticleDTO> findById(
            @PathVariable("article-id") final Integer articleId
    ) {
        var article = articleService.findById(articleId);
        var articleDTO = ArticleDTO.fromArticle(article);
        return ResponseEntity.ok(articleDTO);
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<ArticlePage> findAllByAuthor(
            @PathVariable("user-id") final Integer userId,
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "5") final Integer size

    ) {
        var articles = articleService.findAllByAuthor(userId, page, size);
        var articlePage = ArticlePage.fromArticles(articles);
        return ResponseEntity.ok(articlePage);
    }

}
