package net.astechdesign.diningsolutions;


import android.support.v4.app.Fragment;

public class CustomerListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CustomerListFragment();
    }
}
