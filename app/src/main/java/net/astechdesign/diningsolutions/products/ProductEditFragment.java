package net.astechdesign.diningsolutions.products;

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
import android.widget.CheckBox;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Product;


public class ProductEditFragment extends DialogFragment {

    private TextView mNameText;
    private TextView mPriceText;
    private TextView mBarcodeText;
    private Product product;
    private EditProductListener mListener;

    public interface EditProductListener {
        void onEditProductPositiveClick(DialogInterface dialog, Product product, boolean deleted);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (product == null) {
            product = Product.create("", "", 0.00, "");
        }
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_product, null);
        mNameText = (TextView) view.findViewById(R.id.product_name);
        mPriceText = (TextView) view.findViewById(R.id.product_price);
        mBarcodeText = (TextView) view.findViewById(R.id.product_barcode);
        mNameText.setText(product.name);
        mPriceText.setText(new Double(product.price).toString());
        mBarcodeText.setText(product.barcode);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.edit_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean deleted = ((CheckBox) view.findViewById(R.id.delete_checkBox)).isChecked();
                        if (product.getId() == null && mNameText.length() == 0) {
                            return;
                        }
                        Product newProduct = Product.create(product.getId(),
                                mNameText.getText().toString(),
                                product.description,
                                Double.parseDouble(mPriceText.getText().toString()),
                                mBarcodeText.getText().toString());
                        mListener.onEditProductPositiveClick(dialog, newProduct, deleted);
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EditProductListener) activity;
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

    public void setProduct(Product product) {
        this.product = product;
    }
}
