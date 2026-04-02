package com.adam9e96.chapter035fileupload.dto;

/**
 * 파일 저장 결과 메타데이터.
 *
 * @param originalFileName 사용자가 올린 원본 파일명
 * @param storedFileName   UUID로 변환된 저장 파일명
 * @param relativePath     저장 상대 경로 (예: "profiles")
 * @param size             파일 크기 (bytes)
 * @param contentType      MIME 타입
 */
public record FileMeta(
        String originalFileName,
        String storedFileName,
        String relativePath,
        long size,
        String contentType
) {
}
