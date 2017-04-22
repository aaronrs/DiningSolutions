package net.astechdesign.diningsolutions.orders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderActivity extends AppCompatActivity
        implements OrderAddProductFragment.ProductAddListener {

    public static final String CUSTOMER = "customer";
    public static final String ADD_PRODUCT = "add_product";
    public static final String EDIT_ENTRY = "edit_entry";

    private Customer mCustomer;
    private Order mOrder;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCustomer = (Customer) getIntent().getSerializableExtra(CUSTOMER);
        mOrder = OrderRepo.get(this).getCurrentOrder(mCustomer);

        setContentView(R.layout.activity_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
                Snackbar.make(view, "Emailing order to " + mCustomer.email.address, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView invoiceDate = (TextView) findViewById(R.id.order_invoice_date);
        invoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(new DeliveryDatePicked(OrderActivity.this), (DSDDate) view.getTag());
                dialog.show(getSupportFragmentManager(), "date_picker");

                Snackbar.make(view, "Clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void displayOrder() {
        if (mOrder.getId() == null) {
            return;
        }

        OrderDetailFragment fragment = new OrderDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER, mCustomer);

        args.putSerializable(ORDER, mOrder);
        initialiseOrderView();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_container, fragment)
                .commit();
    }

    private void initialiseOrderView() {
        setFields(R.id.order_invoice_number, mOrder.invoiceNumber);
        TextView invoiceDateView = setFields(R.id.order_invoice_date, mOrder.created != null ? mOrder.created.getDisplayDate() : "");
        invoiceDateView.setTag(mOrder.created);
    }

    private TextView setFields(int id, String text) {
        TextView view = (TextView) findViewById(id);
        if (text != null) {
            view.setText(text);
        }
        return view;
    }

    private Order createNewOrder() {
        return OrderRepo.get(this).create(mCustomer);
    }

    private void sendEmail() {
        EmailTemplate.sendEmail(this, mCustomer, mOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    public Customer getCustomer() {
        return mCustomer;
    }

    public Order getOrder() {
        return mOrder;
    }

    class DeliveryDatePicked implements DatePickerFragment.DatePickerListener{
        private final OrderActivity activity;

        public DeliveryDatePicked(OrderActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onDatePicked(DSDDate date) {
            for (OrderItem item : mOrder.orderItems) {
                if (date.after(item.deliveryDate)) {
                    OrderItemRepo.get(activity).updateDelivery(item, date);
                }
            }
            OrderRepo.get(activity).updateInvoiceDate(mOrder, date);
            activity.updateInvoice();
        }
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
        mOrder = OrderRepo.get(this).getCurrentOrder(mCustomer);
        displayOrder();
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

    public void newOrder(View view) {
        createNewOrder();
        displayOrder();
    }
}
