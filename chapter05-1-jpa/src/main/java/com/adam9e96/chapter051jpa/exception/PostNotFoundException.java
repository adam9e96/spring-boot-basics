package com.adam9e96.chapter051jpa.exception;

/**
 * 게시글을 찾을 수 없을 때 발생하는 커스텀 예외.
 *
 * <p>{@code RuntimeException}을 상속하므로 Unchecked Exception이다.
 * Checked Exception과 달리 호출 측에서 try-catch나 throws 선언을 강제하지 않는다.</p>
 *
 * <p>{@code GlobalExceptionHandler}에서 이 예외를 잡아 404 응답으로 변환한다.</p>
 */
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Page Not Found id = " + id);
    }
}
