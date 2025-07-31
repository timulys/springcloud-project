package com.example.auth.service.impl;

import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.entity.dto.request.LoginRequestDTO;
import com.example.auth.entity.dto.request.SignUpRequestDTO;
import com.example.auth.entity.dto.response.LoginResponseDTO;
import com.example.auth.entity.dto.response.SignUpResponseDTO;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.exmple.common.entity.dto.ResponseDTO;
import com.exmple.common.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    // Autowired components
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<? super SignUpResponseDTO> signup(SignUpRequestDTO signUpRequestDTO) {
        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(signUpRequestDTO.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .build();
        userRepository.save(user);

        return SignUpResponseDTO.success("회원 가입이 완료되었습니다.");
    }

    @Override
    public ResponseEntity<? super LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()
                -> new UserNotFoundException(loginRequestDTO.getEmail()));

        if (user == null || !passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("fail", "계정 정보가 올바르지 않습니다."));
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), Map.of("role", user.getRole()));
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return LoginResponseDTO.success("로그인 성공", accessToken, refreshToken);
    }
}
