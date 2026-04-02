package com.adam9e96.chapter054querydslsearch.repository;

import lombok.extern.log4j.Log4j2;
import com.adam9e96.chapter054querydslsearch.domain.Board;
import com.adam9e96.chapter054querydslsearch.repository.search.BoardSearchImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@Log4j2
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;


    // 1. insert 기능 테스트
    @Test
    public void testInsert() {
        for (int i = 1; i <= 100; i++) {
            Board board = Board.builder()
                    .title("title1...")
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO: {}", result.getBno());

        }
    }

    // 2. select 기능 테스트
    @Test
    public void testSelect() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno); // 반환형이 Optional <-- 컬렉션의 일종
        Board board = result.orElseThrow();
        // 사용하는 메서드는 findById이고 반환형은 Optional 이다.
        log.info("board select 테스트 : " + board);
    }

    // 3. update 기능 테스트
    @Test
    public void testUpdate() {
        // update 기능은 insert와 동일하게 save() 를 통해서 처리.
        // 동일한 @Id값을 가지는 객체를 생성해서 처리가능.
        // 근데 mabatis보다 비 효율적인 동작이 있다. 조금 복잡하기도 하고

        // 테스트 코드에서 change를 이용해서 update 를 수행
        Long bno = 99L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        board.change("update... title 100", "update content 100");
        boardRepository.save(board);
    }


    // JDBC나 MyBatis 방식으로 처리하면 에러
    // writer가 not-null인데 빠져있어 예외가 발생하는 것을 확인하는 테스트
    @Test
    public void testUpdate2() {
        Long bno = 100L;
        Board board = Board.builder()
                .bno(bno)
                .title("title...")
                .content("content... update2")
                .build();
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            boardRepository.save(board);
            boardRepository.flush(); // 즉시 DB 반영하여 예외 발생
        });
    }


    // 없는 bno를 지정한 경우 - H2의 IDENTITY 전략에서는 직접 ID 지정 시 예외 발생
    @Test
    public void testUpdate3() {
        Long bno = 1000L;
        Board board = Board.builder()
                .bno(bno)
                .title("title...")
                .content("content... update2")
                .writer("user...update")
                .build();
        // MariaDB에서는 insert로 동작하지만, H2 IDENTITY에서는 예외 발생 가능
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            boardRepository.save(board);
            boardRepository.flush();
        });
    }

    // delete 기능 테스트
    @Test
    public void testDelete() {
        Long bno = 2L;
        boardRepository.deleteById(bno);
    }

    // 개발자들이 처음에 JPA를 사용안했던 이유가
    // 간다한 업데이트를 하는데도 select를 2번이나 실행하는등 리소스를 쓸데없이 잡아먹어 부하면에서 불리해서 안썼는데
    // 클라우드 나오고 MSA로 추세가 갈아타지면서 조금 더 유연한? 방식이 선호하게 되었다.

    // 부하를 더 잡아먹더라도 지금은 쓴다.

    @Test
    public void testPaging() {
        // 1 page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);
    }

    @Test
    public void testPaging2() {
        // 1 page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending()); // 아 사이즈를 기준으로 계싼
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total page: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());
        // prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        List<Board> boardList = result.getContent(); // 역순으로 데이터를 가져옴 dno 역순으로.
        boardList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1() {
        // 2 page order by bno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types;
    }

    @Test
    public void testSearchAll2() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // total pages
        log.info(result.getTotalPages());

        // page size
        log.info(result.getSize());

        // pageNumber
        log.info(result.getNumber());

        // prev next
        log.info(result.hasPrevious() + ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }


}