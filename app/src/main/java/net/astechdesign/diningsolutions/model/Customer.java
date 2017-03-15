package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer extends Model {

    public static Customer newCustomer = create();

    public final String name;
    public final Email email;
    public final Phone phone;
    public final boolean current;
    public final DSDDate created;
    public final String referral;
    public final Address address;
    public final List<Order> orderList = new ArrayList<>();
    public final DSDDate visit;

    private Customer(UUID id, String name, Email email, Phone phone, boolean current, DSDDate created, String referral, Address address, DSDDate visit) {
        super(id);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.current = current;
        this.created = created;
        this.referral = referral;
        this.address = address;
        this.visit = visit;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean compareName(String value) {
        for (String part : name.toLowerCase().split(" ")) {
            if (part.startsWith(value.toLowerCase()))
                return true;
        }
        return false;
    }

    public boolean compareAddress(String value) {
        return address.compareAddress(value);
    }

    public boolean compareTown(String value) {
        if (address.town == null) {
            return false;
        }
        return address.town.equals(value);
    }

    public static Customer create(UUID id, String name, Email email, Phone phone, boolean current, DSDDate created, String referral, Address address, DSDDate visit) {
        return new Customer(id, name, email, phone, current, created, referral, address, visit);
    }

    public static Customer create(String name, String email, String phone, boolean current, DSDDate created, String referral, Address address, DSDDate visit) {
        return create(null, name, new Email(email), new Phone(phone), current, created, referral, address, visit);
    }

    public static Customer create(UUID id, String name, String email, String phone, boolean current, DSDDate created, String referral, Address address, DSDDate visit) {
        return create(id, name, new Email(email), new Phone(phone), current, created, referral, address, visit);
    }

    public static Customer create() {
        return create(null, null, (Email)null, null, false, DSDDate.create(), null, null, null);
    }
}
