package net.astechdesign.diningsolutions.app.flow;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.products.ProductEditFragment;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.reports.ReportsActivity;

import static net.astechdesign.diningsolutions.orders.OrderActivity.ADD_PRODUCT;

public abstract class OptionsActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        FragmentManager fm;
        switch (item.getItemId()) {
            case R.id.menu_item_customers:
                intent = new Intent(this, CustomerListActivity.class);
                startActivity(intent);
                return true;
//            case R.id.menu_item_new_customer:
//                fm = activity.getSupportFragmentManager();
//                CustomerEditFragment customerEditFragment = new CustomerEditFragment();
//                customerEditFragment.setCustomer(Customer.newCustomer);
//                customerEditFragment.show(fm, EDIT_CUSTOMER);
//                return true;
            case R.id.menu_item_invoices:
                intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_reports:
                intent = new Intent(this, ReportsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_products:
                intent = new Intent(this, ProductListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_new_product:
                fm = getSupportFragmentManager();
                ProductEditFragment newProductFragment = new ProductEditFragment();
                newProductFragment.show(fm, ADD_PRODUCT);
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
