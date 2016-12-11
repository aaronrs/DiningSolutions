package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.style.TtsSpan;

import net.astechdesign.diningsolutions.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductTable extends CMSTable<Product> {

    private static final String TABLE_NAME = "products";

    public static final String ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_BARCODE = "barcode";
    public static final String PRODUCT_DELETED = "deleted";

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " TEXT, " +
                PRODUCT_NAME + " TEXT, " +
                PRODUCT_DESCRIPTION + " TEXT, " +
                PRODUCT_PRICE + " NUMBER, " +
                PRODUCT_BARCODE + " TEXT, " +
                PRODUCT_DELETED + " INTEGER " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected ContentValues getInsertValues(UUID id, Product product) {
        ContentValues values = new ContentValues();
        values.put(ID, id.toString());
        values.put(PRODUCT_NAME, product.name);
        values.put(PRODUCT_DESCRIPTION, product.description);
        values.put(PRODUCT_PRICE, product.price);
        values.put(PRODUCT_BARCODE, product.barcode);
        values.put(PRODUCT_DELETED, product.isDeleted() ? 1 : 0);
        return values;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
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

}
