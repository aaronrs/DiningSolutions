package net.astechdesign.diningsolutions.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrdersTable;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderRepo {

    public static OrderRepo repo;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private OrdersTable ordersTable;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
        ordersTable = new OrdersTable();
    }

    public List<Order> getmOrders() {
//        mDatabase.rawQueryWithFactory();
        List<Order> mOrders = new ArrayList<>();
        return mOrders;
    }

    public void addOrder(Order order) {
        for (OrderItem item : order.orderItems) {
            ContentValues values = getOrderContentValues(order, item);
            mDatabase.insert(ordersTable.getTableName(), null, values);
        }
    }

    private Cursor queryOrders(String whereClause, String[] whereArgs) {
        return mDatabase.query(ordersTable.getTableName(), null, whereClause, whereArgs, null, null, null);
    }

    private Cursor queryOrderItems(UUID orderId) {
        return mDatabase.query(ordersTable.getTableName(), null, OrdersTable.ORDER_ID + "= ?" , new String[]{orderId.toString()}, null, null, null);
    }

    private ContentValues getOrderContentValues(Order order, OrderItem item) {
        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, order.id.toString());
        values.put(OrdersTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrdersTable.CUSTOMER_ID, order.customerId.toString());
        values.put(OrdersTable.ORDER_DATE, order.created.toString());
        values.put(OrdersTable.DELIVERY_DATE, order.created.toString());
        values.put(OrdersTable.PRODUCT_NAME, item.name);
        values.put(OrdersTable.PRODUCT_BATCH, item.batch);
        values.put(OrdersTable.PRODUCT_PRICE, item.price);
        values.put(OrdersTable.PRODUCT_QUANTITY, item.quantity);
        return values;
    }

    public Order getOrder(UUID id) {
        Cursor cursor = mDatabase.rawQuery("", new String[]{});
        return null;
    }

    private List<OrderItem> getOrderItems(UUID id) {
        Cursor cursor = mDatabase.rawQuery("", new String[]{});
        return null;
    }

    public static Order get(FragmentActivity activity, UUID serializable) {
        return get(activity).getOrder(serializable);
    }

    public static List<OrderItem> getItems(UUID id) {
        return repo.getOrderItems(id);
    }
}
