package com.trending.water.drinking.reminder.appbasiclibs.utils;

public class Map_Helper {
    public String distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double d = lat2;
        double dist = 60.0d * rad2deg(Math.acos((Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(d))) + (Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(d)) * Math.cos(deg2rad(lon1 - lon2))))) * 1.1515d;
        String unit2 = unit.toUpperCase();
        if (unit2.equals("K")) {
            return String.format("%.2f", new Object[]{Double.valueOf(1.609344d * dist)});
        } else if (unit2.equals("N")) {
            return String.format("%.2f", new Object[]{Double.valueOf(0.8684d * dist)});
        } else {
            return String.format("%.2f", new Object[]{Double.valueOf(dist)});
        }
    }

    public String distance(double lat1, double lon1, double lat2, double lon2) {
        double d = lat2;
        return String.format("%.2f", new Object[]{Double.valueOf(60.0d * rad2deg(Math.acos((Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(d))) + (Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(d)) * Math.cos(deg2rad(lon1 - lon2))))) * 1.1515d)});
    }

    public double deg2rad(double deg) {
        return (3.141592653589793d * deg) / 180.0d;
    }

    public double rad2deg(double rad) {
        return (180.0d * rad) / 3.141592653589793d;
    }
}
