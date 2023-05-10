package com.sbu.dj.domain.article;

import com.sbu.dj.domain.article.dto.ArticleNew;
import com.sbu.dj.domain.article.dto.ArticleResponse;
import com.sbu.dj.domain.article.dto.ArticleUpdate;
import com.sbu.dj.domain.comment.*;
import com.sbu.dj.domain.comment.dto.CommentNew;
import com.sbu.dj.domain.comment.dto.CommentResponse;
import com.sbu.dj.domain.tag.TagService;
import com.sbu.dj.domain.user.User;
import com.sbu.dj.domain.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final TagService tagService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public ArticleResponse createArticle(ArticleNew articleNew) {
        Article article = articleMapper.toArticle(articleNew);
        String title = articleNew.title();
        article.setSlug(generateSlug(title));
        article.setTags(tagService.getOrCreateTags(articleNew.tagList()));

        Article savedArticle = articleRepository.save(article);
        return articleMapper.toArticleResponse(savedArticle);
    }

    private static String generateSlug(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
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

    public List<ArticleResponse> getFeedArticles(int offset, int limit) {
        Collection<User> authors = userService.getCurrentUserNN().getFollowings();
        List<Article> resultArticles = articleRepository.findByAuthorInOrderByCreatedAtDesc(authors, PageRequest.of(offset, limit));
        return resultArticles.stream().map(article -> {
            ArticleResponse articleResponse = articleMapper.toArticleResponse(article);
            initArticleResponse(article, articleResponse);
            return articleResponse;
        }).toList();
    }

    @Transactional
    public ArticleResponse updateArticle(String slug, ArticleUpdate articleUpdate) {
        return articleRepository
                .findBySlug(slug)
                .map(article -> {
                    Article updatedArticle = articleMapper.partialUpdate(articleUpdate, article);
                    String title = articleUpdate.title();
                    if (StringUtils.isNotBlank(title)) {
                        updatedArticle.setSlug(generateSlug(title));
                    }
                    return updatedArticle;
                })
                .map(article -> {
                    ArticleResponse articleResponse = articleMapper.toArticleResponse(article);
                    initArticleResponse(article, articleResponse);
                    return articleResponse;
                })
                .orElseThrow(() -> new NoSuchElementException("Article not found by slug: `%s`".formatted(slug)));
    }

    @Transactional
    public void deleteArticle(String slug) {
        articleRepository
                .findBySlug(slug)
                .ifPresentOrElse(
                        article -> {
                            User currentUser = userService.getCurrentUserNN();
                            if (article.getAuthor().equals(currentUser)) articleRepository.delete(article);
                            else throw new IllegalArgumentException("You cannot delete articles written by others.");
                        },
                        () -> {
                            throw new NoSuchElementException("Article not found by slug: `%s`".formatted(slug));
                        });
    }

    @Transactional
    public CommentResponse createComment(String slug, CommentNew commentNew) {
        return articleRepository
                .findBySlug(slug)
                .map(article -> new Comment()
                        .setArticle(article)
                        .setBody(commentNew.body()))
                .map(commentRepository::save)
                .map(comment -> {
                    CommentResponse commentResponse = commentMapper.toDto(comment);
                    initComment(comment, commentResponse);
                    return commentResponse;
                })
                .orElseThrow(() -> new NoSuchElementException("Article not found by slug: `%s`".formatted(slug)));
    }

    private void initComment(Comment comment, CommentResponse commentResponse) {
        User currentUser = userService.getCurrentUser();
        commentResponse.author().setFollowing(
                currentUser != null && currentUser.getFollowings().contains(comment.getAuthor()));
    }
}
