package denshchikov.dmitry.app.model.response.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SubscriptionResponse {

    private final UUID id;
    private final UUID userId;
    private final ZonedDateTime startedOn;
    private final ZonedDateTime endedOn;

}
