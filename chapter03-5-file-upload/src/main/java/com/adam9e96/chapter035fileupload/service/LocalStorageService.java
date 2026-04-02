package com.adam9e96.chapter035fileupload.service;

import com.adam9e96.chapter035fileupload.dto.FileMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 로컬 파일시스템 저장 서비스.
 * <p>
 * UUID 기반 파일명 변환, 경로 탈출(Path Traversal) 방지, 다중 파일 업로드를 지원한다.
 */
@Slf4j
@Service
public class LocalStorageService {

    private final String uploadRoot;

    public LocalStorageService(@Value("${upload.dir}") String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    /**
     * 단일 파일 저장.
     *
     * @param file   업로드된 파일
     * @param subdir 하위 디렉토리 (예: "profiles", "attachments")
     * @return 저장 결과 메타데이터
     */
    public FileMeta storeFile(MultipartFile file, String subdir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 저장할 수 없습니다.");
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        if (original.contains("..")) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다: " + original);
        }

        // UUID 기반 저장 파일명 생성
        String ext = StringUtils.getFilenameExtension(original);
        String stored = (ext == null || ext.isBlank())
                ? UUID.randomUUID().toString()
                : UUID.randomUUID() + "." + ext.toLowerCase();

        Path dir = Paths.get(uploadRoot, subdir).normalize();
        Path target = dir.resolve(stored).normalize();

        // 경로 탈출 방지
        if (!target.startsWith(dir)) {
            throw new SecurityException("경로 탈출이 감지되었습니다.");
        }

        try {
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("파일 저장 중 오류", e);
        }

        log.info("파일 저장 완료: {} -> {}", original, target);

        return new FileMeta(original, stored, subdir.replace("\\", "/"), file.getSize(), file.getContentType());
    }

    /**
     * 다중 파일 저장.
     */
    public List<FileMeta> storeFiles(MultipartFile[] files, String subdir) {
        List<FileMeta> list = new ArrayList<>();
        if (files == null) return list;
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty()) {
                list.add(storeFile(f, subdir));
            }
        }
        return list;
    }

    /**
     * 저장된 파일을 Resource로 로드 (다운로드용).
     */
    public Resource loadAsResource(String subdir, String storedFileName) {
        Path filePath = Paths.get(uploadRoot, subdir, storedFileName).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new IllegalArgumentException("파일을 찾을 수 없습니다: " + storedFileName);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 파일 경로: " + storedFileName, e);
        }
    }

    /**
     * 파일 삭제.
     */
    public boolean deleteFile(String subdir, String storedFileName) {
        Path path = Paths.get(uploadRoot, subdir, storedFileName).normalize();
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException("파일 삭제 중 오류", e);
        }
    }
}
