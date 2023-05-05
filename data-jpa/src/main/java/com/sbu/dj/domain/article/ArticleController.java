package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ArticleResponse createArticle(@Valid @RequestBody ArticleNew articleNew) {
        return articleService.createArticle(articleNew);
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@PathVariable String slug) {
        return articleService.getArticle(slug);
    }
}

