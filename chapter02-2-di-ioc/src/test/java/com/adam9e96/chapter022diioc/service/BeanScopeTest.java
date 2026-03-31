package com.adam9e96.chapter022diioc.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeanScopeTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private NotificationService singletonService1;

    @Autowired
    private NotificationService singletonService2;

    @Test
    @DisplayName("Singleton 빈은 항상 같은 인스턴스를 반환한다")
    void singleton_returnsSameInstance() {
        assertThat(singletonService1).isSameAs(singletonService2);
    }

    @Test
    @DisplayName("Prototype 빈은 매번 새로운 인스턴스를 반환한다")
    void prototype_returnsNewInstance() {
        PrototypeCounter counter1 = applicationContext.getBean(PrototypeCounter.class);
        PrototypeCounter counter2 = applicationContext.getBean(PrototypeCounter.class);

        assertThat(counter1).isNotSameAs(counter2);
    }

    @Test
    @DisplayName("Prototype 빈의 각 인스턴스는 독립된 상태를 가진다")
    void prototype_hasIndependentState() {
        PrototypeCounter counter1 = applicationContext.getBean(PrototypeCounter.class);
        PrototypeCounter counter2 = applicationContext.getBean(PrototypeCounter.class);

        counter1.increment();
        counter1.increment();
        counter2.increment();

        assertThat(counter1.getCount()).isEqualTo(2);
        assertThat(counter2.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("@PostConstruct가 빈 초기화 시 호출된다")
    void postConstruct_isCalledOnInit() {
        NotificationLogger logger = applicationContext.getBean(NotificationLogger.class);

        assertThat(logger.isInitialized()).isTrue();
    }
}
