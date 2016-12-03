package net.astechdesign.diningsolutions.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DSDDateFormatter {
    private static DSDDateFormatter instance;

    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat fileDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
    private SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm");

    public Date dbParse(String dateString) {
        try {
            return dbDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Date parse failed: " + dateString);
        }
    }

    public static DSDDateFormatter instance() {
        if (instance == null) {
            instance = new DSDDateFormatter();
        }
        return instance;
    }

    public String format(Date date) {
        return outputDateFormat.format(date);
    }

    public String formatTime(Date date) {
        return outputTimeFormat.format(date);
    }

    public Date parseFile(String created) {
        try {
            return fileDateFormat.parse(created);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String dbFormat(Date date) {
        return dbDateFormat.format(date);
    }
}
