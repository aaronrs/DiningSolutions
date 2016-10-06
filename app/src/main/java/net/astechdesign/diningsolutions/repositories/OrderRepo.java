package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

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

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private List<Order> mOrders;
    private Map<UUID, Order> mOrderMap;

    private OrderRepo(Context context) {
        OrderItem item1 = new OrderItem("Ham", 12.99, 1, "123");
        OrderItem item2 = new OrderItem("Cheese", 17.50, 1, "124");
        OrderItem item3 = new OrderItem("Onions", 22.44, 2, "100");

        mOrders = new ArrayList<>();
        mOrderMap = new HashMap<>();
        for (int i=0; i <10; i++) {
            List<OrderItem> items = new ArrayList<>();
            items.add(item1);
            items.add(item2);
            items.add(item3);
            Order order = new Order(UUID.randomUUID(), CustomerRepo.get(context).getmCustomers().get(0).id, new DSDDate("2015-01-01"), "000123", items);
            mOrders.add(order);
            mOrderMap.put(order.id, order);
        }
    }

    public List<Order> getmOrders() {
        return mOrders;
    }

    public Order getOrder(UUID id) {
        return mOrderMap.get(id);
    }
}
