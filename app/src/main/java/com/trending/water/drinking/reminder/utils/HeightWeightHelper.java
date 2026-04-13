package com.trending.water.drinking.reminder.utils;

import androidx.core.os.EnvironmentCompat;

import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;

public class HeightWeightHelper {
    private static double format(double value) {
        if (value == Utils.DOUBLE_EPSILON) {
            return -1.0d;
        }
        try {
            return Double.valueOf(new DecimalFormat("###.##").format(value).replace(",", ".").replace("٫", ".")).doubleValue();
        } catch (Exception e) {
            return -1.0d;
        }
    }

    public static double lbToKgConverter(double lb) {
        return format(0.453592d * lb);
    }

    public static double kgToLbConverter(double kg) {
        return format(2.204624420183777d * kg);
    }

    public static double cmToFeetConverter(double cm) {
        return format(cm / 30.0d);
    }

    public static double feetToCmConverter(double feet) {
        return format(30.0d * feet);
    }

    public static double getBMIKg(double height, double weight) {
        return format(weight / Math.pow(height / 100.0d, 2.0d));
    }

    public static double getBMILb(double height, double weight) {
        return format((703.0d * weight) / Math.pow((double) ((int) (12.0d * height)), 2.0d));
    }

    public static String getBMIClassification(double bmi) {
        if (bmi <= Utils.DOUBLE_EPSILON) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (bmi < 18.5d) {
            return "underweight";
        }
        if (bmi < 25.0d) {
            return "normal";
        }
        if (bmi < 30.0d) {
            return "overweight";
        }
        return "obese";
    }

    public static double ozToMlConverter(double oz) {
        return format(29.5735d * oz);
    }

    public static double mlToOzConverter(double ml) {
        return format(0.03381405650328842d * ml);
    }
}
