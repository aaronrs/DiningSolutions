package net.astechdesign.diningsolutions.orders;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.GregorianCalendar;
import java.util.List;

public class OrderItemRecyclerViewAdapter
        extends RecyclerView.Adapter<OrderItemRecyclerViewAdapter.ViewHolder> {

    private static final String DATE_PICKER = "delivery_date_picker";
    private FragmentManager fm;
    private final List<OrderItem> mValues;
    private OrderDetailFragment fragment;

    public OrderItemRecyclerViewAdapter(OrderDetailFragment fragment, List<OrderItem> items) {
        this.fragment = fragment;
        this.fm = fragment.getActivity().getSupportFragmentManager();
        mValues = items;
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
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, CustomerDetailActivity.class);
//                        intent.putExtra(CustomerDetailFragment.ARG_CUSTOMER, holder.getId());
//                        context.startActivity(intent);
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
            mDeliveryDateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.setSelectedOrderItem(mItem);
                    DatePickerFragment dialog = DatePickerFragment.newInstance(GregorianCalendar.getInstance());
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
            mDeliveryDateBtn.setText(mItem.deliveryDate.getDisplayDate());
        }
    }
}
