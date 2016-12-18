package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Model;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.provider.BaseColumns._ID;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String CUSTOMER_ID = "customer_name";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ID + " TEXT, " +
            CUSTOMER_ID + " INTEGER, " +
            ORDER_DATE + " DATE, " +
            INVOICE_NO + " TEXT " +
            ")";

    private final OrderItemTable orderItemTable;

    public OrderTable(OrderItemTable orderItemTable) {
        super(TABLE_NAME, CREATE_TABLE);
        this.orderItemTable = orderItemTable;
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.CUSTOMER_ID, order.customerId.toString());
        values.put(OrderTable.ID, order.id.toString());
        values.put(OrderTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrderTable.ORDER_DATE, order.created.toString());
        return values;
    }

    public List<Order> getOrders(SQLiteDatabase mDatabase, Customer customer) {
        List<Order> orders = new ArrayList<>();
        Cursor orderCursor = mDatabase.rawQuery(DbQuery.ORDER_LIST, new String[]{customer.getId().toString()});
        orderCursor.moveToFirst();
        while (!orderCursor.isAfterLast()) {
            OrderCursorWrapper cursorWrapper = new OrderCursorWrapper(orderCursor);
            orders.add(cursorWrapper.getOrder(null));
        }
        return orders;
    }

}
