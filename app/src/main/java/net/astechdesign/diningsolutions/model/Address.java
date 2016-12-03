package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.util.UUID;

public class Address implements Serializable {

    public final UUID id;
    public final String name;
    public final String line1;
    public final String line2;
    public final String town;
    public final String county;
    public final String postcode;

    public Address(UUID id, String name, String line1, String line2, String town, String county, String postcode) {
        this.id = id;
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }
}
