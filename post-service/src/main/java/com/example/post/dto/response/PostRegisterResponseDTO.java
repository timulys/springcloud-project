package com.example.post.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PostRegisterResponseDTO extends ResponseDTO {
    private final Long id;

    public PostRegisterResponseDTO(String code, String message, Long id) {
        super(code, message);
        this.id = id;
    }

    public static ResponseEntity<? super PostRegisterResponseDTO> success(String message, Long id) {
        PostRegisterResponseDTO result = new PostRegisterResponseDTO("success", message, id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
