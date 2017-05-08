package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.wrappers.OrderCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepo {

    private static OrderRepo repo;
    private static OrderItemRepo itemRepo;
    private final OrderTable orderTable;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context.getApplicationContext());
            itemRepo = new OrderItemRepo(context.getApplicationContext());
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        orderTable = DBHelper.getOrderTable();
    }

    public List<Order> get(Customer customer) {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = orderTable.getOrders(mDatabase, customer);
        while (cursor.moveToNext()) {
            Order order = new OrderCursorWrapper(cursor).getOrder();
            orders.add(order);
        }
        cursor.close();
        if (orders.isEmpty()) {
            UUID id = add(Order.create(customer));
            cursor = orderTable.get(mDatabase, id);
            Order order = new OrderCursorWrapper(cursor).getOrder();
            orders.add(order);
            cursor.close();
        } else {
            for (Order order : orders) {
                List<OrderItem> orderItems = OrderItemRepo.get(mContext).getOrderItems(order);
                order.orderItems = orderItems;
            }
        }
        return orders;
    }

    public UUID add(Order order) {
        UUID id = orderTable.addOrUpdate(mDatabase, order);
        for (OrderItem item : order.orderItems) {
            itemRepo.add(order, item);
        }
        return id;
    }

    public List<Order> get() {
        Cursor cursor = orderTable.get(mDatabase);
        List<Order> orders = new ArrayList<>();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            Order order = new OrderCursorWrapper(cursor).getOrder();
            List<OrderItem> orderItems = itemRepo.getOrderItems(order);
            order.orderItems.addAll(orderItems);
            orders.add(order);
            cursor.moveToNext();
        }
        return orders;
    }

    public void updateInvoiceDate(Order order, DSDDate date) {
        update(order.getId(), order.customerId, date, order.invoiceNumber);
    }

    public void updateInvoiceNumber(Order order, String invoiceNumber) {
        update(order.getId(), order.customerId, order.created, invoiceNumber);
    }

    private void update(UUID id, UUID customerId, DSDDate date, String invoiceNumber) {
        orderTable.addOrUpdate(mDatabase, new Order(id, customerId, date, invoiceNumber));
    }
}
