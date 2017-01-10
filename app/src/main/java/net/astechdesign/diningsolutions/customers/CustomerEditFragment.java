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
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Phone;

public class CustomerEditFragment extends DialogFragment {

    private CustomerEditFragment.CustomerEditListener mListener;
    private Customer mCurrentCustomer;
    private TextView mNameText;
    private TextView mPhoneText;
    private TextView mAddressNameText;
    private TextView mAddressLine1Text;
    private TextView mAddressLine2Text;
    private TextView mAddressTownText;
    private TextView mAddressCountyText;
    private TextView mAddressPostcodeText;
    private TextView mEmailText1;
    private TextView mEmailText2;

    public void setCustomer(Customer mCurrentCustomer) {
        this.mCurrentCustomer = mCurrentCustomer;
    }

    public interface CustomerEditListener {
        void onDialogPositiveClick(DialogInterface dialog, Customer customer);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Customer customer = mCurrentCustomer;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_customer, null);
        mNameText = (TextView) view.findViewById(R.id.customer_name);
        mEmailText1 = (TextView) view.findViewById(R.id.customer_email1);
        mEmailText2 = (TextView) view.findViewById(R.id.customer_email2);
        mPhoneText = (TextView) view.findViewById(R.id.customer_phone);
        mAddressNameText = (TextView) view.findViewById(R.id.address_name);
        mAddressLine1Text = (TextView) view.findViewById(R.id.address_line1);
        mAddressLine2Text = (TextView) view.findViewById(R.id.address_line2);
        mAddressTownText = (TextView) view.findViewById(R.id.address_town);
        mAddressCountyText = (TextView) view.findViewById(R.id.address_county);
        mAddressPostcodeText = (TextView) view.findViewById(R.id.address_postcode);

        if (mCurrentCustomer != null) {
            mNameText.setText(customer.name);
            mEmailText1.setText(customer.email.address.split("@")[0]);
            mEmailText2.setText(customer.email.address.split("@")[1]);
            mPhoneText.setText(customer.phone.number);
            Address address = customer.address;
            if (address != null) {
                mAddressNameText.setText(address.name);
                mAddressLine1Text.setText(address.line1);
                mAddressLine2Text.setText(address.line2);
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
                        Address address = new Address(
                                customer == null ? -1 : customer.address.id,
                                getText(mAddressNameText),
                                getText(mAddressLine1Text),
                                getText(mAddressLine2Text),
                                getText(mAddressTownText),
                                getText(mAddressCountyText),
                                getText(mAddressPostcodeText)
                                );
                        Customer newCustomer = new Customer(
                                customer == null ? -1 : customer.id,
                                getText(mNameText),
                                getText(mEmailText1) + "@" + getText(mEmailText2),
                                getText(mPhoneText),
                                true,
                                customer == null ? new DSDDate() : customer.created,
                                "",
                                address
                                );
                        mListener.onDialogPositiveClick(dialog, newCustomer);
                    }
                })
                .create();
    }

    private String getText(TextView view) {
        if (view.getText() == null) return "";
        return view.getText().toString();
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
}
