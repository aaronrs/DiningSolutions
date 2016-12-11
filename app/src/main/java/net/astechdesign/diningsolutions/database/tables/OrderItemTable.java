package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class OrderItemTable extends CMSTable<OrderItem> {

    public static final String TABLE_NAME = "orderItems";

    public static final String ID = "id";
    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_BATCH = "batch";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_PRICE = "price";
    public static final String DELIVERY_DATE = "delivery_date";

    @Override
    public void create(SQLiteDatabase db) {
        String orderItemTable = "CREATE TABLE " + TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID + " TEXT, " +
                ORDER_ID + " TEXT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_BATCH + " TEXT, " +
                PRODUCT_QUANTITY + " NUMBER, " +
                PRODUCT_PRICE + " NUMBER, " +
                DELIVERY_DATE + " DATE" +
                ")";
        try {
            db.execSQL(orderItemTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    protected ContentValues getInsertValues(UUID id, OrderItem orderItem) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);

        ContentValues values = new ContentValues();
        values.put(ORDER_ID, id.toString());
        values.put(PRODUCT_NAME, orderItem.name);
        values.put(PRODUCT_BATCH, orderItem.batch);
        values.put(PRODUCT_QUANTITY, orderItem.price);
        values.put(PRODUCT_PRICE, orderItem.quantity);
        return values;
    }
}
