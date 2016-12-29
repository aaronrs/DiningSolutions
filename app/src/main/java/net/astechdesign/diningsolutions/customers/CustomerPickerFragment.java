package net.astechdesign.diningsolutions.customers;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import net.astechdesign.diningsolutions.R;

public class CustomerPickerFragment extends DialogFragment {

    private static final String ARG_CUSTOMER = "time";
    public static final String EXTRA_CUSTOMER = "net.astechdesign.diningsolutions.customer";
    public static final int REQUEST_CUSTOMER = 2;

    public static CustomerPickerFragment newInstance() {
        CustomerPickerFragment fragment = new CustomerPickerFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_select_customer, null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.customer_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, "Joe Bloggs");
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, String name) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CUSTOMER, name);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
