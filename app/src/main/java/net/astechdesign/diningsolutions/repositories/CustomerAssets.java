package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.MediaStore;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Email;
import net.astechdesign.diningsolutions.model.Phone;
import net.astechdesign.diningsolutions.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerAssets {

    // FullName|Organisation|Property|Street|Locality|Town|County|Postcode|SaleDate|NumBox

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
        List<String> nameList = Arrays.asList(names);

        List<String> peeps = new ArrayList<>();
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

                if (peeps.contains(name + postcode)) {
                    continue;
                }
                peeps.add(name + postcode);

//                    System.out.println("#" + line);
//                    System.out.println("-" + name + "|" +
//                            email + "|" +
//                            house + "|" +
//                            line1 + "|" +
//                            line2 + "|" +
//                            town + "|" +
//                            county + "|" +
//                            postcode + "|" +
//                            created);

                Address address = new Address(house, line1, line2, town, county, postcode);
                customerList.add(new Customer(null, DSDDate.fileCreate(created), name, address, new Email(email), new Phone("")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    static String[] names = {"Phillips", "Teernez", "Dyer", "Gresham", "Greenwood"};
}
