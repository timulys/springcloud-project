package com.example.auth.service;

import com.example.auth.entity.dto.request.LoginRequestDTO;
import com.example.auth.entity.dto.request.SignUpRequestDTO;
import com.example.auth.entity.dto.response.LoginResponseDTO;
import com.example.auth.entity.dto.response.SignUpResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super SignUpResponseDTO> signup(SignUpRequestDTO signUpRequestDTO);
    ResponseEntity<? super LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
}
