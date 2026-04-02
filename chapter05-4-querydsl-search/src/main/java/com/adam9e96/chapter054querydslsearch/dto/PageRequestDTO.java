package com.adam9e96.chapter054querydslsearch.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    // 페이징 관련 정보 외에 검색의 종류인 type의 키워드 keyword를 추가해서 지정

    @Builder.Default
    private int page =1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색의 종류 : t, c, w, tc, tw, twc

    private String keyword;

}
