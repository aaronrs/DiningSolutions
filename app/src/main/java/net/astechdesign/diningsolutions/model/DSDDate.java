package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DSDDate implements Serializable, Comparable {

    private static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS.SSS");
    private static SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEE dd MMMM yyyy");
    private static SimpleDateFormat displayTimeFormat = new SimpleDateFormat("KK:mm a");

    private Date date;

    public DSDDate(Date date) {
        this.date = date;
    }

    public DSDDate() {
        this(new Date());
    }

    public String dbFormat() {
        return date != null ? Long.toString(date.getTime()) : null;
    }

    private Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Object other) {
        return date.compareTo(((DSDDate) other).date);
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

    public static DSDDate create(Long date) {
        return new DSDDate(new Date(date));
    }

    public static DSDDate create(String dateString) {
        try {
            return new DSDDate(dbDateFormat.parse(dateString));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static DSDDate create(int hour, int min) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, min);
        return new DSDDate(cal.getTime());
    }

    public static DSDDate create(Calendar cal) {
        return new DSDDate(cal.getTime());
    }

    public String getDisplayDate() {
        return displayDateFormat.format(getDate());
    }

    public String getDisplayTime() {
        return displayTimeFormat.format(getDate());
    }

    public void setTime(Calendar newCal) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, newCal.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, newCal.get(Calendar.MINUTE));
        date = cal.getTime();
    }

    public static DSDDate create(Calendar dateCal, Calendar timeCal) {
        dateCal.set(Calendar.HOUR, timeCal.get(Calendar.HOUR));
        dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
        return create(dateCal);
    }

    public static String getDisplayDate(Calendar cal) {
        return displayDateFormat.format(cal.getTime());
    }

    public static String getDisplayTime(Calendar cal) {
        return displayTimeFormat.format(cal.getTime());
    }

    public boolean futureDate() {
        return this.date.compareTo(new Date()) >= 0;
    }

    public String getDisplayDateTime() {
        return getDisplayDate() + " - " + getDisplayTime();
    }
}
