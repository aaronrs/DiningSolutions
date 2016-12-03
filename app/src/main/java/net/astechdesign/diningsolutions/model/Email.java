package net.astechdesign.diningsolutions.model;

import java.io.Serializable;

public class Email implements Serializable {
    public final String address;

    public Email(String address) {
        this.address = address;
    }

    public static Email create(String address) {
        return new Email(address);
    }
}
