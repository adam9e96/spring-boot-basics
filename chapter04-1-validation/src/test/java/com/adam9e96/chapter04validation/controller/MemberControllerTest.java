package com.adam9e96.chapter04validation.controller;

import com.adam9e96.chapter04validation.dto.MemberCreateRequest;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MemberController 통합 테스트.
 * MockMvc를 사용하여 HTTP 요청/응답 전체 흐름을 검증한다.
 * Bean Validation 어노테이션이 올바르게 동작하는지 확인하는 것이 핵심이다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== 유효한 요청 데이터를 만드는 헬퍼 메서드 ==========

    /**
     * 테스트용 유효한 회원가입 요청 객체를 생성한다.
     * 모든 필드가 검증 조건을 만족하는 기본값을 가진다.
     */
    private MemberCreateRequest validRequest() {
        return new MemberCreateRequest(
                "홍길동",
                "hong@example.com",
                "Password1",
                25,
                "010-1234-5678"
        );
    }

    // ========== 회원 생성 성공 테스트 ==========

    /**
     * 유효한 데이터로 회원 생성 시 201 Created를 반환하고,
     * 응답에 id, name, email, age, phone이 포함되며 password는 포함되지 않아야 한다.
     */
    @Test
    void createMember_success() throws Exception {
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("hong@example.com"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.phone").value("010-1234-5678"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    // ========== 이름(name) 검증 테스트 ==========

    /**
     * 이름이 빈 문자열이면 400 Bad Request를 반환하고,
     * 에러 응답에 "name" 키가 포함되어야 한다.
     */
    @Test
    void createMember_blankName() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "", "hong@example.com", "Password1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    /**
     * 이름이 2자 미만(1자)이면 400 Bad Request를 반환해야 한다.
     * @Size(min=2) 검증을 확인한다.
     */
    @Test
    void createMember_shortName() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "A", "hong@example.com", "Password1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    /**
     * 이름이 20자를 초과(21자)하면 400 Bad Request를 반환해야 한다.
     * @Size(max=20) 검증을 확인한다.
     */
    @Test
    void createMember_longName() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "A".repeat(21), "hong@example.com", "Password1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    // ========== 이메일(email) 검증 테스트 ==========

    /**
     * 이메일 형식이 올바르지 않으면 400 Bad Request를 반환하고,
     * 에러 응답에 "email" 키가 포함되어야 한다.
     */
    @Test
    void createMember_invalidEmail() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "notanemail", "Password1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    /**
     * 이메일이 빈 문자열이면 400 Bad Request를 반환해야 한다.
     * @NotBlank 검증을 확인한다.
     */
    @Test
    void createMember_blankEmail() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "", "Password1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    // ========== 비밀번호(password) 검증 테스트 ==========

    /**
     * 비밀번호가 8자 미만(3자)이면 400 Bad Request를 반환해야 한다.
     * @Size(min=8) 검증을 확인한다.
     */
    @Test
    void createMember_shortPassword() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "Ab1", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    /**
     * 비밀번호에 숫자가 포함되지 않으면 400 Bad Request를 반환해야 한다.
     * 비밀번호는 반드시 영문자와 숫자를 모두 포함해야 한다.
     */
    @Test
    void createMember_passwordNoDigit() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "abcdefgh", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    /**
     * 비밀번호에 영문자가 포함되지 않으면 400 Bad Request를 반환해야 한다.
     * 비밀번호는 반드시 영문자와 숫자를 모두 포함해야 한다.
     */
    @Test
    void createMember_passwordNoLetter() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "12345678", 25, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    // ========== 나이(age) 검증 테스트 ==========

    /**
     * 나이가 null이면 400 Bad Request를 반환해야 한다.
     * @NotNull 검증을 확인한다.
     * (JSON에서 age 필드를 제거하여 null을 시뮬레이션한다)
     */
    @Test
    void createMember_nullAge() throws Exception {
        // age를 null로 보내기 위해 JSON을 직접 구성한다
        String json = """
                {
                    "name": "홍길동",
                    "email": "hong@example.com",
                    "password": "Password1",
                    "age": null,
                    "phone": "010-1234-5678"
                }
                """;

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").exists());
    }

    /**
     * 나이가 음수(-1)이면 400 Bad Request를 반환해야 한다.
     * @Min(0) 검증을 확인한다.
     */
    @Test
    void createMember_negativeAge() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", -1, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").exists());
    }

    /**
     * 나이가 150을 초과(151)하면 400 Bad Request를 반환해야 한다.
     * @Max(150) 검증을 확인한다.
     */
    @Test
    void createMember_overMaxAge() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", 151, "010-1234-5678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").exists());
    }

    // ========== 전화번호(phone) 검증 테스트 ==========

    /**
     * 전화번호가 한국 형식(010-xxxx-xxxx)이 아니면 400 Bad Request를 반환해야 한다.
     * 하이픈 없이 숫자만 입력한 경우를 검증한다.
     */
    @Test
    void createMember_invalidPhone() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", 25, "01012345678"
        );

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.phone").exists());
    }

    // ========== 회원 목록 조회 테스트 ==========

    /**
     * 회원을 한 명 생성한 후 GET /members를 호출하면
     * 목록에 최소 1개 이상의 항목이 반환되어야 한다.
     */
    @Test
    void getMembers_afterCreate() throws Exception {
        // 먼저 회원을 생성한다
        mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest())));

        // 회원 목록을 조회한다
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(
                        org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }

    // ========== 회원 단건 조회 테스트 ==========

    /**
     * 회원을 생성한 후 반환된 id로 GET /members/{id}를 호출하면
     * 200 OK와 함께 해당 회원 정보가 반환되어야 한다.
     */
    @Test
    void getMemberById_success() throws Exception {
        // 회원을 생성하고 반환된 id를 추출한다
        String response = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        // 생성된 회원을 id로 조회한다
        mockMvc.perform(get("/members/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("홍길동"));
    }

    /**
     * 존재하지 않는 id로 GET /members/{id}를 호출하면
     * 404 Not Found를 반환하고, 에러 응답에 "error" 키가 포함되어야 한다.
     */
    @Test
    void getMemberById_notFound() throws Exception {
        mockMvc.perform(get("/members/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
