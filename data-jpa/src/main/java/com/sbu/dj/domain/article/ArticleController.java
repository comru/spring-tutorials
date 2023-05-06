package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ArticleResponse.Single createArticle(@Valid @RequestBody ArticleNew articleNew) {
        return new ArticleResponse.Single(articleService.createArticle(articleNew));
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/{slug}")
    public ArticleResponse.Single getArticle(@PathVariable String slug) {
        return new ArticleResponse.Single(articleService.getArticle(slug));
    }


    @GetMapping
    public ArticleResponse.Multiple getArticles(@Nullable @RequestParam(required = false, name = "tag") String tag,
                                                      @Nullable @RequestParam(required = false, name = "author") String author,
                                                      @Nullable @RequestParam(required = false, name = "favorited") String favorited,
                                                      @Nullable @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
                                                      @Nullable @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
        List<ArticleResponse> articles = articleService.getArticles(tag, author, favorited, offset, limit);
        return new ArticleResponse.Multiple(articles);
    }
}

