package com.adam9e96.chapter054querydslsearch.repository;

import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    // JpaRepository 인터페이스를 상속할 때는 엔티티 타입과 @Id 타입을 지정해 주어야 하는 점을 제외하면 코드없이도 개발 가능.
    // step1. 테스트 코드로 테스트

    //

}
