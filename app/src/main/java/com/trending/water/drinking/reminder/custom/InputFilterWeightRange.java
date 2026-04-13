package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterWeightRange implements InputFilter {
    private double max;
    private double min;

    public InputFilterWeightRange(double min2, double max2) {
        this.min = min2;
        this.max = max2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(dend, dest.length()).toString();

            if (newVal.equals("") || newVal.equals(".")) {
                return null;
            }

            double input = Double.parseDouble(newVal);
            if (isInRange(this.min, this.max, input, newVal)) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private boolean isInRange(double a, double b, double c, String cc) {
        if (cc.length() <= 5 && c <= b && c >= a) {
            return true;
        }
        return false;
    }
}
