package denshchikov.dmitry.app.service;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static denshchikov.dmitry.app.util.DateUtils.toUTC;

@Component
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    @Transactional
    public Subscription createSubscription(UUID userId, ZonedDateTime startDateTime) {
        Objects.requireNonNull(userId, "User Id must not be null");
        Objects.requireNonNull(startDateTime, "Subscription start date must not be null");

        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUserId(userId);
        subscription.setStartedOn(toUTC(startDateTime));

        subscription = jdbcAggregateTemplate.insert(subscription);

        return subscription;
    }

    @Transactional
    public Subscription endSubscription(UUID userId, ZonedDateTime endDateTime) {
        Objects.requireNonNull(userId, "User Id must not be null");
        Objects.requireNonNull(endDateTime, "Subscription end date must not be null");

        Subscription subscription = subscriptionRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user " + userId));

        if (subscription.getEndedOn() != null) {
            throw new IllegalStateException("Subscription is already inactive for user " + userId);
        }

        subscription.setEndedOn(toUTC(endDateTime));

        subscription = subscriptionRepository.save(subscription);

        return subscription;
    }

    @Transactional
    public Subscription resubscribeUser(UUID userId, ZonedDateTime startDateTime) {
        Objects.requireNonNull(userId, "User Id must not be null");
        Objects.requireNonNull(startDateTime, "Subscription start date must not be null");

        Subscription subscription = subscriptionRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user " + userId));

        if (subscription.getEndedOn() == null) {
            throw new IllegalStateException("Subscription is already active for user " + userId);
        }

        subscription.setStartedOn(toUTC(startDateTime));
        subscription.setEndedOn(null);

        subscription = subscriptionRepository.save(subscription);

        return subscription;
    }

    public boolean isSubscribed(UUID userId) {
        Objects.requireNonNull(userId, "User Id must not be null");

        Optional<Subscription> subscription = subscriptionRepository.findByUserId(userId);

        return subscription.isPresent() && subscription.get().getEndedOn() == null;
    }

}
