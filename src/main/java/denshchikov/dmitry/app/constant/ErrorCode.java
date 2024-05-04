package denshchikov.dmitry.app.constant;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum ErrorCode {

    TECHNICAL_ERROR("72b2a223-7028-43d7-9590-8a371837196f", "Internal application error");

    private final UUID code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = UUID.fromString(code);
        this.description = description;
    }

}
