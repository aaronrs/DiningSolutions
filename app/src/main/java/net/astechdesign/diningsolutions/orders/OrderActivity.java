package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
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
import android.support.v7.app.AlertDialog;
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
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.CUSTOMER;
import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderActivity extends AppCompatActivity implements OrderAddProductFragment.ProductAddListener, DatePickerFragment.DatePickerListener {

    public static final String ADD_PRODUCT = "add_product";
    private Customer mCustomer;
    private List<Order> mOrders;
    private Order mOrder;
    private Toolbar toolbar;
    private Spinner invoiceDropdown;

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
        ((TextView)findViewById(R.id.order_detail_name)).setText(mCustomer.name);

        mOrders = OrderRepo.get(this).getOrders(mCustomer);
        if (mOrders.isEmpty()) {
            mOrders = Order.emptyOrderList();
        }

        // Setup spinner
        invoiceDropdown = (Spinner) findViewById(R.id.spinner);
        invoiceDropdown.setAdapter(newAdapter());
        invoiceDropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView invoiceDate = (TextView) findViewById(R.id.order_invoice_date);
        invoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(OrderActivity.this, (DSDDate) view.getTag());
                dialog.show(getSupportFragmentManager(), "date_picker");

                Snackbar.make(view, "Clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        intent.setType("message/rfc822");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mCustomer.email.address});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dining Solutions Direct - Invoice : " + mOrder.invoiceNumber);
        intent.putExtra(Intent.EXTRA_TEXT, "Attached please find Invoice No. " + mOrder.invoiceNumber);

        try {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new EmailTemplate(this, mCustomer, mOrder).createPdf()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(OrderActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOrder(int position) {
        mOrder = mOrders.get(position);
        if (mOrder.getId() == null) {
            return;
        }

        OrderDetailFragment fragment = new OrderDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER, mCustomer);

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
        TextView invoiceDateView = setFields(R.id.order_invoice_date, mOrder.created != null ? mOrder.created.getDisplayDate() : "");
        invoiceDateView.setTag(mOrder.created);
    }

    private TextView setFields(int id, String text) {
        TextView view = (TextView) findViewById(id);
        view.setText(text);
        return view;
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
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDatePicked(DSDDate date) {
        for (OrderItem item : mOrder.orderItems) {
            if (date.after(item.deliveryDate)) {
                OrderItemRepo.get(this).updateDelivery(item, date);
            }
        }
        OrderRepo.get(this).updateInvoiceDate(mOrder, date);
        updateInvoice();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            DSDDate date = (DSDDate) data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            OrderRepo.get(this).updateInvoiceDate(mOrder, date);
            updateInvoice();
            return;
        }
    }

    private void createNewOrder() {
        OrderRepo.get(this).create(this, mCustomer);
        mOrders = OrderRepo.get(this).getOrders(mCustomer);
        invoiceDropdown.setAdapter(newAdapter());
    }

    @Override
    public void onAddProductPositiveClick(DialogInterface dialog, OrderItem item, Product product, double price, int quantity, String batch) {
        if (product == null) {
            return;
        }
        DSDDate deliveryDate = DSDDate.create();
        if (item != null) {
            OrderItemRepo.get(this).delete(mOrder, item);
        }
        item = new OrderItem(null, product.name, price, quantity, batch, deliveryDate);
        OrderItemRepo.get(this).add(mOrder, item);
        updateInvoice();

        Snackbar.make(findViewById(R.id.main_content), "Added product " + product.name, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void updateInvoice() {
        mOrders = OrderRepo.get(this).getOrders(mCustomer);
        invoiceDropdown.setAdapter(newAdapter());
        for (int i=0; i < mOrders.size(); i++) {
            if (mOrders.get(i).invoiceNumber.equals(mOrder.invoiceNumber)) {
                showOrder(i);
                break;
            }
        }
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
            textView.setText("Invoice No. " + order.invoiceNumber + " - " + (order.created != null ? order.created.getDisplayDate() : ""));

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
        OrderAddProductFragment newProductFragment = new OrderAddProductFragment();
        newProductFragment.show(fm, ADD_PRODUCT);
    }

    public void deleteItem(View view) {
        final OrderItem item = (OrderItem) view.getTag();
        new AlertDialog.Builder(this)
                .setTitle("Delete Order Item")
                .setMessage("Delete " + item.name + " from order?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        OrderItemRepo.get(OrderActivity.this).delete(mOrder, item);
                        updateInvoice();
                    }}).show();
    }

}
