package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Model;
import net.astechdesign.diningsolutions.model.Order;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String CUSTOMER_ID = "customer_id";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";

    private static String CREATE_TABLE =
            CUSTOMER_ID + " TEXT, " +
            ORDER_DATE + " DATE, " +
            INVOICE_NO + " TEXT" +
            "";

    public OrderTable() {
        super(TABLE_NAME, CREATE_TABLE, CUSTOMER_ID);
    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(INVOICE_NO, order.invoiceNumber);
        values.put(ORDER_DATE, order.created.dbFormat());
        return values;
    }

    public Cursor getOrders(SQLiteDatabase db, Model parent) {
        String orderBy = ORDER_DATE + " DESC, " + INVOICE_NO + " DESC";
        return db.query(TABLE_NAME, null, CUSTOMER_ID + " = ?", new String[]{parent.getDbId()}, null, null, orderBy);
    }
}
