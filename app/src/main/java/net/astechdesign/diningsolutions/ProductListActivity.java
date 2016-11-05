package net.astechdesign.diningsolutions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductsRepo;

import java.util.List;
import java.util.UUID;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Product", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ProductRecyclerViewAdapter(ProductsRepo.get(this)));
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Product product = mValues.get(position);
            holder.mItem = product;
            holder.mNameView.setText(product.name);
            holder.mPriceView.setText(Double.toString(product.price));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final View mView;
            public final EditText mNameView;
            public final EditText mPriceView;
            public Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (EditText) view.findViewById(R.id.product_name);
                mPriceView = (EditText) view.findViewById(R.id.product_price);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }

            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ProductEditorFragment productEditorFragment = new ProductEditorFragment();
                productEditorFragment.setProduct(mItem);
                productEditorFragment.show(fm, "EDIT_PRODUCT");
            }
        }
    }


}
