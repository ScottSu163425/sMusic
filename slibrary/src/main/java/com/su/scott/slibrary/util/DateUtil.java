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


    public static boolean isDateLegal(int y, int m, int d) {
        if (y <= 0 || m <= 0 || d <= 0) {
            return false;
        }

        if (m > 12) {
            return false;
        }

        if (d > 31) {
            return false;
        }

        if (m == 2 && d > 29) {
            return false;
        }

        if (m == 4 || m == 6 || m == 9 || m == 11) {
            if (d != 30) {
                return false;
            }
        }

        if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            if (d != 31) {
                return false;
            }
        }

        if (m == 2) {
            if (isLeapYear(y)) {
                if (d != 29) {
                    return false;
                }
            } else {
                if (d != 28) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isLeapYear(int year) {
        if (year < 0) {
            return false;
        }

        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        } else {
            if (year % 4 == 0) {
                return true;
            }
        }
        return false;
    }
}
