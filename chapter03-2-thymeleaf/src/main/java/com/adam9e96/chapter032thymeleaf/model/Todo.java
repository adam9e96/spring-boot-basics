package com.adam9e96.chapter032thymeleaf.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Todo {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
