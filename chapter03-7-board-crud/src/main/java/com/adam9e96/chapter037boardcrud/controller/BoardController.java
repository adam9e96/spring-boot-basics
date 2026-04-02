package com.adam9e96.chapter037boardcrud.controller;

import com.adam9e96.chapter037boardcrud.entity.Post;
import com.adam9e96.chapter037boardcrud.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    @PostMapping("/posts")
    public String savePost(@ModelAttribute("post") Post post) {
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/posts/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "edit";
    }

    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute("post") Post post) {
        Post existingPost = postService.findById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.save(existingPost);
        return "redirect:/";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/";
    }
}
