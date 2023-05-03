package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping
    public ArticleResponse createArticle(@RequestBody ArticleNew articleNew) {
        return articleService.createArticle(articleNew);
    }

    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@PathVariable String slug) {
        return articleService.getArticle(slug);
    }
}

