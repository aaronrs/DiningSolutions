package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.GregorianCalendar;
import java.util.List;

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.CUSTOMER;
import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderActivity extends AppCompatActivity implements OrderAddProductFragment.ProductAddListener {

    private static final String ADD_PRODUCT = "add_product";
    private static final String DATE_PICKER = "delivery_date";
    private Customer mCustomer;
    private List<Order> mOrders;
    private Order mOrder;
    private OrderAddProductFragment newProductFragment;
    private Toolbar toolbar;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCustomer = (Customer) getIntent().getSerializableExtra(CUSTOMER);
        mOrders = OrderRepo.get(this).getOrders(mCustomer);

        // Setup spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(newAdapter());
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
                Snackbar.make(view, "Emailing order to " + mCustomer.email.address, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (!mOrders.isEmpty()) {
            mOrder = mOrders.get(0);
            initialiseView();
        }
        if (mOrders.size() > 0) {
            showOrder(0);
        }
    }

    private MyOrderAdapter newAdapter() {
        return new MyOrderAdapter(toolbar.getContext(), mOrders);
    }

    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mCustomer.email.address});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dining Solutions Direct - Invoice : " + mOrder.invoiceNumber);
        intent.putExtra(Intent.EXTRA_TEXT, new EmailTemplate(mOrder).toString());

        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(OrderActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrder(int position) {
        OrderDetailFragment fragment = new OrderDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER, mCustomer);

        mOrder = mOrders.get(position);
        args.putSerializable(ORDER, mOrder);
        initialiseView();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, fragment)
                .commit();
    }

    private void initialiseView() {
        setFields(R.id.order_detail_name, mCustomer.name);
        setFields(R.id.order_detail_phone, mCustomer.phone == null ? "" : mCustomer.phone.number);
        setFields(R.id.order_detail_email, mCustomer.email == null ? "" : mCustomer.email.address);
        setFields(R.id.order_invoice_number, mOrder.invoiceNumber);
    }

    private void setFields(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_order:
                createNewOrder();
                showOrder(0);
                return true;
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewOrder() {
        OrderRepo.get(this).create(this, mCustomer);
        mOrders = OrderRepo.get(this).getOrders(mCustomer);
        spinner.setAdapter(newAdapter());
    }

    @Override
    public void onAddProductPositiveClick(DialogInterface dialog, Product product, double price, int quantity, String batch) {
        DSDDate deliveryDate = new DSDDate();
        OrderItem item = new OrderItem(null, product.name, price, quantity, batch, deliveryDate);
        OrderItemRepo.get(this).add(mOrder, item);
        mOrders = OrderRepo.get(this).getOrders(mCustomer);

        spinner.setAdapter(newAdapter());
        for (int i=0; i < mOrders.size(); i++) {
            if (mOrders.get(i).invoiceNumber.equals(mOrder.invoiceNumber)) {
                showOrder(i);
                break;
            }
        }

        Snackbar.make(findViewById(R.id.main_content), "Added product " + product.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private static class MyOrderAdapter extends ArrayAdapter<Order> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyOrderAdapter(Context context, List<Order> orders) {
            super(context, android.R.layout.simple_list_item_1, orders);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            Order order = getItem(position);
            textView.setText("Invoice No. " + order.invoiceNumber + " - " + order.created.getDisplayDate());

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public void addProduct(View view) {
        FragmentManager fm = getSupportFragmentManager();
        newProductFragment = new OrderAddProductFragment();
        newProductFragment.show(fm, ADD_PRODUCT);
    }
}
