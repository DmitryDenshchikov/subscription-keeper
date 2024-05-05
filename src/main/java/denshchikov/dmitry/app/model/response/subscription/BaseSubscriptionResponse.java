package denshchikov.dmitry.app.model.response.subscription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class BaseSubscriptionResponse {

    private final UUID id;
    private final UUID userId;

}
