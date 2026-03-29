package com.adam9e96.chapter04validation.exception;

// 404
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("Member not found. id=" + id);
    }
}
