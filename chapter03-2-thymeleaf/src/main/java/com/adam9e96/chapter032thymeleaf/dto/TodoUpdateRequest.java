package com.adam9e96.chapter032thymeleaf.dto;

public record TodoUpdateRequest(
        String title,
        String description,
        boolean completed
) {
}
