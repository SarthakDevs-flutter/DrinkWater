package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.util.Log;

import com.trending.water.drinking.reminder.appbasiclibs.BaseActivity;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Date_Helper extends BaseActivity {
    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String FormateDateFromString(String inputFormat, String outputFormat, String inputDate) {
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return new SimpleDateFormat(outputFormat, Locale.US).format(df_input.parse(inputDate));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLastDateOfMonth(int month, int year, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(5, calendar.getActualMaximum(5));
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    public Long getMillisecondFromDate(String givenDateString, String format) {
        long timeInMilliseconds = 0;
        try {
            timeInMilliseconds = new SimpleDateFormat(format).parse(givenDateString).getTime();
            PrintStream printStream = System.out;
            printStream.println("Date in milli :: " + timeInMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.valueOf(timeInMilliseconds);
    }

    public long getMillisecond() {
        Calendar cal = Calendar.getInstance();
        cal.set(10, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(9, 0);
        return cal.getTimeInMillis();
    }

    public long getCurrentGMTMillisecond() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, day);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(9, 0);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute, int format) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, day);
        cal.set(10, hour);
        cal.set(12, minute);
        cal.set(13, 0);
        cal.set(9, format);
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute, String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, day);
        cal.set(10, hour);
        cal.set(12, minute);
        cal.set(13, 0);
        if (format.toUpperCase().equals("PM")) {
            cal.set(9, 1);
        } else {
            cal.set(9, 0);
        }
        return cal.getTimeInMillis();
    }

    public long getMillisecond(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, day);
        cal.set(11, hour);
        cal.set(12, minute);
        cal.set(13, 0);
        return cal.getTimeInMillis();
    }

    public long getCurrentMillisecond() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String getTimeWithAP(String time) {
        String fformat;
        String[] separated = time.split(":");
        int fhour = Integer.parseInt("" + separated[0]);
        int fmin = Integer.parseInt("" + separated[1]);
        if (fhour == 0) {
            fhour += 12;
            fformat = "AM";
        } else if (fhour == 12) {
            fformat = "PM";
        } else if (fhour > 12) {
            fhour -= 12;
            fformat = "PM";
        } else {
            fformat = "AM";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.sh.get_2_point("" + fhour));
        sb.append(":");
        sb.append(this.sh.get_2_point("" + fmin));
        sb.append(" ");
        sb.append(fformat);
        return sb.toString();
    }

    public String getDaySuffix(int n) {
        if (n < 1 || n > 31) {
            return "Invalid date";
        }
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public String getFullMonth(String dateInString, String format) {
        try {
            return new SimpleDateFormat("MMMM").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getShortMonth(String dateInString, String format) {
        try {
            return new SimpleDateFormat("MMM").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getMonth(String dateInString, String format) {
        try {
            return new SimpleDateFormat("MM").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDay(String dateInString, String format) {
        try {
            return new SimpleDateFormat("dd").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getYear(String dateInString, String format) {
        try {
            return new SimpleDateFormat("yyyy").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDayswithPrefix(String dateInString, String format) {
        String formated = "0";
        try {
            formated = new SimpleDateFormat("dd").format(new SimpleDateFormat(format).parse(dateInString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formated + getDaySuffix(Integer.parseInt(formated));
    }

    public String getCurrentDateTime(boolean is24TimeFormat) {
        SimpleDateFormat dateFormat;
        if (is24TimeFormat) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm a", Locale.getDefault());
        }
        return dateFormat.format(new Date());
    }

    public String set_format_date(int year, int month, int day, String format) {
        return new SimpleDateFormat(format).format(new Date(year, month, day));
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
    }

    public String getFormatDate(String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date());
    }

    public String getCurrentDate(String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date());
    }

    public String getCurrentTime(boolean is24TimeFormat) {
        SimpleDateFormat dateFormat;
        if (is24TimeFormat) {
            dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        } else {
            dateFormat = new SimpleDateFormat("KK:mm a", Locale.US);
        }
        return dateFormat.format(new Date());
    }

    public long DayDifferent(String str_date1, String str_date2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            long diff = myFormat.parse(str_date2).getTime() - myFormat.parse(str_date1).getTime();
            PrintStream printStream = System.out;
            printStream.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long DayDifferent(String str_date1, String str_date2, String format) {
        SimpleDateFormat myFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            long diff = myFormat.parse(str_date2).getTime() - myFormat.parse(str_date1).getTime();
            PrintStream printStream = System.out;
            printStream.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getDaysAgo(String date) {
        String dateString = date;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        Date serverDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
            serverDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long days1 = serverDate.getTime() - convertedDate.getTime();
        long seconds = days1 / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String str = dateString;
        long months = days / 31;
        PrintStream printStream = System.out;
        SimpleDateFormat simpleDateFormat = dateFormat;
        StringBuilder sb = new StringBuilder();
        long years = days / 365;
        sb.append("serverDate:");
        sb.append(serverDate.getTime());
        printStream.println(sb.toString());
        PrintStream printStream2 = System.out;
        printStream2.println("convertedDate:" + convertedDate.getTime());
        PrintStream printStream3 = System.out;
        printStream3.println("days1:" + days1);
        PrintStream printStream4 = System.out;
        printStream4.println("seconds:" + seconds);
        PrintStream printStream5 = System.out;
        printStream5.println("minutes:" + minutes);
        PrintStream printStream6 = System.out;
        printStream6.println("hours:" + hours);
        PrintStream printStream7 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("days:");
        long days2 = days;
        sb2.append(days2);
        printStream7.println(sb2.toString());
        PrintStream printStream8 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("months:");
        Date date2 = convertedDate;
        long months2 = months;
        sb3.append(months2);
        printStream8.println(sb3.toString());
        PrintStream printStream9 = System.out;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("years:");
        Date date3 = serverDate;
        long j = days1;
        long years2 = years;
        sb4.append(years2);
        printStream9.println(sb4.toString());
        if (seconds < 86400) {
            return "today";
        }
        if (seconds < 172800) {
            return "yesterday";
        }
        if (seconds < 2592000) {
            return days2 + " days ago";
        } else if (seconds < 31104000) {
            if (months2 <= 1) {
                return "one month ago";
            }
            return months2 + " months ago";
        } else if (years2 <= 1) {
            return "one year ago";
        } else {
            return years2 + " years ago";
        }
    }

    public boolean check_current_time_between_2date(String start_date, String end_date) {
        try {
            Date mToday = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            String curTime = sdf.format(mToday);
            Date start = sdf.parse(start_date);
            Date end = sdf.parse(end_date);
            Date userDate = sdf.parse(curTime);
            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(6, 1);
                end.setTime(mCal.getTimeInMillis());
            }
            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());
            if (!userDate.after(start) || !userDate.before(end)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean check_specific_time_between_2date(String start_date, String end_date, String my_date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            Date start = sdf.parse(start_date);
            Date end = sdf.parse(end_date);
            Date userDate = sdf.parse(my_date);
            if (end.before(start)) {
                Calendar mCal = Calendar.getInstance();
                mCal.setTime(end);
                mCal.add(6, 1);
                end.setTime(mCal.getTimeInMillis());
            }
            Log.d("curTime", userDate.toString());
            Log.d("start", start.toString());
            Log.d("end", end.toString());
            if (userDate == start) {
                return true;
            }
            if (!userDate.after(start) || !userDate.before(end)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getGMTDate(String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(cal.getTime());
    }

    public int get_total_days_of_month(int month, int year) {
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month != 2) {
            return 31;
        }
        if (year % 4 == 0) {
            return 29;
        }
        return 28;
    }

    public boolean different_time(String current_time, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date CurrentTime = null;
        Date Time = null;
        try {
            CurrentTime = simpleDateFormat.parse(current_time);
            Time = simpleDateFormat.parse(time);
        } catch (ParseException e) {
        }
        if (Time.getTime() - CurrentTime.getTime() >= 0) {
            return true;
        }
        return false;
    }

    public boolean different_time(String current_time, String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date CurrentTime = null;
        Date Time = null;
        try {
            CurrentTime = simpleDateFormat.parse(current_time);
            Time = simpleDateFormat.parse(time);
        } catch (ParseException e) {
        }
        if (Time.getTime() - CurrentTime.getTime() >= 0) {
            return true;
        }
        return false;
    }

    public String get_2_point(String no) {
        if (no.length() != 1) {
            return no;
        }
        return "0" + no;
    }

    public String getTimeHour(String time) {
        String[] separated = time.split(":");
        int fhour = Integer.parseInt("" + separated[0]);
        return get_2_point("" + fhour);
    }

    public String getTimeMin(String time) {
        String[] separated = time.split(":");
        int fmin = Integer.parseInt("" + separated[1]);
        return get_2_point("" + fmin);
    }

    public String getTimeFormat(String time) {
        String fformat;
        String[] separated = time.split(":");
        int fhour = Integer.parseInt("" + separated[0]);
        if (fhour == 0) {
            fformat = "AM";
        } else if (fhour == 12) {
            fformat = "PM";
        } else if (fhour > 12) {
            fformat = "PM";
        } else {
            fformat = "AM";
        }
        return "" + fformat;
    }

    public String getCurrentTimeSecond(boolean is24TimeFormat) {
        SimpleDateFormat dateFormat;
        if (is24TimeFormat) {
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("KK:mm:ss a", Locale.getDefault());
        }
        return dateFormat.format(new Date());
    }
}
