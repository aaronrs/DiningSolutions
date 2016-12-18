package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderAssets {

    public static List<Order> getOrders(Context context, UUID customerId) {
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("db/orders.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Map<String, List<String[]>> orderMap = new HashMap<>();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] orderInfo = line.split("\\|");
                String key = orderInfo[0] + orderInfo[1];
                if (!orderMap.containsKey(key)) {
                    orderMap.put(key, new ArrayList<String[]>());
                }
                orderMap.get(key).add(orderInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Order> orderList = new ArrayList<>();
        int invoice = 1234;
        for (String key : orderMap.keySet()) {
            List<OrderItem> items = new ArrayList<>();
            DSDDate deliveryDate = null;
            for (String[] orderInfo : orderMap.get(key)) {
                deliveryDate = new DSDDate(orderInfo[1]);
                items.add(new OrderItem(UUID.randomUUID(), null, orderInfo[0], 0, Integer.parseInt(orderInfo[4]), orderInfo[4], deliveryDate));
            }
            orderList.add(new Order(null, customerId, deliveryDate, "" + invoice, items));
            invoice++;
        }

        return orderList;
    }
}
