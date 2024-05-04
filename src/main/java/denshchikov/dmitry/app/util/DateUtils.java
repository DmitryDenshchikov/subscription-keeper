package denshchikov.dmitry.app.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static LocalDateTime currentUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime toUTC(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

}
