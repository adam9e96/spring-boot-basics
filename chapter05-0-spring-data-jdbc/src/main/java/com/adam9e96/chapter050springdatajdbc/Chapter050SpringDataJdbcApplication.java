package com.adam9e96.chapter050springdatajdbc;

import com.adam9e96.chapter050springdatajdbc.entity.Member;
import com.adam9e96.chapter050springdatajdbc.repository.MemberCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Data JDBC 기본 예제
 * JPA 없이 CrudRepository를 사용하여 CRUD 수행
 */
@SpringBootApplication
public class Chapter050SpringDataJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter050SpringDataJdbcApplication.class, args)
                .getBean(Chapter050SpringDataJdbcApplication.class).execute();
    }

    @Autowired
    MemberCrudRepository repository;

    /**
     * 등록과 전체 취득 처리
     */
    private void execute() {
        // 등록
        executeInsert();
        // 전체 취득
        executeSelect();
    }

    /**
     * 등록
     */
    private void executeInsert() {
        // 엔티티 생성(id 는 자동 부여되기 때문에 null 을 설정)
        Member member = new Member(null, "이순신");
        // 리포지토리를 이용해 등록을 수행하고 결과를 취득
        member = repository.save(member);
        // 결과를 표시
        System.out.println("등록 데이터:" + member);
    }

    /**
     * 전체 취득
     */
    private void executeSelect() {
        System.out.println("--- 전체 데이터를 취득합니다 ---");
        // 리포지토리를 이용해 전체 데이터를 취득
        Iterable<Member> members = repository.findAll();
        for (Member member : members) {
            System.out.println(member);
        }
    }
}
