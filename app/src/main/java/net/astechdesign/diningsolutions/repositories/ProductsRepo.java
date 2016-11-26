package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.ProductsTable;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.products.ProductActivity;

import java.util.List;

public class ProductsRepo {

    private static ProductsRepo instance;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final ProductsTable productsTable;

    private ProductsRepo(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
        productsTable = new ProductsTable();
    }

    public static Product get(String string) {
        return null;
    }

    public static List<Product> get(Context context) {
        if (instance == null) {
            instance = new ProductsRepo(context);
        }
        return instance.get();
    }

    public static void update(Context context, Product product) {
        if (instance == null) {
            instance = new ProductsRepo(context);
        }
        instance.update(product);
    }

    private List<Product> get() {
        List<Product> productList = productsTable.get(mDatabase);
        if (productList.isEmpty()) {
            initDb(mContext);
            productList = productsTable.get(mDatabase);
        }
        return productList;
    }

    private void update(Product product) {
        productsTable.update(mDatabase, product);
    }

    private void initDb(Context context) {
        List<Product> productList = ProductAssets.getProducts(context);
        for (Product product : productList) {
            productsTable.add(mDatabase, product);
        }
    }
}
