package com.adam9e96.chapter035fileupload.repository;

import com.adam9e96.chapter035fileupload.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    Optional<UploadedFile> findByStoredFileName(String storedFileName);
}
