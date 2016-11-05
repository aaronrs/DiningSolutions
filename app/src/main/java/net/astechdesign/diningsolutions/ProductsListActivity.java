package net.astechdesign.diningsolutions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.ProductsRepo;

import java.util.List;
import java.util.UUID;

/**
 * An activity representing a list of Todos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TodoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProductsListActivity extends AbstractListActivity<Product, ProductsListActivity.ProductViewHolder> {

    private static final String ADD_PRODUCT = "add_product";

    @Override
    protected boolean optionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_product:
                FragmentManager fm = getSupportFragmentManager();
                dialogFragment = new NewProductFragment();
                dialogFragment.show(fm, getDialogTitle());
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String getDialogTitle() {
        return ADD_PRODUCT;
    }

    @Override
    protected List<Product> getAdapter() {
        return ProductsRepo.get(this);
    }

    @Override
    protected View.OnClickListener getItemOnClickListener(final Product value) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(getArgItemId(), value.id.toString());
                    ProductDetailFragment fragment = new ProductDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.product_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, value.id.toString());
                    context.startActivity(intent);
                }
            }
        };
    }

    @Override
    protected ProductViewHolder getNewViewHolder(View view) {
        return new ProductViewHolder(view);
    }

    @Override
    protected void setHolderContent(ProductsListActivity.ProductViewHolder holder, Product value) {
        holder.mNameView.setText(value.name);
    }

    @Override
    protected String getArgItemId() {
        return ProductDetailFragment.ARG_ITEM_ID;
    }

    @Override
    protected int getListContentId() {
        return R.layout.product_list_content;
    }

    @Override
    protected int getListMenu() {
        return R.menu.menu_product_list;
    }

    @Override
    protected int getContainerView() {
        return R.id.product_detail_container;
    }

    @Override
    protected int getRecyclerView() {
        return R.id.product_list;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_product_list;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public Product mItem;

        public ProductViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.product_name);
        }
    }

    public void saveProduct(View view) {
        UUID id = (UUID) view.getTag();
        String newName = ((EditText) this.findViewById(R.id.product_detail)).getText().toString();
        EditText price = (EditText)this.findViewById(R.id.product_price);
        String newPrice = price.getText().toString();
        ProductsRepo.update(id, newName, newPrice);
    }
}
