package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

public class DigitsInputFilter implements InputFilter {
    private final double maxValue;
    private final int maxDigitsAfterDot;
    private final int maxIntegerDigitsLength;

    public DigitsInputFilter(int maxIntegerDigitsLength, int maxDigitsAfterDot, double maxValue) {
        this.maxIntegerDigitsLength = maxIntegerDigitsLength;
        this.maxDigitsAfterDot = maxDigitsAfterDot;
        this.maxValue = maxValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String allText = getAllText(source, dest, dstart);
        String onlyDigitsText = getOnlyDigitsPart(allText);

        if (allText.isEmpty()) {
            return null; // Let the deletion happen
        }

        try {
            double enteredValue = Double.parseDouble(onlyDigitsText);

            if (enteredValue > maxValue) {
                return ""; // Exceeds max value
            }

            if (onlyDigitsText.contains(".")) {
                int dotIndex = onlyDigitsText.indexOf(".");
                int digitsAfterDot = onlyDigitsText.length() - dotIndex - 1;
                if (digitsAfterDot > maxDigitsAfterDot) {
                    return "";
                }
            } else {
                if (onlyDigitsText.length() > maxIntegerDigitsLength) {
                    return "";
                }
            }

        } catch (NumberFormatException e) {
            return "";
        }

        return null; // Accept the input
    }

    private String getOnlyDigitsPart(String text) {
        return text.replaceAll("[^0-9.]", "");
    }

    private String getAllText(CharSequence source, Spanned dest, int dstart) {
        StringBuilder builder = new StringBuilder(dest);
        if (source.length() == 0) {
            // Source length 0 means a deletion
            if (dstart < builder.length()) {
                builder.deleteCharAt(dstart);
            }
        } else {
            builder.insert(dstart, source);
        }
        return builder.toString();
    }
}
