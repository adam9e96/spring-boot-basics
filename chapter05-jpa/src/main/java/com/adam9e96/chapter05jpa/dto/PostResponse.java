package com.adam9e96.chapter05jpa.dto;

import com.adam9e96.chapter05jpa.entity.Post;

import java.time.LocalDateTime;

/**
 * 게시글 응답 DTO.
 *
 * <p>엔티티를 API 응답에 직접 노출하지 않고 DTO로 변환하여 반환하는 패턴이다.
 * 이렇게 하면:</p>
 * <ul>
 *   <li>엔티티 구조 변경이 API 응답에 영향을 주지 않는다 (캡슐화)</li>
 *   <li>응답에 필요한 필드만 선택적으로 포함할 수 있다</li>
 *   <li>순환 참조 문제를 방지할 수 있다 (연관 엔티티가 있을 때)</li>
 * </ul>
 */
public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * 엔티티 → 응답 DTO 변환 팩토리 메서드.
     * <p>서비스 계층에서 {@code PostResponse.from(post)} 형태로 호출한다.</p>
     */
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
