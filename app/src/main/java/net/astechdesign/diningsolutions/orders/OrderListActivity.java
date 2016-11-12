package net.astechdesign.diningsolutions.orders;


import android.support.v4.app.Fragment;

import net.astechdesign.diningsolutions.SingleFragmentActivity;

public class OrderListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new OrderListFragment();
    }
}
