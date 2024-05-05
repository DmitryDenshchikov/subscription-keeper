package denshchikov.dmitry.app.model.response.subscription;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class SubscriptionResponse {

    private final UUID id;
    private final UUID userId;
    private final ZonedDateTime startedOn;
    private final ZonedDateTime endedOn;

}
