package net.astechdesign.diningsolutions;


import android.support.v4.app.Fragment;

public class OrderListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new OrderListFragment();
    }
}
