# Chapter 03-3 — MVC Model & 폼 바인딩 (계정 관리)

## 학습 목표

`@RestController`(JSON API)가 아닌 **`@Controller`(뷰 반환)** 방식의 Spring MVC를 학습합니다.
Thymeleaf 폼과 `@ModelAttribute`의 양방향 바인딩, HTML 폼에서 PUT/DELETE를 사용하는 HiddenHttpMethodFilter 패턴을 실습합니다.

## 학습 포인트

- `@Controller` vs `@RestController` 차이
- `@ModelAttribute`로 폼 데이터 ↔ 객체 양방향 바인딩
- `th:object` + `th:field`로 Thymeleaf 폼 바인딩
- `BindingResult`로 검증 오류 처리 및 폼 재표시
- `RedirectAttributes.addFlashAttribute()`로 리다이렉트 시 메시지 전달
- `HiddenHttpMethodFilter`로 HTML 폼에서 PUT/DELETE 요청

## `@Controller` vs `@RestController`

| | `@Controller` | `@RestController` |
|---|---|---|
| **반환값** | 뷰 이름 (String) | 응답 본문 (JSON) |
| **동작** | Thymeleaf가 템플릿을 렌더링 | Jackson이 객체를 JSON으로 직렬화 |
| **사용 시점** | 서버 사이드 렌더링 웹 페이지 | REST API |
| **예시** | `return "account/list"` | `return accountList` |

## HiddenHttpMethodFilter란?

HTML `<form>`은 `GET`과 `POST`만 지원합니다. PUT이나 DELETE를 사용하려면:

```html
<form method="post" action="/accounts/1">
    <input type="hidden" name="_method" value="delete"/>
    <button type="submit">삭제</button>
</form>
```

`HiddenHttpMethodFilter`가 `_method` 파라미터를 읽어 실제 HTTP 메서드를 변환합니다.

```yaml
# application.yaml에서 활성화
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true
```

## 페이지 목록

| URL | 메서드 | 설명 |
|-----|--------|------|
| `/accounts` | GET | 계정 목록 |
| `/accounts/new` | GET | 계정 생성 폼 |
| `/accounts` | POST | 계정 생성 처리 |
| `/accounts/{id}` | GET | 계정 상세 |
| `/accounts/{id}/edit` | GET | 계정 수정 폼 |
| `/accounts/{id}` | PUT | 계정 수정 처리 (`_method=put`) |
| `/accounts/{id}` | DELETE | 계정 삭제 처리 (`_method=delete`) |

## 실행 방법

```bash
./gradlew :chapter03-3-mvc-model:bootRun
```

브라우저에서 `http://localhost:8080/accounts/new` 접속

## 구조

```
com.adam9e96.chapter033mvcmodel/
├── entity/
│   └── Account.java              ← @Entity + @ModelAttribute 바인딩 대상
├── repository/
│   └── AccountRepository.java    ← JpaRepository (H2 인메모리 DB)
├── service/
│   └── AccountService.java       ← @Service, @Transactional
├── controller/
│   └── AccountController.java    ← @Controller (뷰 반환)
└── templates/account/
    ├── form.html                 ← 생성 폼 (th:object + th:field)
    ├── list.html                 ← 목록 (th:each + _method=delete)
    ├── detail.html               ← 상세 보기
    └── edit.html                 ← 수정 폼 (_method=put)
```

## 구조 다이어그램

### 요청 흐름도

```mermaid
flowchart LR
    Browser["브라우저"]

    subgraph SpringBoot["Spring Boot Application"]
        Controller["AccountController\n@Controller"]
        Service["AccountService\n@Service"]
        Repo["AccountRepository\nJpaRepository"]
        Thymeleaf["Thymeleaf\n템플릿 엔진"]
        Filter["HiddenHttpMethodFilter\nPOST → PUT/DELETE 변환"]
    end

    subgraph DB["H2 Database"]
        Table[("ACCOUNT 테이블")]
    end

    Browser -- "GET /accounts/new" --> Controller
    Controller -- "model.addAttribute\n(빈 Account 객체)" --> Thymeleaf
    Thymeleaf -- "form.html 렌더링\n(th:object + th:field)" --> Browser

    Browser -- "POST /accounts\n(폼 데이터)" --> Controller
    Controller -- "@ModelAttribute\n(자동 바인딩)" --> Service
    Service -- "save" --> Repo
    Repo -- "JPA" --> Table

    Browser -- "POST + _method=delete" --> Filter
    Filter -- "DELETE로 변환" --> Controller
```

### @ModelAttribute 바인딩 흐름

```mermaid
flowchart TB
    subgraph Form["Thymeleaf 폼"]
        F1["input th:field='*{name}' → name 필드"]
        F2["input th:field='*{phone}' → phone 필드"]
        F3["input th:field='*{address}' → address 필드"]
        F4["checkbox th:field='*{privacyAgreement}'"]
    end

    subgraph Binding["@ModelAttribute 자동 바인딩"]
        Account["Account 객체\n{name, phone, address, privacyAgreement}"]
    end

    subgraph Validation["@Valid 검증"]
        BR["BindingResult\n(오류가 있으면 폼 재표시)"]
    end

    Form -- "POST 전송\n(form data)" --> Binding
    Binding -- "@Valid" --> Validation
    Validation -- "오류 없음" --> Service["AccountService.create()"]
    Validation -- "오류 있음" --> Form
```

### 클래스 다이어그램

```mermaid
classDiagram
    class AccountController {
        -AccountService accountService
        +createForm(model) String
        +create(account, bindingResult, redirectAttributes) String
        +list(model) String
        +detail(id, model) String
        +editForm(id, model) String
        +update(id, account, bindingResult, redirectAttributes) String
        +delete(id, redirectAttributes) String
    }

    class AccountService {
        -AccountRepository accountRepository
        +create(account) Account
        +findAll() List~Account~
        +findById(id) Account
        +update(id, form) Account
        +delete(id) void
    }

    class AccountRepository {
        <<interface>>
    }

    class Account {
        -Long id
        -String name ⟵ @NotBlank
        -String phone ⟵ @NotBlank
        -String address
        -Boolean privacyAgreement
    }

    AccountController --> AccountService : 의존성 주입
    AccountService --> AccountRepository : JPA CRUD
    AccountRepository --> Account : 엔티티 관리
    AccountController ..> Account : @ModelAttribute 바인딩
```

## 핵심 학습 포인트

1. **`@Controller`**: 메서드가 반환하는 String은 뷰 이름이다. Thymeleaf가 `templates/` 아래에서 해당 이름의 HTML 파일을 찾아 렌더링한다
2. **`@ModelAttribute`**: 폼 데이터를 자바 객체로 자동 변환한다. `th:object`/`th:field`와 함께 양방향 바인딩을 구성한다
3. **`BindingResult`**: `@Valid` 검증 실패 시 오류 정보를 담는다. 뷰에서 `th:errors`로 필드별 에러 메시지를 표시할 수 있다
4. **`RedirectAttributes`**: `addFlashAttribute()`로 리다이렉트 후 한 번만 보여줄 메시지(성공/삭제 알림 등)를 전달한다
5. **HiddenHttpMethodFilter**: HTML 폼의 한계(GET/POST만 지원)를 극복하여 `_method` 히든 필드로 PUT/DELETE 요청을 보낸다
6. **PRG 패턴**: Post-Redirect-Get — 폼 제출(POST) 후 리다이렉트(GET)하여 새로고침 시 중복 제출을 방지한다
