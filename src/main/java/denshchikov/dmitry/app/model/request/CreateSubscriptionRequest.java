package denshchikov.dmitry.app.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
