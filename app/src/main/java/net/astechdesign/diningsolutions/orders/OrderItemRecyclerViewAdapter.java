package net.astechdesign.diningsolutions.orders;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.orders.OrderActivity.ADD_PRODUCT;

public class OrderItemRecyclerViewAdapter
        extends RecyclerView.Adapter<OrderItemRecyclerViewAdapter.ViewHolder> {

    private FragmentManager fm;
    private final List<OrderItem> mValues;
    private OrderDetailFragment fragment;
    private FragmentManager mSupportFragmentManager;

    public OrderItemRecyclerViewAdapter(OrderDetailFragment fragment, List<OrderItem> items, FragmentManager supportFragmentManager) {
        this.fragment = fragment;
        this.fm = fragment.getActivity().getSupportFragmentManager();
        this.mValues = items;
        this.mSupportFragmentManager = supportFragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setItem(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = mSupportFragmentManager;
                OrderAddProductFragment newProductFragment = new OrderAddProductFragment();
                newProductFragment.setOrderItem(holder.mItem);
                newProductFragment.show(fm, ADD_PRODUCT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mBatchView;
        public final TextView mPriceView;
        public final TextView mQuantityView;
        public final TextView mCostView;
        public final Button mDeliveryDateBtn;
        public final ImageButton mDeleteBtn;
        public OrderItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.product_name);
            mBatchView = (TextView) view.findViewById(R.id.batch_number);
            mPriceView = (TextView) view.findViewById(R.id.product_price);
            mQuantityView = (TextView) view.findViewById(R.id.product_quantity);
            mCostView = (TextView) view.findViewById(R.id.product_cost);
            mDeliveryDateBtn = (Button) view.findViewById(R.id.delivery_date_btn);
            mDeleteBtn = (ImageButton) view.findViewById(R.id.delete_item);
            mDeliveryDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.setSelectedOrderItem(mItem);
                    DatePickerFragment dialog = DatePickerFragment.newInstance(fragment, mItem.deliveryDate);
                    dialog.setTargetFragment(fragment, DatePickerFragment.REQUEST_DATE);
                    dialog.show(fm, DATE_PICKER);
                }
            });
        }

        public void setItem(OrderItem item) {
            this.mItem = item;
            mNameView.setText(mItem.name);
            mBatchView.setText(mItem.batch);
            mPriceView.setText(Double.toString(mItem.price));
            mQuantityView.setText(Integer.toString(mItem.quantity));
            mCostView.setText(String.format("%.2f", mItem.cost()));
            mDeliveryDateBtn.setText(mItem.deliveryDate.getShortDisplayDate());
            mDeleteBtn.setTag(mItem);
        }
    }
}
