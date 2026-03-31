package com.adam9e96.chapter03restapibasics.service;

import com.adam9e96.chapter03restapibasics.dto.TodoCreateRequest;
import com.adam9e96.chapter03restapibasics.dto.TodoUpdateRequest;
import com.adam9e96.chapter03restapibasics.exception.TodoNotFoundException;
import com.adam9e96.chapter03restapibasics.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// DB 없이도 CRUD 흐름을 학습할 수 있도록 메모리 저장소를 사용하는 서비스다.
@Service
public class TodoService {

    private final Map<Long, Todo> todoStore = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    public TodoService() {
        saveSeedData("Spring Boot 공부", "chapter03 REST API 기본 흐름 익히기");
        saveSeedData("HTTP 메서드 정리", "GET, POST, PUT, DELETE 의미 정리하기");
    }

    public List<Todo> findAll() {
        return todoStore.values().stream()
                .sorted(Comparator.comparing(Todo::getId))
                .toList();
    }

    public Todo findById(Long id) {
        Todo todo = todoStore.get(id);
        if (todo == null) {
            throw new TodoNotFoundException(id);
        }
        return todo;
    }

    public Todo create(TodoCreateRequest request) {
        Long id = sequence.incrementAndGet();

        Todo todo = Todo.builder()
                .id(id)
                .title(request.title())
                .description(request.description())
                .completed(false)
                .build();

        todoStore.put(id, todo);
        return todo;
    }

    public Todo update(Long id, TodoUpdateRequest request) {
        Todo todo = findById(id);
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setCompleted(request.completed());
        return todo;
    }

    public void delete(Long id) {
        Todo removed = todoStore.remove(id);
        if (removed == null) {
            throw new TodoNotFoundException(id);
        }
    }

    private void saveSeedData(String title, String description) {
        Long id = sequence.incrementAndGet();
        todoStore.put(id, Todo.builder()
                .id(id)
                .title(title)
                .description(description)
                .completed(false)
                .build());
    }
}
