package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;

import static net.astechdesign.diningsolutions.orders.OrderActivity.ADD_PRODUCT;

public class OrderFragment extends Fragment
        implements DatePickerFragment.DatePickerListener,
        OrderAddProductFragment.ProductAddListener {

    private Order mOrder;
    private OrderActivity mActivity;
    private OrderItemRepo orderItemRepo;
    private OrderItem selectedOrderItem;
    private View rootView;
    private List<OrderItem> orderItems;
    private OrderItemRecyclerViewAdapter viewAdapter;
    private RecyclerView recyclerView;
    private TextView dateView;
    private TextView totalView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (OrderActivity) getActivity();
        mActivity.setOrderFragment(this);
        mOrder = mActivity.getOrder();
    }

    @Override
    public void onResume() {
        super.onResume();
        mOrder = mActivity.getOrder();
        orderItems.clear();
        orderItems.addAll(mOrder.orderItems);
        dateView.setText(mOrder.created.getDisplayDate());
        totalView.setText(String.format("%.2f", mOrder.total()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderItemRepo = OrderItemRepo.get(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_detail, container);

        rootView.findViewById(R.id.add_product_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = mActivity.getSupportFragmentManager();
                OrderAddProductFragment newProductFragment = new OrderAddProductFragment();
                newProductFragment.setListener(OrderFragment.this);
                newProductFragment.show(fm, ADD_PRODUCT);
            }
        });
        dateView = (TextView) rootView.findViewById(R.id.order_invoice_date);
        dateView.setText(mOrder.created.getDisplayDate());
        totalView = (TextView) rootView.findViewById(R.id.order_detail_total);
        totalView.setText(String.format("%.2f", mOrder.total()));

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(new DeliveryDatePicked(mActivity), (DSDDate) view.getTag());
                dialog.show(mActivity.getSupportFragmentManager(), "date_picker");

                Snackbar.make(view, "Clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        orderItems = mOrder.orderItems;
        viewAdapter = new OrderItemRecyclerViewAdapter(this, orderItems, fragmentManager);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.order_items_list);
        recyclerView.setAdapter(viewAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }

    @Override
    public void onDatePicked(DSDDate date) {
        orderItemRepo.updateDelivery(selectedOrderItem, date);
        update();
    }

    @Override
    public void onAddProductPositiveClick(DialogInterface dialog, OrderItem item, Product product, double price, int quantity, String batch) {
        if (product == null) {
            return;
        }
        if (item != null) {
            OrderItemRepo.get(mActivity).delete(mOrder, item);
        }
        item = new OrderItem(null, product.name, price, quantity, batch, mOrder.created);
        OrderItemRepo.get(mActivity).add(mOrder, item);
        update();
    }

    public void update() {
        List<OrderItem> updatedItems = orderItemRepo.getOrderItems(mOrder);
        orderItems.clear();
        orderItems.addAll(updatedItems);
        viewAdapter.notifyDataSetChanged();
    }

    class DeliveryDatePicked implements DatePickerFragment.DatePickerListener{
        private final OrderActivity activity;

        public DeliveryDatePicked(OrderActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onDatePicked(DSDDate date) {
            for (OrderItem item : mOrder.orderItems) {
                if (date.after(item.deliveryDate)) {
                    OrderItemRepo.get(activity).updateDelivery(item, date);
                }
            }
            OrderRepo.get(activity).updateInvoiceDate(mOrder, date);
        }
    }
}
