package com.adam9e96.chapter036thymeleaflayout.repository;

import com.adam9e96.chapter036thymeleaflayout.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByTitleContaining(String keyword, Pageable pageable);

    Page<Article> findByAuthorContaining(String keyword, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    int incrementViewCount(@Param("id") Long id);
}
