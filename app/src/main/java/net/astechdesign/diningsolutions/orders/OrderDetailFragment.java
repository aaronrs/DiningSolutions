package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.app.SourceMode;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;

public class OrderDetailFragment extends Fragment implements DatePickerFragment.DatePickerListener {

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
//        ((RecyclerView)recyclerView).setAdapter(
//                new OrderItemRecyclerViewAdapter(
//                        this,
//                        orderItemRepo.getOrderItems(mOrder),
//                        getActivity().getSupportFragmentManager()));
    }

    @Override
    public void onDatePicked(SourceMode mode, DSDDate date) {
        orderItemRepo.updateDelivery(selectedOrderItem, date);
        setupRecyclerView(rootView);
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }
}
