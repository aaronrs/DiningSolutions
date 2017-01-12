package net.astechdesign.diningsolutions.customers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.repositories.RepoManager;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.orders.OrderDetailFragment;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.List;

public class CustomerListActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener {

    private static final String ADD_CUSTOMER = "add_customer";
    private static final String EDIT_CUSTOMER = "edit_customer";

    private boolean mTwoPane;
    private CustomerEditFragment newCustomerFragment;
    private CustomerEditFragment customerEditFragment;
    private Customer mCurrentCustomer;
    private View mRecyclerView;
    private CustomerRepo customerRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerRepo = RepoManager.getCustomerRepo(this);
        setContentView(R.layout.activity_customer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                customerEditFragment = new CustomerEditFragment();
                customerEditFragment.setCustomer(mCurrentCustomer);
                customerEditFragment.show(fm, EDIT_CUSTOMER);
            }
        });

        mRecyclerView = findViewById(R.id.customer_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);

        if (findViewById(R.id.customer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
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
                FragmentManager fm = getSupportFragmentManager();
                newCustomerFragment = new CustomerEditFragment();
                newCustomerFragment.show(fm, ADD_CUSTOMER);
                return true;
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, Customer customer) {
        customerRepo.addOrUpdate(customer);
        setupRecyclerView((RecyclerView) mRecyclerView);
        showCustomerDetails(customer);

        Snackbar.make(findViewById(R.id.coordinatorLayout), "Edited Customer " + customer.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(customerRepo.get()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Customer> mValues;

        public SimpleItemRecyclerViewAdapter(List<Customer> items) {
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
                    if (mTwoPane) {
                        showCustomerDetails(holder.mItem);
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CustomerDetailActivity.class);
                        intent.putExtra(CustomerDetailFragment.ARG_CUSTOMER, holder.getId());
                        context.startActivity(intent);
                    }
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

            public int getId() {
                return mItem.id;
            }
        }
    }

    private void showCustomerDetails(Customer customer) {
        Bundle arguments = new Bundle();
        mCurrentCustomer = customer;
        arguments.putSerializable(CustomerDetailFragment.ARG_CUSTOMER, mCurrentCustomer);
        CustomerDetailFragment fragment = new CustomerDetailFragment ();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.customer_detail_container, fragment)
                .commit();
    }

    public void showOrders(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderDetailFragment.CUSTOMER, mCurrentCustomer);
        this.startActivity(intent);
    }
}
