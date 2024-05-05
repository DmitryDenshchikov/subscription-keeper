package denshchikov.dmitry.app.model.response.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscriptionReactivatedResponse extends BaseSubscriptionResponse {

    private final ZonedDateTime startedOn;

    public SubscriptionReactivatedResponse(UUID id, UUID userId, ZonedDateTime startedOn) {
        super(id, userId);
        this.startedOn = startedOn;
    }

}
