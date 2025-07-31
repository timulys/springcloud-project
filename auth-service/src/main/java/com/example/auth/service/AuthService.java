package com.example.auth.service;

import com.example.auth.dto.UserDTO;
import com.example.auth.dto.request.LoginRequestDTO;
import com.example.auth.dto.request.SignUpRequestDTO;
import com.example.auth.dto.request.TokenRequestDTO;
import com.example.auth.dto.response.LoginResponseDTO;
import com.example.auth.dto.response.SignUpResponseDTO;
import com.example.auth.dto.response.TokenResponseDTO;
import com.example.auth.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<? super SignUpResponseDTO> signup(SignUpRequestDTO signUpRequestDTO);
    ResponseEntity<? super LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
    ResponseEntity<? super TokenResponseDTO> refresh(TokenRequestDTO tokenRequestDTO);

    default UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
