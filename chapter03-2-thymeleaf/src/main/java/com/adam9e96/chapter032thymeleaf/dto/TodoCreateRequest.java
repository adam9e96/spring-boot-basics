package com.adam9e96.chapter032thymeleaf.dto;

public record TodoCreateRequest(
        String title,
        String description
) {
}
