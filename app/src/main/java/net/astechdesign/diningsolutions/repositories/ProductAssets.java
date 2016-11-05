package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductAssets {

    private Map<String, Product> productsMap;

    public ProductAssets(Context context) {
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("db/products.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Map<String, Product> productMap = new HashMap<>();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] productInfo = line.split("\\|");
                UUID id = UUID.randomUUID();
                productMap.put(id.toString(), new Product(id, productInfo[1], productInfo[2], new Double(productInfo[3])));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.productsMap = productMap;
    }

    public Map<String, Product> getProducts() {
        return productsMap;
    }
}
