package net.astechdesign.diningsolutions.model;

import java.util.Map;

public class Record {

    public final String[] names;
    public final Map<String, Object> data;

    public Record(String[] names, Map<String, Object> data) {
        this.names = names;
        this.data = data;
    }
}
