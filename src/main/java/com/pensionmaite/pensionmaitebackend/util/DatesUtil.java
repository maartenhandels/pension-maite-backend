package com.pensionmaite.pensionmaitebackend.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Stream;

public class DatesUtil {

    public static int getNumberOfNights(LocalDate checkinDate, LocalDate checkoutDate) {
        return (int) ChronoUnit.DAYS.between(checkinDate, checkoutDate);
    }

    public static boolean dateRangesOverlap(LocalDate startDate1, LocalDate endDate1,
                                            LocalDate startDate2, LocalDate endDate2) {
        // Check if the first range is ongoing (i.e., the end date is null)
        if (endDate1 == null) {
            // The first range is ongoing, so it overlaps with any range that starts on or before its start date
            return startDate2.isAfter(startDate1);
        }

        // Check if the second range is ongoing (i.e., the end date is null)
        if (endDate2 == null) {
            // The second range is ongoing, so it overlaps with any range that starts on or before its start date
            return startDate1.isAfter(startDate2);
        }

        // Both ranges have end dates, so check if they overlap
        return !endDate1.isBefore(startDate2) && !endDate2.isBefore(startDate1);
    }

    public static boolean isRangeBeforeDate(LocalDate startDate1, LocalDate endDate1, LocalDate targetDate) {
        if (Stream.of(startDate1, endDate1, targetDate).anyMatch(Objects::isNull)) {
            return false;
        }
        return startDate1.isBefore(targetDate) && endDate1.isBefore(targetDate);
    }

}
