package com.adam9e96.chapter054querydslsearch.repository.search;

import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.dto.BoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
//    dafdfdf

}
