# Chapter 03-2. Thymeleaf Todo CRUD

Chapter 03에서 만든 REST API를 Thymeleaf 기반 웹 UI로 확장한 챕터.
서버 사이드 렌더링(SSR) 방식으로 브라우저에서 직접 Todo CRUD를 수행할 수 있습니다.

## 학습 포인트

- `@Controller` vs `@RestController` 차이
- Thymeleaf 템플릿 엔진 (`th:each`, `th:text`, `th:href`, `th:action`, `th:value`, `th:checked`)
- `Model` 객체를 통한 뷰에 데이터 전달
- PRG(Post-Redirect-Get) 패턴 (`redirect:`)
- 폼 데이터 바인딩 (Java Record DTO)

## 페이지 목록

| 경로 | 메서드 | 설명 |
|---|---|---|
| `/todos` | GET | 전체 목록 조회 |
| `/todos/new` | GET | 새 할일 등록 폼 |
| `/todos` | POST | 할일 생성 처리 |
| `/todos/{id}` | GET | 상세 조회 |
| `/todos/{id}/edit` | GET | 수정 폼 |
| `/todos/{id}` | POST | 수정 처리 |
| `/todos/{id}/toggle` | POST | 완료/미완료 토글 |
| `/todos/{id}/delete` | POST | 삭제 처리 |

## 실행 방법

```powershell
.\gradlew.bat :chapter03-2-thymeleaf:bootRun
```

브라우저에서 http://localhost:8080/todos 접속

## 구조

```
src/main/
├── java/com/adam9e96/chapter032thymeleaf/
│   ├── controller/   # @Controller - 뷰 이름 반환
│   ├── service/      # 메모리 기반 CRUD 처리
│   ├── dto/          # 폼 바인딩용 Java Record
│   ├── model/        # Todo 도메인 객체
│   └── exception/    # 404 예외 처리
└── resources/templates/todo/
    ├── list.html     # 목록 페이지
    ├── form.html     # 생성 폼
    ├── detail.html   # 상세 페이지
    └── edit.html     # 수정 폼
```

## Chapter 03 vs 03-2 비교

| 항목 | Chapter 03 (REST API) | Chapter 03-2 (Thymeleaf) |
|---|---|---|
| 컨트롤러 | `@RestController` | `@Controller` |
| 응답 형식 | JSON | HTML (Thymeleaf) |
| 클라이언트 | Postman / curl | 웹 브라우저 |
| 데이터 전달 | `@RequestBody` | 폼 파라미터 바인딩 |
| 리다이렉트 | 해당 없음 | PRG 패턴 적용 |
