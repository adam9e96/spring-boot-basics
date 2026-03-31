package com.adam9e96.chapter033mvcmodel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 계정 엔티티.
 * <p>
 * {@code @ModelAttribute}로 Thymeleaf 폼과 양방향 바인딩된다.
 * 폼에서 입력한 값이 이 객체의 필드에 자동으로 매핑된다.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다")
    private String phone;

    private String address;

    @NotNull
    @Builder.Default
    private Boolean privacyAgreement = false;
}
