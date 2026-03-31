package com.adam9e96.chapter022diioc.repository;

import com.adam9e96.chapter022diioc.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@code @Repository} 스테레오타입 데모.
 * <p>
 * 데이터 접근 계층을 나타내는 어노테이션이다.
 * 기능적으로 {@code @Component}와 동일하지만, 데이터 접근 예외를 Spring의 DataAccessException으로
 * 자동 변환해주는 부가 기능이 있다.
 */
@Repository
public class NotificationRepository {

    private final Map<Long, Notification> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    public Notification save(Notification notification) {
        long id = sequence.incrementAndGet();
        notification.setId(id);
        store.put(id, notification);
        return notification;
    }

    public List<Notification> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Notification::getId).reversed())
                .toList();
    }

    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
