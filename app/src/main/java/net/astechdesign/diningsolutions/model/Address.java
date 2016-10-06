package net.astechdesign.diningsolutions.model;

public class Address {
    public final String number;
    public final String name;
    public final String line1;
    public final String town;
    public final String county;
    public final String postcode;

    public Address(String number, String name, String line1, String town, String county, String postcode) {
        this.number = number;
        this.name = name;
        this.line1 = line1;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

    public static Address create(String number, String name, String line1, String town, String county, String postcode) {
        return new Address(number, name, line1, town, county, postcode);
    }
}
