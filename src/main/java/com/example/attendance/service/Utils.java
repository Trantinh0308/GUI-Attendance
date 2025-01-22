package com.example.attendance.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class Utils {

    public static Date createSqlDate(int day,int month,int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return new Date(calendar.getTimeInMillis());
    }

    public static double CalculatorWorkingInDay(Time startTime, Time endTime) {
        LocalTime startLocalTime = startTime.toLocalTime();
        LocalTime endLocalTime = endTime.toLocalTime();
        long hours = ChronoUnit.HOURS.between(startLocalTime, endLocalTime);
        long minutes = ChronoUnit.MINUTES.between(startLocalTime, endLocalTime) % 60;
        double totalHours = hours + minutes / 60.0;
        if (totalHours < 4) {
            return 0.0;
        } else if (totalHours < 8) {
            return 0.5;
        } else {
            return 1.0;
        }
    }

    public static String getShortDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "T2";
            case 2: return "T3";
            case 3: return "T4";
            case 4: return "T5";
            case 5: return "T6";
            case 6: return "T7";
            case 7: return "CN";
            default: return "";
        }
    }
}
