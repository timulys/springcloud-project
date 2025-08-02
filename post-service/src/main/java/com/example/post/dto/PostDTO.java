package com.example.post.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String authorEmail;
    private String authorRole;
    private String createdAt;
    private String updatedAt;

    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();
    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();
}
