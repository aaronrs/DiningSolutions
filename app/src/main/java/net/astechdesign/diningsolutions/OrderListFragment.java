package net.astechdesign.diningsolutions;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderListFragment extends Fragment {
    private RecyclerView mOrderRecyclerView;
    private OrderAdapter mAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        mOrderRecyclerView = (RecyclerView) view.findViewById(R.id.order_recycler_view);
        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        OrderRepo repo = OrderRepo.get(getActivity());
        List<Order> orders = repo.getmOrders();

        mAdapter = new OrderListFragment.OrderAdapter(orders);
        mOrderRecyclerView.setAdapter(mAdapter);
    }

    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mDateTextView;
        private final TextView mBoxCountTextView;

        private Order mOrder;

        public OrderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mDateTextView = (TextView)itemView.findViewById(R.id.item_list_date);
            mBoxCountTextView = (TextView)itemView.findViewById(R.id.item_list_box_count);
        }

        public void bindOrder(Order order) {
            mOrder = order;
            mDateTextView.setText(mOrder.created.toString());
            mBoxCountTextView.setText("Boxes: 14");
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        }
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderListFragment.OrderHolder> {

        private List<Order> mOrders;

        public OrderAdapter(List<Order> mOrders) {
            this.mOrders = mOrders;
        }

        @Override
        public OrderListFragment.OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_order, parent, false);
            return new OrderListFragment.OrderHolder(view);
        }

        @Override
        public void onBindViewHolder(OrderListFragment.OrderHolder holder, int position) {
            Order order = mOrders.get(position);
            holder.bindOrder(order);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }
    }
}
