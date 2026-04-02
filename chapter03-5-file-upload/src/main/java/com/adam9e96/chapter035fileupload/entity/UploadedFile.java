package com.adam9e96.chapter035fileupload.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 업로드된 파일의 메타데이터를 DB에 저장하는 엔티티.
 */
@Entity
@Table(name = "uploaded_files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String storedFileName;

    @Column(nullable = false)
    private String relativePath;

    private long fileSize;

    private String contentType;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime uploadedAt;
}
