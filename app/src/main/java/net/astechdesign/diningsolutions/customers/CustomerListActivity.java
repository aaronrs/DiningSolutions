package net.astechdesign.diningsolutions.customers;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.app.flow.OptionsActivity;
import net.astechdesign.diningsolutions.customers.helpers.AddressTextWatcher;
import net.astechdesign.diningsolutions.customers.helpers.CustomerListener;
import net.astechdesign.diningsolutions.customers.helpers.CustomerTextWatcher;

public class CustomerListActivity extends OptionsActivity {

    private RecyclerView mRecyclerView;
    private CustomerListRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        CustomerManager.setActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.customer_list);
        viewAdapter = new CustomerListRecyclerViewAdapter(this, CustomerManager.getCustomerList());
        recyclerView.setAdapter(viewAdapter);

        final Spinner townSpinner = (Spinner) findViewById(R.id.town_select);
        final TextView customerSelect = (EditText) findViewById(R.id.customer_select);
        final TextView addressSelect = (EditText) findViewById(R.id.address_select);

        ArrayAdapter townAdapter = CustomerManager.getTownAdapter();
        townSpinner.setAdapter(townAdapter);

        townSpinner.setOnItemSelectedListener(new CustomerListener(customerSelect, addressSelect, CustomerManager.getTowns()));
        customerSelect.addTextChangedListener(new CustomerTextWatcher(townSpinner, addressSelect, townAdapter));
        addressSelect.addTextChangedListener(new AddressTextWatcher(townSpinner, customerSelect, townAdapter));
    }

    public void updateView() {
        viewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
