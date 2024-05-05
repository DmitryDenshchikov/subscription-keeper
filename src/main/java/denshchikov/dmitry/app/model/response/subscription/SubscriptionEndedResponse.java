package denshchikov.dmitry.app.model.response.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscriptionEndedResponse extends BaseSubscriptionResponse {

    private final ZonedDateTime startedOn;
    private final ZonedDateTime endedOn;

    public SubscriptionEndedResponse(UUID id, UUID userId, ZonedDateTime startedOn, ZonedDateTime endedOn) {
        super(id, userId);
        this.startedOn = startedOn;
        this.endedOn = endedOn;
    }
}
