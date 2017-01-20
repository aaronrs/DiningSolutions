package net.astechdesign.diningsolutions.repositories.assets;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductAssets {

    public static List<Product> getProducts(Context context) {
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("db/products.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<Product> productList = new ArrayList<>();
        List<String> productNames = new ArrayList<>();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] productInfo = line.split("\\|");
                String name = productInfo[0];
                if (!productNames.contains(name)) {
                    productNames.add(name);
                    productList.add(new Product(UUID.randomUUID(), name, "", new Double(productInfo[1]), null, 0));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return productList;
    }
}
