package net.astechdesign.diningsolutions.model;


import java.io.Serializable;
import java.util.UUID;

public class Customer extends Model implements Serializable {

    public final UUID id;
    public final String name;
    public final Email email;
    public final Phone phone;
    public final boolean current;
    public final DSDDate created;
    public final String referral;
    public final Address address;

    public Customer(UUID id, String name, Email email, Phone phone, boolean current, DSDDate created, String referral, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.current = current;
        this.created = created;
        this.referral = referral;
        this.address = address;
    }

    public Customer(UUID id, String name, String email, String phone, boolean current, DSDDate created, String referral, Address address) {
        this.id = id;
        this.name = name;
        this.email = new Email(email);
        this.phone = new Phone(phone);
        this.current = current;
        this.created = created;
        this.referral = referral;
        this.address = address;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
