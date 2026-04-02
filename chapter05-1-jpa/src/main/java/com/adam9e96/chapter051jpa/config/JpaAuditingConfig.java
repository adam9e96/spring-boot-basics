package com.adam9e96.chapter051jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 활성화 설정 클래스.
 *
 * <p>{@code @EnableJpaAuditing}을 선언하면 엔티티에서 사용하는
 * {@code @CreatedDate}, {@code @LastModifiedDate} 어노테이션이 동작한다.</p>
 *
 * <p>이 설정을 별도 클래스로 분리한 이유:
 * {@code @SpringBootApplication}에 직접 붙여도 되지만,
 * 테스트 시 Auditing 기능만 선택적으로 비활성화하기 어렵기 때문에
 * 별도 {@code @Configuration} 클래스로 분리하는 것이 일반적인 패턴이다.</p>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
