package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.provider.BaseColumns._ID;

public class ProductsTable implements CMSTable {

private static final String TABLE_NAME = "products";

public static final String PRODUCT_NAME = "name";
public static final String PRODUCT_PRICE = "price";

public static final String PRODUCT_DESCRIPTION = "description";
private final AssetManager assets;

public ProductsTable(Context context) {
    assets = context.getAssets();
}

@Override
public String getTableName() {
    return TABLE_NAME;
}

@Override
public void create(SQLiteDatabase db) {
    String query = "CREATE TABLE " + getTableName() + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PRODUCT_NAME + " TEXT, " +
            PRODUCT_DESCRIPTION + " TEXT, " +
            PRODUCT_PRICE + " NUMBER " +
            ")";
    db.execSQL(query);
    initialise(db);
}

@Override
public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

}

private void initialise(SQLiteDatabase db) {
    InputStream input;
    try {
        input = assets.open("db/products.sql");
        if (input == null) return;
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line;
        String previous = "";
        while ((line = br.readLine()) != null) {
            String[] productInfo = line.split("\\|");
            if (! previous.equals(productInfo[2])) {
                db.insert(getTableName(), null, getInsertValues(productInfo[2], "", 0));
                previous = productInfo[2];
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static ContentValues getInsertValues(String name, String description, float price) {
    ContentValues values = new ContentValues();
    values.put(PRODUCT_NAME, name);
    values.put(PRODUCT_DESCRIPTION, description);
    values.put(PRODUCT_PRICE, price);
    return values;
}

public String getProductsFilterQuery(String filter) {
    String like = " LIKE '" + filter + "%' OR "+ ProductsTable.PRODUCT_NAME + "  LIKE '% " + filter + "%'";
    return String.format(PRODUCTS_FILTER_QUERY, like);
}

public final String PRODUCTS_FILTER_QUERY =
        "SELECT " + BaseColumns._ID + ", " + PRODUCT_NAME
                + " FROM " + getTableName()
                + " WHERE " + PRODUCT_NAME + "%s"
                + " ORDER BY " + PRODUCT_NAME;

}
