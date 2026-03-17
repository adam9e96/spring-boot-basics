package com.adam9e96.chapter032thymeleaf.controller;

import com.adam9e96.chapter032thymeleaf.dto.TodoCreateRequest;
import com.adam9e96.chapter032thymeleaf.dto.TodoUpdateRequest;
import com.adam9e96.chapter032thymeleaf.model.Todo;
import com.adam9e96.chapter032thymeleaf.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * 목록 조회
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("todos", todoService.findAll());
        return "todo/list";
    }

    /**
     * 생성 폼
     */
    @GetMapping("/new")
    public String createForm() {
        return "todo/form";
    }

    /**
     * 생성 처리
     */
    @PostMapping
    public String create(TodoCreateRequest request) {
        todoService.create(request);
        return "redirect:/todos";
    }

    /**
     * 상세 조회
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.findById(id));
        return "todo/detail";
    }

    /**
     * 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("todo", todoService.findById(id));
        return "todo/edit";
    }

    /**
     * 수정 처리
     */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, TodoUpdateRequest request) {
        todoService.update(id, request);
        return "redirect:/todos/{id}";
    }

    /**
     * 완료 토글
     */
    @PostMapping("/{id}/toggle")
    public String toggleComplete(@PathVariable Long id) {
        todoService.toggleComplete(id);
        return "redirect:/todos";
    }

    /**
     * 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        todoService.delete(id);
        return "redirect:/todos";
    }
}
