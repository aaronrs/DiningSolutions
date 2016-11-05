package net.astechdesign.diningsolutions;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class NewProductFragment extends DialogFragment {

    private EditText mProductId;
    private EditText mProductName;
    private EditText mProductPrice;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_product, null);
        mProductId = (EditText) view.findViewById(R.id.productId);
        mProductName = (EditText) view.findViewById(R.id.productName);
        mProductPrice = (EditText) view.findViewById(R.id.productPrice);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_product_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
