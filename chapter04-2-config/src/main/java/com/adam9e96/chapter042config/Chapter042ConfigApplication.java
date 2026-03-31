package com.adam9e96.chapter042config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * {@code @ConfigurationPropertiesScan}을 사용하면
 * {@code @ConfigurationProperties}가 붙은 Record/클래스를 자동으로 빈으로 등록한다.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Chapter042ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter042ConfigApplication.class, args);
    }
}
