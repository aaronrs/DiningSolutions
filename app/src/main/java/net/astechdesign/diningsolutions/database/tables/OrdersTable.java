package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;

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

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void create(SQLiteDatabase db) {
        String orderTable = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID + " TEXT, " +
                CUSTOMER_ID + " INTEGER, " +
                ORDER_DATE + " DATE, " +
                INVOICE_NO + " TEXT, " +
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

    public ContentValues getInsertValues(UUID id, UUID customerId, DSDDate orderDate, String invoice) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);

        ContentValues values = new ContentValues();
        values.put(ID, id.toString());
        values.put(CUSTOMER_ID, customerId.toString());
        values.put(ORDER_DATE, dateFormat.format(orderDate));
        values.put(INVOICE_NO, invoice);
        return values;
    }

    public void addOrder(SQLiteDatabase db, Order order) {
        db.beginTransaction();
        db.insert(getTableName(), null, getInsertValues(order.id, order.customerId, order.created, order.invoiceNumber));
        new OrderItemsTable().addOrderItems(db, order);
    }
}
