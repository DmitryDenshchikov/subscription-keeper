package denshchikov.dmitry.app.service;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.repository.SubscriptionRepository;
import denshchikov.dmitry.app.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.Optional;
import java.util.UUID;

import static denshchikov.dmitry.app.util.DateUtils.currentUTC;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceUTest {

    @Mock
    JdbcAggregateTemplate jdbcAggregateTemplate;
    @Mock
    SubscriptionRepository subscriptionRepository;
    @InjectMocks
    SubscriptionService subscriptionService;

    @Test
    void createSubscription_Should_ThrowException_When_ErrorOccurredDuringSubscriptionInserting() {
        // Given
        given(jdbcAggregateTemplate.insert(any(Subscription.class))).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.createSubscription(UUID.randomUUID(), currentUTC().atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void unsubscribeUse_Should_ThrowException_When_ErrorOccurredDuringUpdatingSubscription() {
        // Given
        UUID userId = UUID.randomUUID();
        Subscription subscription = TestDataBuilder.aSubscription(userId);

        given(subscriptionRepository.findByUserIdForUpdate(userId)).willReturn(Optional.of(subscription));
        given(subscriptionRepository.save(any(Subscription.class))).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(userId, currentUTC().atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void unsubscribeUse_Should_ThrowException_When_ErrorOccurredDuringFindingSubscription() {
        // Given
        UUID userId = UUID.randomUUID();

        given(subscriptionRepository.findByUserIdForUpdate(userId)).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(userId, currentUTC().atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void resubscribeUser_Should_ThrowException_When_ErrorOccurredDuringUpdatingSubscription() {
        // Given
        UUID userId = UUID.randomUUID();
        Subscription subscription = TestDataBuilder.aSubscription(userId);
        subscription.setEndedOn(currentUTC());

        given(subscriptionRepository.findByUserIdForUpdate(userId)).willReturn(Optional.of(subscription));
        given(subscriptionRepository.save(any(Subscription.class))).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(userId, currentUTC().atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void resubscribeUser_Should_ThrowException_When_ErrorOccurredDuringFindingSubscription() {
        // Given
        UUID userId = UUID.randomUUID();

        given(subscriptionRepository.findByUserIdForUpdate(userId)).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(userId, currentUTC().atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void isSubscribed_Should_ThrowException_When_ErrorOccurredDuringFindingSubscription() {
        // Given
        UUID userId = UUID.randomUUID();

        given(subscriptionRepository.findByUserId(userId)).willThrow(RuntimeException.class);

        // When & Then
        thenThrownBy(() -> subscriptionService.isSubscribed(userId))
                .isInstanceOf(RuntimeException.class);
    }

}