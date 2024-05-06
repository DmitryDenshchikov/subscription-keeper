package denshchikov.dmitry.app.service;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import static denshchikov.dmitry.app.util.DateUtils.currentUTC;
import static denshchikov.dmitry.app.util.TestDataBuilder.aSubscription;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DataJdbcTest
@Import(SubscriptionService.class)
class SubscriptionServiceITest {

    @SpyBean
    JdbcAggregateTemplate jdbcAggregateTemplate;
    @SpyBean
    SubscriptionRepository subscriptionRepository;
    @Autowired
    SubscriptionService subscriptionService;

    @Test
    void createSubscription_Should_CreateSubscription_When_CorrectDataProvidedAndNoErrorsOccurred() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        // When
        Subscription subscription = subscriptionService.createSubscription(userId,
                subscriptionStartDateTime.atZone(UTC));

        // Then
        verify(jdbcAggregateTemplate, times(1)).insert(subscription);

        then(subscription)
                .satisfies(s -> assertThat(s.getId()).isNotNull())
                .satisfies(s -> assertThat(s.getEndedOn()).isNull())
                .extracting(
                        Subscription::getUserId,
                        Subscription::getStartedOn
                )
                .containsExactly(
                        userId,
                        subscriptionStartDateTime
                );
    }

    @Test
    void createSubscription_Should_ThrowException_When_SubscriptionForGivenUserAlreadyExists() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        // When
        subscriptionService.createSubscription(userId, subscriptionStartDateTime.atZone(UTC));

        // Then
        thenThrownBy(() -> subscriptionService.createSubscription(userId, subscriptionStartDateTime.atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createSubscription_Should_ThrowException_When_UserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.createSubscription(null, ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void createSubscription_Should_ThrowException_When_StartDateIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.createSubscription(UUID.randomUUID(), null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_EndSubscription_When_CorrectDataProvidedAndNoErrorsOccurred() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionEndDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        Subscription subscription = subscriptionService.updateSubscription(userId, null,
                subscriptionEndDateTime.atZone(UTC));

        // Then
        verify(subscriptionRepository, times(1)).save(subscription);

        then(subscription)
                .extracting(
                        Subscription::getId,
                        Subscription::getUserId,
                        Subscription::getStartedOn,
                        Subscription::getEndedOn
                )
                .containsExactly(
                        savedSubscription.getId(),
                        userId,
                        savedSubscription.getStartedOn(),
                        subscriptionEndDateTime
                );
    }

    @Test
    void updateSubscription_Should_ReactivateSubscription_When_CorrectDataProvidedAndNoErrorsOccurred() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        savedSubscription.setEndedOn(currentUTC());

        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        Subscription subscription = subscriptionService.updateSubscription(userId,
                subscriptionStartDateTime.atZone(UTC), null);

        // Then
        verify(subscriptionRepository, times(1)).save(subscription);

        then(subscription)
                .extracting(
                        Subscription::getId,
                        Subscription::getUserId,
                        Subscription::getStartedOn,
                        Subscription::getEndedOn
                )
                .containsExactly(
                        savedSubscription.getId(),
                        userId,
                        subscriptionStartDateTime,
                        null
                );
    }

    @Test
    void updateSubscription_Should_ThrowException_When_BothDatesAreNull() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(UUID.randomUUID(), null, null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_BothDatesArePresent() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(UUID.randomUUID(), ZonedDateTime.now(UTC), ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_EndingSubscriptionAndSubscriptionAlreadyInactive() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionEndDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        savedSubscription.setEndedOn(currentUTC());

        jdbcAggregateTemplate.insert(savedSubscription);

        // When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(userId, null, subscriptionEndDateTime.atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_EndingSubscriptionAndNoSubscriptionFound() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(UUID.randomUUID(), null, ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_EndingSubscriptionAndUserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(null, null, ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_ReactivatingSubscriptionAndSubscriptionIsActive() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        jdbcAggregateTemplate.insert(savedSubscription);

        // When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(userId, subscriptionStartDateTime.atZone(UTC), null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_ReactivatingSubscriptionAndSubscriptionIsNotFound() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(UUID.randomUUID(), ZonedDateTime.now(UTC), null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void updateSubscription_Should_ThrowException_When_ReactivatingSubscriptionAndUserIdIsNull() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.updateSubscription(null, ZonedDateTime.now(UTC), null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void isSubscribed_Should_ReturnTrue_When_ActiveSubscriptionFound() {
        // Given
        UUID userId = UUID.randomUUID();
        Subscription savedSubscription = aSubscription(userId);
        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        boolean result = subscriptionService.isSubscribed(userId);

        // Then
        verify(subscriptionRepository, times(1)).findByUserId(userId);

        then(result)
                .isTrue();
    }

    @Test
    void isSubscribed_Should_ReturnFalse_When_InactiveSubscriptionFound() {
        // Given
        UUID userId = UUID.randomUUID();
        Subscription savedSubscription = aSubscription(userId);
        savedSubscription.setEndedOn(currentUTC());
        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        boolean result = subscriptionService.isSubscribed(userId);

        // Then
        verify(subscriptionRepository, times(1)).findByUserId(userId);

        then(result)
                .isFalse();
    }

    @Test
    void isSubscribed_Should_ReturnFalse_When_SubscriptionNotFound() {
        // Given & When
        boolean subscribed = subscriptionService.isSubscribed(UUID.randomUUID());

        // Then
        then(subscribed)
                .isFalse();
    }

    @Test
    void isSubscribed_Should_ThrowException_When_UserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.isSubscribed(null))
                .isInstanceOf(RuntimeException.class);
    }

}