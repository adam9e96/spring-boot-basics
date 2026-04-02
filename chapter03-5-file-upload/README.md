# Chapter 03-5 — 파일 업로드 (MultipartFile)

## 학습 목표

Spring Boot에서 **파일 업로드/다운로드/삭제**를 구현합니다.
`MultipartFile`을 활용한 단일·다중 업로드, UUID 기반 파일명 변환, 경로 탈출(Path Traversal) 방지까지 실무에서 필요한 파일 처리 패턴을 학습합니다.

## 학습 포인트

- `MultipartFile` — Spring이 제공하는 업로드 파일 추상화
- `enctype="multipart/form-data"` — 파일 업로드 폼 필수 속성
- UUID 기반 파일명 변환 — 파일명 충돌 방지
- Path Traversal 방지 — `..` 포함 파일명 차단, `normalize()` + `startsWith()` 검증
- `Resource` 반환 — 파일 다운로드 시 `Content-Disposition` 헤더 설정
- 파일 메타데이터 DB 저장 — 원본 파일명, 저장 파일명, 크기, 타입 관리

## 핵심 코드

### MultipartFile 설정 (application.yaml)

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB       # 단일 파일 최대 크기
      max-request-size: 10MB    # 전체 요청 최대 크기
```

### 파일 저장 서비스 — UUID 변환 + 경로 탈출 방지

```java
@Service
public class LocalStorageService {

    @Value("${upload.dir}")
    private String uploadRoot;

    public FileMeta storeFile(MultipartFile file, String subdir) {
        String original = StringUtils.cleanPath(file.getOriginalFilename());

        // 경로 탈출 시도 차단
        if (original.contains("..")) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }

        // UUID 기반 저장 파일명 생성
        String ext = StringUtils.getFilenameExtension(original);
        String stored = UUID.randomUUID() + "." + ext.toLowerCase();

        Path dir = Paths.get(uploadRoot, subdir).normalize();
        Path target = dir.resolve(stored).normalize();

        // 경로 탈출 2차 방지
        if (!target.startsWith(dir)) {
            throw new SecurityException("경로 탈출이 감지되었습니다.");
        }

        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return new FileMeta(original, stored, subdir, file.getSize(), file.getContentType());
    }
}
```

### 업로드 폼 (Thymeleaf)

```html
<!-- enctype="multipart/form-data" 필수 -->
<form th:action="@{/upload}" method="post" enctype="multipart/form-data">
    <input type="file" name="file" required>
    <button type="submit">업로드</button>
</form>

<!-- 다중 파일: multiple 속성 추가 -->
<form th:action="@{/upload/multiple}" method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple required>
    <button type="submit">업로드</button>
</form>
```

### Controller — 업로드/다운로드

```java
// 단일 업로드
@PostMapping("/upload")
public String handleUpload(@RequestParam("file") MultipartFile file) {
    FileMeta meta = storageService.storeFile(file, "attachments");
    fileRepository.save(/* ... */);
    return "redirect:/";
}

// 다운로드 — Resource + Content-Disposition 헤더
@GetMapping("/download/{id}")
public ResponseEntity<Resource> download(@PathVariable Long id) {
    UploadedFile file = fileRepository.findById(id).orElseThrow();
    Resource resource = storageService.loadAsResource(file.getRelativePath(), file.getStoredFileName());

    String encoded = URLEncoder.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encoded)
            .body(resource);
}
```

## 파일 업로드 보안 체크리스트

| 항목 | 구현 방법 |
|------|----------|
| **파일명 충돌 방지** | UUID 기반 파일명 변환 |
| **Path Traversal 방지** | `cleanPath()` + `..` 차단 + `normalize()` + `startsWith()` |
| **파일 크기 제한** | `spring.servlet.multipart.max-file-size` |
| **확장자/MIME 타입 검증** | 서비스 계층에서 허용 목록 확인 (확장 가능) |
| **원본 파일명 보존** | DB에 메타데이터로 별도 저장 |

## 실행 방법

```bash
./gradlew :chapter03-5-file-upload:bootRun
```

- 브라우저: `http://localhost:8080`

## API 목록

| Method | Path | 설명 |
|--------|------|------|
| `GET` | `/` | 업로드 폼 + 파일 목록 |
| `POST` | `/upload` | 단일 파일 업로드 |
| `POST` | `/upload/multiple` | 다중 파일 업로드 |
| `GET` | `/download/{id}` | 파일 다운로드 |
| `POST` | `/delete/{id}` | 파일 삭제 |

## 구조

```
com.adam9e96.chapter035fileupload/
├── controller/
│   └── FileUploadController.java  ← 업로드/다운로드/삭제
├── service/
│   └── LocalStorageService.java   ← UUID 파일명, 경로 탈출 방지
├── dto/
│   └── FileMeta.java              ← 저장 결과 메타데이터 (Record)
├── entity/
│   └── UploadedFile.java          ← 파일 메타 DB 엔티티
├── repository/
│   └── UploadedFileRepository.java
└── resources/
    ├── application.yaml
    └── templates/upload.html      ← 업로드 폼 + 파일 목록
```

## 핵심 학습 포인트

1. **`MultipartFile`**: Spring이 업로드 파일을 추상화한 인터페이스. `getOriginalFilename()`, `getInputStream()`, `getSize()` 등 제공
2. **UUID 파일명**: 동일 파일명 업로드 시 충돌 방지. 원본 파일명은 DB에 별도 보관
3. **경로 탈출 방지**: `../../../etc/passwd` 같은 공격을 `cleanPath()` + `normalize()` + `startsWith()`로 이중 차단
4. **`enctype="multipart/form-data"`**: 파일 업로드 시 HTML form에 반드시 지정해야 하는 인코딩 타입
5. **`Content-Disposition` 헤더**: 다운로드 시 브라우저에 파일명을 전달. 한글 파일명은 `URLEncoder`로 인코딩
6. **다중 파일**: `MultipartFile[]` 배열로 받고, `<input>` 태그에 `multiple` 속성 추가
