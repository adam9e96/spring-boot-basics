package com.adam9e96.chapter04validation.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record MemberCreateRequest(

        @NotBlank(message = "이름은 필수입니다")
        @Size(min = 2, max = 20)
        String name,

        @Email(message = "올바른 이메일 형식이 아닙니다")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, max = 50, message = "비밀번호는 8자 이상이어야 합니다")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "비밀번호는 영문자와 숫자를 모두 포함해야 합니다")
        String password,

        @NotNull
        @Min(0)
        @Max(150)
        Integer age,

        @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다 (010-xxxx-xxxx)")
        String phone
) {
}
