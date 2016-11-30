package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.provider.BaseColumns._ID;

public class OrdersTable implements CMSTable {

    public static final String TABLE_NAME = "orders";

    public static final String ID = "id";

    public static final String CUSTOMER_ID = "customer_name";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";
    private final OrderItemsTable orderItemsTable;

    public OrdersTable() {
        orderItemsTable = new OrderItemsTable();
    }

    @Override
    public void create(SQLiteDatabase db) {
        String orderTable = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID + " TEXT, " +
                CUSTOMER_ID + " INTEGER, " +
                ORDER_DATE + " DATE, " +
                INVOICE_NO + " TEXT " +
                ")";
        try {
            db.execSQL(orderTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, order.id.toString());
        values.put(OrdersTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrdersTable.CUSTOMER_ID, order.customerId.toString());
        values.put(OrdersTable.ORDER_DATE, order.created.toString());
        return values;
    }

    public void addOrder(SQLiteDatabase db, Order order) {
        db.beginTransaction();
        db.insert(TABLE_NAME, null, getInsertValues(order));
        for (OrderItem item : order.orderItems) {
            orderItemsTable.addOrderItem(db, order.id, item);
        }
    }

    public Order getOrder(UUID id) {
        return null;
    }
}
