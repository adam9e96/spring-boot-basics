package com.adam9e96.chapter022diioc.service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Prototype Scope 데모용 클래스.
 * <p>
 * {@code @Scope("prototype")}으로 등록되어, 빈을 요청할 때마다 새 인스턴스가 생성된다.
 * 각 인스턴스는 독립된 카운터를 가진다.
 *
 * @see com.adam9e96.chapter022diioc.config.AppConfig
 */
public class PrototypeCounter {

    private final AtomicInteger count = new AtomicInteger(0);
    private final long createdAt = System.nanoTime();

    public int increment() {
        return count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
