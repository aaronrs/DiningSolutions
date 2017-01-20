package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.UUID;

public class OrderItemTable extends CMSTable<OrderItem> {

    public static final String TABLE_NAME = "orderItems";

    public static final String ORDER_ID = "order_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_BATCH = "batch";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_PRICE = "price";
    public static final String DELIVERY_DATE = "delivery_date";

    private static String CREATE_TABLE =
            ORDER_ID + " TEXT, " +
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_BATCH + " TEXT, " +
            PRODUCT_QUANTITY + " NUMBER, " +
            PRODUCT_PRICE + " NUMBER, " +
            DELIVERY_DATE + " DATE" +
            "";

    public OrderItemTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    public ContentValues getInsertValues(OrderItem item) {
        ContentValues values = new ContentValues();
        values.put(ORDER_ID, item.getDbId());
        values.put(PRODUCT_NAME, item.name);
        values.put(PRODUCT_BATCH, item.batch);
        values.put(PRODUCT_PRICE, item.price);
        values.put(PRODUCT_QUANTITY, item.quantity);
        values.put(DELIVERY_DATE, item.deliveryDate.dbFormat());
        return values;
    }

    @Override
    protected String getParentIdColumn() {
        return ORDER_ID;
    }

    public Cursor getOrderItems(SQLiteDatabase db, Order order) {
        return db.rawQuery(ORDER_ITEMS, new String[]{order.getDbId()});
    }

    public String ORDER_ITEMS = "SELECT " +
            UUID_ID + ", " +
            ORDER_ID + ", " +
            PRODUCT_NAME + ", " +
            PRODUCT_BATCH + ", " +
            PRODUCT_QUANTITY + ", " +
            PRODUCT_PRICE + ", " +
            DELIVERY_DATE + " " +
            " FROM " + OrderItemTable.TABLE_NAME +
            " WHERE " + OrderItemTable.ORDER_ID + " = ? " +
            "";

}
