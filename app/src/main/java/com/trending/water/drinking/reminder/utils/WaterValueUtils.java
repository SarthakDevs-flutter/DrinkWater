package com.trending.water.drinking.reminder.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trending.water.drinking.reminder.R;

public class WaterValueUtils {

    private WaterValueUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Formats the water value with units based on current settings.
     *
     * @param mlValue Value in milliliters (as String)
     * @param ozValue Value in ounces (as String)
     * @return Formatted string with unit
     */
    @NonNull
    public static String getFormattedWaterValue(@Nullable String mlValue, @Nullable String ozValue) {
        String unit = URLFactory.waterUnitValue;
        if (unit.equalsIgnoreCase("ML")) {
            return formatValue(mlValue) + " " + unit;
        } else {
            return formatValue(ozValue) + " " + unit;
        }
    }

    @NonNull
    private static String formatValue(@Nullable String value) {
        if (value == null) return "0";
        try {
            if (value.contains(".")) {
                return URLFactory.DECIMAL_FORMAT.format(Double.parseDouble(value));
            }
        } catch (Exception ignored) {
        }
        return value;
    }

    /**
     * Maps water values to drawable resources.
     *
     * @param mlValue Value in milliliters
     * @param ozValue Value in ounces
     * @return Drawable resource ID
     */
    public static int getContainerImage(double mlValue, double ozValue) {
        boolean isMl = URLFactory.waterUnitValue.equalsIgnoreCase("ML");
        
        if (isMl) {
            if (mlValue == 50.0) return R.drawable.ic_50_ml;
            if (mlValue == 100.0) return R.drawable.ic_100_ml;
            if (mlValue == 150.0) return R.drawable.ic_150_ml;
            if (mlValue == 200.0) return R.drawable.ic_200_ml;
            if (mlValue == 250.0) return R.drawable.ic_250_ml;
            if (mlValue == 300.0) return R.drawable.ic_300_ml;
            if (mlValue == 500.0) return R.drawable.ic_500_ml;
            if (mlValue == 600.0) return R.drawable.ic_600_ml;
            if (mlValue == 700.0) return R.drawable.ic_700_ml;
            if (mlValue == 800.0) return R.drawable.ic_800_ml;
            if (mlValue == 900.0) return R.drawable.ic_900_ml;
            if (mlValue == 1000.0) return R.drawable.ic_1000_ml;
        } else {
            if (ozValue == 2.0) return R.drawable.ic_50_ml;
            if (ozValue == 3.0) return R.drawable.ic_100_ml;
            if (ozValue == 5.0) return R.drawable.ic_150_ml;
            if (ozValue == 7.0) return R.drawable.ic_200_ml;
            if (ozValue == 8.0) return R.drawable.ic_250_ml;
            if (ozValue == 10.0) return R.drawable.ic_300_ml;
            if (ozValue == 17.0) return R.drawable.ic_500_ml;
            if (ozValue == 20.0) return R.drawable.ic_600_ml;
            if (ozValue == 24.0) return R.drawable.ic_700_ml;
            if (ozValue == 27.0) return R.drawable.ic_800_ml;
            if (ozValue == 30.0) return R.drawable.ic_900_ml;
            if (ozValue == 34.0) return R.drawable.ic_1000_ml;
        }
        return R.drawable.ic_custom_ml;
    }
}
