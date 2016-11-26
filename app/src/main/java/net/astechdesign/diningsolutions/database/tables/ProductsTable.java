package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.provider.BaseColumns._ID;

public class ProductsTable implements CMSTable {

    private static final String TABLE_NAME = "products";

    public static final String ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_BARCODE = "barcode";

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " TEXT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_DESCRIPTION + " TEXT, " +
                PRODUCT_PRICE + " NUMBER, " +
                PRODUCT_BARCODE + " TEXT " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static ContentValues getInsertValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(ID, product.id.toString());
        values.put(PRODUCT_NAME, product.name);
        values.put(PRODUCT_DESCRIPTION, product.description);
        values.put(PRODUCT_PRICE, product.price);
        values.put(PRODUCT_BARCODE, product.barcode);
        return values;
    }

    public String getProductsFilterQuery(String filter) {
        String like = " LIKE '" + filter + "%' OR " + ProductsTable.PRODUCT_NAME + "  LIKE '% " + filter + "%'";
        return String.format(PRODUCTS_FILTER_QUERY, like);
    }

    public final String PRODUCTS_FILTER_QUERY =
            "SELECT " + BaseColumns._ID + ", " + PRODUCT_NAME
                    + " FROM " + TABLE_NAME
                    + " WHERE " + PRODUCT_NAME + "%s"
                    + " ORDER BY " + PRODUCT_NAME;

    public List<Product> get(SQLiteDatabase mDatabase) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, PRODUCT_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProductsCursorWrapper productsCursorWrapper = new ProductsCursorWrapper(cursor);
            productList.add(productsCursorWrapper.getProduct());
            cursor.moveToNext();
        }
        return productList;
    }

    public void add(SQLiteDatabase mDatabase, Product product) {
        ContentValues insertValues = getInsertValues(product);
        mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    public void update(SQLiteDatabase mDatabase, Product product) {
        mDatabase.update(TABLE_NAME, getInsertValues(product), ID + " = ?", new String[]{product.id.toString()});
    }
}
