package org.ahroo.nexus.exception;

import lombok.Getter;
import org.ahroo.nexus.entity.Article;
import org.ahroo.nexus.exception.common.IdNotFoundException;

@Getter
public class ArticleNotFoundException extends IdNotFoundException {

    public ArticleNotFoundException(final int id) {
        super(String.valueOf(id), Article.class);
    }
}
