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

    private CustomerRepo (Context context) {
        mCustomers = new ArrayList<>();
        mCustomerMap = new HashMap<>();
        for (int i=0; i <100; i++) {
            Customer customer = Customer.create("Name #" + i, "" + i, "House", "Line1", "town", "county", "AB8 3CD", "name@email.com", "0987654321");
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
}
