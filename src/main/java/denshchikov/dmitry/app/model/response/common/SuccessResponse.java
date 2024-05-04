package denshchikov.dmitry.app.model.response.common;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SuccessResponse<T> implements Response {

    private final T data;

}
