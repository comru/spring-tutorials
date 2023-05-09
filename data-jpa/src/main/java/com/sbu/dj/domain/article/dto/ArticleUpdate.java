package com.sbu.dj.domain.article.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

/**
 * DTO for {@link com.sbu.dj.domain.article.Article}
 */
@JsonTypeName("article")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public record ArticleUpdate(String title,
                            String description,
                            String body) implements Serializable {
}