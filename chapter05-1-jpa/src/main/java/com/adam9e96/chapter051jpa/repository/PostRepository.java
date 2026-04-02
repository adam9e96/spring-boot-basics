package com.adam9e96.chapter051jpa.repository;

import com.adam9e96.chapter051jpa.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시글 리포지토리 인터페이스.
 *
 * <p>{@code JpaRepository<Post, Long>}을 상속하면 별도 구현 없이 다음 메서드들이 자동 제공된다:</p>
 * <ul>
 *   <li>{@code save(Post)} — 저장 (INSERT / UPDATE)</li>
 *   <li>{@code findById(Long)} — PK로 단건 조회</li>
 *   <li>{@code findAll()} / {@code findAll(Pageable)} — 전체 조회 / 페이징 조회</li>
 *   <li>{@code deleteById(Long)} — PK로 삭제</li>
 *   <li>{@code count()} — 전체 건수 조회</li>
 * </ul>
 *
 * <h3>쿼리 메서드 (Query Method)</h3>
 * <p>Spring Data JPA의 "메서드 이름 기반 쿼리 생성" 기능을 활용한다.
 * 메서드 이름의 규칙(findBy + 필드명 + 조건 키워드)만 지키면
 * JPA가 메서드 이름을 분석해서 자동으로 SQL을 생성한다.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 작성자(author)로 게시글 목록을 조회한다.
     * <p>생성되는 쿼리: {@code SELECT * FROM post WHERE author = ?}</p>
     */
    List<Post> findByAuthor(String author);

    /**
     * 제목에 키워드가 포함된 게시글을 검색한다.
     * <p>{@code Containing} → SQL의 {@code LIKE '%keyword%'} 와 동일하다.</p>
     * <p>생성되는 쿼리: {@code SELECT * FROM post WHERE title LIKE '%keyword%'}</p>
     */
    List<Post> findByTitleContaining(String keyword);


}
