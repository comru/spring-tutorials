package com.sbu.dj.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sbu.dj.domain.article.Article;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Article}
 */
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public record ArticleNew(@NotNull String title,
                         @NotNull String description,
                         @NotNull String body,
                         Set<String> tagList) implements Serializable {
}