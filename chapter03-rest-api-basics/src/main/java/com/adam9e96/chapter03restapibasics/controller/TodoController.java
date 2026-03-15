package com.adam9e96.chapter03restapibasics.controller;

import com.adam9e96.chapter03restapibasics.dto.TodoCreateRequest;
import com.adam9e96.chapter03restapibasics.dto.TodoUpdateRequest;
import com.adam9e96.chapter03restapibasics.model.Todo;
import com.adam9e96.chapter03restapibasics.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RESTful CRUD 패턴을 가장 직관적으로 보여주기 위한 Todo 컨트롤러다.
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> getTodos() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody TodoCreateRequest request) {
        return todoService.create(request);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody TodoUpdateRequest request) {
        return todoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
    }
}
