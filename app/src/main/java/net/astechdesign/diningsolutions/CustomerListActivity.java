package net.astechdesign.diningsolutions;

import android.app.Fragment;

public class CustomerListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CustomerListFragment();
    }
}
