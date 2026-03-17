package com.adam9e96.chapter032thymeleaf.service;

import com.adam9e96.chapter032thymeleaf.dto.TodoCreateRequest;
import com.adam9e96.chapter032thymeleaf.dto.TodoUpdateRequest;
import com.adam9e96.chapter032thymeleaf.exception.TodoNotFoundException;
import com.adam9e96.chapter032thymeleaf.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {

    private final ConcurrentHashMap<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public TodoService() {
        saveSeedData("Spring Boot 학습", "Thymeleaf 기반 CRUD 구현하기");
        saveSeedData("프로젝트 설계", "멀티 모듈 프로젝트 구조 이해하기");
    }

    public List<Todo> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparingLong(Todo::getId))
                .toList();
    }

    public Todo findById(Long id) {
        Todo todo = store.get(id);
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
        store.put(id, todo);
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
        if (store.remove(id) == null) {
            throw new TodoNotFoundException(id);
        }
    }

    public void toggleComplete(Long id) {
        Todo todo = findById(id);
        todo.setCompleted(!todo.isCompleted());
    }

    private void saveSeedData(String title, String description) {
        Long id = sequence.incrementAndGet();
        store.put(id, Todo.builder()
                .id(id)
                .title(title)
                .description(description)
                .completed(false)
                .build());
    }
}
