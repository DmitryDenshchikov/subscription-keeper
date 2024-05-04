package denshchikov.dmitry.app.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SubscriptionResponse {

    private final UUID id;
    private final UUID userId;
    private final LocalDateTime startedOn;
    private final LocalDateTime endedOn;

}
