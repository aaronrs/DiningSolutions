package net.astechdesign.diningsolutions.orders;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductRepo;

import java.util.List;


public class OrderAddProductFragment extends DialogFragment {

    private ProductAddListener mListener;
    private EditText mBatchView;
    private EditText mPriceView;
    private EditText mQuantityView;
    private Product mProduct;

    public interface ProductAddListener {
        void onDialogPositiveClick(DialogInterface dialog, Product product, double price, int quantity, String batch);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_product, null);
        mPriceView = (EditText) view.findViewById(R.id.product_price);
        mBatchView = (EditText) view.findViewById(R.id.product_batch);
        mQuantityView = (EditText) view.findViewById(R.id.product_quantity);

        List<Product> productList = ProductRepo.get(getContext()).get();
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                productList);

        final AutoCompleteTextView productDropdown = (AutoCompleteTextView) view.findViewById(R.id.product_name_select);
        productDropdown.setThreshold(3);
        productDropdown.setAdapter(adapter);
        productDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mProduct = (Product)parent.getAdapter().getItem(position);
                mPriceView.setText(Double.toString(mProduct.price));
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(dialog,
                                mProduct,
                                Double.parseDouble(mPriceView.getText().toString()),
                                Integer.parseInt(mQuantityView.getText().toString()),
                                mBatchView.getText().toString());
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OrderAddProductFragment.ProductAddListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ProductAddListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }

}