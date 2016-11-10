package net.astechdesign.diningsolutions;

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

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.DSDTime;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductsRepo;


public class EditProductFragment extends DialogFragment {

    private TextView mNameText;
    private TextView mPriceText;
    private Product product;
    private EditProductListener mListener;

    public interface EditProductListener {
        void onDialogPositiveClick(DialogInterface dialog, Product product);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_product, null);
        mNameText = (TextView) view.findViewById(R.id.product_name);
        mPriceText = (TextView) view.findViewById(R.id.product_price);
        mNameText.setText(product.getName());
        mPriceText.setText(new Double(product.getPrice()).toString());
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.edit_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        product.setName(mNameText.getText().toString());
                        product.setPrice(mPriceText.getText().toString());
                        mListener.onDialogPositiveClick(dialog, product);
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
