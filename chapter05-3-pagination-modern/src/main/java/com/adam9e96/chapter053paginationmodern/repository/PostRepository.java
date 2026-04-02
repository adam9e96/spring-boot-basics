package com.adam9e96.chapter053paginationmodern.repository;

import com.adam9e96.chapter053paginationmodern.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA Repository.
 * <p>
 * {@link JpaRepository}를 상속하면 {@code findAll(Pageable)}이 자동 제공된다.
 * 직접 페이지네이션 로직을 구현할 필요가 없다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 제목에 키워드가 포함된 게시글을 페이지네이션으로 조회한다.
     * <p>
     * Spring Data가 메서드 이름을 분석하여 쿼리를 자동 생성한다.
     */
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
