package com.trending.water.drinking.reminder.appbasiclibs.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

public class MapHelper {

    @NonNull
    public String calculateDistance(double lat1, double lon1, double lat2, double lon2, @Nullable String unit) {
        double distance = calculateDistanceRaw(lat1, lon1, lat2, lon2);
        
        if (unit == null) {
            return String.format(Locale.US, "%.2f", distance);
        }
        
        switch (unit.toUpperCase()) {
            case "K": // Kilometers
                return String.format(Locale.US, "%.2f", distance * 1.609344);
            case "N": // Nautical Miles
                return String.format(Locale.US, "%.2f", distance * 0.8684);
            case "M": // Miles (explicit)
            default: // Miles (default)
                return String.format(Locale.US, "%.2f", distance);
        }
    }

    @NonNull
    public String calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return String.format(Locale.US, "%.2f", calculateDistanceRaw(lat1, lon1, lat2, lon2));
    }

    private double calculateDistanceRaw(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + 
                     Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        return dist * 60 * 1.1515; // Result in Miles
    }
}
