# Chapter 01. Hello

Spring Boot 웹 프로젝트를 가장 단순한 형태로 실행해보는 챕터입니다.
정적 HTML 파일이 기본 경로(`/`)에서 어떻게 제공되는지 확인하는 것이 핵심입니다.

## 학습 포인트

- `@SpringBootApplication`의 역할
- Spring Boot 애플리케이션 실행 방식
- `src/main/resources/static` 아래 파일이 정적 리소스로 노출되는 규칙

## 실행 방법

루트 프로젝트에서:

```powershell
.\gradlew.bat :chapter01-hello:bootRun
```

또는 `chapter01-hello` 폴더 안에서:

```powershell
.\gradlew.bat bootRun
```

## 확인 방법

- 브라우저에서 `http://localhost:8080` 접속
- `Hello World` 메시지가 보이면 정상 실행입니다.

## 파일 설명

- `Chapter01HelloApplication.java`: 애플리케이션 시작 클래스
- `application.yaml`: 애플리케이션 이름 설정
- `static/index.html`: 루트 경로에서 제공되는 정적 페이지
