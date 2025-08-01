package com.example.post.service;

import com.example.post.dto.UserDTO;
import com.example.post.dto.request.PostRegisterRequestDTO;
import com.example.post.dto.response.PostRegisterResponseDTO;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<? super PostRegisterResponseDTO> register(UserDTO userDTO, PostRegisterRequestDTO requestDTO);
}
