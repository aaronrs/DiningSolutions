package net.astechdesign.diningsolutions;

import android.app.Fragment;

public class OrderActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new OrderFragment();
    }
}
