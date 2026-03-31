package com.adam9e96.chapter022diioc.service;

import com.adam9e96.chapter022diioc.dto.NotificationRequest;
import com.adam9e96.chapter022diioc.dto.NotificationResponse;
import com.adam9e96.chapter022diioc.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        NotificationSender emailSender = new EmailNotificationSender();
        NotificationSender smsSender = new SmsNotificationSender();
        NotificationSender slackSender = new SlackNotificationSender();
        List<NotificationSender> allSenders = List.of(emailSender, smsSender, slackSender);
        NotificationRepository repository = new NotificationRepository();
        NotificationLogger logger = new NotificationLogger();

        notificationService = new NotificationService(emailSender, allSenders, repository, logger);
    }

    @Test
    @DisplayName("기본 채널(이메일)로 알림을 전송한다")
    void send_withDefaultChannel() {
        NotificationRequest request = new NotificationRequest("user@example.com", "테스트 메시지");

        NotificationResponse response = notificationService.send(request);

        assertThat(response.channel()).isEqualTo("EMAIL");
        assertThat(response.recipient()).isEqualTo("user@example.com");
        assertThat(response.message()).isEqualTo("테스트 메시지");
        assertThat(response.id()).isNotNull();
    }

    @Test
    @DisplayName("모든 채널로 알림을 전송한다")
    void sendAll_toAllChannels() {
        NotificationRequest request = new NotificationRequest("user@example.com", "전체 전송");

        List<NotificationResponse> responses = notificationService.sendAll(request);

        assertThat(responses).hasSize(3);
        assertThat(responses).extracting(NotificationResponse::channel)
                .containsExactly("EMAIL", "SMS", "SLACK");
    }

    @Test
    @DisplayName("전송 이력을 조회한다")
    void findAll_afterSending() {
        notificationService.send(new NotificationRequest("a@test.com", "첫 번째"));
        notificationService.send(new NotificationRequest("b@test.com", "두 번째"));

        List<NotificationResponse> all = notificationService.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("사용 가능한 채널 목록을 반환한다")
    void getAvailableChannels() {
        List<String> channels = notificationService.getAvailableChannels();

        assertThat(channels).containsExactly("EMAIL", "SMS", "SLACK");
    }
}
