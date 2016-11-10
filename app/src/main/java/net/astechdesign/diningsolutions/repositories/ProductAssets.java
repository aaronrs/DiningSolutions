package net.astechdesign.diningsolutions.repositories;

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
        InputStream is = null;
        try {
            is = assets.open("db/products.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        List<Product> productList = new ArrayList<>();
        String previous = "";
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] productInfo = line.split("\\|");
                if (! previous.equals(productInfo[2])) {
                    productList.add(new Product(UUID.randomUUID(), productInfo[1], productInfo[2], new Double(productInfo[3])));
                    previous = productInfo[2];
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return productList;
    }
}
