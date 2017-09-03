package net.astechdesign.diningsolutions.app;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.orders.OrderFragment;
import net.astechdesign.diningsolutions.orders.OrderItemRecyclerViewAdapter;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderItemRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderManager implements DatePickerFragment.DatePickerListener {

    private static OrderManager instance = new OrderManager();
    private static Context context;
    private static OrderRepo repo;
    private static CurrentCustomer currentCustomer;

    private List<Order> orders;
    private Order currentOrder;
    private OrderFragment orderFragment;
    private List<OrderItem> orderItems;
    private OrderItemRecyclerViewAdapter viewAdapter;
    private OrderItem currentOrderItem;

    public static void setContext(Context applicationContext) {
        context = applicationContext;
        repo = OrderRepo.get(context);
    }

    public static List<Order> getOrders() {
        return instance.orders;
    }

    public static void save(Order order) {
        OrderRepo.get(instance.context).add(order);
    }

    public static void delete(OrderItem item) {
        OrderItemRepo.get(instance.context).delete(instance.currentOrder, item);
    }

    public static void add(OrderItem item) {
        instance.currentOrder.addItem(item);
        OrderItemRepo.get(instance.context).add(instance.currentOrder, item);
        instance.orderFragment.updateAdapter();
    }

    public static Order getOrder() {
        if (instance.orders == null) {
            instance.orders = repo.get(CurrentCustomer.get());
        }
        if (instance.orders.isEmpty()) {
            instance.currentOrder = Order.create(CurrentCustomer.get());
        } else if (instance.currentOrder == null) {
            instance.currentOrder = instance.orders.get(0);
        }
        return instance.currentOrder;
    }

    public static void newOrder() {
        if (instance.orders.get(0).created.isCurrent()) {
            instance.currentOrder = instance.orders.get(0);
        } else {
            Order order = Order.create(CurrentCustomer.get());
            OrderRepo.get(instance.context).add(order);
        }
        updateAdapter();
    }

    public static void removeItem(OrderItem item) {
        instance.currentOrder.orderItems.remove(item);

    }

    public static void setOrderDetails(OrderFragment fragment) {
        instance.orderFragment = fragment;
    }

    public static Order getCurrentOrder() {
        return instance.currentOrder;
    }

    public static OrderItemRecyclerViewAdapter getViewAdapter(FragmentActivity activity) {
        if (instance.orderItems == null) {
            instance.orderItems = new ArrayList<>();
        }
        instance.viewAdapter = new OrderItemRecyclerViewAdapter(activity, instance.orderItems, activity.getSupportFragmentManager());
        return instance.viewAdapter;
    }

    public static void updateAdapter() {
        instance.orderItems.clear();
        instance.orderItems.addAll(instance.currentOrder.orderItems);
        instance.viewAdapter.notifyDataSetChanged();
    }

    public static void updateInvoiceDate(DSDDate date) {
        OrderRepo.get(instance.context).updateInvoiceDate(getCurrentOrder(), date);
    }

    public static void updateDeliveryDate(OrderItem orderItem, DSDDate date) {
        OrderItemRepo.get(instance.context).updateDelivery(orderItem, date);
    }

    public static void latestOrder() {
        instance.currentOrder = instance.orders.get(0);
        updateAdapter();
    }

    public static void previousOrder() {
        int index = instance.orders.indexOf(instance.currentOrder);
        if (index < instance.orders.size() -1 ) {
            instance.currentOrder = instance.orders.get(index + 1);
        }
        updateAdapter();
    }

    public static void nextOrder() {
        int index = instance.orders.indexOf(instance.currentOrder);
        if (index > 0) {
            instance.currentOrder = instance.orders.get(index - 1);
        }
        updateAdapter();
    }

    @Override
    public void onDatePicked(SourceMode mode, DSDDate newDate) {

    }

    public static void setOrderItem(OrderItem mItem) {
        instance.currentOrderItem = mItem;
    }

    public static DatePickerFragment.DatePickerListener getInstance() {
        return getInstance();
    }

    public static void create() {
        repo.add(Order.create(CurrentCustomer.get()));
    }
}
