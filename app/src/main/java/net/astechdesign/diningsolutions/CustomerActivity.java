package net.astechdesign.diningsolutions;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CustomerFragment();
    }
}
