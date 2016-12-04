package com.su.scott.slibrary.util;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DateUtil {

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return (Calendar.getInstance().get(Calendar.MONTH) + 1);
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static String getCurrentMonthDoubleDigit() {
        return toDoubleDigit(getCurrentMonth());
    }

    public static String getCurrentDayDoubleDigit() {
        return toDoubleDigit(getCurrentDay());
    }

    private static String toDoubleDigit(int number) {
        String numberStr = String.valueOf(number);

        if (numberStr.length() == 2) {
            return numberStr;
        }

        if (numberStr.length() > 2) {
            return numberStr.substring(0, 2);
        }

        return "0" + numberStr;
    }

    public static String getYYYYMMDD() {
        return new StringBuilder()
                .append(getCurrentYear())
                .append(getCurrentMonth())
                .append(getCurrentDay())
                .toString();
    }


}
