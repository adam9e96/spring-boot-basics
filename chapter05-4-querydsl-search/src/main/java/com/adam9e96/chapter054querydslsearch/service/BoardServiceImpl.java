package com.adam9e96.chapter054querydslsearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.dto.BoardDTO;
import com.adam9e96.chapter054querydslsearch.repository.BoardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {
        Board board = modelMapper.map(boardDTO, Board.class);
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        Optional<Board> optionalBoard = boardRepository.findById(bno);
        Board board = optionalBoard.orElseThrow();
        return modelMapper.map(board, BoardDTO.class);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> optionalBoard = boardRepository.findById(boardDTO.getBno());
        Board board = optionalBoard.orElseThrow();
        board.change(boardDTO.getTitle(), boardDTO.getContent());
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }
}
