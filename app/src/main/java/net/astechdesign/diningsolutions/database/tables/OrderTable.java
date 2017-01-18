package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderAssets;

import java.util.ArrayList;
import java.util.List;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String CUSTOMER_ID = "customer_id";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";

    private static String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CUSTOMER_ID + " INTEGER, " +
            ORDER_DATE + " DATE, " +
            INVOICE_NO + " TEXT " +
            ")";

    public OrderTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    public void upgrade(int oldVersion, int newVersion) {
    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.CUSTOMER_ID, order.customerId);
        values.put(OrderTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrderTable.ORDER_DATE, order.created.dbFormat());
        return values;
    }

    public List<Order> getOrders(SQLiteDatabase db, Customer customer) {
        List<Order> orders = new ArrayList<>();
        Cursor orderCursor = db.rawQuery(DbQuery.CUSTOMER_ORDERS, new String[]{Integer.toString(customer.getId())});
        orderCursor.moveToFirst();
        while (!orderCursor.isAfterLast()) {
            OrderCursorWrapper cursorWrapper = new OrderCursorWrapper(orderCursor);
            orders.add(cursorWrapper.getOrder());
            orderCursor.moveToNext();
        }
        return orders;
    }

    public int newInvoiceNumber(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{INVOICE_NO}, null, null, null, null, INVOICE_NO + " DESC", "1");
        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0)) + 1;
    }
}
