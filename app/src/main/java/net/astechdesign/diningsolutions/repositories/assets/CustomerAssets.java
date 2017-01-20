package net.astechdesign.diningsolutions.repositories.assets;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class CustomerAssets {

    public static List<Customer> getCustomers(Context context) {
        AssetManager assets = context.getAssets();
        InputStream is;
        try {
            is = assets.open("db/sorted_customers.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Pattern pattern = Pattern.compile("^\\d{1,3} .*");

        Set<String> peeps = new HashSet<>();
        List<Customer> customerList = new ArrayList<>();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] customerInfo = line.split("\\|");
                String name = customerInfo[0].trim();
                String email = customerInfo[1].trim();
                String house = customerInfo[2].trim();
                if (house.contains("@")) {
                    email = house;
                    house = "";
                }
                String line1 = customerInfo[3].trim();
                if (pattern.matcher(line1).matches()) {
                    house = line1.trim().split(" ")[0];
                    line1 = line1.substring(house.length() + 1);
                }
                String line2 = customerInfo[4].trim();
                String town = customerInfo[5].trim();
                String county = customerInfo[6].trim();
                String postcode = customerInfo[7].trim();
                String created = customerInfo[8].trim();

                if (peeps.add(name + postcode)) {
                    Address address = new Address(UUID.randomUUID(), house, line1, line2, town, county, postcode);
                    customerList.add(new Customer(null, name, email, "", true, DSDDate.fileCreate(created), "", address));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }
}
