package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Address extends Model {

    public final String name;
    public final String line1;
    public final String line2;
    public final String town;
    public final String county;
    public final String postcode;

    public Address(UUID id, String name, String line1, String line2, String town, String county, String postcode) {
        super(id);
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s, %s", name, line1, town, postcode);
    }

    public boolean compareAddress(String address) {
        address = address.toLowerCase();
        if (name.toLowerCase().startsWith(address) ||
                line1.toLowerCase().startsWith(address) ||
                line2.toLowerCase().startsWith(address) ||
                county.toLowerCase().startsWith(address) ||
                postcode.toLowerCase().startsWith(address)) {
            return true;
        }
        return false;
    }
}
