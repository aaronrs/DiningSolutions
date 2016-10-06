package net.astechdesign.diningsolutions.model;

public class Distributor {
    public final String number;
    public final String name;
    public final Phone mobile;

    private Distributor(String number, String name, Phone mobile) {
        this.number = number;
        this.name = name;
        this.mobile = mobile;
    }

    public static Distributor create(String number, String name, Phone mobile) {
        return new Distributor(number, name, mobile);
    }
}
