package net.astechdesign.diningsolutions.orders;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import static net.astechdesign.diningsolutions.orders.OrderDetailFragment.ORDER;

public class OrderActivity extends AppCompatActivity {

    public static final String CUSTOMER = "customer";
    public static final String ADD_PRODUCT = "add_product";
    public static final String EDIT_ENTRY = "edit_entry";

    private Customer mCustomer;
    private Order mOrder;
    private Toolbar toolbar;
    private OrderFragment orderFragment;

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

//    public void deleteItem(View view) {
//        final OrderItem item = (OrderItem) view.getTag();
//        new AlertDialog.Builder(this)
//                .setTitle("Delete Order Item")
//                .setMessage("Delete " + item.name + " from order?")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        OrderItemRepo.get(OrderActivity.this).delete(mOrder, item);
//                        updateInvoice();
//                    }}).show();
//    }

    public void newOrder(View view) {
        OrderRepo.get(this).add(Order.create(mCustomer));
        orderFragment.update();
    }

    public void setOrderFragment(OrderFragment orderFragment) {
        this.orderFragment = orderFragment;
    }
}
