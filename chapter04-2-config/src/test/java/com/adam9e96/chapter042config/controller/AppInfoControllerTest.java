package com.adam9e96.chapter042config.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /app/info — 앱 기본 정보를 반환한다")
    void getAppInfo() throws Exception {
        mockMvc.perform(get("/app/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spring Boot 학습 프로젝트"))
                .andExpect(jsonPath("$.version").value("1.0.0"))
                .andExpect(jsonPath("$.contactEmail").value("admin@example.com"));
    }

    @Test
    @DisplayName("GET /app/environment — 환경 정보를 반환한다")
    void getEnvironment() throws Exception {
        mockMvc.perform(get("/app/environment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.greeting").isString())
                .andExpect(jsonPath("$.apiBaseUrl").isString())
                .andExpect(jsonPath("$.apiMaxRetries").isNumber());
    }

    @Test
    @DisplayName("GET /app/features — 기능 플래그를 반환한다")
    void getFeatures() throws Exception {
        mockMvc.perform(get("/app/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationEnabled").isBoolean())
                .andExpect(jsonPath("$.maintenanceMode").isBoolean());
    }
}
