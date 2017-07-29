package net.astechdesign.diningsolutions.customers;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.app.flow.OptionsActivity;
import net.astechdesign.diningsolutions.customers.helpers.AddressTextWatcher;
import net.astechdesign.diningsolutions.customers.helpers.CustomerListener;
import net.astechdesign.diningsolutions.customers.helpers.CustomerTextWatcher;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerListActivity extends OptionsActivity {

    private RecyclerView mRecyclerView;
    private List<Customer> mCustomerList;
    private List<Customer> mFilteredCustomerList = new ArrayList<>();
    private ArrayAdapter<String> mTownAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.customer_list);

        setupCustomerList();

        final Set<String> townSet = new HashSet<>();
        for (Customer customer : mCustomerList) {
            townSet.add(customer.address.town);
        }
        final List<String> towns = new ArrayList<>(townSet);
        Collections.sort(towns);
        towns.add(0, "Select Town");

        final Spinner spinner = (Spinner) findViewById(R.id.town_select);
        final TextView customerSelect = (EditText) findViewById(R.id.customer_select);
        final TextView addressSelect = (EditText) findViewById(R.id.address_select);

        mTownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, towns);
        spinner.setAdapter(mTownAdapter);

        spinner.setOnItemSelectedListener(new CustomerListener(this, customerSelect, addressSelect, towns));
        customerSelect.addTextChangedListener(new CustomerTextWatcher(this, spinner, addressSelect, mTownAdapter));
        addressSelect.addTextChangedListener(new AddressTextWatcher(this, spinner, customerSelect, mTownAdapter));
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCustomerList();
    }

    private void setupCustomerList() {
        mCustomerList = CustomerRepo.get(this).get();
        mFilteredCustomerList.clear();
        mFilteredCustomerList.addAll(this.mCustomerList);
        setupRecyclerView();
    }

    public void updateRecyclerName(String name) {
        if (updateRecycler(name)) {
            for (Customer customer : mCustomerList) {
                if (customer.compareName(name)) {
                    mFilteredCustomerList.add(customer);
                }
            }
        }
        setupRecyclerView();
    }

    public void updateRecyclerAddress(String address) {
        if (updateRecycler(address)) {
            for (Customer customer : mCustomerList) {
                if (customer.compareAddress(address)) {
                    mFilteredCustomerList.add(customer);
                }
            }
        }
        setupRecyclerView();
    }

    public void updateRecyclerTown(String town) {
        if (updateRecycler(town)) {
            for (Customer customer : mCustomerList) {
                if (customer.compareTown(town)) {
                    mFilteredCustomerList.add(customer);
                }
            }
        }
        setupRecyclerView();
    }

    private boolean updateRecycler(String value) {
        mFilteredCustomerList.clear();
        if (value.length() == 0) {
            mFilteredCustomerList.addAll(mCustomerList);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        mRecyclerView.setAdapter(new CustomerListRecyclerViewAdapter(this, mFilteredCustomerList));
    }

}
