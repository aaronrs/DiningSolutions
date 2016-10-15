package net.astechdesign.diningsolutions;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CustomerActivity extends SingleFragmentActivity {

    public static final String EXTRA_CUSTOMER_ID = "net.astechdesign.diningsolutions.customerId";

    @Override
    protected Fragment createFragment() {
        return new CustomerFragment();
    }

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CustomerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER_ID, id);
        return intent;
    }
}
