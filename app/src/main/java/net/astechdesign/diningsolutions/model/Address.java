package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Address extends Model {

    public final String line;
    public final String town;
    public final String county;
    public final String postcode;

    private Address(UUID id, String line, String town, String county, String postcode) {
        super(id);
        this.line = line;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", line, town, postcode);
    }

    public boolean compareAddress(String address) {
        address = address.toLowerCase();
        if (line.toLowerCase().contains(address) ||
                county.toLowerCase().startsWith(address) ||
                postcode.toLowerCase().startsWith(address)) {
            return true;
        }
        return false;
    }

    public static Address create(UUID id, String line, String town, String county, String postcode) {
        return new Address(id, line, town, county, postcode);
    }

    public static Address create() {
        return create(null, "", "", "", "");
    }
}
