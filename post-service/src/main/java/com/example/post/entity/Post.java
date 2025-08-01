package com.example.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
    name = "posts",
    indexes = {
            @Index(name = "idx_post_title", columnList = "title"),
            @Index(name = "idx_post_author_name", columnList = "authorName")
    }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "fileNames")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT") // TEXT Type은 엔진의 최대 크기까지 저장 가능(대용량 텍스트 저장에 최적화)
    private String content;

    @Column(nullable = false)
    private String authorEmail; // 작성자 이메일 정보(추후 Auth-Service Feign Call의 파라미터로 사용, 검색조건)

    @Column(nullable = false)
    private String authorName;  // 작성자 이름(검색조건)

    private String authorRole;  // 작성자 권한

    @Builder.Default
    @ElementCollection
    @Column(name = "file_name")
    @CollectionTable(name = "post_files", joinColumns = @JoinColumn(name = "post_id"))
    private List<PostFile> fileNames = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addImage(PostFile image) {
        image.setOrd(fileNames.size());
        fileNames.add(image);
    }

    public void addImageString(String fileName) {
        PostFile postFile = PostFile.builder()
                .fileName(fileName)
                .build();
        addImage(postFile);
    }

    public void clearList() {
        this.fileNames.clear();
    }
}
