package com.swasthyamitra.healthportal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DATE_PATTERN = "dd-MM-yyyy";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

    public static Date stringToDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        return formatter.parse(dateStr);
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }
}
