package net.astechdesign.diningsolutions.model;

public class Address extends Model {

    public final String name;
    public final String line1;
    public final String line2;
    public final String town;
    public final String county;
    public final String postcode;

    public Address(int id, String name, String line1, String line2, String town, String county, String postcode) {
        super(id);
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

    public String getHeader() {
        return String.format("%s %s %s, %s", name, line1, town, postcode);
    }
}
