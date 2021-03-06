package net.astechdesign.diningsolutions.products;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductRepo;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductEditFragment.EditProductListener {

    public static final String EDIT_PRODUCT = "EDIT_PRODUCT";
    private static final String ADD_PRODUCT = "add_product";
    private ProductRecyclerViewAdapter adapter;
    private View mRecyclerView;
    private ProductEditFragment newProductFragment;
    private List<Product> mProductList;
    private List<Product> mFilteredProducts;
    private EditText mProductSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Product List");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mProductSelect = (EditText) findViewById(R.id.product_select);
        mProductSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString().trim();
                if (value.length() == 0 || value.length() > 2) {
                    updateRecycler(value);
                }
            }
        });

        mProductList = ProductRepo.get(this).get();
        mFilteredProducts = new ArrayList<>();
        mFilteredProducts.addAll(mProductList);

        mRecyclerView = findViewById(R.id.product_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
    }

    private void updateRecycler(String value) {
        mProductList = ProductRepo.get(this).get();
        mFilteredProducts.clear();
        if (value.trim().length() == 0) {
            mFilteredProducts.addAll(mProductList);
        }
        for (Product product : mProductList) {
            if (product.name.toLowerCase().contains(value.toLowerCase())) {
                mFilteredProducts.add(product);
            }
        }
        setupRecyclerView(mRecyclerView);
    }

    private void setupRecyclerView(@NonNull View recyclerView) {
        adapter = new ProductRecyclerViewAdapter(mFilteredProducts);
        ((RecyclerView) recyclerView).setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_product:
                mProductSelect.setText("");
                FragmentManager fm = getSupportFragmentManager();
                newProductFragment = new ProductEditFragment();
                newProductFragment.show(fm, ADD_PRODUCT);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEditProductPositiveClick(DialogInterface dialog, Product product, boolean deleted) {
        if (deleted) {
            ProductRepo.get(this).delete(product);
            Toast.makeText(this, "Product deleted: " + product.name, Toast.LENGTH_SHORT).show();
        } else {
            ProductRepo.get(this).addOrUpdate(product);
        }
        updateRecycler("");
    }

    public class ProductRecyclerViewAdapter
            extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

        private final List<Product> mValues;

        public ProductRecyclerViewAdapter(List<Product> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProductSelect.setText("");
                    FragmentManager fm = getSupportFragmentManager();
                    ProductEditFragment dialog = new ProductEditFragment();
                    dialog.setProduct(holder.getItem());
                    dialog.show(fm, EDIT_PRODUCT);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mNameView;
            private final TextView mPriceView;
            private final TextView mBarcodeView;
            private Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.product_name);
                mPriceView = (TextView) view.findViewById(R.id.product_price);
                mBarcodeView = (TextView) view.findViewById(R.id.product_barcode);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }

            public void setItem(Product item) {
                this.mItem = item;
                mNameView.setText(item.name);
                mPriceView.setText("£" + Double.toString(item.price));
//                mBarcodeView.setText(item.barcode);
            }

            public Product getItem() {
                return mItem;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        System.out.println("I am here");
    }
}
