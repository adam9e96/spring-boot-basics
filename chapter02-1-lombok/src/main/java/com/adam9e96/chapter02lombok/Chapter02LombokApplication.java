package com.adam9e96.chapter02lombok;

import com.adam9e96.chapter02lombok.model.Member;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Chapter 02의 시작점이다.
// 애플리케이션이 뜰 때 CommandLineRunner를 이용해 Lombok으로 생성된 코드를 바로 실습한다.
@SpringBootApplication
public class Chapter02LombokApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Chapter02LombokApplication.class, args);
    }

    @Override
    public void run(String @NonNull ... args) {
        // Builder로 객체를 만들면 긴 생성자 호출보다 의도가 더 잘 드러난다.
        Member member1 = Member.builder()
                .id(1L)
                .name("Adam")
                .email("adam@gmail.com")
                .age(30)
                .build();

        // @ToString 덕분에 객체 상태를 바로 출력해볼 수 있다.
        System.out.println("Member1 (Builder & ToString): " + member1);

        // 기본 생성자와 Setter를 함께 써서 값을 채우는 방식도 확인한다.
        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("guilty");
        member2.setEmail("guilty@gmail.com");
        member2.setAge(25);

        // Getter도 Lombok이 자동 생성해준다.
        System.out.println("Member2 Name (Getter): " + member2.getName());
        System.out.println("Member2 (ToString): " + member2);

        // 모든 필드를 받는 생성자를 통해 한 번에 객체를 만들 수도 있다.
        Member member3 = new Member(3L, "John", "john@gmail.com", 40);
        System.out.println("Member3 (AllArgsConstructor): " + member3);
    }
}
