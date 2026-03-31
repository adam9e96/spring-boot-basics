package com.adam9e96.chapter04validation.service;

import com.adam9e96.chapter04validation.dto.MemberCreateRequest;
import com.adam9e96.chapter04validation.exception.MemberNotFoundException;
import com.adam9e96.chapter04validation.model.Member;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MemberService {

    private final Map<Long, Member> memberStore = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    // 회원 생성
    public Member create(MemberCreateRequest request) {
        Long id = sequence.incrementAndGet();
        Member member = Member.builder()
                .id(id)
                .name(request.name())
                .age(request.age())
                .email(request.email())
                .phone(request.phone())
                .password(request.password())
                .build();
        memberStore.put(id, member);
        return member;
    }

    // 전체 회원 목록 반환
    public List<Member> findAll() {
        return memberStore.values().stream()
                .sorted(Comparator.comparing(Member::getId))
                .toList();
    }

    // ID로 회원 조회, 없으면 MemberNotFoundException 발생
    public Member findById(Long id) {
        Member member = memberStore.get(id);
        if (member == null){
            throw new MemberNotFoundException(id);
        }
        return member;
    }
}
