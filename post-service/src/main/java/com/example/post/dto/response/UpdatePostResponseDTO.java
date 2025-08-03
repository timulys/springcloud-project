package com.example.post.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class UpdatePostResponseDTO extends ResponseDTO {
    public UpdatePostResponseDTO(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<UpdatePostResponseDTO> success(String message) {
        UpdatePostResponseDTO result = new UpdatePostResponseDTO("success", message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
