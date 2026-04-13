package com.trending.water.drinking.reminder.custom;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.List;

public class InputFilterRange implements InputFilter {
    List<Double> elements;
    private double min;

    public InputFilterRange(double min2, List<Double> elements2) {
        this.min = min2;
        this.elements = elements2;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement + dest.subSequence(dend, dest.length()).toString();

            if (newVal.isEmpty() || newVal.equals(".")) {
                return null;
            }

            double input = Double.parseDouble(newVal);
            if (isInRange(this.min, this.elements, input, newVal)) {
                return null;
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private boolean isInRange(double a, List<Double> b, double c, String cc) {
        if (cc.length() > 4) {
            return false;
        }
        for (int k = 0; k < b.size(); k++) {
            if (b.get(k).doubleValue() == c) {
                return true;
            }
        }
        return false;
    }
}
