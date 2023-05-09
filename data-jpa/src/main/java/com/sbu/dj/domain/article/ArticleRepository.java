package com.sbu.dj.domain.article;

import com.sbu.dj.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);

    @Query("""
                    SELECT a FROM Article a
                    WHERE (:tag IS NULL OR :tag IN (SELECT t.name FROM a.tags t))
                    AND (:author IS NULL OR a.author.username = :author)
                    AND (:favorited IS NULL OR :favorited IN (SELECT f.username FROM a.favouringUsers f))
                    ORDER BY a.createdAt DESC
                    """)
    Page<Article> findByFacets(
            @Param("tag") String tag,
            @Param("author") String author,
            @Param("favorited") String favorited,
            Pageable pageable);

    List<Article> findByAuthorInOrderByCreatedAtDesc(Collection<User> authors, Pageable pageable);


}