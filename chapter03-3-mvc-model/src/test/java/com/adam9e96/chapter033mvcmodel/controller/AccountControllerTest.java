package com.adam9e96.chapter033mvcmodel.controller;

import com.adam9e96.chapter033mvcmodel.entity.Account;
import com.adam9e96.chapter033mvcmodel.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /accounts/new — 계정 생성 폼을 보여준다")
    void createForm() throws Exception {
        mockMvc.perform(get("/accounts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/form"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @DisplayName("POST /accounts — 유효한 데이터로 계정을 생성한다")
    void create_success() throws Exception {
        mockMvc.perform(post("/accounts")
                        .param("name", "홍길동")
                        .param("phone", "010-1234-5678")
                        .param("address", "서울시")
                        .param("privacyAgreement", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/accounts/*"));

        assertThat(accountRepository.findAll()).hasSize(1);
        assertThat(accountRepository.findAll().getFirst().getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("POST /accounts — 이름 누락 시 폼을 다시 보여준다")
    void create_validationError() throws Exception {
        mockMvc.perform(post("/accounts")
                        .param("name", "")
                        .param("phone", "010-1234-5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/form"))
                .andExpect(model().hasErrors());

        assertThat(accountRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("GET /accounts — 계정 목록을 보여준다")
    void list() throws Exception {
        accountRepository.save(Account.builder().name("홍길동").phone("010-1111-2222").privacyAgreement(true).build());
        accountRepository.save(Account.builder().name("김철수").phone("010-3333-4444").privacyAgreement(false).build());

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/list"))
                .andExpect(model().attributeExists("accounts"));
    }

    @Test
    @DisplayName("GET /accounts/{id} — 계정 상세를 보여준다")
    void detail() throws Exception {
        Account saved = accountRepository.save(
                Account.builder().name("홍길동").phone("010-1111-2222").privacyAgreement(true).build());

        mockMvc.perform(get("/accounts/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/detail"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @DisplayName("DELETE /accounts/{id} — _method=delete로 계정을 삭제한다")
    void delete_withHiddenMethod() throws Exception {
        Account saved = accountRepository.save(
                Account.builder().name("삭제대상").phone("010-0000-0000").privacyAgreement(false).build());

        // HTML 폼에서 _method=delete 히든 필드로 DELETE 요청을 시뮬레이션
        mockMvc.perform(post("/accounts/" + saved.getId())
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accounts"));

        assertThat(accountRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("PUT /accounts/{id} — _method=put으로 계정을 수정한다")
    void update_withHiddenMethod() throws Exception {
        Account saved = accountRepository.save(
                Account.builder().name("홍길동").phone("010-1111-2222").privacyAgreement(true).build());

        mockMvc.perform(post("/accounts/" + saved.getId())
                        .param("_method", "put")
                        .param("name", "김영희")
                        .param("phone", "010-9999-8888")
                        .param("privacyAgreement", "true"))
                .andExpect(status().is3xxRedirection());

        Account updated = accountRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("김영희");
        assertThat(updated.getPhone()).isEqualTo("010-9999-8888");
    }
}
