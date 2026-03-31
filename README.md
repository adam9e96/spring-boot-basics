# Spring Boot Basics

Spring Boot를 멀티 모듈 형태로 가볍게 실습하는 학습 프로젝트입니다.
각 챕터를 독립적인 서브 프로젝트로 분리해서, 주제별로 실행하고 비교하기 쉽게 구성했습니다.

## 기술 스택

| 항목 | 버전 |
|------|------|
| Spring Boot | 4.0.3 |
| Java | 21 |
| Build Tool | Gradle (멀티 모듈) |
| Test | JUnit 5 |

## 학습 목표

- Spring Boot 프로젝트의 기본 실행 구조 이해
- 정적 리소스 서빙 방식 이해
- Lombok으로 반복 코드를 줄이는 방법 학습
- REST API 설계 및 CRUD 구현 흐름 학습
- 멀티 모듈 프로젝트에서 챕터별 예제를 관리하는 방법 익히기

## 프로젝트 구조

```text
spring-boot-basics/
├─ chapter01-hello/             # 가장 단순한 Spring Boot 웹 애플리케이션
├─ chapter02-1-lombok/          # Lombok 어노테이션 실습
├─ chapter03-1-rest-api-basics/ # REST API CRUD 실습
├─ build.gradle                 # 공통 플러그인/의존성 설정
└─ settings.gradle              # 멀티 모듈 등록
```

## 챕터 요약

### Chapter 01. Hello

- `spring-boot-starter-web`으로 가장 기본적인 웹 애플리케이션 구성
- `static/index.html`을 통해 정적 페이지가 어떻게 노출되는지 확인
- `@SpringBootApplication`이 애플리케이션 시작점 역할을 한다는 점 학습

실행 후 확인:

- 브라우저: `http://localhost:8080`

### Chapter 02. Lombok

- `@Getter`, `@Setter`, `@Builder`, `@ToString`
- `@NoArgsConstructor`, `@AllArgsConstructor`
- Lombok으로 생성된 메서드를 테스트와 컨트롤러에서 사용하는 흐름 확인
- `MemberTest`에서 각 어노테이션 동작을 단위 테스트로 검증

실행 후 확인:

- 브라우저: `http://localhost:8080/member`

### Chapter 03. REST API Basics

- 메모리 기반 `Todo` CRUD 구현 (`ConcurrentHashMap` + `AtomicLong`)
- `GET`, `POST`, `PUT`, `DELETE` 흐름 연습
- `@RequestBody`, `@PathVariable`, 상태 코드와 예외 응답 처리 학습
- Java Record를 활용한 DTO (`TodoCreateRequest`, `TodoUpdateRequest`)
- `@RestControllerAdvice`를 활용한 전역 예외 처리 (`GlobalExceptionHandler`)
- `TodoServiceTest`에서 CRUD 및 예외 케이스 단위 테스트

실행 후 확인:

- 브라우저 또는 API 클라이언트: `http://localhost:8080/todos`

## 실행 방법

루트에서 원하는 챕터만 선택해서 실행할 수 있습니다.

```bash
# Chapter 01
./gradlew :chapter01-hello:bootRun

# Chapter 02
./gradlew :chapter02-1-lombok:bootRun

# Chapter 03
./gradlew :chapter03-1-rest-api-basics:bootRun
```

Windows에서는 `./gradlew` 대신 `.\gradlew.bat`을 사용합니다.

## 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 챕터 테스트
./gradlew :chapter02-1-lombok:test
./gradlew :chapter03-1-rest-api-basics:test
```

## 학습 메모

- 루트 `build.gradle`에 공통 설정(`Spring Boot 플러그인`, `Java 21`, `Lombok`, `JUnit 5`)을 두고, 챕터별 `build.gradle`에는 필요한 의존성만 추가하는 방식입니다.
- DTO에 Java Record를 사용하여 불변 객체를 간결하게 표현합니다.
- 실제 실무 프로젝트보다 단순한 구조지만, 개념을 단계적으로 익히기에는 충분한 형태입니다.
