package com.adam9e96.chapter036thymeleaflayout.dto;

/**
 * 게시글 작성/수정 요청 DTO.
 */
public record ArticleRequest(
        String title,
        String content,
        String author
) {
}
