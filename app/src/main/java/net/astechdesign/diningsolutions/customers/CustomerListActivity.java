package net.astechdesign.diningsolutions.customers;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener {

    private static final String ADD_CUSTOMER = "add_customer";
    public static final String CUSTOMER_ID = "net.astechdesign.diningsolutions.customer_id";

    private CustomerEditFragment newCustomerFragment;
    private RecyclerView mRecyclerView;
    private List<Customer> mCustomerList;
    private List<Customer> mFilteredCustomerList;
    private TextView mCustomerSelect;

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

        setupCustomerList();

        mCustomerSelect = (EditText) findViewById(R.id.customer_select);
        mCustomerSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                updateRecycler(value);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.customer_list);
        setupRecyclerView();
    }

    private void setupCustomerList() {
        mCustomerList = CustomerRepo.get(this).get();
        mFilteredCustomerList = new ArrayList<>();
        mFilteredCustomerList.addAll(mCustomerList);
    }

    private void updateRecycler(String value) {
        mFilteredCustomerList.clear();
        if (value.trim().length() == 0) {
            mFilteredCustomerList.addAll(mCustomerList);
        }
        for (Customer customer : mCustomerList) {
            if (customer.compareValue(value)) {
                mFilteredCustomerList.add(customer);
            }
        }
        setupRecyclerView();
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

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, Customer customer) {

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
            public Customer mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mPhoneView = (TextView) view.findViewById(R.id.telephone);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mPhoneView.getText() + "'";
            }

            public void setItem(Customer item) {
                this.mItem = item;
                mNameView.setText(item.name);
                mPhoneView.setText(item.phone.number);
            }
        }
    }

}
