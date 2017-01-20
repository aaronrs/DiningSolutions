package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;

public class OrderDetailFragment extends Fragment {

    public static final String ADD_ORDER = "add_order";
    public static final String CUSTOMER = "customer";
    public static final String ORDER = "order";
    private OrderRepo mOrderRepo;

    private Customer mCustomer;
    private Order mOrder;
    private OrderItemRepo orderItemRepo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderRepo = OrderRepo.get(getContext());
        orderItemRepo = OrderItemRepo.get(getContext());

        mCustomer = (Customer)getArguments().getSerializable(CUSTOMER);
        if (getArguments().containsKey(ORDER)) {
            mOrder = (Order) getArguments().getSerializable(ORDER);
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("Orders");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);
        setupRecyclerView(rootView);

//        setFields(rootView, R.id.order_detail_name, mCustomer.name);
//        setFields(rootView, R.id.order_detail_phone, mCustomer.phone == null ? "" : mCustomer.phone.number);
//        setFields(rootView, R.id.order_detail_email, mCustomer.email == null ? "" : mCustomer.email.address);
//        setFields(rootView, R.id.order_invoice_number, mOrder.invoiceNumber);
//
        return rootView;
    }

    private void setupRecyclerView(View rootView) {
        View recyclerView = rootView.findViewById(R.id.order_items_list);
        assert recyclerView != null;
        ((RecyclerView)recyclerView).setAdapter(new SimpleItemRecyclerViewAdapter(orderItemRepo.getOrderItems(mOrder)));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<OrderItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<OrderItem> items) {
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
            public OrderItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.product_name);
                mBatchView = (TextView) view.findViewById(R.id.batch_number);
                mPriceView = (TextView) view.findViewById(R.id.product_price);
                mQuantityView = (TextView) view.findViewById(R.id.product_quantity);
                mCostView = (TextView) view.findViewById(R.id.product_cost);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }

            public void setItem(OrderItem item) {
                this.mItem = item;
                mNameView.setText(item.name);
                mBatchView.setText(item.batch);
                mPriceView.setText(Double.toString(item.price));
//                mQuantityView.setText(item.quantity);
                mCostView.setText(Double.toString(item.price * item.quantity));
            }
        }
    }

    private void setFields(View rootView, int id, String text) {
        ((TextView) rootView.findViewById(id)).setText(text);
    }
}
