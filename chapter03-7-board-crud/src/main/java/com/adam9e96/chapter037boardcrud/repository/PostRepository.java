package com.adam9e96.chapter037boardcrud.repository;

import com.adam9e96.chapter037boardcrud.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}