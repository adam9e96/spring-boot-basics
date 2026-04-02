package com.adam9e96.chapter054querydslsearch.service;

import lombok.extern.log4j.Log4j2;
import com.adam9e96.chapter054querydslsearch.dto.BoardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class BoardServiceImplTest {
    @Autowired
    private BoardService boardService;

    @Test
    public void register(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("테스트")
                .content("내용")
                .writer("작성자").build();

        Long bno = boardService.register(boardDTO);

        log.info(bno);
    }

}