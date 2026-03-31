package com.adam9e96.chapter022diioc.controller;

import tools.jackson.databind.ObjectMapper;
import com.adam9e96.chapter022diioc.dto.NotificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /notifications — 기본 채널로 알림 전송")
    void send() throws Exception {
        NotificationRequest request = new NotificationRequest("user@test.com", "테스트");

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.recipient").value("user@test.com"));
    }

    @Test
    @DisplayName("POST /notifications/all — 모든 채널로 알림 전송")
    void sendAll() throws Exception {
        NotificationRequest request = new NotificationRequest("user@test.com", "전체 전송");

        mockMvc.perform(post("/notifications/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("GET /notifications/channels — 채널 목록 조회")
    void getChannels() throws Exception {
        mockMvc.perform(get("/notifications/channels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @DisplayName("GET /notifications/scope-demo — Singleton vs Prototype 비교")
    void scopeDemo() throws Exception {
        mockMvc.perform(get("/notifications/scope-demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prototype_같은_인스턴스인가").value(false));
    }
}
