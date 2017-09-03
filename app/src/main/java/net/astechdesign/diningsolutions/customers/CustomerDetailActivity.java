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
import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.UUID;

import static net.astechdesign.diningsolutions.customers.CustomerEditFragment.EDIT_CUSTOMER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class CustomerDetailActivity extends AppCompatActivity implements CustomerEditFragment.CustomerEditListener {

    public static final String CUSTOMER_ID = "net.astechdesign.diningsolutions.customer_id";
    public static final String CUSTOMER  = "net.astechdesign.diningsolutions.customer";

    private CustomerEditFragment customerEditFragment;
    private NextVisitFragment nextVisitFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        View.OnClickListener editListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                customerEditFragment = new CustomerEditFragment();
                customerEditFragment.show(fm, EDIT_CUSTOMER);
            }
        };
        fab.setOnClickListener(editListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showCustomerDetails();
    }

    private void showCustomerDetails() {
        setText(R.id.customer_name, CurrentCustomer.getName());
        setText(R.id.customer_email, CurrentCustomer.getEmailAddress());
        setText(R.id.customer_phone, CurrentCustomer.getPhoneNumber());
        setText(R.id.visit_date_time, CurrentCustomer.getVisitDate().getDisplayDateTime());
        setText(R.id.address_line, CurrentCustomer.getAddressLine());
        setText(R.id.address_town, CurrentCustomer.getAddressTown());
        setText(R.id.address_county, CurrentCustomer.getAddressCounty());
        setText(R.id.address_postcode, CurrentCustomer.getAddressPostcode());
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
        this.startActivity(intent);
    }

    public void addNewVisit(View view) {
        FragmentManager fm = getSupportFragmentManager();
        nextVisitFragment = new NextVisitFragment();
        Bundle args = new Bundle();
        args.putSerializable(NextVisitFragment.CUSTOMER, CurrentCustomer.get());
        nextVisitFragment.setArguments(args);
        nextVisitFragment.show(fm, ADD_TASK);
    }

    @Override
    public void onCustomerEditClick(DialogInterface dialog) {
        showCustomerDetails();
        Snackbar.make(findViewById(R.id.coordinatorLayoutDetail), "Edited Customer " + CurrentCustomer.getName(), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void deleteCustomer(View v) {
        final Activity home = this;
        new AlertDialog.Builder(this)
                .setTitle("Delete Customer - " + CurrentCustomer.getName())
                .setMessage("Do you want to delete this customer?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(CustomerDetailActivity.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                        CustomerManager.delete();
                        home.finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
}
