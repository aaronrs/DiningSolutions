package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.wrappers.OrderItemCursorWrapper;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderItemRepo {

    private static OrderItemRepo repo;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final OrderItemTable orderItemTable;

    public static OrderItemRepo get(Context context) {
        if (repo == null) {
            repo = new OrderItemRepo(context.getApplicationContext());
        }
        return repo;
    }

    public OrderItemRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        orderItemTable = DBHelper.getOrderItemTable();
    }

    public List<OrderItem> getOrderItems(Order order) {
        Cursor cursor = orderItemTable.getOrderItems(mDatabase, order);
        List<OrderItem> orderItems = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderItemCursorWrapper cursorWrapper = new OrderItemCursorWrapper(cursor);
            orderItems.add(cursorWrapper.getOrderItem());
            cursor.moveToNext();
        }
        cursor.close();
        return orderItems;
    }

    public void add(Order order, OrderItem item) {
        orderItemTable.addOrUpdate(mDatabase, order, item);
    }

    public void delete(Order order, OrderItem item) {
        orderItemTable.delete(mDatabase, order.getDbId(), item);
    }

    public void updateDelivery(OrderItem orderItem, DSDDate date) {
        orderItemTable.updateDelivery(mDatabase, orderItem, date);
    }
}
