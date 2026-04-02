# Chapter 02. Lombok

자바 라이브러리인 **Lombok**의 주요 기능을 학습하고 실습하는 챕터입니다.
반복적으로 작성하던 Getter, Setter, 생성자, Builder 코드를 어노테이션으로 줄이는 흐름을 확인합니다.

## 학습 포인트

- `@Getter`, `@Setter`
- `@NoArgsConstructor`, `@AllArgsConstructor`
- `@Builder`
- `@ToString`
- Lombok으로 생성된 코드가 컨트롤러와 테스트에서 어떻게 활용되는지 확인

## 프로젝트 구조

- `model/Member.java`: Lombok 어노테이션을 적용한 모델
- `controller/MemberController.java`: `Member` 객체를 JSON으로 반환하는 간단한 API
- `Chapter02LombokApplication.java`: 실행 시 콘솔에서 Lombok 사용 예시를 출력하는 진입점
- `MemberTest.java`: Lombok으로 생성된 메서드가 기대대로 동작하는지 검증하는 테스트

## 주요 코드 사용법

### Lombok 어노테이션 적용 (Member.java)

```java
@Getter           // 모든 필드의 getter 자동 생성
@Setter           // 모든 필드의 setter 자동 생성
@NoArgsConstructor // 기본 생성자 — new Member()
@AllArgsConstructor // 전체 필드 생성자 — new Member(id, name, email, age)
@Builder           // 빌더 패턴 — Member.builder().name("Adam").build()
@ToString          // toString() 자동 생성 — Member(id=1, name=Adam, ...)
public class Member {
    private Long id;
    private String name;
    private String email;
    private int age;
}
```

### Builder 패턴으로 객체 생성

```java
Member member = Member.builder()
        .id(1L)
        .name("Adam")
        .email("adam@gmail.com")
        .age(30)
        .build();
```

### Getter/Setter 사용

```java
Member member = new Member();       // @NoArgsConstructor
member.setId(2L);                    // @Setter
member.setName("guilty");
System.out.println(member.getName()); // @Getter → "guilty"
```

### AllArgsConstructor 사용

```java
Member member = new Member(3L, "John", "john@gmail.com", 40);
```

### Controller에서 Builder로 JSON 응답

```java
@RestController
public class MemberController {
    @GetMapping("/member")
    public Member getMember() {
        return Member.builder()
                .id(1L)
                .name("Spring Boot User")
                .email("user@example.com")
                .age(28)
                .build();
    }
}
```

## 실행 방법

루트 프로젝트에서:

```powershell
.\gradlew.bat :chapter02-1-lombok:bootRun
```

또는 `chapter02-1-lombok` 폴더 안에서:

```powershell
.\gradlew.bat bootRun
```

## 실행 후 확인

- 콘솔 로그: `CommandLineRunner`가 실행되며 Builder, Setter, AllArgsConstructor 예제가 출력됩니다.
- 브라우저: `http://localhost:8080/member`

예상 응답:

```json
{
  "id": 1,
  "name": "Spring Boot User",
  "email": "user@example.com",
  "age": 28
}
```

## 테스트 실행

```powershell
.\gradlew.bat :chapter02-1-lombok:test
```

테스트에서는 Builder, Getter/Setter, 생성자, `toString()` 동작을 함께 확인합니다.
