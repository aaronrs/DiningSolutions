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
        mDatabase = new DBHelper(mContext).getWritableDatabase();
        orderTable = DBHelper.getOrderTable();
    }

    public List<Order> getOrders(Customer customer) {
        Cursor cursor = orderTable.getOrders(mDatabase, customer);
        List<Order> orders = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            Order order = new OrderCursorWrapper(cursor).getOrder();
            List<OrderItem> orderItems = itemRepo.getOrderItems(order);
            order.orderItems.addAll(orderItems);
            orders.add(order);
            cursor.moveToNext();
        }
        return orders;
    }

    public void create(Customer mCustomer) {
        int invoiceNumber = orderTable.newInvoiceNumber(mDatabase);
        Order order = new Order(null, new DSDDate(), Integer.toString(invoiceNumber));
        orderTable.addOrUpdate(mDatabase, mCustomer, order);
    }

    public void add(Customer customer, Order order) {
        orderTable.addOrUpdate(mDatabase, customer, order);
        for (OrderItem item : order.orderItems) {
            itemRepo.add(mDatabase, order, item);
        }
    }
}
