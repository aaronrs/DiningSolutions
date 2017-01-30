package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

public class OrderItemTable extends CMSTable<OrderItem> {

    public static final String TABLE_NAME = "orderItems";

    public static final String PARENT_ID = "order_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_BATCH = "batch";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_PRICE = "price";
    public static final String DELIVERY_DATE = "delivery_date";

    private static String CREATE_TABLE =
            PARENT_ID + " TEXT, " +
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_BATCH + " TEXT, " +
            PRODUCT_QUANTITY + " NUMBER, " +
            PRODUCT_PRICE + " NUMBER, " +
            DELIVERY_DATE + " DATE" +
            "";

    public OrderItemTable() {
        super(TABLE_NAME, CREATE_TABLE, PARENT_ID);
    }

    public ContentValues getInsertValues(OrderItem item) {
        ContentValues values = new ContentValues();
        values.put(PARENT_ID, item.getDbId());
        values.put(PRODUCT_NAME, item.name);
        values.put(PRODUCT_BATCH, item.batch);
        values.put(PRODUCT_PRICE, item.price);
        values.put(PRODUCT_QUANTITY, item.quantity);
        values.put(DELIVERY_DATE, item.deliveryDate.dbFormat());
        return values;
    }

    public Cursor getOrderItems(SQLiteDatabase db, Order parent) {
        return db.query(TABLE_NAME, null, PARENT_ID + " = ?", new String[]{parent.getDbId()}, null, null, null);
    }

    public void updateDelivery(SQLiteDatabase db, OrderItem orderItem, DSDDate date) {
        ContentValues values = new ContentValues();
        values.put(DELIVERY_DATE, date.dbFormat());
        db.update(TABLE_NAME, values, UUID_ID + " = ?", new String[]{orderItem.getDbId()});
    }
}
