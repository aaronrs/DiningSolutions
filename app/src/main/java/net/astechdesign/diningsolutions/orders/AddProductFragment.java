package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.database.tables.ProductCursorWrapper;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductRepo;


public class AddProductFragment extends DialogFragment {

    private ProductAddListener mListener;

    public interface ProductAddListener {
        void onDialogPositiveClick(DialogInterface dialog, Product product);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_product, null);
        AutoCompleteTextView productDropdown = (AutoCompleteTextView) view.findViewById(R.id.product_name_select);
        productDropdown.setThreshold(3);
        productDropdown.setAdapter(getProductsAdapter());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.add_product_title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Product product = new Product(1, "name", "desc", 10.0, "1234", 0);
                        mListener.onDialogPositiveClick(dialog, product);
                    }
                })
                .create();
    }

    private SimpleCursorAdapter getProductsAdapter() {
        final Context context = getContext().getApplicationContext();
        final ProductRepo productRepo = ProductRepo.get(context);
        FilterQueryProvider filter = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint != null) {
                    return productRepo.getProductCursor(constraint);
                }
                return null;
            }
        };
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                productRepo.getProductCursor(),
                new String[]{ProductTable.PRODUCT_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );
        simpleCursorAdapter.setFilterQueryProvider(filter);
        simpleCursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                Product product = new ProductCursorWrapper(cursor).getProduct();
                return product.name;
            }
        });
        return simpleCursorAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddProductFragment.ProductAddListener) activity;
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