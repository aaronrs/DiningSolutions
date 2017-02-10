package net.astechdesign.diningsolutions.customers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.orders.OrderDetailFragment;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.TaskRepo;
import net.astechdesign.diningsolutions.tasks.NewTaskFragment;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.TimePickerFragment.TIME_PICKER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class CustomerListActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener, NewTaskFragment.NewTaskListener  {

    private static final String ADD_CUSTOMER = "add_customer";
    private static final String EDIT_CUSTOMER = "edit_customer";
    public static final String CUSTOMER_ID = "net.astechdesign.diningsolutions.customer_id";

    private CustomerEditFragment newCustomerFragment;
    private CustomerEditFragment customerEditFragment;
    private Customer mCurrentCustomer;
    private RecyclerView mRecyclerView;
    private List<Customer> mCustomerList;
    private List<Customer> mFilteredCustomerList;
    private TextView mCustomerSelect;
    private CustomerDetailFragment customerDetailFragment;
    private NewTaskFragment newTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        UUID customerId = (UUID) getIntent().getSerializableExtra(CUSTOMER_ID);

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
        for (Customer customer : mFilteredCustomerList) {
            if (customerId !=  null && customerId.equals(customer.getId())) {
                showCustomerDetails(customer);
                break;
            }
        }
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
                mCustomerSelect.setText("");
                FragmentManager fm = getSupportFragmentManager();
                newCustomerFragment = new CustomerEditFragment();
                newCustomerFragment.show(fm, ADD_CUSTOMER);
                return true;
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
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

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, Customer customer) {
        CustomerRepo.get(this).addOrUpdate(customer);
        mCustomerList = CustomerRepo.get(this).get();
        updateRecycler("");
        showCustomerDetails(customer);

        Snackbar.make(findViewById(R.id.coordinatorLayout), "Edited Customer " + customer.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
                    showCustomerDetails(holder.mItem);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.mView.getWindowToken(), 0);
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

    private void showCustomerDetails(Customer customer) {
        mCustomerSelect.setText("");
        Bundle arguments = new Bundle();
        mCurrentCustomer = customer;
        arguments.putSerializable(CustomerDetailFragment.ARG_CUSTOMER, mCurrentCustomer);
        customerDetailFragment = new CustomerDetailFragment();
        customerDetailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.customer_detail_container, customerDetailFragment)
                .commit();
    }

    public void showOrders(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderDetailFragment.CUSTOMER, mCurrentCustomer);
        this.startActivity(intent);
    }

    public void addNewVisit(View view) {
        Customer customer = (Customer) view.getTag();
        FragmentManager fm = getSupportFragmentManager();
        newTaskFragment = new NewTaskFragment();
        Bundle args = new Bundle();
        args.putString(NewTaskFragment.HEADER, "Next Visit");
        args.putString(NewTaskFragment.TITLE, "Visit - " + customer.name);
        args.putSerializable(NewTaskFragment.CUSTOMER_ID, customer);
        newTaskFragment.setArguments(args);
        newTaskFragment.show(fm, ADD_TASK);
    }

    @Override
    public void onNewTaskPositiveClick(DialogInterface dialog, DSDDate date, String title, String description) {
        CustomerRepo.get(this).updateVisit(mCurrentCustomer, date, description);
        mCurrentCustomer = CustomerRepo.get(this).get(mCurrentCustomer.getId());
        showCustomerDetails(mCurrentCustomer);
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(GregorianCalendar.getInstance());
        dialog.setTargetFragment(newTaskFragment, DatePickerFragment.REQUEST_DATE);
        dialog.show(fm, DATE_PICKER);
    }

    public void getTime(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(new DSDDate());
        dialog.setTargetFragment(newTaskFragment, TimePickerFragment.REQUEST_TIME);
        dialog.show(fm, TIME_PICKER);
    }


}
