package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class OrderDetailFragment extends Fragment {

    public static final String ADD_ORDER = "add_order";
    public static final String CUSTOMER = "customer";
    public static final String ORDER = "order";

    private Customer mCustomer;
    private Order mOrder;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        View recyclerView = rootView.findViewById(R.id.product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        if (mOrder != null) {
            setFields(rootView, R.id.order_detail_name, Integer.toString(mOrder.id));
            setFields(rootView, R.id.order_detail_phone, Integer.toString(mOrder.customerId));
            setFields(rootView, R.id.order_detail_email, mOrder.created.toString());
        }

        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new OrderListActivity.SimpleItemRecyclerViewAdapter(OrderRepo.get(this).getmOrders()));
    }

    private void setFields(View rootView, int id, String text) {
        ((TextView) rootView.findViewById(id)).setText(text);
    }
}
