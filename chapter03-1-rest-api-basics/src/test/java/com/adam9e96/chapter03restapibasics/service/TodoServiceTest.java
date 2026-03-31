package com.adam9e96.chapter03restapibasics.service;

import com.adam9e96.chapter03restapibasics.dto.TodoCreateRequest;
import com.adam9e96.chapter03restapibasics.dto.TodoUpdateRequest;
import com.adam9e96.chapter03restapibasics.exception.TodoNotFoundException;
import com.adam9e96.chapter03restapibasics.model.Todo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TodoServiceTest {

    private final TodoService todoService = new TodoService();

    @Test
    void findAllReturnsSeedData() {
        List<Todo> todos = todoService.findAll();

        assertEquals(2, todos.size());
    }

    @Test
    void createAddsNewTodo() {
        Todo created = todoService.create(new TodoCreateRequest("새 할 일", "CRUD 생성 테스트"));

        assertNotNull(created.getId());
        assertEquals("새 할 일", created.getTitle());
        assertFalse(created.isCompleted());
    }

    @Test
    void findByIdReturnsExistingTodo() {
        Todo todo = todoService.findById(1L);

        assertEquals(1L, todo.getId());
        assertNotNull(todo.getTitle());
    }

    @Test
    void updateChangesTodoFields() {
        Todo updated = todoService.update(1L, new TodoUpdateRequest(
                "수정된 제목",
                "설명도 수정",
                true
        ));

        assertEquals("수정된 제목", updated.getTitle());
        assertEquals("설명도 수정", updated.getDescription());
        assertTrue(updated.isCompleted());
    }

    @Test
    void deleteRemovesTodo() {
        todoService.delete(2L);

        assertThrows(TodoNotFoundException.class, () -> todoService.findById(2L));
    }

    @Test
    void findByIdThrowsExceptionWhenTodoDoesNotExist() {
        assertThrows(TodoNotFoundException.class, () -> todoService.findById(999L));
    }
}
