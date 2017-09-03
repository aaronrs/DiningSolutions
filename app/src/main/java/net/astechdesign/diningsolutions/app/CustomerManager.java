package net.astechdesign.diningsolutions.app;

import android.content.Context;
import android.widget.ArrayAdapter;

import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerManager {

    protected static Context context;
    private static CustomerRepo repo;
    private static List<Customer> mCustomerList = new ArrayList<>();
    private static CustomerListActivity mActivity;

    public static void setContext(Context appContext) {
        context = appContext;
        repo = CustomerRepo.get(context);
    }

    public static List<Customer> getCustomerList() {
        mCustomerList.clear();
        mCustomerList.addAll(repo.get());
        return mCustomerList;
    }

    public static Customer get(UUID customerId) {
        return repo.get(customerId);
    }

    public static void filter(String key, String value) {
        mCustomerList.clear();
        switch (key) {
            case "name":
                mCustomerList.addAll(repo.findByName(value));
                break;
            case "address":
                mCustomerList.addAll(repo.findByAddress(value));
                break;
            case "town":
                mCustomerList.addAll(repo.findByTown(value));
                break;
            default:
                mCustomerList.addAll(repo.get());
        }
        mActivity.updateView();
    }

    public static void updateView() {
        mActivity.updateView();
    }

    public static ArrayAdapter getTownAdapter() {
        List<String> towns = getTowns();
        towns.add(0, "Select Town");

        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, towns);
    }

    public static List<String> getTowns() {
        return repo.getTowns();
    }

    public static UUID create(String name) {
        Customer customer = Customer.create(name);
        return repo.addOrUpdate(customer);
    }

    public static void update(String field, String value) {
        repo.update(CurrentCustomer.get(), field, value);
    }

    public static void save(Customer customer) {
        repo.addOrUpdate(customer);
    }

    public static void create(String name, String email, String phone, Address address) {
        Customer customer = Customer.create(CurrentCustomer.get().getId(), name, email, phone, true, DSDDate.create(), "", address, DSDDate.create());
        UUID uuid = repo.addOrUpdate(customer);
        CurrentCustomer.set(uuid);
    }

    public static void delete() {
        repo.deleteCustomer(CurrentCustomer.get());
        CurrentCustomer.set((Customer)null);
    }

    public static void setActivity(CustomerListActivity activity) {
        mActivity = activity;

    }
}
