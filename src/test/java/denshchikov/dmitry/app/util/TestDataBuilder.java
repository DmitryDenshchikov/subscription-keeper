package denshchikov.dmitry.app.util;

import denshchikov.dmitry.app.model.domain.Subscription;

import java.util.UUID;

import static denshchikov.dmitry.app.util.DateUtils.currentUTC;
import static denshchikov.dmitry.app.util.DateUtils.toUTC;

public final class TestDataBuilder {

    private TestDataBuilder() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static Subscription aSubscription(UUID userId) {
        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUserId(userId);
        subscription.setStartedOn(currentUTC());

        return subscription;
    }

}
