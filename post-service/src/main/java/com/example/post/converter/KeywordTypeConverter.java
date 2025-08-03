package com.example.post.converter;

import com.example.post.dto.enums.KeywordType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KeywordTypeConverter implements Converter<String, KeywordType> {
    // 소문자 -> Enum 대문자 매핑
    @Override
    public KeywordType convert(String source) {
        try {
            return KeywordType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid keyword type: " + source);
        }
    }
}
