package com.adam9e96.chapter054querydslsearch.service;

import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.dto.BoardDTO;

public interface BoardService {
    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);


    void modify(BoardDTO boardDTO);

    void remove(Long bno);


}
