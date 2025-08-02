package com.example.common.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Post not found by post_id : " + id);
    }
}
