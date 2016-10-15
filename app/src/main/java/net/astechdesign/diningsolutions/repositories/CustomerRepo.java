package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

import net.astechdesign.diningsolutions.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerRepo {

    public static CustomerRepo repo;

    public static CustomerRepo get(Context context) {
        if (repo == null) {
            repo = new CustomerRepo(context);
        }
        return repo;
    }

    private List<Customer> mCustomers;
    private Map<UUID, Customer> mCustomerMap;

    private CustomerRepo(Context context) {
        mCustomers = new ArrayList<>();
        mCustomerMap = new HashMap<>();
        for (String[] data : customers) {
            Customer customer = Customer.create(data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7],data[8]);
            mCustomers.add(customer);
            mCustomerMap.put(customer.id, customer);
        }
    }

    public List<Customer> getmCustomers() {
        return mCustomers;
    }

    public Customer getCustomer(UUID id) {
        return mCustomerMap.get(id);
    }

    private String[][] customers = {
            {"Aaron Southwell", "56", "Potters Barn", "Deanway", "Chalfont St Giles", "Buckinghamshire", "HP8 4JT", "aaronrs@gmail.com", "01494871610"},
            {"Jamie Stanley",    "63", "", "Park Terrace", "GLAN HONDDU",  "Buckinghamshire", "LD3 7DH", "", ""},
            {"Amelie Alexander", "24", "", "Thirsk Road",  "BLAIRYTHAN",   "Buckinghamshire", "AB41 6HG", "", ""},
            {"Samantha Dodd",    "62", "", "Broad Street", "LOWER HAYTON", "Buckinghamshire", "SY8 0PT", "", ""},
            {"Lily Thompson",    "61", "", "Ash Lane",     "YARNSCOMBE",   "Buckinghamshire", "EX31 5FA", "", ""}
    };
}
