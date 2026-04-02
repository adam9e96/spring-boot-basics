package com.adam9e96.chapter051jpa.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 게시글 생성 요청 DTO.
 *
 * <p>Java Record로 선언하면 필드, 생성자, getter, equals/hashCode, toString이 자동 생성된다.
 * Record는 불변(immutable)이므로 setter가 없다 → DTO에 적합하다.</p>
 *
 * <p>{@code @NotBlank} — null, 빈 문자열(""), 공백 문자열("  ") 모두 거부한다.
 * 컨트롤러에서 {@code @Valid}와 함께 사용하면 요청 본문의 유효성을 자동 검증한다.</p>
 */
public record PostCreateRequest(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotBlank
        String author
) {
}
