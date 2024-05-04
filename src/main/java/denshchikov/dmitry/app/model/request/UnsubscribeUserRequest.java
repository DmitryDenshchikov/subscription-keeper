package denshchikov.dmitry.app.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.time.ZonedDateTime;

@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@EqualsAndHashCode
@ToString
public class UnsubscribeUserRequest {

    private final ZonedDateTime endDateTime;

}
