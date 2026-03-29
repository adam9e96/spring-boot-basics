package com.adam9e96.chapter04validation.dto;

public record MemberResponse(
        Long id,
        String name,
        String email,
        Integer age,
        String phone
) {
}
