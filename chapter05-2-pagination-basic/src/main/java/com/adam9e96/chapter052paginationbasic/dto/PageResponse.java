package com.adam9e96.chapter052paginationbasic.dto;

import java.util.List;

/**
 * 페이지네이션 응답 DTO.
 * <p>
 * Spring Data의 {@code Page<T>}를 사용하지 않고 직접 만들어서
 * 페이지네이션에 필요한 메타데이터가 무엇인지 이해한다.
 *
 * @param content       현재 페이지의 데이터 목록
 * @param pageNumber    현재 페이지 번호 (0부터 시작)
 * @param pageSize      한 페이지당 항목 수
 * @param totalElements 전체 항목 수
 * @param totalPages    전체 페이지 수
 * @param hasNext       다음 페이지 존재 여부
 * @param hasPrevious   이전 페이지 존재 여부
 */
public record PageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
) {
    /**
     * 데이터와 페이지 정보로부터 PageResponse를 생성한다.
     * <p>
     * totalPages, hasNext, hasPrevious는 자동 계산된다.
     */
    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean hasNext = pageNumber < totalPages - 1;
        boolean hasPrevious = pageNumber > 0;
        return new PageResponse<>(content, pageNumber, pageSize, totalElements, totalPages, hasNext, hasPrevious);
    }
}
