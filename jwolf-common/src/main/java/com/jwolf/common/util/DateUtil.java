package com.jwolf.common.util;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Description: TODO
 *
 * @author majun
 * @version 1.0
 * @date 2021-11-26 21:30
 */
public class DateUtil {
    /**
     *  localDateTime->Date
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = localDateTime.atZone(zone).toInstant();
    return Date.from(instant);
}

}
