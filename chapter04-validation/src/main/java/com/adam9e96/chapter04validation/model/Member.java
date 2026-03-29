package com.adam9e96.chapter04validation.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Integer age;
    private String phone;
}
