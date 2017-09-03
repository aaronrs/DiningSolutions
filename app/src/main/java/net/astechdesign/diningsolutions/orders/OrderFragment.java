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
import net.astechdesign.diningsolutions.app.OrderManager;
import net.astechdesign.diningsolutions.app.SourceMode;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;

import static net.astechdesign.diningsolutions.orders.OrderActivity.ADD_PRODUCT;

public class OrderFragment extends Fragment
        implements DatePickerFragment.DatePickerListener {

    private OrderItem selectedOrderItem;
    private View rootView;
    private OrderItemRecyclerViewAdapter viewAdapter;
    private RecyclerView recyclerView;
    private TextView dateView;
    private TextView totalView;
    private OrderActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        OrderManager.setOrderDetails(this);
        mActivity = (OrderActivity) getActivity();
        mActivity.setOrderFragment(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_detail, container);

        initialiseFields(rootView);

        viewAdapter = OrderManager.getViewAdapter(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.order_items_list);
        recyclerView.setAdapter(viewAdapter);

//        updateAdapter();
        return rootView;
    }

    public void updateAdapter() {
        updateAdapter(0);
    }

    public void updateAdapter(int index) {
        OrderManager.updateAdapter();
        Order currentOrder = OrderManager.getCurrentOrder();
        dateView.setText(currentOrder.created.getDisplayDate());
        totalView.setText(String.format("%.2f", currentOrder.total()));
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateAdapter();
    }

    public void setSelectedOrderItem(OrderItem selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }

//    @Override
//    public void onAddProductPositiveClick(DialogInterface dialog, OrderItem item, Product product, double price, int quantity, String batch) {
//        if (item != null) {
//            OrderItemRepo.get(mActivity).delete(mOrder, item);
//        }
//        item = new OrderItem(null, product.name, price, quantity, batch, mOrder.created);
//        OrderItemRepo.get(mActivity).add(mOrder, item);
//        updateAdapter();
//    }

    @Override
    public void onDatePicked(SourceMode mode, DSDDate date) {
        Order order = OrderManager.getOrder();
        switch (mode) {
            case INVOICE :
                OrderManager.updateInvoiceDate(date);
                break;
            case ITEM:
                OrderManager.updateDeliveryDate(selectedOrderItem, date);
                break;
        }
        updateAdapter();
    }

    private void initialiseFields(View rootView) {
        final List<Order> orders = OrderManager.getOrders();
        rootView.findViewById(R.id.btn_new_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orders.get(0).created.isCurrent()) {
                    OrderManager.newOrder();
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
                OrderManager.latestOrder();
            }
        });
        rootView.findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderManager.previousOrder();
            }
        });
        rootView.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderManager.nextOrder();
            }
        });

        Order order = OrderManager.getOrder();
        dateView = (TextView) rootView.findViewById(R.id.order_invoice_date);
        dateView.setText(order.created.getDisplayDate());
        final OrderFragment fragment = this;
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(SourceMode.INVOICE, fragment, (DSDDate) view.getTag());
                dialog.show(mActivity.getSupportFragmentManager(), "date_picker");
            }
        });

        totalView = (TextView) rootView.findViewById(R.id.order_detail_total);
        totalView.setText(String.format("%.2f", order.total()));
    }

    public void deleteOrderItem(final OrderItem item) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Order Item")
                .setMessage("Delete " + item.name + " from order?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        OrderManager.removeItem(item);
                        updateAdapter();
                    }}).show();
    }
}
