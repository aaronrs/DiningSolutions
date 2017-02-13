package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DSDDate implements Serializable, Comparable {

    private static SimpleDateFormat displayDateFormat = new SimpleDateFormat("EEE dd MMMM yyyy");
    private static SimpleDateFormat displayTimeFormat = new SimpleDateFormat("KK:mm a");

    public final Date date;

    private DSDDate(Date date) {
        this.date = date;
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

    public static DSDDate create(Date date) {
        return new DSDDate(date);
    }

    public static DSDDate create() {
        return new DSDDate(new Date());
    }

    public static DSDDate create(Long date) {
        return create(new Date(date));
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

    public String getDisplayDateTime() {
        if (date.getTime() == 0) {
            return "no date set";
        }
        return getDisplayDate() + " - " + getDisplayTime();
    }

    public static DSDDate withTime(DSDDate date, DSDDate time) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date.date);
        cal.set(Calendar.HOUR_OF_DAY, time.getHour());
        cal.set(Calendar.MINUTE, time.getMinute());
        return create(cal);
    }

    public boolean futureDate() {
        return this.date.compareTo(new Date()) >= 0;
    }

    public boolean isCurrent() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return date.after(cal.getTime());
    }
}
