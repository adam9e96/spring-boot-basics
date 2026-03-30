package com.adam9e96.chapter05jpa.service;

import com.adam9e96.chapter05jpa.dto.PostCreateRequest;
import com.adam9e96.chapter05jpa.dto.PostResponse;
import com.adam9e96.chapter05jpa.dto.PostUpdateRequest;
import com.adam9e96.chapter05jpa.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// @SpringBootTest는 전체 애플리케이션 컨텍스트를 로드하여 통합 테스트를 수행한다.
// @Transactional은 각 테스트 후 자동 롤백하여 테스트 간 데이터 격리를 보장한다.
@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void create_게시글을_생성하고_응답값을_검증한다() {
        // given — 게시글 생성 요청을 준비한다
        PostCreateRequest request = new PostCreateRequest("JPA 학습", "JPA를 배워보자", "홍길동");

        // when — 게시글을 생성한다
        PostResponse response = postService.create(request);

        // then — 생성된 게시글의 필드 값이 올바른지 확인한다
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo("JPA 학습");
        assertThat(response.content()).isEqualTo("JPA를 배워보자");
        assertThat(response.author()).isEqualTo("홍길동");
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void findAll_페이징이_올바르게_동작하는지_확인한다() {
        // given — 게시글 5개를 생성한다
        for (int i = 1; i <= 5; i++) {
            postService.create(new PostCreateRequest("게시글 " + i, "내용 " + i, "작성자"));
        }

        // when — 페이지 크기 3, 첫 번째 페이지를 요청한다
        Page<PostResponse> page = postService.findAll(PageRequest.of(0, 3, Sort.by("id")));

        // then — 페이지 크기가 3이고 전체 요소가 5개인지 확인한다
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findById_존재하는_게시글을_ID로_조회한다() {
        // given — 게시글을 하나 생성한다
        PostResponse created = postService.create(
                new PostCreateRequest("테스트 제목", "테스트 내용", "테스터")
        );

        // when — 생성된 게시글의 ID로 조회한다
        PostResponse found = postService.findById(created.id());

        // then — 조회된 게시글의 제목이 일치하는지 확인한다
        assertThat(found.title()).isEqualTo("테스트 제목");
        assertThat(found.author()).isEqualTo("테스터");
    }

    @Test
    void findById_존재하지_않는_ID로_조회하면_예외가_발생한다() {
        // when & then — 존재하지 않는 ID(999)로 조회하면 PostNotFoundException이 발생해야 한다
        assertThatThrownBy(() -> postService.findById(999L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void update_게시글을_수정하고_변경사항을_확인한다() {
        // given — 게시글을 하나 생성한다
        PostResponse created = postService.create(
                new PostCreateRequest("원래 제목", "원래 내용", "홍길동")
        );

        // when — 제목과 내용을 수정한다
        PostUpdateRequest updateRequest = new PostUpdateRequest("수정된 제목", "수정된 내용");
        PostResponse updated = postService.update(created.id(), updateRequest);

        // then — 수정된 필드가 올바르게 반영되었는지 확인한다
        assertThat(updated.title()).isEqualTo("수정된 제목");
        assertThat(updated.content()).isEqualTo("수정된 내용");
        // 작성자는 변경되지 않아야 한다
        assertThat(updated.author()).isEqualTo("홍길동");
    }

    @Test
    void delete_게시글을_삭제하면_더이상_조회되지_않는다() {
        // given — 게시글을 하나 생성한다
        PostResponse created = postService.create(
                new PostCreateRequest("삭제할 게시글", "삭제될 내용", "홍길동")
        );

        // when — 게시글을 삭제한다
        postService.delete(created.id());

        // then — 삭제된 게시글을 조회하면 예외가 발생해야 한다
        assertThatThrownBy(() -> postService.findById(created.id()))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void findByAuthor_작성자로_게시글_목록을_조회한다() {
        // given — 서로 다른 작성자의 게시글을 생성한다
        postService.create(new PostCreateRequest("글 1", "내용 1", "홍길동"));
        postService.create(new PostCreateRequest("글 2", "내용 2", "홍길동"));
        postService.create(new PostCreateRequest("글 3", "내용 3", "김철수"));

        // when — "홍길동" 작성자로 검색한다
        List<PostResponse> posts = postService.findByAuthor("홍길동");

        // then — 홍길동이 작성한 게시글 2건이 조회되어야 한다
        assertThat(posts).hasSize(2);
        assertThat(posts).allMatch(p -> p.author().equals("홍길동"));
    }

    @Test
    void searchByTitle_제목_키워드로_게시글을_검색한다() {
        // given — 다양한 제목의 게시글을 생성한다
        postService.create(new PostCreateRequest("JPA 기초", "내용", "작성자"));
        postService.create(new PostCreateRequest("JPA 심화", "내용", "작성자"));
        postService.create(new PostCreateRequest("Spring MVC", "내용", "작성자"));

        // when — "JPA" 키워드로 검색한다
        List<PostResponse> results = postService.searchByTitle("JPA");

        // then — "JPA"가 제목에 포함된 게시글 2건이 검색되어야 한다
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(p -> p.title().contains("JPA"));
    }
}
