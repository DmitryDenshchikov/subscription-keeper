package denshchikov.dmitry.app.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.ZoneOffset.UTC;

public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static LocalDateTime currentUTC() {
        return LocalDateTime.now(UTC).truncatedTo(ChronoUnit.MILLIS);
    }

    public static LocalDateTime toUTC(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(UTC).toLocalDateTime();
    }

    public static ZonedDateTime toUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(UTC);
    }

}
