package net.astechdesign.diningsolutions.customers.helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;

public class AddressTextWatcher implements TextWatcher {
    private final Spinner spinner;
    private final TextView customerSelect;
    private ArrayAdapter<String> mTownAdapter;

    public AddressTextWatcher(Spinner spinner, TextView customerSelect, ArrayAdapter<String> mTownAdapter) {
        this.spinner = spinner;
        this.customerSelect = customerSelect;
        this.mTownAdapter = mTownAdapter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString().trim();
        if (value.length() > 0) {
            spinner.setAdapter(mTownAdapter);
            if (customerSelect.getText().length() != 0) {
                customerSelect.setText("");
            }
        }
        CustomerManager.filter("address", value);
    }
}