package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    private static final String ADD_ORDER = "add_order";
    public static final String ARG_CUSTOMER = "mCustomer";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private NewOrderFragment newOrderFragment;
    private OrderEditFragment editOrderFragment;
    private Customer mCustomer;
    private OrderRepo mOrderRepo;
    private Order mCurrentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        mCustomer = (Customer) getIntent().getSerializableExtra(ARG_CUSTOMER);
        mCustomer = mCustomer == null ? CustomerRepo.get(this).get(0) : mCustomer;

        mOrderRepo = new OrderRepo(this);

        TextView name = (TextView) findViewById(R.id.customer_name);
        name.setText(mCustomer.name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                editOrderFragment = new OrderEditFragment();
                editOrderFragment.setCustomer(mCustomer);
                editOrderFragment.show(fm, ADD_ORDER);
            }
        });

        View recyclerView = findViewById(R.id.order_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.order_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_order:
                FragmentManager fm = getSupportFragmentManager();
                newOrderFragment = new NewOrderFragment();
                newOrderFragment.show(fm, ADD_ORDER);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mOrderRepo.getOrders(mCustomer)));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Order> mValues;

        public SimpleItemRecyclerViewAdapter(List<Order> items) {
            mValues = items;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        showOrderDetails(holder.mItem);
                    } else {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, OrderDetailActivity.class);
//                        intent.putExtra(OrderDetailFragment.ARG_ORDER, holder.getId());
//
//                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public Order mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.order_delivery_date);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mIdView.getText() + "'";
            }

            public void setItem(Order item) {
                this.mItem = item;
                mIdView.setText(item.created == null ? new DSDDate().toString() : item.created.toString());
            }

            public int getId() {
                return mItem.id;
            }
        }
    }

    private void showOrderDetails(Order order) {
        Bundle arguments = new Bundle();
        mCurrentOrder = order;
        arguments.putSerializable(OrderDetailFragment.ARG_ORDER, order);
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.order_detail_container, fragment)
                .commit();
    }
}
