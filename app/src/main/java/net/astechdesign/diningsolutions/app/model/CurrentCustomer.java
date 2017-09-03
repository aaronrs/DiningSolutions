package net.astechdesign.diningsolutions.app.model;

import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.UUID;

public class CurrentCustomer {

    private static CurrentCustomer instance;

    private Customer customer;

    private static CurrentCustomer getInstance() {
        if (instance == null) {
            instance = new CurrentCustomer();
        }
        return instance;
    }

    public static CurrentCustomer set(Customer customer) {
        getInstance().setCustomer(customer);
        return instance;
    }

    public static void set(UUID uuid) {
        set(CustomerManager.get(uuid));
    }

    private CurrentCustomer() {
        customer = Customer.create();
    }

    private void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static Customer get() {
        return getInstance().customer;
    }

    public static String getName() {
        if (hasCustomer()) {
            return instance.customer.name;
        }
        return "";
    }

    public static String getEmailAddress() {
        if (hasCustomer()) {
            return instance.customer.email.address;
        }
        return "";
    }

    public static String getPhoneNumber() {
        if (hasCustomer()) {
            return instance.customer.email.address;
        }
        return "";
    }

    public static DSDDate getVisitDate() {
        if (hasCustomer()) {
            return instance.customer.visit;
        }
        return null;
    }

    public static String getAddressLine() {
        if (hasCustomer()) {
            return instance.customer.address.line;
        }
        return "";
    }

    public static String getAddressTown() {
        if (hasCustomer()) {
            return instance.customer.address.town;
        }
        return "";
    }

    public static String getAddressCounty() {
        if (hasCustomer()) {
            return instance.customer.address.county;
        }
        return "";
    }

    public static String getAddressPostcode() {
        if (hasCustomer()) {
            return instance.customer.address.postcode;
        }
        return "";
    }

    private static boolean hasCustomer() {
        return instance.customer != null;
    }
}
