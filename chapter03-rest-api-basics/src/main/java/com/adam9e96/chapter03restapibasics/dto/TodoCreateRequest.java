package com.adam9e96.chapter03restapibasics.dto;

// POST 요청에서 새 Todo를 만들 때 받는 입력값이다.
public record TodoCreateRequest(
        String title,
        String description
) {
}
