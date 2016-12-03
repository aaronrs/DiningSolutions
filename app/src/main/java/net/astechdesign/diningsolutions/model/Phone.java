package net.astechdesign.diningsolutions.model;

import java.io.Serializable;

public class Phone implements Serializable {

    public final String number;

    public Phone(String number) {
        this.number = number;
    }

    public static Phone create(String number) {
        return new Phone(number);
    }
}
