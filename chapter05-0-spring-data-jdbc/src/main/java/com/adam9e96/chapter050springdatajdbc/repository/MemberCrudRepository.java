package com.adam9e96.chapter050springdatajdbc.repository;

import com.adam9e96.chapter050springdatajdbc.entity.Member;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JDBC의 CrudRepository 사용 (JpaRepository가 아님)
 * JPA 없이 SQL 매핑으로 CRUD 수행
 */
public interface MemberCrudRepository extends CrudRepository<Member, Integer> {
}
