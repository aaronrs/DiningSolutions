package net.astechdesign.diningsolutions.model;


import java.util.Date;
import java.util.UUID;

public class Customer {
    public final UUID id;
    public final Date created;
    public final String name;
    public final Address address;
    public final Email email;
    public final Phone phone;

    public Customer(UUID id, Date created, String name, Address address, Email email, Phone phone) {
        this.id = id;
        this.created = created;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public static Customer create(String name,
                                  String houseNumber,
                                  String houseName,
                                  String line1,
                                  String town,
                                  String county,
                                  String postcode,
                                  String emailAddress,
                                  String phoneNumber
    ) {
        Address address = new Address(houseNumber, houseName, line1, town, county, postcode);
        Email email = new Email(emailAddress);
        Phone phone = new Phone(phoneNumber);
        return new Customer(UUID.randomUUID(), new Date(), name, address, email, phone);
    }
}
