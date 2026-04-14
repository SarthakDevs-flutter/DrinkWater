package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterWeightRange implements InputFilter {
    private final double max;
    private final double min;

    public InputFilterWeightRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(dend, dest.length()).toString();

            if (newVal.isEmpty() || ".".equals(newVal)) {
                return null;
            }

            double input = Double.parseDouble(newVal);
            
            // Limit to 5 characters including decimal point
            if (newVal.length() <= 5 && input >= min && input <= max) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }
}
