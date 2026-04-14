package com.trending.water.drinking.reminder.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

public class HeightWeightHelper {

    private HeightWeightHelper() {
        // Private constructor to prevent instantiation
    }

    private static double format(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return -1.0;
        }
        try {
            return Double.parseDouble(String.format(Locale.US, "%.2f", value));
        } catch (Exception e) {
            return -1.0;
        }
    }

    public static double convertLbToKg(double lb) {
        return format(0.453592 * lb);
    }

    public static double convertKgToLb(double kg) {
        return format(2.2046226218 * kg);
    }

    public static double convertCmToFeet(double cm) {
        return format(cm / 30.48);
    }

    public static double convertFeetToCm(double feet) {
        return format(30.48 * feet);
    }

    public static double calculateBmiMetric(double heightCm, double weightKg) {
        if (heightCm <= 0) return -1.0;
        return format(weightKg / Math.pow(heightCm / 100.0, 2.0));
    }

    public static double calculateBmiImperial(double heightFeet, double weightLb) {
        if (heightFeet <= 0) return -1.0;
        // Formula: 703 * weight (lb) / [height (in)]^2
        double heightInches = heightFeet * 12.0;
        return format((703.0 * weightLb) / Math.pow(heightInches, 2.0));
    }

    @NonNull
    public static String getBmiClassification(double bmi) {
        if (bmi <= 0) {
            return "Unknown";
        }
        if (bmi < 18.5) {
            return "Underweight";
        }
        if (bmi < 25.0) {
            return "Normal";
        }
        if (bmi < 30.0) {
            return "Overweight";
        }
        return "Obese";
    }

    public static double convertOzToMl(double oz) {
        return format(29.5735 * oz);
    }

    public static double convertMlToOz(double ml) {
        return format(0.033814 * ml);
    }
}
