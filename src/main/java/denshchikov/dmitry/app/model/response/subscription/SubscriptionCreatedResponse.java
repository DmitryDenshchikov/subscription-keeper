package denshchikov.dmitry.app.model.response.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SubscriptionCreatedResponse extends BaseSubscriptionResponse{

    private final ZonedDateTime startedOn;

    public SubscriptionCreatedResponse(UUID id, UUID userId, ZonedDateTime startedOn) {
        super(id, userId);
        this.startedOn = startedOn;
    }

}
