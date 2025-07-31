package com.example.auth.controller;

import com.example.auth.entity.dto.request.SignUpRequestDTO;
import com.example.auth.entity.dto.response.SignUpResponseDTO;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    // Autowired Components
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<? super SignUpResponseDTO> signup(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        return authService.signup(signUpRequestDTO);
    }
}
