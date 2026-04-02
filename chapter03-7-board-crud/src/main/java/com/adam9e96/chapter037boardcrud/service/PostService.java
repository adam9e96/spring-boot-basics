package com.adam9e96.chapter037boardcrud.service;

import com.adam9e96.chapter037boardcrud.entity.Post;
import com.adam9e96.chapter037boardcrud.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}