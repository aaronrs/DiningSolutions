package net.astechdesign.diningsolutions.customers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerListActivity extends AppCompatActivity {

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (customerSelect.getText().length() != 0) {
                        customerSelect.setText("");
                    }
                    if (addressSelect.getText().length() != 0) {
                        addressSelect.setText("");
                    }
                    updateRecyclerTown(towns.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        customerSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                if (value.length() > 0) {
                    spinner.setAdapter(mTownAdapter);
                    if (addressSelect.getText().length() != 0) {
                        addressSelect.setText("");
                    }
                }
                updateRecyclerName(value);
            }
        });
        addressSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                if (value.length() > 0) {
                    spinner.setAdapter(mTownAdapter);
                    if (customerSelect.getText().length() != 0) {
                        customerSelect.setText("");
                    }
                }
                updateRecyclerAddress(value);
            }
        });
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

    private void updateRecyclerName(String name) {
        if (updateRecycler(name)) {
            for (Customer customer : mCustomerList) {
                if (customer.compareName(name)) {
                    mFilteredCustomerList.add(customer);
                }
            }
        }
        setupRecyclerView();
    }

    private void updateRecyclerAddress(String address) {
        if (updateRecycler(address)) {
            for (Customer customer : mCustomerList) {
                if (customer.compareAddress(address)) {
                    mFilteredCustomerList.add(customer);
                }
            }
        }
        setupRecyclerView();
    }

    private void updateRecyclerTown(String town) {
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
        switch (item.getItemId()) {
            case R.id.menu_item_new_customer:
                Intent intent = new Intent(this, CustomerDetailActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_item_products:
                intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        mRecyclerView.setAdapter(new CustomerListRecyclerViewAdapter(mFilteredCustomerList));
    }

    public class CustomerListRecyclerViewAdapter
            extends RecyclerView.Adapter<CustomerListRecyclerViewAdapter.ViewHolder> {

        private final List<Customer> mValues;

        public CustomerListRecyclerViewAdapter(List<Customer> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.customer_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustomerListActivity.this, CustomerDetailActivity.class);
                    intent.putExtra(CustomerDetailActivity.CUSTOMER_ID, holder.mItem.getId());
                    CustomerListActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues == null ? 0 : mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mPhoneView;
            public final TextView mAddressView;
            public Customer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mPhoneView = (TextView) view.findViewById(R.id.telephone);
                mAddressView = (TextView) view.findViewById(R.id.address);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }

            public void setItem(Customer item) {
                this.mItem = item;
                mNameView.setText(item.name);
                mPhoneView.setText(item.phone.number);
                mAddressView.setText(item.address.toString());
            }
        }
    }

}
