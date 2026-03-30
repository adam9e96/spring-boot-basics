package com.adam9e96.chapter05jpa.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 게시글 수정 요청 DTO.
 *
 * <p>수정 시에는 제목과 내용만 변경 가능하다.
 * 작성자(author)는 수정 대상이 아니므로 이 DTO에 포함하지 않는다.</p>
 */
public record PostUpdateRequest(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
