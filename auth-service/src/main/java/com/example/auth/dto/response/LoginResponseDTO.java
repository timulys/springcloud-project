package com.example.auth.dto.response;

import com.example.auth.dto.UserDTO;
import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class LoginResponseDTO extends ResponseDTO {
    private final UserDTO user;
    private final String accessToken;
    private final String refreshToken;

    public LoginResponseDTO(String code, String message, UserDTO user, String accessToken, String refreshToken) {
        super(code, message);
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<LoginResponseDTO> success(String message, UserDTO user, String accessToken, String refreshToken) {
        LoginResponseDTO result = new LoginResponseDTO("success", message, user, accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
