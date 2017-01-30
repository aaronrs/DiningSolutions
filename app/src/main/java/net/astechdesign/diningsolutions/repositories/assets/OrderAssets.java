package net.astechdesign.diningsolutions.repositories.assets;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAssets {

    private static Map<String, Order> orderMap = new HashMap<>();

    public static List<Order> getOrders(Context context) {
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("db/orders.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                addOrder(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(orderMap.values());
    }

    private static void addOrder(String info) {
        String[] order = info.split("\\|");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        DSDDate created = null;
        try {
            created = new DSDDate(dateFormat.parse(order[2]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!orderMap.containsKey(order[0])) {
            orderMap.put(order[0], new Order(null, null, created, order[3]));
        }
        Order order1 = orderMap.get(order[0]);
        order1.addItem(new OrderItem(null, order[4], Double.parseDouble(order[7]), Integer.parseInt(order[6]), order[5], created));
    }
}
