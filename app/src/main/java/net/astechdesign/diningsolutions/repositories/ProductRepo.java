package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Product;

import java.util.List;

public class ProductRepo {

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final ProductTable productTable;
    private final DBHelper dbHelper;

    public ProductRepo(Context context) {
        mContext = context.getApplicationContext();
        dbHelper = DBHelper.getDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        productTable = dbHelper.getProductTable();
    }

    public List<Product> get() {
        List<Product> productList = productTable.get(mDatabase);
        if (productList == null || productList.isEmpty()) {
            initDb(mContext);
            productList = productTable.get(mDatabase);
        }
        return productList;
    }

    public void addOrUpdate(Product product) {
        productTable.addOrUpdate(mDatabase, product);
    }

    private void initDb(Context context) {
        List<Product> productList = ProductAssets.getProducts(context);
        for (Product product : productList) {
            productTable.addOrUpdate(mDatabase, product);
        }
    }

    public Product get(int id) {
        return productTable.get(mDatabase, id);
    }
}
