package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import com.sbu.dj.domain.tag.Tag;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleMapper {
    Article toArticle(ArticleNew articleNew);

    ArticleNew toArticleNew(Article article);

    @Mapping(source = "tags", target = "tagList", qualifiedByName = "tagToTagNameMapper")
    ArticleResponse toArticleResponse(Article article);

    @Named("tagToTagNameMapper")
    public static Set<String> tagToTagNameMapper(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}