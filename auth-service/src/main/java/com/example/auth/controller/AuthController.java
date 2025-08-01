package com.example.auth.controller;

import com.example.auth.dto.request.LoginRequestDTO;
import com.example.auth.dto.request.SignUpRequestDTO;
import com.example.auth.dto.request.TokenRequestDTO;
import com.example.auth.dto.response.LoginResponseDTO;
import com.example.auth.dto.response.SignUpResponseDTO;
import com.example.auth.dto.response.TokenResponseDTO;
import com.example.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    // Autowired Components
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<? super SignUpResponseDTO> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
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
