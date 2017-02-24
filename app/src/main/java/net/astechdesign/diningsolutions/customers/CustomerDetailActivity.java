package net.astechdesign.diningsolutions.customers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.orders.OrderDetailFragment;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.UUID;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.TimePickerFragment.TIME_PICKER;
import static net.astechdesign.diningsolutions.customers.CustomerEditFragment.EDIT_CUSTOMER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class CustomerDetailActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener, NextVisitFragment.NextVisitListener {

    public static final String CUSTOMER_ID = "net.astechdesign.diningsolutions.customer_id";
    public static final String CUSTOMER  = "net.astechdesign.diningsolutions.customer";

    private CustomerEditFragment customerEditFragment;
    private Customer mCustomer;
    private NextVisitFragment nextVisitFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID id = (UUID) getIntent().getSerializableExtra(CUSTOMER_ID);
        mCustomer = id != null ? CustomerRepo.get(this).get(id) : Customer.newCustomer;

        setContentView(R.layout.activity_customer_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                customerEditFragment = new CustomerEditFragment();
                customerEditFragment.setCustomer(mCustomer);
                customerEditFragment.show(fm, EDIT_CUSTOMER);
            }
        };
        fab.setOnClickListener(editListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mCustomer == Customer.newCustomer) {
            editListener.onClick(null);
        } else {
            showCustomerDetails();
        }
    }

    private void showCustomerDetails() {
        findViewById(R.id.customer_order_btn).setTag(mCustomer.getDbId());
        findViewById(R.id.next_visit).setTag(mCustomer);
        setText(R.id.customer_name, mCustomer.name);
        setText(R.id.customer_email, mCustomer.email.address);
        setText(R.id.customer_phone, mCustomer.phone.number);
        setText(R.id.visit_date_time, mCustomer.visit.getDisplayDateTime());
        Address address = mCustomer.address;
        if (address != null) {
            setText(R.id.address_name, address.name);
            setText(R.id.address_line1, address.line1);
            setText(R.id.address_line2, address.line2);
            setText(R.id.address_town, address.town);
            setText(R.id.address_county, address.county);
            setText(R.id.address_postcode, address.postcode);
        }
    }

    private void setText(int id, String value) {
        TextView view = (TextView) findViewById(id);
        if (value.trim().isEmpty()) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(value);
        }
    }

    public void showOrders(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderDetailFragment.CUSTOMER, mCustomer);
        this.startActivity(intent);
    }

    public void addNewVisit(View view) {
        Customer customer = (Customer) view.getTag();
        FragmentManager fm = getSupportFragmentManager();
        nextVisitFragment = new NextVisitFragment();
        Bundle args = new Bundle();
        args.putSerializable(NextVisitFragment.CUSTOMER, customer);
        nextVisitFragment.setArguments(args);
        nextVisitFragment.show(fm, ADD_TASK);
    }

    @Override
    public void onDialogPositiveClick(DialogInterface dialog, Customer customer) {
        CustomerRepo.get(this).addOrUpdate(customer);
        mCustomer = CustomerRepo.get(this).get(customer.getId());
        showCustomerDetails();
        Snackbar.make(findViewById(R.id.coordinatorLayoutDetail), "Edited Customer " + mCustomer.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onPositiveClick(DialogInterface dialog, DSDDate date) {
        CustomerRepo.get(this).updateVisit(mCustomer, date);
        mCustomer = CustomerRepo.get(this).get(mCustomer.getId());
        showCustomerDetails();
    }

    public void getDate(View v) {
        DatePickerFragment dialog = DatePickerFragment.newInstance(nextVisitFragment, DSDDate.create());
        dialog.show(getSupportFragmentManager(), DATE_PICKER);
    }

    public void getTime(View v) {
        TimePickerFragment dialog = TimePickerFragment.newInstance(nextVisitFragment, DSDDate.create());
        dialog.show(getSupportFragmentManager(), TIME_PICKER);
    }
}
