package net.astechdesign.diningsolutions.customers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

public class CustomerEditFragment extends DialogFragment {

    public static final String EDIT_CUSTOMER = "edit_customer";

    private CustomerEditListener mListener;
    private Customer mCurrentCustomer;
    private TextView mNameText;
    private TextView mPhoneText;
    private TextView mAddressLineText;
    private TextView mAddressTownText;
    private TextView mAddressCountyText;
    private TextView mAddressPostcodeText;
    private TextView mEmailText1;

    public void setCustomer(Customer mCurrentCustomer) {
        this.mCurrentCustomer = mCurrentCustomer;
    }

    public interface CustomerEditListener {
        void onCustomerEditClick(DialogInterface dialog, Customer customer);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Customer customer = mCurrentCustomer;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_customer, null);
        mNameText = (TextView) view.findViewById(R.id.customer_name);
        mEmailText1 = (TextView) view.findViewById(R.id.customer_email);
        mPhoneText = (TextView) view.findViewById(R.id.customer_phone);
        mAddressLineText = (TextView) view.findViewById(R.id.address_line);
        mAddressTownText = (TextView) view.findViewById(R.id.address_town);
        mAddressCountyText = (TextView) view.findViewById(R.id.address_county);
        mAddressPostcodeText = (TextView) view.findViewById(R.id.address_postcode);

        if (mCurrentCustomer != Customer.newCustomer) {
            mNameText.setText(customer.name);
            mEmailText1.setText(customer.email.address);
            mPhoneText.setText(customer.phone.number);
            Address address = customer.address;
            if (address != null) {
                mAddressLineText.setText(address.line);
                mAddressTownText.setText(address.town);
                mAddressCountyText.setText(address.county);
                mAddressPostcodeText.setText(address.postcode);
            }
        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.edit_customer_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Address address = Address.create(
                                null,
                                getText(mAddressLineText),
                                getText(mAddressTownText),
                                getText(mAddressCountyText),
                                getText(mAddressPostcodeText)
                                );
                        Customer newCustomer = Customer.create(
                                customer.getId(),
                                getText(mNameText),
                                getText(mEmailText1),
                                getText(mPhoneText),
                                true,
                                customer.created,
                                "",
                                address,
                                DSDDate.create()
                                );
                        mListener.onCustomerEditClick(dialog, newCustomer);
                    }
                })
                .create();
    }

    private String getText(TextView view) {
        if (view.getText() == null) return "";
        return view.getText().toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CustomerEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CustomerEditListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }
}
