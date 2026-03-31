package com.adam9e96.chapter022diioc.controller;

import com.adam9e96.chapter022diioc.dto.NotificationRequest;
import com.adam9e96.chapter022diioc.dto.NotificationResponse;
import com.adam9e96.chapter022diioc.service.NotificationService;
import com.adam9e96.chapter022diioc.service.PrototypeCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final ApplicationContext applicationContext;

    /**
     * 기본 채널(이메일)로 알림 전송
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse send(@RequestBody NotificationRequest request) {
        return notificationService.send(request);
    }

    /**
     * 모든 채널로 알림 전송
     */
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public List<NotificationResponse> sendAll(@RequestBody NotificationRequest request) {
        return notificationService.sendAll(request);
    }

    /**
     * 전송된 알림 목록 조회
     */
    @GetMapping
    public List<NotificationResponse> findAll() {
        return notificationService.findAll();
    }

    /**
     * 사용 가능한 채널 목록 조회
     */
    @GetMapping("/channels")
    public List<String> getChannels() {
        return notificationService.getAvailableChannels();
    }

    /**
     * Singleton vs Prototype 스코프 비교 데모.
     * <p>
     * Prototype 빈은 {@code ApplicationContext.getBean()}으로 매번 새 인스턴스를 받는다.
     * Singleton 빈({@code NotificationService})은 항상 동일한 인스턴스이다.
     */
    @GetMapping("/scope-demo")
    public Map<String, Object> scopeDemo() {
        PrototypeCounter counter1 = applicationContext.getBean(PrototypeCounter.class);
        PrototypeCounter counter2 = applicationContext.getBean(PrototypeCounter.class);

        counter1.increment();
        counter2.increment();

        return Map.of(
                "prototype_같은_인스턴스인가", counter1 == counter2,
                "prototype_counter1_id", counter1.getCreatedAt(),
                "prototype_counter2_id", counter2.getCreatedAt(),
                "singleton_service_hash", System.identityHashCode(notificationService),
                "설명", "prototype은 매번 새 인스턴스, singleton은 항상 같은 인스턴스"
        );
    }
}
