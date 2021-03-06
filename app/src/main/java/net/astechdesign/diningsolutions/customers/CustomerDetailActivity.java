package net.astechdesign.diningsolutions.customers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.UUID;

import static net.astechdesign.diningsolutions.customers.CustomerEditFragment.EDIT_CUSTOMER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class CustomerDetailActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener {

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
        if (mCustomer.email.address.length() > 0) {
            setText(R.id.customer_email, mCustomer.email.address);
        }
        if (mCustomer.phone.number.length() > 0) {
            setText(R.id.customer_phone, mCustomer.phone.number);
        }
        setText(R.id.visit_date_time, mCustomer.visit.getDisplayDateTime());
        Address address = mCustomer.address;
        if (address != null) {
            setText(R.id.address_line, address.line);
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
        intent.putExtra(OrderActivity.CUSTOMER, mCustomer);
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
    public void onCustomerEditClick(DialogInterface dialog, Customer customer) {
        UUID customerId = CustomerRepo.get(this).addOrUpdate(customer);
        mCustomer = CustomerRepo.get(this).get(customerId);
        showCustomerDetails();
        Snackbar.make(findViewById(R.id.coordinatorLayoutDetail), "Edited Customer " + mCustomer.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void deleteCustomer(View v) {
        final Activity home = this;
        new AlertDialog.Builder(this)
                .setTitle("Delete Customer - " + mCustomer.name)
                .setMessage("Do you want to delete this customer?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(CustomerDetailActivity.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                        CustomerRepo.get(home).deleteCustomer(mCustomer);
                        home.finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
