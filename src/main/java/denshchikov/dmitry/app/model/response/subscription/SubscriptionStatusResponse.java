package denshchikov.dmitry.app.model.response.subscription;

import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class SubscriptionStatusResponse {

    private final SubscriptionStatus subscriptionStatus;

}
