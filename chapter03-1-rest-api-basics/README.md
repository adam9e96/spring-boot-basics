# Chapter 03. REST API Basics

Spring Boot에서 가장 기본적인 RESTful CRUD 패턴을 연습하는 챕터입니다.
DB 없이 메모리 저장소를 사용해서 HTTP 메서드와 요청/응답 흐름에 집중할 수 있도록 구성했습니다.

## 학습 포인트

- `@RestController`
- `@RequestMapping`
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- `@PathVariable`
- `@RequestBody`
- `201 Created`, `204 No Content`, `404 Not Found`

## API 목록

- `GET /todos` : 전체 조회
- `GET /todos/{id}` : 단건 조회
- `POST /todos` : 생성
- `PUT /todos/{id}` : 수정
- `DELETE /todos/{id}` : 삭제

## 실행 방법

```powershell
.\gradlew.bat :chapter03-1-rest-api-basics:bootRun
```

## 요청 예시

### 생성

```http
POST /todos
Content-Type: application/json

{
  "title": "Spring Boot 복습",
  "description": "REST API CRUD 다시 정리하기"
}
```

### 수정

```http
PUT /todos/1
Content-Type: application/json

{
  "title": "수정된 제목",
  "description": "설명 수정",
  "completed": true
}
```

## 구조

- `controller`: HTTP 요청 진입점
- `service`: 메모리 기반 CRUD 처리
- `dto`: 요청 바인딩용 객체
- `model`: Todo 도메인 객체
- `exception`: 404 응답 처리

### 요청 흐름도

```mermaid
flowchart LR
    Client["Client\n(HTTP)"]

    subgraph SpringBoot["Spring Boot Application"]
        Controller["TodoController\n@RestController"]
        Service["TodoService\n@Service"]
        Store[("ConcurrentHashMap\n(메모리 저장소)")]
        ExHandler["GlobalExceptionHandler\n@RestControllerAdvice"]
    end

    Client -- "요청\nGET/POST/PUT/DELETE\n/todos" --> Controller
    Controller -- "위임" --> Service
    Service -- "CRUD" --> Store
    Service -. "TodoNotFoundException" .-> ExHandler
    ExHandler -. "404 Not Found\n{message: ...}" .-> Client
    Controller -- "200/201/204\nJSON 응답" --> Client
```

### 클래스 다이어그램

```mermaid
classDiagram
    class TodoController {
        -TodoService todoService
        +getTodos() List~Todo~
        +getTodo(id) Todo
        +createTodo(request) Todo
        +updateTodo(id, request) Todo
        +deleteTodo(id) void
    }

    class TodoService {
        -Map~Long, Todo~ todoStore
        -AtomicLong sequence
        +findAll() List~Todo~
        +findById(id) Todo
        +create(request) Todo
        +update(id, request) Todo
        +delete(id) void
    }

    class Todo {
        -Long id
        -String title
        -String description
        -boolean completed
    }

    class TodoCreateRequest {
        <<record>>
        +String title
        +String description
    }

    class TodoUpdateRequest {
        <<record>>
        +String title
        +String description
        +boolean completed
    }

    class TodoNotFoundException {
        +TodoNotFoundException(id)
    }

    class GlobalExceptionHandler {
        +handleTodoNotFound(ex) Map
    }

    TodoController --> TodoService : 의존성 주입
    TodoService --> Todo : 생성/조회/수정/삭제
    TodoService --> TodoNotFoundException : throws
    TodoController ..> TodoCreateRequest : @RequestBody
    TodoController ..> TodoUpdateRequest : @RequestBody
    GlobalExceptionHandler ..> TodoNotFoundException : @ExceptionHandler
```
