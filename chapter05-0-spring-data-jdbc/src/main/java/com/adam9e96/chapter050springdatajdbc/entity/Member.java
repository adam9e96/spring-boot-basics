package com.adam9e96.chapter050springdatajdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Spring Data JDBC 엔티티
 * JPA의 @Entity 대신 @Id(org.springframework.data.annotation)만 사용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    /**
     * Member 번호
     */
    @Id
    private Integer id;
    /**
     * Member 이름
     */
    private String name;
}
