package com.adam9e96.chapter051jpa.service;

import com.adam9e96.chapter051jpa.dto.PostCreateRequest;
import com.adam9e96.chapter051jpa.dto.PostResponse;
import com.adam9e96.chapter051jpa.dto.PostUpdateRequest;
import com.adam9e96.chapter051jpa.entity.Post;
import com.adam9e96.chapter051jpa.exception.PostNotFoundException;
import com.adam9e96.chapter051jpa.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 게시글 비즈니스 로직을 담당하는 서비스 클래스.
 *
 * <h3>트랜잭션 전략</h3>
 * <ul>
 *   <li>{@code @Transactional} (클래스 레벨) — 모든 public 메서드에 기본 트랜잭션 적용 (읽기+쓰기)</li>
 *   <li>{@code @Transactional(readOnly = true)} (메서드 레벨) — 조회 전용 메서드에 적용하면
 *       JPA가 변경 감지(dirty checking)를 건너뛰어 성능이 향상된다.
 *       또한 DB 레플리카(읽기 전용 DB)로 라우팅하는 데에도 활용할 수 있다.</li>
 * </ul>
 *
 * <h3>DTO 변환 패턴</h3>
 * <p>엔티티(Post)를 직접 반환하지 않고 {@code PostResponse} DTO로 변환하여 반환한다.
 * 이렇게 하면 엔티티의 내부 구조가 API 응답에 노출되지 않아 유지보수에 유리하다.</p>
 */
@Service
@Transactional // 클래스 레벨: 모든 public 메서드에 트랜잭션 적용
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동 생성 → 생성자 주입
public class PostService {

    private final PostRepository postRepository;

    /**
     * 새 게시글을 생성한다.
     * <p>DTO → 엔티티 변환 → 저장 → 엔티티 → 응답 DTO 변환 순서로 처리한다.</p>
     */
    public PostResponse create(PostCreateRequest request) {
        Post post = new Post();
        post.setTitle(request.title());
        post.setContent(request.content());
        post.setAuthor(request.author());
        // save()는 엔티티를 영속화(persist)하고, ID와 Auditing 필드가 채워진 엔티티를 반환한다.
        return PostResponse.from(postRepository.save(post));
    }

    /**
     * 모든 게시글을 페이징하여 조회한다.
     * <p>{@code Pageable} 파라미터로 page, size, sort 정보를 한 번에 받을 수 있다.</p>
     * <p>{@code Page.map()}으로 엔티티 → DTO 변환을 간결하게 처리한다.</p>
     */
    @Transactional(readOnly = true) // 읽기 전용: dirty checking 생략 → 성능 최적화
    public Page<PostResponse> findAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponse::from);

    }

    /**
     * ID로 게시글을 단건 조회한다.
     * <p>{@code findById()}는 {@code Optional<Post>}를 반환하므로,
     * 존재하지 않으면 {@code PostNotFoundException}을 던진다.</p>
     */
    @Transactional(readOnly = true)
    public PostResponse findById(Long id) {
        return postRepository.findById(id)
                .map(PostResponse::from)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    /**
     * 게시글을 수정한다.
     * <p><b>JPA 변경 감지(Dirty Checking)</b>: 영속 상태(managed)인 엔티티의 필드를
     * setter로 변경하면, 트랜잭션 커밋 시점에 JPA가 변경사항을 감지하여
     * 자동으로 UPDATE SQL을 실행한다. 따라서 별도의 {@code save()} 호출이 필요 없다.</p>
     */
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        // 영속 상태의 엔티티 필드를 변경 → 트랜잭션 커밋 시 자동 UPDATE (Dirty Checking)
        post.setTitle(request.title());
        post.setContent(request.content());

        return PostResponse.from(post);

    }

    /** 게시글을 삭제한다. */
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    /** 작성자로 게시글 목록을 조회한다. */
    @Transactional(readOnly = true)
    public List<PostResponse> findByAuthor(String author) {
        return postRepository.findByAuthor(author).stream()
                .map(PostResponse::from)
                .toList();
    }

    /** 제목에 키워드가 포함된 게시글을 검색한다. */
    @Transactional(readOnly = true)
    public List<PostResponse> searchByTitle(String title) {
        return postRepository.findByTitleContaining(title).stream()
                .map(PostResponse::from)
                .toList();
    }


}
