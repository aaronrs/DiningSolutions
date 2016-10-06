package net.astechdesign.diningsolutions.model;

public class Phone {

    public final String number;

    public Phone(String number) {
        this.number = number;
    }

    public static Phone create(String number) {
        return new Phone(number);
    }
}
