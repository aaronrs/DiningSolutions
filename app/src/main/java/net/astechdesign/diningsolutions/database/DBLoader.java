package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.assets.CustomerAssets;
import net.astechdesign.diningsolutions.repositories.assets.OrderAssets;
import net.astechdesign.diningsolutions.repositories.assets.ProductAssets;

import java.util.List;

public class DBLoader {


    public static void create(SQLiteDatabase db, CMSTable... tables) {
        for (CMSTable table : tables) {
            table.create(db);
        }
    }

    public static void load(Context context, SQLiteDatabase db, ProductTable productTable, CustomerTable customerTable, OrderTable orderTable) {
        List<Product> productList = ProductAssets.getProducts(context);
        for (Product product : productList) {
            productTable.addOrUpdate(db, product);
        }

        List<Customer> customerList = CustomerAssets.getCustomers(context);
        for (Customer customer: customerList) {
            customerTable.addOrUpdate(db, customer);
        }

        int customerId = customerTable.getId(db, "Southwell");
        List<Order> orderList = OrderAssets.getOrders(context, customerId);
        for (Order order : orderList) {
            orderTable.addOrUpdate(db, order);
        }
    }

    public static void showTable(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        int count = cursor.getCount();
        String[] columnNames = cursor.getColumnNames();
        System.out.println(count);
        System.out.println(columnNames);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
    }
}
