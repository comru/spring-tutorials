package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import com.sbu.dj.domain.article.dto.ArticleUpdate;
import com.sbu.dj.domain.tag.Tag;
import com.sbu.dj.domain.user.UserMapper;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class})
public interface ArticleMapper {

    Article toArticle(ArticleNew articleDto);

    @Mapping(source = "tags", target = "tagList", qualifiedByName = "tagsToTagNames")
    ArticleResponse toArticleResponse(Article article);

    @Named("tagsToTagNames")
    default Set<String> tagsToTagNames(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Article partialUpdate(ArticleUpdate articleUpdate, @MappingTarget Article article);
}