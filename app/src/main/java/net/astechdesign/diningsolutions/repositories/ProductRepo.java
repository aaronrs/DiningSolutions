package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.wrappers.ProductCursorWrapper;
import net.astechdesign.diningsolutions.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepo {

    private static ProductRepo repo;

    private final SQLiteDatabase mDatabase;
    private final ProductTable productTable;
    private final Context mContext;

    public static ProductRepo get(Context context) {
        if (repo == null) {
            repo = new ProductRepo(context.getApplicationContext());
        }
        return repo;
    }

    private ProductRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        productTable = DBHelper.getProductTable();
    }

    public List<Product> get() {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = productTable.get(mDatabase, ProductTable.PRODUCT_NAME);
        while (!cursor.isAfterLast()) {
            ProductCursorWrapper productCursorWrapper = new ProductCursorWrapper(cursor);
            productList.add(productCursorWrapper.getProduct());
            cursor.moveToNext();
        }
        return productList;
    }

    public void addOrUpdate(Product product) {
        productTable.addOrUpdate(mDatabase, product);
    }

    public void delete(Product product) {
        productTable.delete(mDatabase, product);
    }

    public Product get(String name) {
        Cursor cursor = productTable.getProduct(mDatabase, name);
        return new ProductCursorWrapper(cursor).getProduct();
    }
}
