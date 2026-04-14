package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.NonNull;

import java.util.List;

public class InputFilterRange implements InputFilter {
    private final List<Double> allowedElements;
    private final double min;

    public InputFilterRange(double min, List<Double> allowedElements) {
        this.min = min;
        this.allowedElements = allowedElements;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(dend, dest.length()).toString();

            if (newVal.isEmpty() || newVal.equals(".")) {
                return null;
            }

            // The original logic had a length limit of 4
            if (newVal.length() > 4) {
                return "";
            }

            double input = Double.parseDouble(newVal);
            if (isAllowed(input)) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private boolean isAllowed(double input) {
        // The original logic checked against the elements list.
        // It didn't use the 'min' value, but we preserve it for now if needed.
        for (Double element : allowedElements) {
            if (element != null && element == input) {
                return true;
            }
        }
        return false;
    }
}
