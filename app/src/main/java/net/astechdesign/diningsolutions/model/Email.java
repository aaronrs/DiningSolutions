package net.astechdesign.diningsolutions.model;

public class Email {
    public final String address;

    public Email(String address) {
        this.address = address;
    }

    public static Email create(String address) {
        return new Email(address);
    }
}
