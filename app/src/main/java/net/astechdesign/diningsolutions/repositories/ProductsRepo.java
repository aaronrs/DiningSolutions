package net.astechdesign.diningsolutions.repositories;

import android.content.ComponentName;
import android.content.Context;
import android.text.TextWatcher;

import net.astechdesign.diningsolutions.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductsRepo {

    private static ProductsRepo instance;
    private final Map<String, Product> productsMap;
    private List<Product> productsList;

    public ProductsRepo(Context context) {
        ProductAssets productAssets = new ProductAssets(context);
        productsMap = productAssets.getProducts();
        productsList = new ArrayList<>(productsMap.values());
        Collections.sort(productsList);
    }

    public static Product get(Context context, String id) {
        init(context);
        return instance.productsMap.get(id);
    }

    public static List<Product> get(Context context) {
        init(context);
        return instance.productsList;
    }

    private static void init(Context context) {
        if (instance == null) {
            instance = new ProductsRepo(context);
        }
    }

    public static void update(UUID id, String newName, String newPrice) {
        instance.update(new Product(id, "", newName, new Double(newPrice)));
    }

    private void update(Product newProduct) {
        productsMap.put(newProduct.id.toString(), newProduct);
        productsList = new ArrayList<>(productsMap.values());
    }

    private void update(String id, String name) {
        Product product = productsMap.get(id);
        product.setName(name);
    }

    public static void update(Context context, UUID id, String name) {
        init(context);
        instance.update(id.toString(), name);
    }
}
