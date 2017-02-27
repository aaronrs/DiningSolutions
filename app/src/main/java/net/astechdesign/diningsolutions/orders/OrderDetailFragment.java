package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;

public class OrderDetailFragment extends Fragment {

    public static final String CUSTOMER = "customer";
    public static final String ORDER = "order";

    private Order mOrder;
    private OrderItemRepo orderItemRepo;
    private OrderItem selectedOrderItem;
    private View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderItemRepo = OrderItemRepo.get(getContext());

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
        rootView = inflater.inflate(R.layout.order_detail, container, false);
        TextView totalView = (TextView) rootView.findViewById(R.id.order_detail_total);
        totalView.setText(String.format("%.2f", mOrder.total()));
        setupRecyclerView(rootView);

        return rootView;
    }

    private void setupRecyclerView(View rootView) {
        View recyclerView = rootView.findViewById(R.id.order_items_list);
        assert recyclerView != null;
        ((RecyclerView)recyclerView).setAdapter(
                new OrderItemRecyclerViewAdapter(
                        this,
                        orderItemRepo.getOrderItems(mOrder),
                        getActivity().getSupportFragmentManager()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            DSDDate date = (DSDDate) data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            orderItemRepo.updateDelivery(selectedOrderItem, date);
            setupRecyclerView(rootView);
            return;
        }

    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }
}
