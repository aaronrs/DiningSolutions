package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderRepo {

    public static OrderRepo repo;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private List<Order> mOrders;
    private Map<UUID, Order> mOrderMap;

    private OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
    }

    private OrderRepo(Context context, int x) {
        mOrders = new ArrayList<>();
        mOrderMap = new HashMap<>();
        for (Object[] orderValues : orderList) {
            List<OrderItem> items = new ArrayList<>();
            for (int i=2; i < 3;) {
                items.add(new OrderItem((String) orderValues[i++],(Double) orderValues[i++], (Integer) orderValues[i++], (String)orderValues[i++]));
            }
            Order order = new Order(UUID.randomUUID(),
                    CustomerRepo.get(context).get(0).id,
                    new DSDDate((String) orderValues[0]),
                    (String) orderValues[1],
                    items);
            mOrders.add(order);
            mOrderMap.put(order.id, order);
        }
//
//        OrderItem item1 = new OrderItem("Ham", 12.99, 1, "123");
//        OrderItem item2 = new OrderItem("Cheese", 17.50, 1, "124");
//        OrderItem item3 = new OrderItem("Onions", 22.44, 2, "100");
//
//        for (int i=0; i <10; i++) {
//            List<OrderItem> items = new ArrayList<>();
//            items.add(item1);
//            items.add(item2);
//            items.add(item3);
//            Order order = new Order(UUID.randomUUID(), CustomerRepo.get(context).getmCustomers().get(0).id, new DSDDate("2015-01-01"), "000123", items);
//        }
    }

    public List<Order> getmOrders() {
        return mOrders;
    }

    public void addOrder(Order order) {
        mOrders.add(order);
        mOrderMap.put(order.id, order);
    }

    public Order getOrder(UUID id) {
        return mOrderMap.get(id);
    }

    Object[][] orderList = {
            {"2015-01-01", "00001", "Ham", 1.99, 1, "123","Cheese", 1.50, 1, "124","Onions", 2.01, 2, "100"},
            {"2015-06-31", "00002", "Eggs", 1.00, 12, "125","Butter", 0.50, 1, "144","Jam", 1.01, 1, "104"},
            {"2016-10-01", "00003", "Chips", 2.99, 2, "189","Cheese", 1.50, 1, "132","Onions", 2.01, 5, "100"},
    };

    public static Order get(FragmentActivity activity, UUID serializable) {
        return get(activity).getOrder(serializable);
    }
}
