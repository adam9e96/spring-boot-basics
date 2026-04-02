package com.adam9e96.chapter052paginationbasic.config;

import com.adam9e96.chapter052paginationbasic.entity.Post;
import com.adam9e96.chapter052paginationbasic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 애플리케이션 시작 시 테스트 데이터 100건을 생성한다.
 * <p>
 * 페이지네이션 동작을 확인하기 위해 충분한 양의 데이터가 필요하다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public void run(String... args) {
        for (int i = 1; i <= 100; i++) {
            postRepository.save(new Post("게시글 " + i, "내용 " + i));
        }
        log.info("테스트 데이터 100건 생성 완료");
    }
}
