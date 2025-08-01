package com.example.auth.service.impl;

import com.example.auth.dto.request.LoginRequestDTO;
import com.example.auth.dto.request.SignUpRequestDTO;
import com.example.auth.dto.request.TokenRequestDTO;
import com.example.auth.dto.response.LoginResponseDTO;
import com.example.auth.dto.response.SignUpResponseDTO;
import com.example.auth.dto.response.TokenResponseDTO;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import com.example.auth.service.AuthService;
import com.example.common.entity.dto.ResponseDTO;
import com.example.common.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
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
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .role(Role.USER)
                .name(signUpRequestDTO.getName())
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
        log.info("USER INFO : {}", user.toString());
        String accessToken = jwtUtil.generateAccessToken(entityToDTO(user));
        String refreshToken = jwtUtil.generateRefreshToken(entityToDTO(user));

        return LoginResponseDTO.success("로그인 성공", entityToDTO(user), accessToken, refreshToken);
    }

    @Override
    public ResponseEntity<? super TokenResponseDTO> refresh(TokenRequestDTO tokenRequestDTO) {
        if (!jwtUtil.validateToken(tokenRequestDTO.getRefreshToken())) {
            return TokenResponseDTO.fail("유효하지 않은 Refresh Token입니다.");
        }

        String email = jwtUtil.getEmailFromToken(tokenRequestDTO.getRefreshToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtUtil.generateAccessToken(entityToDTO(user));
        String newRefreshToken = jwtUtil.generateRefreshToken(entityToDTO(user));

        return TokenResponseDTO.success("토큰 재발급 완료",  newAccessToken, newRefreshToken);
    }
}
