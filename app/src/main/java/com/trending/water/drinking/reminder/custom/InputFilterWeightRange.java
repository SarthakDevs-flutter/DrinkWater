package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

import com.github.mikephil.charting.utils.Utils;

public class InputFilterWeightRange implements InputFilter {
    private double max;
    private double min;

    public InputFilterWeightRange(double min2, double max2) {
        this.min = min2;
        this.max = max2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            if (isInRange(this.min, this.max, Double.parseDouble(dest.toString() + source.toString()), dest.toString() + source.toString())) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private boolean isInRange(double a, double b, double c, String cc) {
        if (cc.length() <= 5 && c <= b && c >= a && c % 0.5d == Utils.DOUBLE_EPSILON) {
            return true;
        }
        return false;
    }
}
