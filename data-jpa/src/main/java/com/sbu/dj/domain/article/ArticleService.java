package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import com.sbu.dj.domain.tag.TagService;
import com.sbu.dj.domain.user.User;
import com.sbu.dj.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@AllArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final TagService tagService;
    private final UserService userService;

    @Transactional
    public ArticleResponse createArticle(ArticleNew articleNew) {
        Article article = articleMapper.toArticle(articleNew);
        String title = articleNew.title();
        article.setSlug(title.toLowerCase().replaceAll("\\s+", "-"));
        article.setTags(tagService.getOrCreateTags(articleNew.tagList()));

        Article savedArticle = articleRepository.save(article);
        return articleMapper.toArticleResponse(savedArticle);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found: `%s`".formatted(slug)));
        ArticleResponse articleResponse = articleMapper.toArticleResponse(article);

        initArticleResponse(article, articleResponse);

        return articleResponse;
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticles(String tag, String author, String favorited, int offset, int limit) {
        Page<Article> resultArticles = articleRepository.findByFacets(tag, author, favorited, PageRequest.of(offset, limit));
        Page<ArticleResponse> articleResponsePage = resultArticles.map(article -> {
            ArticleResponse articleResponse = articleMapper.toArticleResponse(article);
            initArticleResponse(article, articleResponse);
            return articleResponse;
        });
        return articleResponsePage.getContent();
    }

    private void initArticleResponse(Article article, ArticleResponse articleResponse) {
        User currentUser = userService.getCurrentUser();
        Set<User> favouringUsers = article.getFavouringUsers();
        articleResponse.setFavorited(currentUser != null && favouringUsers.contains(currentUser));
        articleResponse.setFavoritesCount(favouringUsers.size());
        articleResponse.getAuthor().setFollowing(currentUser != null && currentUser.getFollowings().contains(article.getAuthor()));
    }
}
