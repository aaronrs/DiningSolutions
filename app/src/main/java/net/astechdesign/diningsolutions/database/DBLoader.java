package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.wrappers.CustomerCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.assets.CustomerAssets;
import net.astechdesign.diningsolutions.repositories.assets.OrderAssets;
import net.astechdesign.diningsolutions.repositories.assets.ProductAssets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.astechdesign.diningsolutions.database.tables.CMSTable.TableType.CUSTOMER;
import static net.astechdesign.diningsolutions.database.tables.CMSTable.TableType.PRODUCT;

public class DBLoader {

    public static void load(Context context, SQLiteDatabase db) {
        List<Product> productList = ProductAssets.getProducts(context);
        for (Product product : productList) {
            DBHelper.getProductTable().addOrUpdate(db, product);
        }

        CustomerTable table = CustomerTable.table();
        List<Customer> customerList = CustomerAssets.getCustomers(context);
        for (Customer customer: customerList) {
            table.addOrUpdate(db, customer);
        }

        Cursor cursor = table.get(db, "Southwell");
        cursor.moveToFirst();
        Customer customer = new CustomerCursorWrapper(cursor).getCustomer();
        cursor.close();
        List<Order> orderList = OrderAssets.getOrders(context);
        for (Order order : orderList) {
            DBHelper.getOrderTable().addOrUpdate(db, customer, order);
            for (OrderItem item : order.orderItems) {
                DBHelper.getOrderItemTable().addOrUpdate(db, order, item);
            }
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
