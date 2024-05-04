package denshchikov.dmitry.app.model.response.common;

import lombok.*;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ErrorResponse implements Response {

    private final ErrorDetails error;

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class ErrorDetails {

        private final UUID code;
        private final String message;

    }

}
