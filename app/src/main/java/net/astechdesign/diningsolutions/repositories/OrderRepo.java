package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderRepo {

    private static OrderRepo repo;
    private final OrderTable orderTable;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context.getApplicationContext());
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context;
        mDatabase = new DBHelper(mContext).getWritableDatabase();
        orderTable = DBHelper.getOrderTable();
    }

    public List<Order> getOrders(Customer customer) {
        List<Order> orders = orderTable.getOrders(mDatabase, customer);
        return orders;
    }

    public void create(Customer mCustomer) {
        int invoiceNumber = orderTable.newInvoiceNumber(mDatabase);
        Order order = new Order(-1, mCustomer.getId(), new DSDDate(), Integer.toString(invoiceNumber));
        orderTable.addOrUpdate(mDatabase, order);
    }

    public List<Order> getOrdersByDate(Customer mCustomer) {
        List<Order> orders = getOrders(mCustomer);
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order lhs, Order rhs) {
                if (lhs.created.equals(rhs.created)) {
                    return - lhs.invoiceNumber.compareTo(rhs.invoiceNumber);
                }
                return - lhs.created.compareTo(rhs.created);
            }
        });
        return orders;
    }

    public void add(Order mOrder) {
        orderTable.addOrUpdate(mDatabase, mOrder);
    }
}
