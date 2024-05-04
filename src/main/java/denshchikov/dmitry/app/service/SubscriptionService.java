package denshchikov.dmitry.app.service;

import denshchikov.dmitry.app.model.domain.Subscription;
import denshchikov.dmitry.app.model.domain.User;
import denshchikov.dmitry.app.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static denshchikov.dmitry.app.util.DateUtils.currentUTC;
import static denshchikov.dmitry.app.util.DateUtils.toUTC;

@Component
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    @Transactional
    public Subscription createSubscriptionAndUser(UUID userId, ZonedDateTime startDateTime) {
        User user = new User(userId, currentUTC(), currentUTC());
        jdbcAggregateTemplate.insert(user);

        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUserId(userId);
        subscription.setStartedOn(toUTC(startDateTime));
        subscription.setCreatedOn(currentUTC());
        subscription.setUpdatedOn(currentUTC());

        jdbcAggregateTemplate.insert(subscription);

        return subscription;
    }

    @Transactional
    public Subscription unsubscribeUser(UUID userId, ZonedDateTime endDateTime) {
        Subscription subscription = subscriptionRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user " + userId));

        subscription.setEndedOn(toUTC(endDateTime));
        subscription.setUpdatedOn(currentUTC());

        subscription = subscriptionRepository.save(subscription);

        return subscription;
    }

    @Transactional
    public Subscription resubscribeUser(UUID userId, ZonedDateTime startDateTime) {
        Subscription subscription = subscriptionRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new RuntimeException("No subscription found for user " + userId));

        subscription.setStartedOn(toUTC(startDateTime));
        subscription.setEndedOn(null);
        subscription.setUpdatedOn(currentUTC());

        subscription = subscriptionRepository.save(subscription);

        return subscription;
    }

    public boolean isSubscribed(UUID userId) {
        Optional<Subscription> subscription = subscriptionRepository.findByUserId(userId);

        return subscription.isPresent();
    }

}
