package com.adam9e96.chapter053paginationmodern.config;

import com.adam9e96.chapter053paginationmodern.entity.Post;
import com.adam9e96.chapter053paginationmodern.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;

    @Override
    public void run(String... args) {
        var posts = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new Post("게시글 " + i, "내용 " + i))
                .toList();
        postRepository.saveAll(posts);
        log.info("테스트 데이터 100건 생성 완료");
    }
}
