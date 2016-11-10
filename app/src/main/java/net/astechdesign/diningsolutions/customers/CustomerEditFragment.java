package net.astechdesign.diningsolutions.customers;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Phone;

import java.util.Date;

public class CustomerEditFragment extends DialogFragment {

    private TextView mNameText;
    private TextView mPriceText;
    private Customer customer;
    private CustomerEditFragment.CustomerEditListener mListener;

    public interface CustomerEditListener {
        void onDialogPositiveClick(DialogInterface dialog, Customer customer);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_product, null);
        mNameText = (TextView) view.findViewById(R.id.product_name);
        mPriceText = (TextView) view.findViewById(R.id.product_price);
        mNameText.setText(customer.name);
        mPriceText.setText(customer.phone.number);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.edit_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Customer newCustomer = new Customer(CustomerEditFragment.this.customer.id, new Date(), mNameText.getText().toString(), null, null, new Phone(mPriceText.getText().toString()));
                        mListener.onDialogPositiveClick(dialog, newCustomer);
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CustomerEditFragment.CustomerEditListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
