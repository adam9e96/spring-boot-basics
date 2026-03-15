package com.adam9e96.chapter02lombok.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testLombokFeatures() {
        // Given
        Long id = 1L;
        String name = "Test User";
        String email = "test@example.com";
        int age = 25;

        // Builder가 각 필드를 올바르게 설정하는지 확인한다.
        Member member = Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .age(age)
                .build();

        // Getter가 Lombok에 의해 정상 생성되었는지 검증한다.
        assertEquals(id, member.getId());
        assertEquals(name, member.getName());
        assertEquals(email, member.getEmail());
        assertEquals(age, member.getAge());

        // Setter로 값을 바꾼 뒤 변경 결과도 확인한다.
        String newName = "Updated User";
        member.setName(newName);

        // Setter 이후 Getter가 변경된 값을 반환해야 한다.
        assertEquals(newName, member.getName());

        // toString()도 Lombok이 자동 생성하므로 주요 필드가 문자열에 포함되는지 확인한다.
        String toString = member.toString();
        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("name=" + newName));
        assertTrue(toString.contains("email=" + email));
        assertTrue(toString.contains("age=" + age));
    }

    @Test
    void testNoArgsConstructor() {
        Member member = new Member();
        assertNotNull(member);
    }

    @Test
    void testAllArgsConstructor() {
        Member member = new Member(2L, "User2", "user2@example.com", 30);
        assertEquals(2L, member.getId());
        assertEquals("User2", member.getName());
    }
}
