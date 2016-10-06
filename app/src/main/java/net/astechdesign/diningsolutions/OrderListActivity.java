package net.astechdesign.diningsolutions;

import android.app.Fragment;

public class OrderListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new OrderListFragment();
    }
}
