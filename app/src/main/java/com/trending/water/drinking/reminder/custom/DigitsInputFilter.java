package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

public class DigitsInputFilter implements InputFilter {
    private final String DOT = ".";
    private double mMax;
    private int mMaxDigitsAfterLength;
    private int mMaxIntegerDigitsLength;

    public DigitsInputFilter(int maxDigitsBeforeDot, int maxDigitsAfterDot, double maxValue) {
        this.mMaxIntegerDigitsLength = maxDigitsBeforeDot;
        this.mMaxDigitsAfterLength = maxDigitsAfterDot;
        this.mMax = maxValue;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String allText = getAllText(source, dest, dstart);
        String onlyDigitsText = getOnlyDigitsPart(allText);
        if (allText.isEmpty()) {
            return null;
        }
        try {
            return checkMaxValueRule(Double.parseDouble(onlyDigitsText), onlyDigitsText);
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private CharSequence checkMaxValueRule(double enteredValue, String onlyDigitsText) {
        if (enteredValue > this.mMax) {
            return "";
        }
        return handleInputRules(onlyDigitsText);
    }

    private CharSequence handleInputRules(String onlyDigitsText) {
        if (isDecimalDigit(onlyDigitsText)) {
            return checkRuleForDecimalDigits(onlyDigitsText);
        }
        return checkRuleForIntegerDigits(onlyDigitsText.length());
    }

    private boolean isDecimalDigit(String onlyDigitsText) {
        return onlyDigitsText.contains(".");
    }

    private CharSequence checkRuleForDecimalDigits(String onlyDigitsPart) {
        if (onlyDigitsPart.substring(onlyDigitsPart.indexOf("."), onlyDigitsPart.length() - 1).length() > this.mMaxDigitsAfterLength) {
            return "";
        }
        return null;
    }

    private CharSequence checkRuleForIntegerDigits(int allTextLength) {
        if (allTextLength > this.mMaxIntegerDigitsLength) {
            return "";
        }
        return null;
    }

    private String getOnlyDigitsPart(String text) {
        return text.replaceAll("[^0-9?!\\.]", "");
    }

    private String getAllText(CharSequence source, Spanned dest, int dstart) {
        if (dest.toString().isEmpty()) {
            return "";
        }
        if (source.toString().isEmpty()) {
            return deleteCharAtIndex(dest, dstart);
        }
        return new StringBuilder(dest).insert(dstart, source).toString();
    }

    private String deleteCharAtIndex(Spanned dest, int dstart) {
        StringBuilder builder = new StringBuilder(dest);
        builder.deleteCharAt(dstart);
        return builder.toString();
    }
}
