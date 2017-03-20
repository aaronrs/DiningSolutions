package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.UUID;

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.CUSTOMER;
import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderActivity extends AppCompatActivity
        implements OrderAddProductFragment.ProductAddListener,
        DatePickerFragment.DatePickerListener,
        EditEntryFragment.EditEntryAddListener {

    public static final String CUSTOMER_ID = "customer_id";
    public static final String ADD_PRODUCT = "add_product";
    public static final String EDIT_ENTRY = "edit_entry";
    private Customer mCustomer;
    private Order mOrder;
    private Toolbar toolbar;

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

        UUID customerId = (UUID) getIntent().getSerializableExtra(CUSTOMER_ID);
        mCustomer = CustomerRepo.get(this).get(customerId);
        ((TextView)findViewById(R.id.order_detail_name)).setText(mCustomer.name);

        initialiseCustomerView();

        mOrder = OrderRepo.get(this).getCurrentOrder(mCustomer);
        if (mOrder == null) {
            createNewOrder();
            mOrder = OrderRepo.get(this).getCurrentOrder(mCustomer);
        }

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

    private void showOrder() {
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

    private void initialiseCustomerView() {
        setFields(R.id.order_detail_name, mCustomer.name, "Customer Name");
        setFields(R.id.order_detail_phone, mCustomer.phone == null ? "" : mCustomer.phone.number);
        setFields(R.id.order_detail_email, mCustomer.email == null ? "" : mCustomer.email.address);
        setFields(R.id.order_detail_address, mCustomer.address.toString());
    }

    private void initialiseOrderView() {
        setFields(R.id.order_invoice_number, mOrder.invoiceNumber);
        TextView invoiceDateView = setFields(R.id.order_invoice_date, mOrder.created != null ? mOrder.created.getDisplayDate() : "");
        invoiceDateView.setTag(mOrder.created);
    }

    private TextView setFields(int id, final String text, final String name) {
        TextView textView = setFields(id, text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                EditEntryFragment fragment = new EditEntryFragment();
                fragment.value(name, text == null ? "" : text);
                fragment.show(fm, EDIT_ENTRY);
            }
        });
        return textView;
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
                showOrder();
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
        mOrder = OrderRepo.get(this).getCurrentOrder(mCustomer);
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
                showOrder();
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

    @Override
    public void onEditPositiveClick(DialogInterface dialog) {

    }
}
