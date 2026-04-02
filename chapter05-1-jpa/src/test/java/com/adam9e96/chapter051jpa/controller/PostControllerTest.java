package com.adam9e96.chapter051jpa.controller;

import com.adam9e96.chapter051jpa.dto.PostCreateRequest;
import com.adam9e96.chapter051jpa.dto.PostResponse;
import com.adam9e96.chapter051jpa.service.PostService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest + @AutoConfigureMockMvc 조합으로 실제 서블릿 컨테이너 없이 MVC 계층을 테스트한다.
// @Transactional은 각 테스트 후 자동 롤백하여 테스트 간 데이터 격리를 보장한다.
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostService postService;

    // 테스트용 게시글을 생성하는 헬퍼 메서드
    private PostResponse createTestPost(String title, String content, String author) {
        return postService.create(new PostCreateRequest(title, content, author));
    }

    @Test
    void POST_게시글_생성_요청이_성공하면_201을_반환한다() throws Exception {
        // given — 생성할 게시글 JSON을 준비한다
        String requestBody = objectMapper.writeValueAsString(
                new PostCreateRequest("새 게시글", "게시글 내용입니다", "홍길동")
        );

        // when & then — POST 요청 시 201 Created와 함께 생성된 게시글 정보가 반환된다
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("새 게시글"))
                .andExpect(jsonPath("$.content").value("게시글 내용입니다"))
                .andExpect(jsonPath("$.author").value("홍길동"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void GET_게시글_목록을_페이징_파라미터와_함께_조회한다() throws Exception {
        // given — 게시글 5개를 생성한다
        for (int i = 1; i <= 5; i++) {
            createTestPost("게시글 " + i, "내용 " + i, "작성자");
        }

        // when & then — 페이지 크기 3, 첫 번째 페이지를 createdAt 내림차순으로 요청한다
        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "createdAt,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void GET_게시글을_ID로_조회하면_200을_반환한다() throws Exception {
        // given — 게시글을 하나 생성한다
        PostResponse created = createTestPost("조회 테스트", "내용", "홍길동");

        // when & then — 생성된 게시글의 ID로 조회하면 200과 함께 게시글 정보가 반환된다
        mockMvc.perform(get("/posts/{id}", created.id()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("조회 테스트"))
                .andExpect(jsonPath("$.author").value("홍길동"));
    }

    @Test
    void GET_존재하지_않는_ID로_조회하면_404를_반환한다() throws Exception {
        // when & then — 존재하지 않는 ID(999)로 조회하면 404 Not Found가 반환된다
        mockMvc.perform(get("/posts/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void PUT_게시글을_수정하면_200을_반환한다() throws Exception {
        // given — 게시글을 하나 생성하고 수정 요청 JSON을 준비한다
        PostResponse created = createTestPost("원래 제목", "원래 내용", "홍길동");
        String updateBody = """
                {
                    "title": "수정된 제목",
                    "content": "수정된 내용"
                }
                """;

        // when & then — PUT 요청 시 200과 함께 수정된 게시글 정보가 반환된다
        mockMvc.perform(put("/posts/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 내용"))
                .andExpect(jsonPath("$.author").value("홍길동"));
    }

    @Test
    void DELETE_게시글을_삭제하면_204를_반환한다() throws Exception {
        // given — 게시글을 하나 생성한다
        PostResponse created = createTestPost("삭제할 글", "내용", "홍길동");

        // when & then — DELETE 요청 시 204 No Content가 반환된다
        mockMvc.perform(delete("/posts/{id}", created.id()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 삭제 후 조회하면 404가 반환되어야 한다
        mockMvc.perform(get("/posts/{id}", created.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void GET_작성자로_게시글을_검색한다() throws Exception {
        // given — 서로 다른 작성자의 게시글을 생성한다
        createTestPost("글 1", "내용 1", "홍길동");
        createTestPost("글 2", "내용 2", "홍길동");
        createTestPost("글 3", "내용 3", "김철수");

        // when & then — author 파라미터로 검색하면 해당 작성자의 게시글만 반환된다
        mockMvc.perform(get("/posts/search")
                        .param("author", "홍길동"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void GET_제목_키워드로_게시글을_검색한다() throws Exception {
        // given — 다양한 제목의 게시글을 생성한다
        createTestPost("JPA 기초", "내용", "작성자");
        createTestPost("JPA 심화", "내용", "작성자");
        createTestPost("Spring MVC", "내용", "작성자");

        // when & then — keyword 파라미터로 검색하면 제목에 키워드가 포함된 게시글만 반환된다
        mockMvc.perform(get("/posts/search")
                        .param("keyword", "JPA"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void POST_제목이_빈값이면_400을_반환한다() throws Exception {
        // given — 제목이 빈 문자열인 요청을 준비한다
        String invalidBody = """
                {
                    "title": "",
                    "content": "내용은 있음",
                    "author": "홍길동"
                }
                """;

        // when & then — 유효성 검증 실패 시 400 Bad Request가 반환되어야 한다
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
