package dev.JustRed23.stonebrick.version;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public interface Version {

    default CompareResult compare(Version other) {
        if (buildTime().after(other.buildTime()))
            return CompareResult.NEWER;
        else if (buildTime().before(other.buildTime()))
            return CompareResult.OLDER;

        else if (getVersion().equals(other.getVersion()))
            return CompareResult.EQUAL;

        else
            return CompareResult.UNKNOWN;
    }

    String getVersion();
    Date buildTime();

    static Date getDateFromString(String date) {
        TemporalAccessor ta = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(date);
        Instant i = Instant.from(ta);
        return Date.from(i);
    }
}
