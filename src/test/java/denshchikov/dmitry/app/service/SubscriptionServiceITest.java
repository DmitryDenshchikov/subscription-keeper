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
    void endSubscription_should_endSubscription_When_CorrectDataProvidedAndNoErrorsOccurred() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionEndDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        Subscription subscription = subscriptionService.endSubscription(userId,
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
    void endSubscription_should_ThrowException_When_SubscriptionAlreadyInactive() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionEndDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        savedSubscription.setEndedOn(currentUTC());

        jdbcAggregateTemplate.insert(savedSubscription);

        // When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(userId, subscriptionEndDateTime.atZone(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void endSubscription_should_ThrowException_When_NoSubscriptionFound() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(UUID.randomUUID(), ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void endSubscription_should_ThrowException_When_UserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(null, ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void endSubscription_should_ThrowException_When_StartDateIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.endSubscription(UUID.randomUUID(), null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void resubscribeUser_should_endSubscription_When_CorrectDataProvidedAndNoErrorsOccurred() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        savedSubscription.setEndedOn(currentUTC());

        jdbcAggregateTemplate.insert(savedSubscription);

        // When
        Subscription subscription = subscriptionService.resubscribeUser(userId,
                subscriptionStartDateTime.atZone(UTC));

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
    void resubscribeUser_should_ThrowException_When_SubscriptionIsActive() {
        // Given
        UUID userId = UUID.randomUUID();
        LocalDateTime subscriptionStartDateTime = currentUTC();

        Subscription savedSubscription = aSubscription(userId);
        jdbcAggregateTemplate.insert(savedSubscription);

        // When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(userId, subscriptionStartDateTime.atZone(UTC)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void resubscribeUser_should_ThrowException_When_SubscriptionIsNotFound() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(UUID.randomUUID(), ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void resubscribeUser_should_ThrowException_When_UserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(null, ZonedDateTime.now(UTC)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void resubscribeUser_should_ThrowException_When_StartDateIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.resubscribeUser(UUID.randomUUID(), null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void isSubscribed_should_ReturnTrue_When_ActiveSubscriptionFound() {
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
    void isSubscribed_should_ReturnFalse_When_InactiveSubscriptionFound() {
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
    void isSubscribed_should_ReturnFalse_When_SubscriptionNotFound() {
        // Given & When
        boolean subscribed = subscriptionService.isSubscribed(UUID.randomUUID());

        // Then
        then(subscribed)
                .isFalse();
    }

    @Test
    void isSubscribed_should_ThrowException_When_UserIdIsNotProvided() {
        // Given & When & Then
        thenThrownBy(() -> subscriptionService.isSubscribed(null))
                .isInstanceOf(RuntimeException.class);
    }

}