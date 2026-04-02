# Chapter 03-6 — Thymeleaf Layout Dialect

## 학습 목표

**Thymeleaf Layout Dialect**를 사용하여 공통 레이아웃(header, footer, sidebar)을 한 곳에 정의하고,
각 페이지에서 **`layout:decorate`로 상속**받아 본문만 교체하는 패턴을 학습합니다.
chapter03-2에서 배운 기본 Thymeleaf를 확장하여 실무에서 사용하는 **템플릿 재사용 구조**를 구현합니다.

## 학습 포인트

- **Layout Dialect** 의존성 추가 및 설정
- `layout:decorate` — 부모 레이아웃 상속
- `layout:fragment` — 자식 페이지가 교체할 영역 지정
- `th:fragment` — 재사용 가능한 HTML 조각 선언
- `th:replace` — fragment를 현재 위치에 삽입
- Fragment 파라미터 — 페이지네이션, 검색 등 재사용 가능한 컴포넌트

## 핵심 개념: 레이아웃 상속

### 기본 Thymeleaf vs Layout Dialect

| 방식 | 기본 Thymeleaf | Layout Dialect |
|------|---------------|----------------|
| **접근** | 각 페이지에서 fragment를 개별 삽입 | 부모 레이아웃을 상속받아 본문만 교체 |
| **키워드** | `th:replace`, `th:insert` | `layout:decorate`, `layout:fragment` |
| **중복** | 매 페이지마다 `<head>`, `<body>` 구조 반복 | 한 번만 정의, 자식은 content만 작성 |
| **유지보수** | header 변경 시 모든 페이지 수정 | layout.html 한 곳만 수정 |

### 레이아웃 구조

```
layouts/layout.html          ← 마스터 레이아웃 (head, body 구조)
├── fragments/header.html    ← th:fragment="header"
├── fragments/footer.html    ← th:fragment="footer"
├── fragments/sidebar.html   ← th:fragment="sidebar"
├── fragments/pagination.html ← th:fragment="pager(...)" (파라미터 있는 fragment)
└── fragments/search.html    ← th:fragment="articleSearch(...)"

article/list.html            ← layout:decorate="~{layouts/layout}"
article/view.html            ← layout:decorate="~{layouts/layout}"
article/write.html           ← layout:decorate="~{layouts/layout}"
```

### 부모 레이아웃 (layout.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout" lang="ko">
<head>
    <title>My App</title>
    <link href="bootstrap.css" rel="stylesheet">
    <!-- 자식이 추가 CSS를 넣을 수 있는 fragment -->
    <th:block layout:fragment="css"></th:block>
</head>
<body>
    <!-- 공통 header 삽입 -->
    <header th:replace="~{fragments/header :: header}"></header>

    <!-- 자식 페이지가 교체할 영역 -->
    <div layout:fragment="content">
        <!-- 기본 내용 (자식이 override) -->
    </div>

    <aside th:replace="~{fragments/sidebar :: sidebar}"></aside>
    <footer th:replace="~{fragments/footer :: footer}"></footer>
</body>
</html>
```

### 자식 페이지 (article/list.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{layouts/layout}" lang="ko">

<!-- layout:fragment="content"만 작성하면 부모의 해당 영역이 교체됨 -->
<div layout:fragment="content">
    <h3>게시판</h3>
    <table>...</table>

    <!-- 재사용 가능한 pagination fragment (파라미터 전달) -->
    <nav th:replace="~{fragments/pagination :: pager(
        page=${list}, basePath='/', ...
    )}"></nav>
</div>
</html>
```

### Fragment에 파라미터 전달

```html
<!-- 선언: pagination.html -->
<th:block th:fragment="pager(page, basePath, field, keyword, start, end)">
    <nav>
        <li th:each="p : ${#numbers.sequence(start, end)}"
            th:classappend="${p == (page.number + 1)} ? 'active'">
            <a th:text="${p}" th:href="@{__${basePath}__(page=${p})}">1</a>
        </li>
    </nav>
</th:block>

<!-- 사용: list.html -->
<nav th:replace="~{fragments/pagination :: pager(
    page=${list}, basePath='/', field=${field}, keyword=${keyword},
    start=${startPage}, end=${endPage}
)}"></nav>
```

## 의존성 추가

```groovy
// build.gradle
implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.4.0'
```

Spring Boot가 자동으로 Layout Dialect를 등록합니다. 별도 `@Bean` 설정 불필요.

## 실행 방법

```bash
./gradlew :chapter03-6-thymeleaf-layout:bootRun
```

- 브라우저: `http://localhost:8080`

## 구조

```
com.adam9e96.chapter036thymeleaflayout/
├── controller/
│   └── ArticleController.java     ← 목록(페이지네이션+검색), 상세, 작성, 삭제
├── entity/
│   └── Article.java               ← 게시글 엔티티 (title, content, author, viewCount)
├── repository/
│   └── ArticleRepository.java     ← 검색 + 조회수 증가 쿼리
├── dto/
│   └── ArticleRequest.java        ← 작성 요청 DTO (Record)
├── config/
│   └── DataInitializer.java       ← 테스트 데이터 30건 생성
└── resources/
    ├── application.yaml
    └── templates/
        ├── layouts/layout.html    ← 마스터 레이아웃
        ├── fragments/
        │   ├── header.html        ← 네비게이션 바
        │   ├── footer.html        ← 하단 영역
        │   ├── sidebar.html       ← 사이드바 메뉴
        │   ├── pagination.html    ← 재사용 페이지네이션 (파라미터 fragment)
        │   └── search.html        ← 재사용 검색 폼 (파라미터 fragment)
        └── article/
            ├── list.html          ← 게시글 목록 (레이아웃 상속)
            ├── view.html          ← 게시글 상세
            └── write.html         ← 게시글 작성
```

## 핵심 학습 포인트

1. **`layout:decorate`**: 자식 페이지에서 부모 레이아웃을 지정. `~{layouts/layout}`으로 경로 지정
2. **`layout:fragment`**: 부모가 `layout:fragment="content"`로 교체 가능 영역을 선언하면, 자식이 같은 이름으로 override
3. **`th:fragment`**: 재사용 가능한 HTML 조각을 선언. 파라미터를 받을 수도 있음
4. **`th:replace`**: fragment를 현재 태그 자체를 대체하여 삽입 (`th:insert`는 태그 내부에 삽입)
5. **Fragment 파라미터**: `th:fragment="pager(page, basePath)"` → 호출 시 값 전달하여 재사용성 극대화
6. **레이아웃 상속 효과**: header/footer 변경 시 `layout.html` 한 곳만 수정하면 모든 페이지에 반영
