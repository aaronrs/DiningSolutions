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
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.UUID;

public class OrderDetailFragment extends Fragment {

    public static final String ARG_ORDER = "order";

    private Order mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ORDER)) {
            mItem = (Order)getArguments().getSerializable(ARG_ORDER);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Orders");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_detail, container, false);

        View recyclerView = rootView.findViewById(R.id.product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


        if (mItem != null) {
            setFields(rootView, R.id.order_detail_name, Integer.toString(mItem.id));
            setFields(rootView, R.id.order_detail_phone, Integer.toString(mItem.customerId));
            setFields(rootView, R.id.order_detail_email, mItem.created.toString());
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
