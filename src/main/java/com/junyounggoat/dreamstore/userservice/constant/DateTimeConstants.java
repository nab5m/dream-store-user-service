package com.junyounggoat.dreamstore.userservice.constant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public abstract class DateTimeConstants {
    public static final LocalDateTime DB_MAX_DATETIME = LocalDateTime.of(
            LocalDate.of(9999, 12, 31),
            LocalTime.of(23, 59, 59)
    );
    public static final int ONE_MINUTE_SECONDS = 60;
    public static final int ONE_MONTH_SECONDS = ONE_MINUTE_SECONDS * 60 * 24 * 31;
    public static final int ONE_YEAR_SECONDS = ONE_MINUTE_SECONDS * 60 * 24 * 365;

    public static final int REFRESH_TOKEN_VALID_SECONDS = 10 * 24 * 60 * 60;   // 10일
}
