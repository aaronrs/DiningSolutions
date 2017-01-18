package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.CustomerAssets;
import net.astechdesign.diningsolutions.repositories.OrderAssets;
import net.astechdesign.diningsolutions.repositories.ProductAssets;

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

        List<Order> orderList = OrderAssets.getOrders(context, 1);
        for (Order order : orderList) {
            orderTable.addOrUpdate(db, order);
        }
    }
}
