package com.example.post.dto.request;

import com.example.post.dto.enums.KeywordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String keyword = ""; // 검색용
    @Builder.Default
    private KeywordType keywordType = KeywordType.TITLE;
}
