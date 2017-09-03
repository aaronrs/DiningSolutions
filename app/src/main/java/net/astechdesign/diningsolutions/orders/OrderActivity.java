package net.astechdesign.diningsolutions.orders;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.app.OrderManager;
import net.astechdesign.diningsolutions.app.flow.OptionsActivity;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;

public class OrderActivity extends OptionsActivity {

    public static final String ADD_PRODUCT = "add_product";
    public static final String EDIT_ENTRY = "edit_entry";

    private List<Order> mOrders;
    private Order mOrder;
    private Toolbar toolbar;
    private OrderFragment orderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrders = OrderManager.getOrders();
        mOrder = OrderManager.getCurrentOrder();
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
                Snackbar.make(view, "Emailing order to " + CurrentCustomer.get().email.address, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void sendEmail() {
        EmailTemplate.sendEmail(this, mOrder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void setOrderFragment(OrderFragment orderFragment) {
        this.orderFragment = orderFragment;
    }

    public void newOrder() {
        OrderManager.create();
    }
}
