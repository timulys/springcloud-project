package com.example.post.dto.response;

import com.example.common.entity.dto.ResponseDTO;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UpdatePostResponseDTO extends ResponseDTO {
    public UpdatePostResponseDTO(String code, String message) {
        super(code, message);
    }
}
