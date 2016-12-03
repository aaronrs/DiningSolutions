package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.UUID;

import static android.provider.BaseColumns._ID;

public class OrderItemTable implements CMSTable {

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
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    public ContentValues getInsertValues(UUID orderId, UUID id, String name, String batch, int quantity, double price, DSDDate deliveryDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);

        ContentValues values = new ContentValues();
        values.put(ORDER_ID, id.toString());
        values.put(PRODUCT_NAME, name);
        values.put(PRODUCT_BATCH, batch);
        values.put(PRODUCT_QUANTITY, quantity);
        values.put(PRODUCT_PRICE, price);
        values.put(DELIVERY_DATE, dateFormat.format(deliveryDate));
        return values;
    }

    public void addOrderItem(SQLiteDatabase db, UUID orderId, OrderItem item) {
        db.insert(TABLE_NAME, null, getInsertValues(orderId, item.id, item.name, item.batch, item.quantity, item.price, item.deliveryDate));
    }
}
