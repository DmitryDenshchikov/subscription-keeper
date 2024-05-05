package denshchikov.dmitry.app.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@EqualsAndHashCode
@ToString
public class CreateSubscriptionRequest {

    @NotNull
    private final UUID userId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final ZonedDateTime startDateTime;

}
