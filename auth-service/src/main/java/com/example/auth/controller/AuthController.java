package com.example.auth.controller;

import com.example.auth.dto.request.LoginRequestDTO;
import com.example.auth.dto.request.SignUpRequestDTO;
import com.example.auth.dto.request.TokenRequestDTO;
import com.example.auth.dto.response.LoginResponseDTO;
import com.example.auth.dto.response.SignUpResponseDTO;
import com.example.auth.dto.response.TokenResponseDTO;
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

    @PostMapping("/login")
    public ResponseEntity<? super LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<? super TokenResponseDTO> refresh(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return authService.refresh(tokenRequestDTO);
    }
}
