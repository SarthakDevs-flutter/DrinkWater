package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
    private final Context context;

    public StringHelper(@Nullable Context context) {
        this.context = context;
    }

    public StringHelper() {
        this(null);
    }

    public boolean isBlank(@Nullable String data) {
        return data == null || data.trim().isEmpty() || data.trim().equalsIgnoreCase("null");
    }

    @NonNull
    public String getSqlValue(@Nullable String input) {
        if (input == null) return "";
        return input.replace("'", "''");
    }

    @NonNull
    public String getSqlValueReverse(@Nullable String input) {
        if (input == null) return "";
        return input.replace("\\", "");
    }

    @NonNull
    public String getHtmlData(@NonNull String content) {
        return "<html>" +
                "<head><style>@font-face {font-family: 'Robo';src: url('file:///android_asset/Roboto-Bold.ttf');}body {font-family: 'Robo';}</style></head>" +
                "<body style=\"text-align:justify\">" + content + "</body></html>";
    }

    @NonNull
    public String getHtmlDataNormal(@NonNull String content) {
        return "<html>" +
                "<head><style>@font-face {font-family: 'Robo';src: url('file:///android_asset/Roboto-Regular.ttf');}body {font-family: 'Robo';}</style></head>" +
                "<body style=\"text-align:justify\">" + content + "</body></html>";
    }

    @NonNull
    public List<String> getListFromCommaSeparated(@Nullable String commaString) {
        if (isBlank(commaString)) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(commaString.split(",")));
    }

    @NonNull
    public List<String> getListFromCommaSeparated(@Nullable String commaString, @NonNull String delimiter) {
        if (isBlank(commaString)) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(commaString.split(Pattern.quote(delimiter))));
    }

    @NonNull
    public String[] getArrayFromCommaSeparated(@Nullable String commaString) {
        if (isBlank(commaString)) return new String[0];
        return commaString.split(",");
    }

    @NonNull
    public String formatTwoDigits(@Nullable String number) {
        if (number == null) return "00";
        if (number.length() > 1) return number;
        return "0" + number;
    }

    @NonNull
    public String get2DigitYear(@Nullable String year) {
        if (year == null || year.length() < 2) return year != null ? year : "";
        return year.substring(year.length() - 2);
    }

    @NonNull
    public String capitalizeFirst(@Nullable String data) {
        if (data == null || data.isEmpty()) return "";
        String first = data.substring(0, 1).toUpperCase(Locale.US);
        if (data.length() == 1) return first;
        return first + data.substring(1).toLowerCase(Locale.US);
    }

    @NonNull
    public String capitalizeAll(@Nullable String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        Matcher matcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(input);
        while (matcher.find()) {
            matcher.appendReplacement(sb, Objects.requireNonNull(matcher.group(1)).toUpperCase(Locale.US) + matcher.group(2).toLowerCase(Locale.US));
        }
        return matcher.appendTail(sb).toString();
    }

    @NonNull
    public String getString(@StringRes int resId) {
        if (context == null) return "";
        return context.getString(resId);
    }

    @NonNull
    public String[] getStringArray(int resId) {
        if (context == null) return new String[0];
        return context.getResources().getStringArray(resId);
    }

    @NonNull
    public List<String> getStringList(int resId) {
        return new ArrayList<>(Arrays.asList(getStringArray(resId)));
    }

    @NonNull
    public String formatAddress(@Nullable String street, @Nullable String city, @Nullable String state, @Nullable String zip, @Nullable String country) {
        List<String> parts = new ArrayList<>();
        if (!TextUtils.isEmpty(street)) parts.add(street);
        if (!TextUtils.isEmpty(city)) parts.add(city);
        if (!TextUtils.isEmpty(state)) parts.add(state);

        StringBuilder address = new StringBuilder(TextUtils.join(", ", parts));

        if (!TextUtils.isEmpty(zip)) {
            if (address.length() > 0) address.append(" - ");
            address.append(zip);
        }

        if (!TextUtils.isEmpty(country)) {
            if (address.length() > 0) address.append(", ");
            address.append(country);
        }

        return address.toString();
    }

    // Legacy method names for compatibility if needed, though refactoring calls is better
    public boolean check_blank_data(@Nullable String data) {
        return isBlank(data);
    }

    public String get_2_point(@Nullable String no) {
        return formatTwoDigits(no);
    }
}
