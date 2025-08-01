package com.example.post.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostFile {
    private String fileName;
    @Setter
    private int ord;
}
