package com.adam9e96.chapter022diioc.config;

import com.adam9e96.chapter022diioc.service.PrototypeCounter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Bean Scope 데모를 위한 설정 클래스.
 * <p>
 * Singleton(기본)과 Prototype 스코프의 차이를 보여준다.
 * - Singleton: 애플리케이션 전체에서 하나의 인스턴스만 존재
 * - Prototype: 요청할 때마다 새로운 인스턴스 생성
 */
@Configuration
public class AppConfig {

    @Bean
    @Scope("prototype")
    public PrototypeCounter prototypeCounter() {
        return new PrototypeCounter();
    }
}
