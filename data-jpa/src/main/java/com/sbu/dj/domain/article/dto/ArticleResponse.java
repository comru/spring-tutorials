package com.sbu.dj.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sbu.dj.domain.article.Article;
import com.sbu.dj.domain.user.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link Article}
 */
@Data
public class ArticleResponse implements Serializable {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private UserDto.Profile author;
    private Instant createdAt;
    private Instant updatedAt;
    private String slug;
    private String title;
    private String description;
    private String body;
    private Set<String> tagList;
    private boolean favorited;
    private int favoritesCount;

    public record Multiple(
            List<ArticleResponse> articles,
            int articlesCount) {

        public Multiple(List<ArticleResponse> articles) {
            this(articles, articles.size());
        }
    }

    public record Single(ArticleResponse article) {
    }
}