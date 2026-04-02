package com.adam9e96.chapter035fileupload.controller;

import com.adam9e96.chapter035fileupload.dto.FileMeta;
import com.adam9e96.chapter035fileupload.entity.UploadedFile;
import com.adam9e96.chapter035fileupload.repository.UploadedFileRepository;
import com.adam9e96.chapter035fileupload.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 파일 업로드/다운로드/삭제 컨트롤러.
 */
@Controller
@RequiredArgsConstructor
public class FileUploadController {

    private static final String SUBDIR = "attachments";

    private final LocalStorageService storageService;
    private final UploadedFileRepository fileRepository;

    /**
     * 업로드 폼 + 파일 목록 페이지.
     */
    @GetMapping("/")
    public String uploadPage(Model model) {
        List<UploadedFile> files = fileRepository.findAll();
        model.addAttribute("files", files);
        return "upload";
    }

    /**
     * 단일 파일 업로드 처리.
     */
    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "파일을 선택해주세요.");
            return "redirect:/";
        }

        FileMeta meta = storageService.storeFile(file, SUBDIR);

        fileRepository.save(UploadedFile.builder()
                .originalFileName(meta.originalFileName())
                .storedFileName(meta.storedFileName())
                .relativePath(meta.relativePath())
                .fileSize(meta.size())
                .contentType(meta.contentType())
                .build());

        redirectAttributes.addFlashAttribute("message",
                "업로드 성공: " + meta.originalFileName());
        return "redirect:/";
    }

    /**
     * 다중 파일 업로드 처리.
     */
    @PostMapping("/upload/multiple")
    public String handleMultipleUpload(@RequestParam("files") MultipartFile[] files,
                                       RedirectAttributes redirectAttributes) {
        List<FileMeta> metaList = storageService.storeFiles(files, SUBDIR);

        for (FileMeta meta : metaList) {
            fileRepository.save(UploadedFile.builder()
                    .originalFileName(meta.originalFileName())
                    .storedFileName(meta.storedFileName())
                    .relativePath(meta.relativePath())
                    .fileSize(meta.size())
                    .contentType(meta.contentType())
                    .build());
        }

        redirectAttributes.addFlashAttribute("message",
                metaList.size() + "개 파일 업로드 성공");
        return "redirect:/";
    }

    /**
     * 파일 다운로드.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        UploadedFile file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다: " + id));

        Resource resource = storageService.loadAsResource(file.getRelativePath(), file.getStoredFileName());

        String encodedFileName = URLEncoder.encode(file.getOriginalFileName(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    /**
     * 파일 삭제.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        UploadedFile file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다: " + id));

        storageService.deleteFile(file.getRelativePath(), file.getStoredFileName());
        fileRepository.delete(file);

        redirectAttributes.addFlashAttribute("message",
                "삭제 완료: " + file.getOriginalFileName());
        return "redirect:/";
    }
}
