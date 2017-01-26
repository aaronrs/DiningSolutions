package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DSDDate implements Serializable, Comparable {

    private static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS.SSS");
    private static SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
    private static SimpleDateFormat displayTimeFormat = new SimpleDateFormat("HH:mm");

    private String dateString;

    public DSDDate(String dateString) {
        this.dateString = dateString;
    }

    public DSDDate(Date date) {
        this(dbDateFormat.format(date));
    }

    public DSDDate() {
        this(new Date());
    }

    public String dbFormat() {
        return dateString;
    }

    private Date getDate() {
        try {
            return dbDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(Object other) {
        return dateString.compareTo(((DSDDate) other).dateString);
    }

    public int getYear() {
        return getCalendarVal(Calendar.YEAR);
    }

    public int getMonth() {
        return getCalendarVal(Calendar.MONTH);
    }

    public int getDay() {
        return getCalendarVal(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return getCalendarVal(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return getCalendarVal(Calendar.MINUTE);
    }

    private int getCalendarVal(int field) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(getDate());
        return cal.get(field);
    }

    public static DSDDate create(int hour, int min) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, min);
        return new DSDDate(cal.getTime());
    }

    public String getDisplayDate() {
        return displayDateFormat.format(getDate());
    }

    public String getDisplayTime() {
        return displayTimeFormat.format(getDate());
    }

    public String getOutput() {
        return dateString;
    }
}
