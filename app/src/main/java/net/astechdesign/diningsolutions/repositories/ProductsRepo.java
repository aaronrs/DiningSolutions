package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

import net.astechdesign.diningsolutions.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProductsRepo {

    private static ProductsRepo instance;

    private final List<Product> productList;
    private final Map<UUID, Product> productMap;

    public ProductsRepo(Context context) {
        productList = ProductAssets.getProducts(context);
        productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.id, product);
        }
    }

    public static Product get(String string) {
        return null;
    }

    public static List<Product> get(Context context) {
        if (instance == null) {
            instance = new ProductsRepo(context);
        }
        return instance.productList;
    }
}
