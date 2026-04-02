package com.adam9e96.chapter054querydslsearch.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.function.BooleanSupplier;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {




    // QuerydslRepositorySupport 생성자
    public BoardSearchImpl() {
        super(Board.class);
    }

    // BoardSearch 인터페이스의 구현 메소드
    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board; // Q 도메인 객체
        JPQLQuery<Board> query = from(board); // select ... from board
//        query.where(board.title.contains("1")); // where title like

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(board.title.contains("11")); // title like
        booleanBuilder.or(board.content.contains("11")); // content like

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));


        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); // 쿼리 실행 결과를 list 에 저장
        long count = query.fetchCount(); // 실행횟수 저장

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        BooleanBuilder booleanBuilder = null;
        if ((types != null && types.length > 0) && keyword != null) {// 검색 조건과 키워드가 있다면
            booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            query.where(booleanBuilder);
        } // enf if

        // bno > 0
        query.where(board.bno.gt(0L));

        // paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

//        return null;
        return new PageImpl<Board>(list, pageable, count);
    }



}
