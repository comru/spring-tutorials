package com.sbu.dj.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sbu.dj.domain.article.Article;
import com.sbu.dj.domain.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link Article}
 */
@Data
@RequiredArgsConstructor
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ArticleResponse implements Serializable {
    private final UserDto.Profile author;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String slug;
    private final String title;
    private final String description;
    private final String body;
    private Set<String> tagList;
    private boolean favorited;
    private int favoritesCount;
}