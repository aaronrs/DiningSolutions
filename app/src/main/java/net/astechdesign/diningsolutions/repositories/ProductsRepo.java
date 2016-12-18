package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Product;

import java.util.List;

public class ProductsRepo {

    private static ProductsRepo instance;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final ProductTable productTable;
    private final DBHelper dbHelper;

    private ProductsRepo(Context context) {
        mContext = context.getApplicationContext();
        dbHelper = DBHelper.getDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        productTable = dbHelper.getProductTable();
    }

    public static Product get(String string) {
        return null;
    }

    public static List<Product> get(Context context) {
        return getInstance(context).get();
    }

    public static void addOrUpdate(Context context, Product product) {
        getInstance(context).addOrUpdate(product);
    }

    private static ProductsRepo getInstance(Context context) {
        if (instance == null) {
            instance = new ProductsRepo(context);
        }
        return instance;
    }

    private List<Product> get() {
        List<Product> productList = productTable.get(mDatabase);
        if (productList == null || productList.isEmpty()) {
            initDb(mContext);
            productList = productTable.get(mDatabase);
        }
        return productList;
    }

    private void addOrUpdate(Product product) {
        productTable.addOrUpdate(mDatabase, product);
    }

    private void initDb(Context context) {
        List<Product> productList = ProductAssets.getProducts(context);
        for (Product product : productList) {
            productTable.addOrUpdate(mDatabase, product);
        }
    }
}
