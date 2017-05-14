package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.ArrayList;
import java.util.List;

import static net.astechdesign.diningsolutions.orders.OrderActivity.ADD_PRODUCT;

public class OrderFragment extends Fragment
        implements DatePickerFragment.DatePickerListener,
        OrderAddProductFragment.ProductAddListener {

    private List<Order> mOrders;
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
    private Customer mCustomer;
    private int mIndex;
    private OrderRepo orderRepo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (OrderActivity) getActivity();
        mActivity.setOrderFragment(this);
        mCustomer = mActivity.getCustomer();
        mOrders = mActivity.getOrders();
        mOrder = mOrders.get(mIndex);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderRepo = OrderRepo.get(getContext());
        orderItemRepo = OrderItemRepo.get(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_detail, container);

        initialiseFields(rootView);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        orderItems = new ArrayList<>();
        viewAdapter = new OrderItemRecyclerViewAdapter(this, orderItems, fragmentManager);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.order_items_list);
        recyclerView.setAdapter(viewAdapter);

        updateAdapter();
        return rootView;
    }

    public void updateAdapter() {
        updateAdapter(0);
    }

    public void updateAdapter(int index) {
        List<Order> orders = mActivity.getOrders();
        index = index < 0 ? 0 : index >= orders.size() ? mIndex : index;
        mIndex = index;
        mOrder = orders.get(index);
        orderItems.clear();
        orderItems.addAll(mOrder.orderItems);
        dateView.setText(mOrder.created.getDisplayDate());
        viewAdapter.notifyDataSetChanged();
        totalView.setText(String.format("%.2f", mOrder.total()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mOrders = mActivity.getOrders();
        mOrder = mOrders.get(0);
        updateAdapter();
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
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
        updateAdapter();
    }

    @Override
    public void onDatePicked(String mode, DSDDate date) {
        if ("INVOICE".equals(mode)) {

        } else
        if ("ITEM".equals(mode)) {
            orderItemRepo.updateDelivery(selectedOrderItem, date);
        }
        updateAdapter();
    }

    private void initialiseFields(View rootView) {
        rootView.findViewById(R.id.btn_new_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mOrders.get(0).created.isCurrent()) {
                    mActivity.newOrder();
                }
                updateAdapter();
            }
        });
        rootView.findViewById(R.id.add_product_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = mActivity.getSupportFragmentManager();
                OrderAddProductFragment newProductFragment = new OrderAddProductFragment();
                newProductFragment.setListener(OrderFragment.this);
                newProductFragment.show(fm, ADD_PRODUCT);
            }
        });
        rootView.findViewById(R.id.btn_latest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdapter();
            }
        });
        rootView.findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdapter(mIndex + 1);
            }
        });
        rootView.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAdapter(mIndex - 1);
            }
        });

        dateView = (TextView) rootView.findViewById(R.id.order_invoice_date);
        dateView.setText(mOrder.created.getDisplayDate());
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = DatePickerFragment.newInstance("INVOICE", OrderFragment.this, (DSDDate) view.getTag());
                dialog.show(mActivity.getSupportFragmentManager(), "date_picker");
            }
        });

        totalView = (TextView) rootView.findViewById(R.id.order_detail_total);
        totalView.setText(String.format("%.2f", mOrder.total()));
    }

    public void deleteOrderItem(final OrderItem item) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Order Item")
                .setMessage("Delete " + item.name + " from order?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        OrderItemRepo.get(getActivity()).delete(mOrder, item);
                        updateAdapter();
                    }}).show();
    }
}
