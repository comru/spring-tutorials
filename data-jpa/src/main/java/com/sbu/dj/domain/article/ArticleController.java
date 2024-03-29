package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import com.sbu.dj.domain.article.dto.ArticleUpdate;
import com.sbu.dj.domain.comment.dto.CommentNew;
import com.sbu.dj.domain.comment.dto.CommentResponse;
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

    @GetMapping("/feed")
    public ArticleResponse.Multiple getFeedArticles(@Nullable @RequestParam(required = false, name = "offset", defaultValue = "0") int offset,
                                                    @Nullable @RequestParam(required = false, name = "limit", defaultValue = "20") int limit) {
        return new ArticleResponse.Multiple(articleService.getFeedArticles(offset, limit));
    }

    @PutMapping("/{slug}")
    public ArticleResponse.Single updateArticle(@PathVariable String slug, @RequestBody ArticleUpdate articleUpdate) {
        return new ArticleResponse.Single(articleService.updateArticle(slug, articleUpdate));
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug) {
        articleService.deleteArticle(slug);
    }

    @PostMapping("/{slug}/comments")
    public CommentResponse.Single createComment(@PathVariable String slug, @RequestBody CommentNew commentNew) {
        return new CommentResponse.Single(articleService.createComment(slug, commentNew));
    }

    @GetMapping("/{slug}/comments")
    public CommentResponse.Multiple getArticleComments(@PathVariable String slug) {
        return new CommentResponse.Multiple(articleService.getArticleComments(slug));
    }
}

