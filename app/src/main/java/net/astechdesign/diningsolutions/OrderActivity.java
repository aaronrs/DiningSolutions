package net.astechdesign.diningsolutions;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class OrderActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new OrderFragment();
    }
}
