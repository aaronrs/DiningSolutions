package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductTable extends CMSTable<Product> {

    private static final String TABLE_NAME = "products";

    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_BARCODE = "barcode";
    public static final String PRODUCT_DELETED = "deleted";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_DESCRIPTION + " TEXT, " +
            PRODUCT_PRICE + " NUMBER, " +
            PRODUCT_BARCODE + " TEXT, " +
            PRODUCT_DELETED + " INTEGER " +
            ")";

    public ProductTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected ContentValues getInsertValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.name);
        values.put(PRODUCT_DESCRIPTION, product.description);
        values.put(PRODUCT_PRICE, product.price);
        values.put(PRODUCT_BARCODE, product.barcode);
        values.put(PRODUCT_DELETED, product.isDeleted() ? 1 : 0);
        return values;
    }

    public List<Product> get(SQLiteDatabase mDatabase) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, PRODUCT_DELETED + " = 0", null, null, null, PRODUCT_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
            productList.add(productCursorWrapper.getProduct());
            cursor.moveToNext();
        }
        return productList;
    }

    public Product get(SQLiteDatabase mDatabase, int id) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, _ID + " = ? " + PRODUCT_DELETED + " = 0", id(id), null, null, PRODUCT_NAME);
        cursor.moveToFirst();
        ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
        return productCursorWrapper.getProduct();
    }
}
