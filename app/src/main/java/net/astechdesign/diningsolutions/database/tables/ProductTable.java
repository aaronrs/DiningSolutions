package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Product;

import java.util.UUID;

public class ProductTable extends CMSTable<Product> {

    public static final String TABLE_NAME = "products";

    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_BARCODE = "barcode";
    public static final String PRODUCT_DELETED = "deleted";

    private static String CREATE_TABLE =
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_DESCRIPTION + " TEXT, " +
            PRODUCT_PRICE + " NUMBER, " +
            PRODUCT_BARCODE + " TEXT, " +
            PRODUCT_DELETED + " INTEGER " +
            "";

    public ProductTable() {
        super(TABLE_NAME, CREATE_TABLE);
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

    @Override
    protected String getParentIdColumn() {
        return null;
    }

    public Cursor get(SQLiteDatabase db) {
        return db.query(TABLE_NAME, null, PRODUCT_DELETED + " = 0", null, null, null, PRODUCT_NAME);
    }

    public Cursor get(SQLiteDatabase db, int id) {
        return db.query(TABLE_NAME, null, _ID + " = ? " + PRODUCT_DELETED + " = 0", selectionArgs(id), null, null, PRODUCT_NAME);
    }

    public Cursor get(SQLiteDatabase mDatabase, String filter) {
        return mDatabase.query(TABLE_NAME, null, PRODUCT_NAME + " LIKE ?", new String[]{filter}, null, null, PRODUCT_NAME);
    }
}
