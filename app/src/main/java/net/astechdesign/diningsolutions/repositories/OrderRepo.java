package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;

import java.util.List;
import java.util.UUID;

public class OrderRepo {

    public static OrderRepo repo;
    private Context mContext;

    private SQLiteDatabase mDatabase;
    private OrderTable orderTable;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        DBHelper dbHelper = DBHelper.getDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        orderTable = dbHelper.getOrderTable();
    }

    public List<Order> getOrders(Customer customer) {
        List<Order> orders = orderTable.getOrders(mDatabase, customer);
        if (orders.isEmpty()) {
            initDb(mContext);
        }
        return orders;
    }

    public static List<Order> get(FragmentActivity activity, Customer customer) {
        return get(activity).getOrders(customer);
    }
    private void initDb(Context context) {
        List<Order> productList = OrderAssets.getOrders(context, CustomerRepo.get(context).get(0).id);
        for (Order order : productList) {
            orderTable.addOrUpdate(mDatabase, order);
        }
    }

}
