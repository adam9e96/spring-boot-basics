package com.adam9e96.chapter052paginationbasic.repository;

import com.adam9e96.chapter052paginationbasic.entity.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EntityManager를 직접 사용한 수동 페이지네이션 Repository.
 * <p>
 * Spring Data JPA의 {@code JpaRepository}를 사용하지 않고,
 * JPQL의 {@code setFirstResult()}와 {@code setMaxResults()}로
 * 오프셋 기반 페이지네이션의 원리를 직접 구현한다.
 */
@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager entityManager;

    /**
     * 오프셋 기반 페이지네이션 조회.
     * <p>
     * SQL로 표현하면: {@code SELECT * FROM post ORDER BY id LIMIT :limit OFFSET :offset}
     *
     * @param offset 건너뛸 행 수 (0부터 시작)
     * @param limit  가져올 행 수
     */
    public List<Post> findAllWithPaging(int offset, int limit) {
        return entityManager.createQuery("SELECT p FROM Post p ORDER BY p.id", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * 전체 게시글 수를 조회한다.
     * <p>
     * 총 페이지 수를 계산하기 위해 필요하다.
     */
    public long count() {
        return entityManager.createQuery("SELECT COUNT(p) FROM Post p", Long.class)
                .getSingleResult();
    }

    @Transactional
    public Post save(Post post) {
        entityManager.persist(post);
        return post;
    }
}
