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
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductRepo;

import org.w3c.dom.Text;

import java.util.List;

public class OrderAddProductFragment extends DialogFragment {

    private ProductAddListener mListener;
    private TextView mBatchView;
    private TextView mPriceView;
    private Spinner mQuantityView;
    private Product mProduct;
    private OrderItem mItem;
    private int mQuantity;

    public interface ProductAddListener {
        void onAddProductPositiveClick(DialogInterface dialog, OrderItem item, Product product, double price, int quantity, String batch);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_product, null);
        mPriceView = (TextView) view.findViewById(R.id.add_item_price);
        mBatchView = (TextView) view.findViewById(R.id.add_item_batch);
        mQuantityView = (Spinner) view.findViewById(R.id.add_item_quantity);
        mQuantityView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, new String[]{"1","2","3","4","5","6","7","8","9","10"}));
        mQuantityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQuantity = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        List<Product> productList = ProductRepo.get(getContext()).get();
        ArrayAdapter<Product> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                productList);

        final AutoCompleteTextView productDropdown = (AutoCompleteTextView) view.findViewById(R.id.add_item_name_select);
        productDropdown.setThreshold(3);
        productDropdown.setAdapter(adapter);
        productDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mProduct = (Product)parent.getAdapter().getItem(position);
                mPriceView.setText(Double.toString(mProduct.price));
            }
        });
        if (mItem != null) {
            mPriceView.setText(Double.toString(mItem.price));
            mBatchView.setText(mItem.batch);
            mQuantityView.setSelection(mItem.quantity - 1);

            for (int i = 0; i < productList.size() ; i++) {
                if (productList.get(i).name.equals(mItem.name)) {
                    productDropdown.setListSelection(i);
                    mProduct = productList.get(i);
                    productDropdown.setText(mProduct.name);
                    mPriceView.requestFocus();
                    break;
                }
            }
            productDropdown.setEnabled(false);
        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onAddProductPositiveClick(dialog,
                                mItem,
                                mProduct,
                                Double.parseDouble(mProduct != null ? mPriceView.getText().toString() : "0"),
                                mProduct != null ? mQuantity : 0,
                                mBatchView.getText().toString());
                    }
                })
                .create();
    }

    public void setOrderItem(OrderItem item) {
        this.mItem = item;
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