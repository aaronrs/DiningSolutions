package net.astechdesign.diningsolutions;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import net.astechdesign.diningsolutions.model.Product;

public class ProductEditorFragment extends DialogFragment {

    private EditText mProductId;
    private EditText mProductName;
    private EditText mProductPrice;
    private Product product;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_product, null);
        mProductId = (EditText) view.findViewById(R.id.productId);
        mProductId.setText(product.id.toString());
        mProductName = (EditText) view.findViewById(R.id.productName);
        mProductName.setText(product.name);
        mProductPrice = (EditText) view.findViewById(R.id.productPrice);
        mProductPrice.setText(new Double(product.price).toString());
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_product_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public void setProduct(Product mItem) {
        this.product = mItem;
    }
}
