package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Model;
import net.astechdesign.diningsolutions.model.Order;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";

    private static String CREATE_TABLE =
            ORDER_DATE + " INTEGER, " +
            INVOICE_NO + " TEXT" +
            "";

    public OrderTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(INVOICE_NO, order.invoiceNumber);
        values.put(ORDER_DATE, order.created.dbFormat());
        return values;
    }

    public Cursor getOrders(SQLiteDatabase db, Model parent) {
        String orderBy = ORDER_DATE + " DESC, " + INVOICE_NO + " DESC";
        return db.query(TABLE_NAME, null, PARENT_ID + " = ?", new String[]{parent.getDbId()}, null, null, orderBy);
    }

    public Cursor get(SQLiteDatabase db) {
        String orderBy = ORDER_DATE + " DESC, " + INVOICE_NO + " DESC";
        return db.query(TABLE_NAME, null, null, null, null, null, orderBy);
    }

    public Cursor getOrderSummary(SQLiteDatabase db) {
        String query = "" +
                "SELECT strftime('%Y-%m-%d', " + ORDER_DATE + " / 1000, 'unixepoch')" + " as " + ORDER_DATE + ", " +
                OrderItemTable.PRODUCT_NAME + ", " +
                "SUM(" + OrderItemTable.PRODUCT_QUANTITY  + ") as quantity " +
                "FROM " + TABLE_NAME  + " JOIN " +
                OrderItemTable.TABLE_NAME + " " +
                "WHERE " + TABLE_NAME + "." + UUID_ID  + " = " +
                OrderItemTable.TABLE_NAME + "." + PARENT_ID + " " +
                "GROUP BY strftime('%Y-%m-%d', " + ORDER_DATE + " / 1000, 'unixepoch')" + ", " +
                OrderItemTable.PRODUCT_NAME;
        return db.rawQuery(query, null);
    }
}
