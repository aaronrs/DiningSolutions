package net.astechdesign.diningsolutions.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Distributor {

    public final String number;
    public final String name;
    public final String mobile;
    public final String email;

    private Distributor(String number, String name, String mobile, String email) {
        this.number = number;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
    }

    public static Distributor get(Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String name = preferences.getString("distributor_name", "");
            String number = preferences.getString("distributor_number", "");
            String mobile = preferences.getString("distributor_mobile", "");
            String email = preferences.getString("distributor_email", "");
            return new Distributor(number, name, mobile, email);
    }
}
