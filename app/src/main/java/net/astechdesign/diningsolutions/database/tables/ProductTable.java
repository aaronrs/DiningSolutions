package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Product;

public class ProductTable extends CMSTable<Product> {

    public static final String TABLE_NAME = "products";

    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_BARCODE = "barcode";

    private static String CREATE_TABLE =
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_DESCRIPTION + " TEXT, " +
            PRODUCT_PRICE + " NUMBER, " +
            PRODUCT_BARCODE + " TEXT " +
            "";

    public ProductTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected void insertDbValues(ContentValues values, Product product) {
        values.put(PRODUCT_NAME, product.name);
        values.put(PRODUCT_DESCRIPTION, product.description);
        values.put(PRODUCT_PRICE, product.price);
        values.put(PRODUCT_BARCODE, product.barcode);
    }

    public Cursor get(SQLiteDatabase mDatabase, String orderby, String filter) {
        return mDatabase.query(TABLE_NAME, null, PRODUCT_NAME + " LIKE ?", new String[]{filter}, null, null, orderby);
    }

    public Cursor getProduct(SQLiteDatabase mDatabase, String name) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, PRODUCT_NAME + " = ?", new String[]{name}, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
