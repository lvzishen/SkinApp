package com.ads.lib.util;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.goodmorning.config.GlobalConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eric Tjitra on 2/27/2017.
 */

public class CalendarUtils {

    private static final String TAG = "CalendarUtils";
    private static final boolean DEBUG = GlobalConfig.DEBUG;

    // Format Pattern: https://developer.android.com/reference/java/text/SimpleDateFormat.html
    private static final String TIME_FORMAT_24H = "HH:mm";
    private static final String TIME_FORMAT_12H = "K:mm a";

    private static final String CHAR_SPACE = " ";

    public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"};

    public static long daysToMillis(int days) {
        return days * 24 * 60 * 60 * 1000L;
    }

    public static int getDaysDifference(long startMillis, long endMillis) {
        return (int) TimeUnit.DAYS.convert(endMillis - startMillis, TimeUnit.MILLISECONDS);
    }

    public static int millisToDays(long millis) {
        return (int) TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * 将毫秒数转换为 * days * hours * minutes * seconds的格式
     * @param
     * @return
     */
    public static String millisToDuring(long milliSecond) {
        StringBuilder during = new StringBuilder();

        final long days = milliSecond / (1000 * 3600 * 24);
        during.append(days > 0 ? days + " days " : "");

        final long hours = (milliSecond % (1000 * 3600 * 24)) / (1000 * 3600);
        during.append(hours > 0 ? hours + " hours " : "");

        final long minutes = (milliSecond % (1000 * 3600)) / (1000 * 60);
        during.append(minutes > 0 ? minutes + " minutes " : "");

        final long seconds = (milliSecond % (1000 * 60)) / 1000;
        during.append(seconds > 0 ? seconds + " seconds" : "");

        return String.valueOf(during);
    }

    public static boolean isCalendarInBetween(Calendar targetCal,
                                              Calendar lowBound, Calendar highBound) {
        if (targetCal == null || lowBound == null || highBound == null)
            return false;

        if (DEBUG) {
            Log.d(TAG, "isCalendarInBetween, low    : " + lowBound.getTime());
            Log.d(TAG, "isCalendarInBetween, high   : " + highBound.getTime());
            Log.d(TAG, "isCalendarInBetween, target : " + targetCal.getTime());
        }
        return targetCal.after(lowBound) && targetCal.before(highBound);
    }

    public static String getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static String getMonthName(int month) {
        if (month >= 0 && month < 12)
            return MONTH_NAMES[month];

        return null;
    }

    public static String getDateString(Context cxt, long millis, boolean autoConvert) {
        if (millis < 0)
            return null;

        Calendar currCal = Calendar.getInstance();
        Calendar targetCal = (Calendar) currCal.clone();
        targetCal.setTimeInMillis(millis);

        Calendar testYesterdayCal = (Calendar) targetCal.clone();
        testYesterdayCal.add(Calendar.DAY_OF_MONTH, 1);

        String dateText = null;
        if (!autoConvert) {
            // Use default format if autoConvert is false
            DateFormat dateFormat = DateFormat.getDateInstance();
            return dateFormat.format(targetCal.getTime());
        }

        if (DateUtils.isToday(millis)) { // Today
            dateText = cxt.getString(com.cleanerapp.supermanager.baselib.R.string.string_today);
        } else if (DateUtils.isToday(testYesterdayCal.getTimeInMillis())) { // Yesterday
            dateText = cxt.getString(com.cleanerapp.supermanager.baselib.R.string.string_yesterday);
        } else { // Others
            dateText = DateFormat.getDateInstance().format(targetCal.getTime());
        }

        return dateText;
    }

    public static String getTime(Context context, long millis) {
        if (millis < 0)
            return null;

        Calendar targetCal = Calendar.getInstance();
        targetCal.setTimeInMillis(millis);

        boolean is24h = android.text.format.DateFormat.is24HourFormat(context);
        if (DEBUG) {
            Log.d(TAG, "getTimeString | 24 hour: " + is24h);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(is24h ? TIME_FORMAT_24H : TIME_FORMAT_12H, Locale.US);
        return sdf.format(targetCal.getTime());
    }

    public static String getDateAndTime(Context cxt, long millis, String separator, boolean autoConvert) {
        StringBuilder sb = new StringBuilder();
        sb.append(getDateString(cxt, millis, autoConvert));
        sb.append(separator);
        sb.append(getTime(cxt, millis));
        return sb.toString();
    }

    public static boolean isNumberInBetween(int target, int lowerBound, int upperBound) {
        return target >= lowerBound && target <= upperBound;
    }

    /**
     * This method is basically used to extract time elements from a string
     * Example: Extract "18:10" to hour=18 and minute=10
     *
     * @param timeString Time in a string format
     * @param defHour    default hour should error occur (in 24-hour format)
     * @param defMinute  default minute should error occur (in 24-hour format)
     * @return int[0]=hour, int[1]=minute
     */
    public static int[] getTimeElements(String timeString, int defHour, int defMinute) {
        // Use default value if format is wrong
        boolean useDefault = TextUtils.isEmpty(timeString) || !timeString.contains(":");
        if (DEBUG) {
            Log.d(TAG, "isInTargetTime | useDefault:" + useDefault);
        }

        int hour;
        int minute;
        if (!useDefault) {
            String separator = ":";
            try {
                hour = Integer.valueOf(timeString.substring
                        (0, timeString.indexOf(separator)));
                minute = Integer.valueOf(timeString.substring
                        (timeString.indexOf(separator) + 1, timeString.length()));
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "isInTargetTime | " + e.toString());
                }
                return null;
            }
        } else {
            hour = defHour;
            minute = defMinute;
        }

        return new int[]{hour, minute};
    }

    /**
     * Checks if current time is in the range of the given time
     *
     * @param startHour starting hour
     * @param startMin  starting minute
     * @param endHour   end hour
     * @param endMin    end minute
     * @return {@code true} if current time is in the range of given time, or {@code false} otherwise
     */
    public static boolean isCurrentlyInTargetTime(int startHour, int startMin, int endHour, int endMin) {
        // This is the only valid hour and minute range
        if (!isNumberInBetween(startHour, 0, 23)
                || !isNumberInBetween(startMin, 0, 59)
                || !isNumberInBetween(endHour, 0, 23)
                || !isNumberInBetween(endMin, 0, 59)) {

            if (DEBUG) {
                Log.d(TAG, "isCurrentlyInTargetTime | invalid time range");
            }
            return false;
        }

        boolean is24HrMode = (endHour == startHour) && (endMin == startMin);
        if (is24HrMode) {
            if (DEBUG) {
                Log.d(TAG, "isInTargetTime | is24HrMode");
            }
            return true;
        }

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = (Calendar) startCal.clone();
        Calendar currentCal = (Calendar) startCal.clone();

        startCal.set(Calendar.HOUR_OF_DAY, startHour);
        startCal.set(Calendar.MINUTE, startMin);
        startCal.set(Calendar.SECOND, 0);

        endCal.set(Calendar.HOUR_OF_DAY, endHour);
        endCal.set(Calendar.MINUTE, endMin);
        endCal.set(Calendar.SECOND, 0);

        // Check if the end time is smaller than start time
        // Ex: start 23:00, end 02:00, or 10:15 to 10:00 (almost 24 hrs)
        boolean overclockMode = endHour <= startHour;
        if (overclockMode) {

            // The end time should be the next day
            endCal.add(Calendar.DAY_OF_MONTH, 1);

            // Though it's in overclock mode,
            // only add 1 day to current time when its hour is less than end hour.
            // Because only then will the currentCal be regarded as the next day
            if (currentCal.get(Calendar.HOUR_OF_DAY) <= endHour) {
                currentCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return isCalendarInBetween(currentCal, startCal, endCal);
    }

    /**
     * Converts minutes into "x hour(s) x minute(s)"
     *
     * @param context
     * @param minutes
     * @return
     */
    public static String getHoursAndMinutes(Context context, int minutes) {
        if (context == null || context.getResources() == null || minutes < 0)
            return null;

        StringBuilder finalStr = new StringBuilder();
        String qMinutes = context.getResources().getQuantityString(com.cleanerapp.supermanager.baselib.R.plurals.string_x_minutes, minutes);
        if (minutes <= 60) {
            finalStr.append(String.format(Locale.US, qMinutes, minutes));
        } else {
            int hour = minutes / 60;
            int remainingMinutes = minutes % 60;
            String qHour = context.getResources().getQuantityString(com.cleanerapp.supermanager.baselib.R.plurals.string_x_hours, hour);
            finalStr.append(String.format(Locale.US, qHour, hour));
            if (remainingMinutes != 0) {
                finalStr.append(CHAR_SPACE);
                finalStr.append(String.format(Locale.US, qMinutes, remainingMinutes));
            }
        }
        return finalStr.toString();
    }

}