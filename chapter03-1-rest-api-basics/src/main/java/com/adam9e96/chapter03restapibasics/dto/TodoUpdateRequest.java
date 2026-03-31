package com.adam9e96.chapter03restapibasics.dto;

// PUT 요청에서 Todo 전체 상태를 갱신할 때 받는 입력값이다.
public record TodoUpdateRequest(
        String title,
        String description,
        boolean completed
) {
}
