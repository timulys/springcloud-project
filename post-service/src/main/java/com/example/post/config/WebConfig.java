package com.example.post.config;

import com.example.post.converter.KeywordTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final KeywordTypeConverter keywordTypeConverter;

    public WebConfig(KeywordTypeConverter keywordTypeConverter) {
        this.keywordTypeConverter = keywordTypeConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(keywordTypeConverter);
    }
}
