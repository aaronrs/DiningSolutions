package net.astechdesign.diningsolutions.model;

import android.widget.DatePicker;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DSDDate implements Serializable {

    private static final DSDDateFormatter dateFormatter = DSDDateFormatter.instance();
    private final Date date;
    public final int year;
    public final int month;
    public final int day;

    public static DSDDate create(DatePicker mDatePicker) {
        Calendar cal = Calendar.getInstance();
        cal.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
        return new DSDDate(cal.getTime());
    }

    public DSDDate(String dateString) {
        this(dateFormatter.parse(dateString));
    }

    public DSDDate(Date date) {
        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    public DSDDate() {
        this(new Date());
    }

    @Override
    public String toString() {
        return dateFormatter.format(date);
    }

    public static DSDDate fileCreate(String created) {
        Date date = dateFormatter.parseFile(created);
        return new DSDDate();
    }
}
