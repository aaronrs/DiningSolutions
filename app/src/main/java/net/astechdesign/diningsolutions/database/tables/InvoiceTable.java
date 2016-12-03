package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.provider.BaseColumns._ID;

public class InvoiceTable implements CMSTable {

    public static final String TABLE_NAME = "invoices";
    public static final String INVOICE_NUMBER = "invoice_no";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String ORDER_DATE = "order_date";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_BATCH = "batch";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_PRICE = "price";
    public static final String DELIVERY_DATE = "delivery_date";

    private final AssetManager assetManager;

    public InvoiceTable(Context context) {
        assetManager = context.getAssets();
    }

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INVOICE_NUMBER + " INTEGER, " +
                CUSTOMER_NAME + " INTEGER, " +
                ORDER_DATE + " DATE, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_BATCH + " TEXT, " +
                PRODUCT_QUANTITY + " NUMBER, " +
                PRODUCT_PRICE + " NUMBER, " +
                DELIVERY_DATE + " DATE" +
                ")";
        db.execSQL(query);
        initialise(db);
    }

    private void initialise(SQLiteDatabase db) {
        try {
            InputStream input = assetManager.open("db/testInvoices.txt");
            if (input == null) return;
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = br.readLine()) != null) {
                db.insert(TABLE_NAME, null, getInsertValues(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

     }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues getInsertValues(String line) {
        ContentValues values = new ContentValues();
        String[] invoice = line.split("\\|");
        values.put(INVOICE_NUMBER, Integer.parseInt(invoice[0]));
        values.put(CUSTOMER_NAME, invoice[1]);
        values.put(ORDER_DATE, invoice[2]);
        values.put(PRODUCT_NAME, invoice[3]);
        values.put(PRODUCT_BATCH, invoice[4]);
        values.put(PRODUCT_QUANTITY, Integer.parseInt(invoice[5]));
        values.put(PRODUCT_PRICE, Double.parseDouble(invoice[6]));
        values.put(DELIVERY_DATE, invoice[7]);
        return values;
    }

    public String getInvoicesQuery() {
        return InvoicesQueries.GET_INVOICES;
    }
}
