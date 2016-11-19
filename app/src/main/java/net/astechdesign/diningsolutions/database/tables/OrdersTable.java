package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.BaseColumns._ID;

public class OrdersTable implements CMSTable {

    public static final String CUSTOMER_NAME = "customer_name";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_BATCH = "batch";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_PRICE = "price";
    public static final String DELIVERY_DATE = "delivery_date";

    @Override
    public String getTableName() {
        return "orders";
    }

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + getTableName() + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMER_NAME + " INTEGER, " +
                ORDER_DATE + " DATE, " +
                INVOICE_NO + " TEXT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_BATCH + " TEXT, " +
                PRODUCT_QUANTITY + " NUMBER, " +
                PRODUCT_PRICE + " NUMBER, " +
                DELIVERY_DATE + " DATE" +
                ")";
        try {
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialise(db);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initialise(SQLiteDatabase db) {
        db.insert(getTableName(), null, getInsertValues("Tom Thumb", new Date(),"inv01", "Product X", "batch", 1, 10f, new Date()));
        db.insert(getTableName(), null,  getInsertValues("Tom", new Date(), "inv01", "prod01", "batch01", 1, 20.0f, new Date()));
    }

    public ContentValues getInsertValues(String customer, Date orderDate, String invoice, String product, String batch, int quantity, float price, Date deliveryDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);

        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, customer);
        values.put(ORDER_DATE, dateFormat.format(orderDate));
        values.put(INVOICE_NO, invoice);
        values.put(PRODUCT_NAME, product);
        values.put(PRODUCT_BATCH, batch);
        values.put(PRODUCT_QUANTITY, quantity);
        values.put(PRODUCT_PRICE, price);
        values.put(DELIVERY_DATE, dateFormat.format(deliveryDate));
        return values;
    }

    public void createOrder(SQLiteDatabase db, String customer, Date orderDate, String invoice, String product, String batch, int quantity, float price, Date deliveryDate) {
        db.insert(getTableName(), null, getInsertValues(customer, orderDate, invoice, product, batch, quantity, price, deliveryDate));
    }
}
