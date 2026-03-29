package com.adam9e96.chapter04validation.service;

import com.adam9e96.chapter04validation.dto.MemberCreateRequest;
import com.adam9e96.chapter04validation.exception.MemberNotFoundException;
import com.adam9e96.chapter04validation.model.Member;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MemberService 단위 테스트.
 * 스프링 컨텍스트 없이 순수 자바 단위 테스트로 서비스 로직을 검증한다.
 */
class MemberServiceTest {

    private final MemberService memberService = new MemberService();

    /**
     * 회원 생성 시 id가 자동으로 부여되고,
     * 요청 데이터가 정확히 저장되는지 확인한다.
     */
    @Test
    void create() {
        MemberCreateRequest request = new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", 25, "010-1234-5678"
        );

        Member member = memberService.create(request);

        assertNotNull(member.getId());
        assertEquals("홍길동", member.getName());
        assertEquals("hong@example.com", member.getEmail());
        assertEquals("Password1", member.getPassword());
        assertEquals(25, member.getAge());
        assertEquals("010-1234-5678", member.getPhone());
    }

    /**
     * 회원을 생성한 후 전체 목록을 조회하면
     * 생성한 회원이 목록에 포함되어야 한다.
     */
    @Test
    void findAll() {
        memberService.create(new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", 25, "010-1234-5678"
        ));
        memberService.create(new MemberCreateRequest(
                "김철수", "kim@example.com", "Password2", 30, "010-5678-1234"
        ));

        List<Member> members = memberService.findAll();

        assertTrue(members.size() >= 2);
    }

    /**
     * 생성된 회원의 id로 단건 조회 시
     * 올바른 회원 정보가 반환되는지 확인한다.
     */
    @Test
    void findById() {
        Member created = memberService.create(new MemberCreateRequest(
                "홍길동", "hong@example.com", "Password1", 25, "010-1234-5678"
        ));

        Member found = memberService.findById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("홍길동", found.getName());
    }

    /**
     * 존재하지 않는 id로 조회 시
     * MemberNotFoundException이 발생해야 한다.
     */
    @Test
    void findById_notFound() {
        assertThrows(MemberNotFoundException.class,
                () -> memberService.findById(999L));
    }
}
