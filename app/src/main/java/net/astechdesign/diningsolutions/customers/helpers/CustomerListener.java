package net.astechdesign.diningsolutions.customers.helpers;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;

import java.util.List;

public class CustomerListener implements AdapterView.OnItemSelectedListener {
    private final TextView customerSelect;
    private final TextView addressSelect;
    private final List<String> towns;

    public CustomerListener(TextView customerSelect, TextView addressSelect, List<String> towns) {
        this.customerSelect = customerSelect;
        this.addressSelect = addressSelect;
        this.towns = towns;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            if (customerSelect.getText().length() != 0) {
                customerSelect.setText("");
            }
            if (addressSelect.getText().length() != 0) {
                addressSelect.setText("");
            }
            CustomerManager.filter("town", towns.get(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        CustomerManager.getCustomerList();
        CustomerManager.updateView();
    }
}
