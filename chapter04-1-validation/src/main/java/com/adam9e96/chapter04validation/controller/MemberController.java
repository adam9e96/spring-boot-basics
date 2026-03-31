package com.adam9e96.chapter04validation.controller;

import com.adam9e96.chapter04validation.dto.MemberCreateRequest;
import com.adam9e96.chapter04validation.dto.MemberResponse;
import com.adam9e96.chapter04validation.model.Member;
import com.adam9e96.chapter04validation.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(@RequestBody @Valid MemberCreateRequest request) {
        Member member = memberService.create(request);
        return toResponse(member);
    }

    @GetMapping
    public List<MemberResponse> findAll() {
        return memberService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("{id}")
    public MemberResponse findById(@PathVariable Long id) {
        return toResponse(memberService.findById(id));
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getAge(),
                member.getPhone()
        );
    }
}
