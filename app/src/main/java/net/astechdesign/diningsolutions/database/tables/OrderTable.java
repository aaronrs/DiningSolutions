package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static android.provider.BaseColumns._ID;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String ID = "id";

    public static final String CUSTOMER_ID = "customer_name";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";
    private final OrderItemTable orderItemTable;

    public OrderTable() {
        orderItemTable = new OrderItemTable();
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

    public ContentValues getInsertValues(UUID id, Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.ID, order.id.toString());
        values.put(OrderTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrderTable.CUSTOMER_ID, order.customerId.toString());
        values.put(OrderTable.ORDER_DATE, order.created.toString());
        return values;
    }

    @Override
    protected String getTableName() {
        return null;
    }

    public void addOrder(SQLiteDatabase db, Order order) {
        db.beginTransaction();
        db.insert(TABLE_NAME, null, getInsertValues(order));
        for (OrderItem item : order.orderItems) {
//            orderItemTable.addOrderItem(db, order.id, item);
        }
    }

    public List<Order> getOrders(UUID customerId) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(),
                customerId,
                new DSDDate(),
                "123",
                Collections.<OrderItem>emptyList()));
        orders.add(new Order(UUID.randomUUID(),
                customerId,
                new DSDDate(),
                "123",
                Collections.<OrderItem>emptyList()));
        return orders;
    }
}
