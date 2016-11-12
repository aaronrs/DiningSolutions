package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;
import java.util.UUID;

public class OrderPagerActivity extends AppCompatActivity {

    private static final String EXTRA_ORDER_ID = "net.astechdesign.diningsolutions.order_id";
    private ViewPager mViewPager;
    private List<Order> mOrders;

    public static Intent newIntent(Context context, UUID orderId) {
        Intent intent = new Intent(context, OrderPagerActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pager);

        UUID orderId = (UUID) getIntent().getSerializableExtra(EXTRA_ORDER_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_order_pager_view_pager);

        mOrders = OrderRepo.get(this).getmOrders();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Order order = mOrders.get(position);
                System.out.println(order.id);
                return OrderFragment.newInstance(order.id);
            }

            @Override
            public int getCount() {
                return mOrders.size();
            }
        });
    }
}
