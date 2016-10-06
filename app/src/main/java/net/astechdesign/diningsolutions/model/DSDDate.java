package net.astechdesign.diningsolutions.model;

import java.util.Date;

public class DSDDate {

    private final DSDDateFormatter dateFormatter = DSDDateFormatter.instance();
    private final Date date;

    public DSDDate(String dateString) {
        this.date = dateFormatter.parse(dateString);
    }

    public DSDDate() {
        this.date = new Date();
    }

    @Override
    public String toString() {
        return dateFormatter.format(date);
    }
}
