package com.example.post.dto;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
public class UserDTO {
    private String email;
    private String name;
    private String role;

    public UserDTO(String email, String name, String role) {
        this.email = email;
        // WebFlux -> Web 한글 데이터 전달 이슈로 인한 Base64 인/디코딩
        this.name = new String(Base64.getDecoder().decode(name), StandardCharsets.UTF_8);
        this.role = role;
    }
}
