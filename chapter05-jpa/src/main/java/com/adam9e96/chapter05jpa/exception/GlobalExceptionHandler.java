package com.adam9e96.chapter05jpa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리기.
 *
 * <p>{@code @RestControllerAdvice} = {@code @ControllerAdvice + @ResponseBody}.
 * 모든 컨트롤러에서 발생하는 예외를 한 곳에서 일관되게 처리한다.</p>
 *
 * <p>각 메서드는 특정 예외 타입을 처리하며, {@code @ResponseStatus}로 HTTP 상태 코드를,
 * 반환값으로 에러 응답 본문(JSON)을 정의한다.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bean Validation 실패 시 발생하는 예외를 처리한다.
     * <p>컨트롤러에서 {@code @Valid}로 검증할 때 {@code @NotBlank} 등의 제약 조건을
     * 위반하면 이 메서드가 호출되어 400 Bad Request를 반환한다.</p>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    /**
     * 게시글을 찾을 수 없을 때 발생하는 예외를 처리한다.
     * <p>서비스 계층에서 {@code PostNotFoundException}을 던지면
     * 이 메서드가 잡아서 404 Not Found 응답으로 변환한다.</p>
     */
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public Map<String, String> handlePageNotFoundException(
            PostNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }


}
