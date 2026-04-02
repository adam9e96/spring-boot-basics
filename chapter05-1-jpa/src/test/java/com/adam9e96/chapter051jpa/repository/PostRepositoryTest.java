package com.adam9e96.chapter051jpa.repository;

import com.adam9e96.chapter051jpa.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest는 JPA 관련 컴포넌트만 로드하여 빠른 슬라이스 테스트를 가능하게 한다.
// 기본적으로 내장 H2 데이터베이스를 사용하고, 각 테스트 후 자동으로 롤백된다.
@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post savedPost;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 게시글 하나를 미리 저장해둔다.
        Post post = new Post();
        post.setTitle("JPA 학습");
        post.setContent("Spring Data JPA를 배워보자");
        post.setAuthor("홍길동");
        savedPost = postRepository.save(post);
    }

    @Test
    void save_새로운_게시글을_저장하고_ID가_생성되는지_확인한다() {
        // given — 새 게시글을 준비한다
        Post post = new Post();
        post.setTitle("새 게시글");
        post.setContent("새로운 내용입니다");
        post.setAuthor("김철수");

        // when — 저장한다
        Post saved = postRepository.save(post);

        // then — ID가 자동 생성되고 필드 값이 올바른지 확인한다
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("새 게시글");
        assertThat(saved.getContent()).isEqualTo("새로운 내용입니다");
        assertThat(saved.getAuthor()).isEqualTo("김철수");
    }

    @Test
    void findById_저장된_게시글을_ID로_조회할_수_있다() {
        // when — setUp에서 저장한 게시글의 ID로 조회한다
        Optional<Post> found = postRepository.findById(savedPost.getId());

        // then — 게시글이 존재하고 제목이 일치하는지 확인한다
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("JPA 학습");
    }

    @Test
    void findByAuthor_작성자로_게시글_목록을_조회한다() {
        // given — 같은 작성자의 게시글을 하나 더 추가한다
        Post anotherPost = new Post();
        anotherPost.setTitle("두 번째 글");
        anotherPost.setContent("같은 작성자의 두 번째 글");
        anotherPost.setAuthor("홍길동");
        postRepository.save(anotherPost);

        // when — 작성자 이름으로 검색한다
        List<Post> posts = postRepository.findByAuthor("홍길동");

        // then — 해당 작성자의 게시글이 2건인지 확인한다
        assertThat(posts).hasSize(2);
        assertThat(posts).allMatch(p -> p.getAuthor().equals("홍길동"));
    }

    @Test
    void findByTitleContaining_제목에_키워드가_포함된_게시글을_검색한다() {
        // given — 다양한 제목의 게시글을 추가한다
        Post post1 = new Post();
        post1.setTitle("JPA 심화 과정");
        post1.setContent("QueryDSL을 배워보자");
        post1.setAuthor("김영한");
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Spring MVC 기초");
        post2.setContent("컨트롤러를 만들어보자");
        post2.setAuthor("김영한");
        postRepository.save(post2);

        // when — "JPA" 키워드로 제목 검색한다
        List<Post> results = postRepository.findByTitleContaining("JPA");

        // then — "JPA"가 제목에 포함된 게시글 2건이 검색되어야 한다
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(p -> p.getTitle().contains("JPA"));
    }

    @Test
    void findAll_페이징과_정렬이_올바르게_동작하는지_확인한다() {
        // given — 게시글을 여러 개 추가하여 페이징 테스트 준비를 한다
        for (int i = 1; i <= 5; i++) {
            Post post = new Post();
            post.setTitle("게시글 " + i);
            post.setContent("내용 " + i);
            post.setAuthor("작성자");
            postRepository.save(post);
        }

        // when — 페이지 크기 3, 첫 번째 페이지를 ID 내림차순으로 요청한다
        Page<Post> page = postRepository.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))
        );

        // then — 페이지 크기가 3이고, 전체 요소가 6개(setUp 1건 + 추가 5건)인지 확인한다
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void update_게시글을_수정하면_변경사항이_반영된다() {
        // when — 제목과 내용을 수정하고 저장한다
        savedPost.setTitle("수정된 제목");
        savedPost.setContent("수정된 내용");
        Post updated = postRepository.saveAndFlush(savedPost);

        // then — 수정된 필드 값이 올바른지 확인한다
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
        assertThat(updated.getContent()).isEqualTo("수정된 내용");
    }

    @Test
    void delete_게시글을_삭제하면_조회되지_않는다() {
        // given — setUp에서 저장한 게시글의 ID를 기억해둔다
        Long id = savedPost.getId();

        // when — 게시글을 삭제한다
        postRepository.delete(savedPost);

        // then — 삭제 후 해당 ID로 조회하면 결과가 없어야 한다
        Optional<Post> found = postRepository.findById(id);
        assertThat(found).isEmpty();
    }
}
