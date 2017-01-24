package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.util.Date;

public class DSDTime implements Serializable {

    private static final DSDDateFormatter dateFormatter = DSDDateFormatter.instance();
    private final Date date;

    public DSDTime() {
        this(new Date());
    }

    public DSDTime(Date date) {
        this.date = date;
    }

    public DSDTime(int hours, int minutes) {
        Date date = new Date();
        date.setHours(hours);
        date.setMinutes(minutes);
        this.date = date;
    }

    @Override
    public String toString() {
        return dateFormatter.formatTime(date);
    }
}
