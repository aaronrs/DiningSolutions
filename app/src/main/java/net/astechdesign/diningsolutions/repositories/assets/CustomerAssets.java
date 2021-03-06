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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            SimpleDateFormat fileDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while ((line = br.readLine()) != null) {
                String[] customerInfo = line.split("\\|");
                String name = customerInfo[0].trim();
                String email = customerInfo[1].trim();
                String house = customerInfo[2].trim();
                if (house.contains("@")) {
                    email = house;
                    house = "";
                }
                String addressLine = (house != "" ? house + ", " : "") + customerInfo[3].trim();
                String town = customerInfo[5].trim();
                String county = customerInfo[6].trim();
                String postcode = customerInfo[7].trim();
                String created = customerInfo[8].trim();

                if (peeps.add(name + postcode)) {
                    Address address = Address.create(UUID.randomUUID(), addressLine, town, county, postcode);
                    Date createDate = fileDateFormat.parse(created);
                    customerList.add(Customer.create(name, email, "", true, DSDDate.create(createDate), "", address, null));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }
}
