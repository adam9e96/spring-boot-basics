package com.adam9e96.chapter022diioc.service;

import com.adam9e96.chapter022diioc.dto.NotificationRequest;
import com.adam9e96.chapter022diioc.dto.NotificationResponse;
import com.adam9e96.chapter022diioc.model.Notification;
import com.adam9e96.chapter022diioc.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 알림 서비스.
 * <p>
 * DI 핵심 개념 데모:
 * <ul>
 *   <li>{@code @Qualifier}: 동일 타입 빈이 여러 개일 때 특정 빈을 선택</li>
 *   <li>{@code List<NotificationSender>}: 해당 타입의 모든 빈을 컬렉션으로 주입</li>
 * </ul>
 * <p>
 * 참고: Lombok의 {@code @RequiredArgsConstructor}는 {@code @Qualifier}를 생성자 파라미터에
 * 복사하지 않으므로, 명시적 생성자를 작성한다.
 */
@Service
public class NotificationService {

    private final NotificationSender primarySender;
    private final List<NotificationSender> allSenders;
    private final NotificationRepository repository;
    private final NotificationLogger notificationLogger;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 명시적 생성자 — {@code @Qualifier}로 primarySender에 emailSender 빈을 지정한다.
     * {@code allSenders}에는 NotificationSender 타입의 모든 빈이 자동 주입된다.
     */
    public NotificationService(
            @Qualifier("emailSender") NotificationSender primarySender,
            List<NotificationSender> allSenders,
            NotificationRepository repository,
            NotificationLogger notificationLogger
    ) {
        this.primarySender = primarySender;
        this.allSenders = allSenders;
        this.repository = repository;
        this.notificationLogger = notificationLogger;
    }

    /**
     * 기본 채널(이메일)로 알림을 전송한다.
     */
    public NotificationResponse send(NotificationRequest request) {
        String result = primarySender.send(request.recipient(), request.message());
        Notification notification = saveNotification(primarySender.getChannel(), request);
        notificationLogger.log(notification);
        return NotificationResponse.from(notification);
    }

    /**
     * 등록된 모든 채널로 알림을 전송한다.
     */
    public List<NotificationResponse> sendAll(NotificationRequest request) {
        return allSenders.stream()
                .map(sender -> {
                    sender.send(request.recipient(), request.message());
                    Notification notification = saveNotification(sender.getChannel(), request);
                    notificationLogger.log(notification);
                    return NotificationResponse.from(notification);
                })
                .toList();
    }

    /**
     * 전송된 모든 알림을 조회한다.
     */
    public List<NotificationResponse> findAll() {
        return repository.findAll().stream()
                .map(NotificationResponse::from)
                .toList();
    }

    /**
     * 사용 가능한 채널 목록을 반환한다.
     */
    public List<String> getAvailableChannels() {
        return allSenders.stream()
                .map(NotificationSender::getChannel)
                .toList();
    }

    private Notification saveNotification(String channel, NotificationRequest request) {
        Notification notification = Notification.builder()
                .channel(channel)
                .recipient(request.recipient())
                .message(request.message())
                .sentAt(LocalDateTime.now().format(FORMATTER))
                .build();
        return repository.save(notification);
    }
}
