package com.adam9e96.chapter036thymeleaflayout.config;

import com.adam9e96.chapter036thymeleaflayout.entity.Article;
import com.adam9e96.chapter036thymeleaflayout.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) {
        IntStream.rangeClosed(1, 30).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("게시글 " + i)
                        .content("게시글 " + i + "의 내용입니다.")
                        .author(i % 3 == 0 ? "관리자" : "사용자" + (i % 5 + 1))
                        .viewCount(0)
                        .build())
        );
    }
}
