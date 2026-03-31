# Chapter 05 — JPA + H2 게시판 CRUD API

## 학습 목표

이 챕터에서는 **Spring Data JPA**와 **H2 인메모리 데이터베이스**를 사용하여 게시판 CRUD API를 구현합니다.
Chapter 03에서 `ConcurrentHashMap`으로 구현했던 저장소를 실제 데이터베이스(JPA)로 대체하는 과정을 경험합니다.

## 아키텍처

```
                         [Client]
                            │
                      HTTP Request
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                    Spring Boot Application               │
│                                                          │
│   ┌──────────────────────────────────────────────────┐   │
│   │  PostController (@RestController)                │   │
│   │  POST/GET/PUT/DELETE /posts                      │   │
│   │  @Valid @RequestBody → Bean Validation 실행      │   │
│   └──────────────┬───────────────────────────────────┘   │
│                  │                                       │
│          DTO ────┤ PostCreateRequest (Record)             │
│          변환    │ PostUpdateRequest (Record)             │
│                  │ PostResponse     (Record)              │
│                  ▼                                       │
│   ┌──────────────────────────────────────────────────┐   │
│   │  PostService (@Service, @Transactional)          │   │
│   │  create / findAll / findById / update / delete   │   │
│   │  findByAuthor / searchByTitle                    │   │
│   │  조회 메서드: @Transactional(readOnly = true)    │   │
│   └──────────────┬───────────────────────────────────┘   │
│                  │                                       │
│                  ▼                                       │
│   ┌──────────────────────────────────────────────────┐   │
│   │  PostRepository (JpaRepository<Post, Long>)      │   │
│   │  기본 CRUD 자동 생성 + 쿼리 메서드               │   │
│   │  findByAuthor / findByTitleContaining            │   │
│   └──────────────┬───────────────────────────────────┘   │
│                  │                                       │
│          Hibernate (JPA 구현체)                          │
│          SQL 자동 생성                                    │
│                  │                                       │
│   ┌──────────────┴───────────────────────────────────┐   │
│   │  Post (@Entity)                                  │   │
│   │  id / title / content / author                   │   │
│   │  createdAt (@CreatedDate)                        │   │
│   │  updatedAt (@LastModifiedDate)                   │   │
│   └──────────────────────────────────────────────────┘   │
│                                                          │
│   ┌──────────────────────────────────────────────────┐   │
│   │  GlobalExceptionHandler (@RestControllerAdvice)  │   │
│   │  PostNotFoundException → 404                     │   │
│   │  MethodArgumentNotValidException → 400           │   │
│   └──────────────────────────────────────────────────┘   │
│                                                          │
│   ┌──────────────────────────────────────────────────┐   │
│   │  JpaAuditingConfig (@EnableJpaAuditing)          │   │
│   │  createdAt/updatedAt 자동 관리 활성화            │   │
│   └──────────────────────────────────────────────────┘   │
│                                                          │
└──────────────────────────┬──────────────────────────────┘
                           │
                    JDBC (자동 설정)
                           │
                           ▼
                ┌─────────────────────┐
                │  H2 In-Memory DB    │
                │  jdbc:h2:mem:testdb │
                │                     │
                │  POST 테이블        │
                │  ┌───┬───────────┐  │
                │  │id │ title     │  │
                │  │   │ content   │  │
                │  │   │ author    │  │
                │  │   │ createdAt │  │
                │  │   │ updatedAt │  │
                │  └───┴───────────┘  │
                │                     │
                │  h2-console:        │
                │  localhost:8080/    │
                │  h2-console         │
                └─────────────────────┘
```

**핵심 포인트**: 코드에서 SQL을 직접 작성하는 곳은 없습니다.
`PostRepository`에 메서드 이름만 선언하면 Spring Data JPA가 SQL을 자동 생성하고,
Hibernate가 H2 DB에 실행합니다. `application.yaml`의 `show-sql: true` 설정으로
콘솔에서 자동 생성된 SQL을 확인할 수 있습니다.

## H2 데이터베이스란?

**H2**는 Java로 작성된 **인메모리 관계형 데이터베이스**입니다.

### 왜 H2를 사용하는가?

| 특징 | 설명 |
|------|------|
| **설치 불필요** | MySQL, PostgreSQL처럼 별도 설치/실행이 필요 없다. 의존성만 추가하면 끝 |
| **인메모리 모드** | 애플리케이션 시작 시 DB가 생성되고, 종료 시 사라진다. 환경 오염 없음 |
| **빠른 피드백** | DB 설정 없이 JPA 학습과 API 테스트에 바로 집중할 수 있다 |
| **SQL 호환** | 표준 SQL을 지원하므로 나중에 MySQL/PostgreSQL로 교체해도 코드 변경이 거의 없다 |
| **웹 콘솔 제공** | `http://localhost:8080/h2-console`에서 브라우저로 데이터를 직접 조회/수정 가능 |

### 학습 단계에서의 역할

```
Chapter 03: ConcurrentHashMap (순수 메모리, DB 없음)
       ↓
Chapter 05: H2 인메모리 DB (JPA + 실제 SQL 실행) ← 현재
       ↓
실무:       MySQL / PostgreSQL (운영 DB)
```

Chapter 03에서는 `ConcurrentHashMap`으로 데이터를 저장했지만, 이는 실제 DB가 아니라 SQL도 없고 테이블 구조도 없었습니다.
H2를 도입하면 **JPA가 실제 SQL을 생성하고 실행하는 과정**을 `show-sql: true` 옵션으로 직접 확인할 수 있습니다.
나중에 운영 DB로 전환할 때는 `application.yaml`의 접속 정보만 바꾸면 됩니다.

## 핵심 개념

- **JPA Entity** — `@Entity`, `@Id`, `@GeneratedValue`로 테이블과 매핑되는 자바 객체 정의
- **Spring Data JPA Repository** — `JpaRepository`를 상속하여 기본 CRUD + 쿼리 메서드 자동 생성
- **JPA Auditing** — `@CreatedDate`, `@LastModifiedDate`로 생성/수정 시간 자동 관리
- **Pageable** — `Page`, `PageRequest`, `Sort`를 활용한 페이징 및 정렬
- **Bean Validation** — `@NotBlank`, `@Valid`로 요청 데이터 유효성 검증
- **H2 Console** — 브라우저에서 `http://localhost:8080/h2-console`로 데이터 직접 확인

## 구현해야 할 파일

테스트 코드와 설정 파일은 이미 제공되어 있습니다. 아래 파일들을 직접 구현하세요.

### 1. Entity

- `entity/Post.java` — JPA 엔티티
  - `id` (Long) — `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`
  - `title` (String) — `@NotBlank`
  - `content` (String) — `@NotBlank`
  - `author` (String) — `@NotBlank`
  - `createdAt` (LocalDateTime) — `@CreatedDate`
  - `updatedAt` (LocalDateTime) — `@LastModifiedDate`
  - `@EntityListeners(AuditingEntityListener.class)` 필요
  - Lombok `@Getter`, `@Setter`, `@NoArgsConstructor` 활용

### 2. Config

- `config/JpaAuditingConfig.java` — `@Configuration` + `@EnableJpaAuditing`

### 3. Repository

- `repository/PostRepository.java` — `JpaRepository<Post, Long>` 상속
  - `findByAuthor(String author)` -> `List<Post>`
  - `findByTitleContaining(String keyword)` -> `List<Post>`

### 4. DTO (Java Record)

- `dto/PostCreateRequest.java` — `record(String title, String content, String author)` + `@NotBlank`
- `dto/PostUpdateRequest.java` — `record(String title, String content)` + `@NotBlank`
- `dto/PostResponse.java` — `record(Long id, String title, String content, String author, LocalDateTime createdAt, LocalDateTime updatedAt)`

### 5. Service

- `service/PostService.java` — `@Service`, `@Transactional`
  - `create(PostCreateRequest)` -> `PostResponse`
  - `findAll(Pageable)` -> `Page<PostResponse>`
  - `findById(Long)` -> `PostResponse`
  - `update(Long, PostUpdateRequest)` -> `PostResponse`
  - `delete(Long)`
  - `findByAuthor(String)` -> `List<PostResponse>`
  - `searchByTitle(String)` -> `List<PostResponse>`

### 6. Controller

- `controller/PostController.java` — `@RestController`, `@RequestMapping("/posts")`
  - `POST /posts` -> 201 Created
  - `GET /posts` -> 200 (페이징: `?page=0&size=10&sort=createdAt,desc`)
  - `GET /posts/{id}` -> 200
  - `PUT /posts/{id}` -> 200
  - `DELETE /posts/{id}` -> 204 No Content
  - `GET /posts/search?author=xxx` -> 200
  - `GET /posts/search?keyword=xxx` -> 200

### 7. Exception

- `exception/PostNotFoundException.java` — `RuntimeException` 상속
- `exception/GlobalExceptionHandler.java` — `@RestControllerAdvice`
  - `PostNotFoundException` -> 404 + `Map<String, String>` 응답
  - `MethodArgumentNotValidException` -> 400 응답 처리

## 실행 방법

```bash
# 애플리케이션 실행
./gradlew :chapter05-jpa:bootRun

# 테스트 실행 (구현 전에는 컴파일 에러 발생)
./gradlew :chapter05-jpa:test
```

## API 테스트 예시 (curl)

```bash
# 게시글 생성
curl -X POST http://localhost:8080/posts \
  -H "Content-Type: application/json" \
  -d '{"title":"첫 게시글","content":"안녕하세요!","author":"홍길동"}'

# 게시글 목록 조회 (페이징)
curl "http://localhost:8080/posts?page=0&size=10&sort=createdAt,desc"

# 게시글 단건 조회
curl http://localhost:8080/posts/1

# 게시글 수정
curl -X PUT http://localhost:8080/posts/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"수정된 제목","content":"수정된 내용"}'

# 게시글 삭제
curl -X DELETE http://localhost:8080/posts/1

# 작성자로 검색
curl "http://localhost:8080/posts/search?author=홍길동"

# 제목 키워드로 검색
curl "http://localhost:8080/posts/search?keyword=JPA"
```

## API 테스트 예시 (Postman)

### 1. 게시글 생성

| 항목 | 값 |
|------|-----|
| Method | `POST` |
| URL | `http://localhost:8080/posts` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | 아래 참고 |

```json
{
    "title": "첫 게시글",
    "content": "안녕하세요!",
    "author": "홍길동"
}
```

> 응답: `201 Created`

### 2. 게시글 목록 조회 (페이징)

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| URL | `http://localhost:8080/posts` |
| Params | `page=0`, `size=10`, `sort=createdAt,desc` |

Postman의 **Params** 탭에서:

| KEY | VALUE |
|-----|-------|
| page | 0 |
| size | 10 |
| sort | createdAt,desc |

> 응답: `200 OK` (Page 객체 — `content`, `totalElements`, `totalPages` 등 포함)

### 3. 게시글 단건 조회

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| URL | `http://localhost:8080/posts/1` |

> 응답: `200 OK` / 존재하지 않으면 `404 Not Found`

### 4. 게시글 수정

| 항목 | 값 |
|------|-----|
| Method | `PUT` |
| URL | `http://localhost:8080/posts/1` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | 아래 참고 |

```json
{
    "title": "수정된 제목",
    "content": "수정된 내용"
}
```

> 응답: `200 OK`

### 5. 게시글 삭제

| 항목 | 값 |
|------|-----|
| Method | `DELETE` |
| URL | `http://localhost:8080/posts/1` |

> 응답: `204 No Content` (본문 없음)

### 6. 작성자로 검색

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| URL | `http://localhost:8080/posts/search` |
| Params | `author=홍길동` |

> 응답: `200 OK` (배열)

### 7. 제목 키워드로 검색

| 항목 | 값 |
|------|-----|
| Method | `GET` |
| URL | `http://localhost:8080/posts/search` |
| Params | `keyword=JPA` |

> 응답: `200 OK` (배열)

### 8. 유효성 검증 실패 테스트

| 항목 | 값 |
|------|-----|
| Method | `POST` |
| URL | `http://localhost:8080/posts` |
| Headers | `Content-Type: application/json` |
| Body (raw JSON) | 아래 참고 |

```json
{
    "title": "",
    "content": "내용은 있음",
    "author": "홍길동"
}
```

> 응답: `400 Bad Request` (title이 빈 값이므로 `@NotBlank` 검증 실패)

## 힌트

- `Post` 엔티티에서 `@EntityListeners(AuditingEntityListener.class)`를 사용하려면 `@EnableJpaAuditing` 설정이 필요합니다.
- `PostResponse`로 변환할 때 Entity -> Record 변환 메서드(`from` 또는 `toResponse`)를 만들면 편리합니다.
- `@Valid` 어노테이션을 컨트롤러 파라미터에 붙여야 Bean Validation이 동작합니다.
- `@Transactional(readOnly = true)`를 조회 메서드에 사용하면 성능 최적화에 도움이 됩니다.
