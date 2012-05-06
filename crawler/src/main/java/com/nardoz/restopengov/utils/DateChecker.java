package com.nardoz.restopengov.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateChecker {

    private static String[] formats = new String[] {
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd"
    };

    public static int compare(String str1, String str2) {

        Date date1 = findFormat(str1);
        Date date2 = findFormat(str2);

        if(date1 != null && date2 != null) {
            return date1.compareTo(date2);
        }

        return -1;
    }

    public static Date findFormat(String str) {

        Date date = null;

        for(String format : formats) {

            SimpleDateFormat sdf = new SimpleDateFormat(format);

            try {
                date = sdf.parse(str);
            } catch(ParseException e) {}

            if(date != null) {
                break;
            }
        }

        return date;
    }

}
