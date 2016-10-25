package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

import net.astechdesign.diningsolutions.model.Product;

import java.util.List;

public class ProductsRepo {

    private static ProductsRepo instance;

    private final List<Product> productList;

    public ProductsRepo(Context context) {
        productList = ProductAssets.getProducts(context);
        instance = this;
    }

    public static Product get(String string) {
        return null;
    }

    public static List<Product> get() {
        return instance.productList;
    }
}
