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

    private Customer(UUID id, String name, Email email, Phone phone, boolean current, DSDDate created, String referral, Address address) {
        super(id);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.current = current;
        this.created = created;
        this.referral = referral;
        this.address = address;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean compareValue(String value) {
        for (String part : name.toLowerCase().split(" ")) {
            if (part.startsWith(value.toLowerCase()))
                return true;
        }
        return false;
    }

    public static Customer create(UUID id, String name, Email email, Phone phone, boolean current, DSDDate created, String referral, Address address) {
        return new Customer(id, name, email, phone, current, created, referral, address);
    }

    public static Customer create(UUID id, String name, String email, String phone, boolean current, DSDDate created, String referral, Address address) {
        return create(id, name, new Email(email), new Phone(phone), current, created, referral, address);
    }
}
