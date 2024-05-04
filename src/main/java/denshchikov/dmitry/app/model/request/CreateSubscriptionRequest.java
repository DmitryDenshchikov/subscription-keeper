package denshchikov.dmitry.app.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@EqualsAndHashCode
@ToString
public class CreateSubscriptionRequest {

    private final UUID userId;
    private final ZonedDateTime startDateTime;

}
