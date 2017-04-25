package net.astechdesign.diningsolutions.orders;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductRepo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderAddProductFragment extends DialogFragment {

    private ProductAddListener mListener;
    private TextView mBatchView;
    private TextView mPriceView;
    private Spinner mQuantityView;
    private Product mProduct;
    private OrderItem mItem;
    private int mQuantity;
    private Map<String, Product> productMap;

    public void setListener(OrderFragment listener) {
        mListener = listener;
    }

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
        productMap = new TreeMap<>();
        for (Product product : productList) {
            productMap.put(product.name, product);
        }

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                productList);

        final AutoCompleteTextView productDropdown = (AutoCompleteTextView) view.findViewById(R.id.add_item_name_select);
        productDropdown.setThreshold(1);
        productDropdown.setAdapter(adapter);
        productDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mProduct = (Product)parent.getAdapter().getItem(position);
                mPriceView.setText(Double.toString(mProduct.price));
            }
        });

        final Button newProductBtn = (Button) view.findViewById(R.id.add_new_product);
        newProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = productDropdown.getText().toString();
                double price = Double.parseDouble(mPriceView.getText().toString());
                ProductRepo productRepo = ProductRepo.get(getContext());
                productRepo.addOrUpdate(Product.create(name, price));
                Product newProduct = productRepo.get(name);
                mProduct = newProduct;
                view.setEnabled(false);
            }
        });

        AutoCompleteTextView.Validator validator = new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                if (productMap.containsKey(text.toString().toUpperCase())) {
                    newProductBtn.setEnabled(false);
                    return true;
                }
                newProductBtn.setEnabled(true);
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return invalidText.toString().toUpperCase();
            }
        };
        productDropdown.setValidator(validator);

        if (mItem != null) {
            mPriceView.setText(Double.toString(mItem.price));
            mBatchView.setText(mItem.batch);
            mQuantityView.setSelection(mItem.quantity - 1);

            Product mProduct = productMap.get(mItem.name);
            productDropdown.setListSelection(productList.indexOf(mProduct));
            productDropdown.setText(mProduct.name);
            mPriceView.requestFocus();
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
}