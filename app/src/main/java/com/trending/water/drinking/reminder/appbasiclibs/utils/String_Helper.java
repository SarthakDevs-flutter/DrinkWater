package com.trending.water.drinking.reminder.appbasiclibs.utils;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.load.Key;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class String_Helper {
    Activity act;
    Context mContext;

    public String_Helper(Context mContext2, Activity act2) {
        this.mContext = mContext2;
        this.act = act2;
    }

//    public StringBody getMultiplePartParam(String str) {
//        try {
//            return new StringBody(str, Charset.forName(Key.STRING_CHARSET_NAME));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public boolean check_blank_data(String data) {
        if (data.equals("") || data.isEmpty() || data.length() == 0 || data.equals("null") || data == null) {
            return true;
        }
        return false;
    }

    public String getSqlValue(String input) {
        return input.replace("'", "''");
    }

    public String getSqlValue_reverse(String input) {
        return input.replace("\\", "");
    }

    public String getHtmlData(String data) {
        return "<html>" + "<head><style>@font-face {font-family: 'verdana';src: url('file:///android_asset/Roboto-Bold.ttf');}body {font-family: 'verdana';}</style></head>" + "<body style=\"text-align:justify\">" + data + "</body></html>";
    }

    public String getHtmlDataNormal(String data) {
        return "<html>" + "<head><style>@font-face {font-family: 'verdana';src: url('file:///android_asset/Roboto-Regular.ttf');}body {font-family: 'verdana';}</style></head>" + "<body style=\"text-align:justify\">" + data + "</body></html>";
    }

//    public String getCommaSeparatedString(ArrayList<String> arr) {
//        String lst_data = "";
//        for (int k = 0; k < arr.size(); k++) {
//            lst_data = lst_data + arr.get(k) + ",";
//        }
//        if (check_blank_data(lst_data) == 0) {
//            return lst_data.substring(0, lst_data.length() - 1);
//        }
//        return lst_data;
//    }
//
//    public String getCommaSeparatedString(ArrayList<String> arr, String sign) {
//        String lst_data = "";
//        for (int k = 0; k < arr.size(); k++) {
//            lst_data = lst_data + arr.get(k) + sign;
//        }
//        if (check_blank_data(lst_data) == 0) {
//            return lst_data.substring(0, lst_data.length() - 1);
//        }
//        return lst_data;
//    }

    public ArrayList<String> getArrayListFromCommaSeparatedString(String comma_string) {
        ArrayList<String> arr = new ArrayList<>();
        String[] str_arr = comma_string.split(",");
        for (String add : str_arr) {
            arr.add(add);
        }
        return arr;
    }

    public ArrayList<String> getArrayListFromCommaSeparatedString(String comma_string, String sign) {
        ArrayList<String> arr = new ArrayList<>();
        String[] str_arr = comma_string.split(sign);
        for (String add : str_arr) {
            arr.add(add);
        }
        return arr;
    }

    public String[] getArrayFromCommaSeparatedString(String comma_string) {
        return comma_string.split(",");
    }

    public String[] getArrayFromCommaSeparatedString(String comma_string, String sign) {
        return comma_string.split(sign);
    }

    public String get_2_point(String no) {
        if (no.length() != 1) {
            return no;
        }
        return "0" + no;
    }

    public String get_2_digit_year(String no) {
        return no.substring(2);
    }

    public String firstLetterCaps(String data) {
        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", 2).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public String get_string(int id) {
        return this.mContext.getResources().getString(id);
    }

    public String[] get_array(int id) {
        return this.mContext.getResources().getStringArray(id);
    }

    public ArrayList<String> get_arraylist(int id) {
        return new ArrayList<>(Arrays.asList(this.mContext.getResources().getStringArray(id)));
    }

    public String get_user_address(String street, String city, String state, String zipcode, String country) {
        String final_address = street;
        if (!city.equals("") && !city.isEmpty()) {
            final_address = final_address + " , " + city;
        }
        if (!state.equals("") && !state.isEmpty()) {
            final_address = final_address + " , " + state;
        }
        if (!zipcode.equals("") && !zipcode.isEmpty()) {
            final_address = final_address + " - " + zipcode;
        }
        if (country.equals("") || country.isEmpty()) {
            return final_address;
        }
        return final_address + " , " + country;
    }
}
