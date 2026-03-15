package com.adam9e96.chapter03restapibasics.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Todo not found. id=" + id);
    }
}
