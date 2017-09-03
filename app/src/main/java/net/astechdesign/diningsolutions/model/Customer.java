package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer extends Model {

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
        return create(null, name, Email.create(email), Phone.create(phone), current, created, referral, address, visit);
    }

    public static Customer create(UUID id, String name, String email, String phone, boolean current, DSDDate created, String referral, Address address, DSDDate visit) {
        return create(id, name, Email.create(email), Phone.create(phone), current, created, referral, address, visit);
    }

    public static Customer create() {
        return create(null, "", "", "", true, DSDDate.create(), "", Address.create(), DSDDate.EMPTY_DATE);
    }

    public static Customer create(String name) {
        return create(null, name, "", "", true, DSDDate.create(), "", Address.create(), null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (email.address != null ? !email.address.equals(customer.email.address) : customer.email.address != null) return false;
        return phone.number != null ? phone.number.equals(customer.phone.number) : customer.phone.number == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }

    public boolean isNew() {
        return false;
    }
}
