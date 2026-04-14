package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateHelper {

     public DateHelper() {
     }

     private static final String TAG = "DateHelper";

    @NonNull
    public  String getDate(long milliSeconds, @NonNull String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @NonNull
    public  String formatDateFromString(@NonNull String inputFormat, @NonNull String outputFormat, @NonNull String inputDate) {
        SimpleDateFormat dfInput = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            Date date = dfInput.parse(inputDate);
            if (date != null) {
                return new SimpleDateFormat(outputFormat, Locale.US).format(date);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date", e);
        }
        return "";
    }

    @NonNull
    public  String getLastDateOfMonth(int month, int year, @NonNull String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat(format, Locale.US).format(calendar.getTime());
    }

    public long getMillisecondFromDate(@NonNull String dateString, @NonNull String format) {
        try {
            Date date = new SimpleDateFormat(format, Locale.US).parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date string", e);
        }
        return 0;
    }

    public long getStartOfDayMillisecond() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public long getCurrentGMTMillisecond() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute, int amPm) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, amPm);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute, @NonNull String amPmStr) {
        int amPm = amPmStr.equalsIgnoreCase("PM") ? Calendar.PM : Calendar.AM;
        return getMillisecond(year, month, day, hour, minute, amPm);
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public long getCurrentMillisecond() {
        return System.currentTimeMillis();
    }

    @NonNull
    public String getTimeWithAP(@NonNull String time) {
        try {
            String[] separated = time.split(":");
            int hour = Integer.parseInt(separated[0]);
            int min = Integer.parseInt(separated[1]);
            String amPm;

            if (hour == 0) {
                hour = 12;
                amPm = "AM";
            } else if (hour == 12) {
                amPm = "PM";
            } else if (hour > 12) {
                hour -= 12;
                amPm = "PM";
            } else {
                amPm = "AM";
            }
            
            return formatTwoDigits(hour) + ":" + formatTwoDigits(min) + " " + amPm;
        } catch (Exception e) {
            return time;
        }
    }

    @NonNull
    public String getDaySuffix(int n) {
        if (n < 1 || n > 31) return "";
        if (n >= 11 && n <= 13) return "th";
        switch (n % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    @NonNull
    private String extractFromDate(@NonNull String dateStr, @NonNull String format, @NonNull String outFormat) {
        try {
            Date date = new SimpleDateFormat(format, Locale.US).parse(dateStr);
            if (date != null) {
                return new SimpleDateFormat(outFormat, Locale.US).format(date);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extracting from date: " + dateStr, e);
        }
        return "";
    }

    @NonNull
    public String getFullMonth(@NonNull String dateStr, @NonNull String format) {
        return extractFromDate(dateStr, format, "MMMM");
    }

    @NonNull
    public String getShortMonth(@NonNull String dateStr, @NonNull String format) {
        return extractFromDate(dateStr, format, "MMM");
    }

    @NonNull
    public String getMonth(@NonNull String dateStr, @NonNull String format) {
        return extractFromDate(dateStr, format, "MM");
    }

    @NonNull
    public String getDay(@NonNull String dateStr, @NonNull String format) {
        return extractFromDate(dateStr, format, "dd");
    }

    @NonNull
    public String getYear(@NonNull String dateStr, @NonNull String format) {
        return extractFromDate(dateStr, format, "yyyy");
    }

    @NonNull
    public String getDaysWithPrefix(@NonNull String dateStr, @NonNull String format) {
        String dayStr = getDay(dateStr, format);
        if (dayStr.isEmpty()) return "";
        int day = Integer.parseInt(dayStr);
        return dayStr + getDaySuffix(day);
    }

    @NonNull
    public String getCurrentDateTime(boolean is24HourFormat) {
        String pattern = is24HourFormat ? "yyyy-MM-dd HH:mm" : "yyyy-MM-dd hh:mm a";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }

    @NonNull
    public String setFormatDate(int year, int month, int day, @NonNull String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return new SimpleDateFormat(format, Locale.US).format(cal.getTime());
    }

    @NonNull
    public String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
    }

    @NonNull
    public String getCurrentDate(@NonNull String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date());
    }

    @NonNull
    public String getCurrentTime(boolean is24HourFormat) {
        String pattern = is24HourFormat ? "HH:mm" : "hh:mm a";
        return new SimpleDateFormat(pattern, Locale.US).format(new Date());
    }

    public long getDayDifference(@NonNull String dateStr1, @NonNull String dateStr2) {
        return getDayDifference(dateStr1, dateStr2, "yyyy-MM-dd");
    }

    public long getDayDifference(@NonNull String dateStr1, @NonNull String dateStr2, @NonNull String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date1 = sdf.parse(dateStr1);
            Date date2 = sdf.parse(dateStr2);
            if (date1 != null && date2 != null) {
                long diff = date2.getTime() - date1.getTime();
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error calculating day difference", e);
        }
        return 0;
    }

    @NonNull
    public String getDaysAgo(@NonNull String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            Date convertedDate = sdf.parse(dateStr);
            if (convertedDate == null) return "";

            long diffInMillis = System.currentTimeMillis() - convertedDate.getTime();
            long seconds = diffInMillis / 1000;
            long days = seconds / 86400;
            long months = days / 31;
            long years = days / 365;

            if (seconds < 86400) return "today";
            if (seconds < 172800) return "yesterday";
            if (seconds < 2592000) return days + " days ago";
            if (seconds < 31104000) return (months <= 1) ? "one month ago" : months + " months ago";
            return (years <= 1) ? "one year ago" : years + " years ago";
        } catch (Exception e) {
            Log.e(TAG, "Error parsing days ago date", e);
            return "";
        }
    }

    public boolean isCurrentTimeBetween(@NonNull String startDateStr, @NonNull String endDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            String curTimeStr = sdf.format(new Date());
            return isSpecificTimeBetween(startDateStr, endDateStr, curTimeStr);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSpecificTimeBetween(@NonNull String startDateStr, @NonNull String endDateStr, @NonNull String specificTimeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            Date start = sdf.parse(startDateStr);
            Date end = sdf.parse(endDateStr);
            Date target = sdf.parse(specificTimeStr);

            if (start == null || end == null || target == null) return false;

            if (end.before(start)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(end);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                end = cal.getTime();
            }
            
            return (target.equals(start) || target.after(start)) && target.before(end);
        } catch (Exception e) {
            Log.e(TAG, "Error checking time between dates", e);
            return false;
        }
    }

    @NonNull
    public String getGMTDate(@NonNull String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    public int getTotalDaysOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public boolean isTimeAfter(@NonNull String currentTimeStr, @NonNull String targetTimeStr) {
        return isTimeAfter(currentTimeStr, targetTimeStr, "HH:mm");
    }

    public boolean isTimeAfter(@NonNull String currentTimeStr, @NonNull String targetTimeStr, @NonNull String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            Date current = sdf.parse(currentTimeStr);
            Date target = sdf.parse(targetTimeStr);
            if (current != null && target != null) {
                return target.getTime() >= current.getTime();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing time comparison", e);
        }
        return false;
    }

    @NonNull
    public String formatTwoDigits(int number) {
        return String.format(Locale.US, "%02d", number);
    }

    @NonNull
    public String getTimeHour(@NonNull String time) {
        String[] separated = time.split(":");
        return formatTwoDigits(Integer.parseInt(separated[0]));
    }

    @NonNull
    public String getTimeMin(@NonNull String time) {
        String[] separated = time.split(":");
        return formatTwoDigits(Integer.parseInt(separated[1]));
    }

    @NonNull
    public String getTimeAmPm(@NonNull String time) {
        try {
            String[] separated = time.split(":");
            int hour = Integer.parseInt(separated[0]);
            return (hour < 12) ? "AM" : "PM";
        } catch (Exception e) {
            return "AM";
        }
    }

    @NonNull
    public String getCurrentTimeWithSeconds(boolean is24HourFormat) {
        String pattern = is24HourFormat ? "HH:mm:ss" : "hh:mm:ss a";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }
}
