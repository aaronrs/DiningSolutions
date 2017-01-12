package net.astechdesign.diningsolutions.todos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.astechdesign.diningsolutions.customers.CustomerPickerFragment;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.DSDTime;
import net.astechdesign.diningsolutions.model.Todo;
import net.astechdesign.diningsolutions.repositories.RepoManager;
import net.astechdesign.diningsolutions.repositories.TodoRepo;

import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private static final String ADD_TODO = "add_todo";
    private static final String DATE_PICKER = "date_picker";
    private static final String TIME_PICKER = "time_picker";
    private static final String CUSTOMER_PICKER = "customer_picker";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private NewTodoFragment newTodoFragment;
    private TodoRepo todoRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoRepo = RepoManager.getTodoRepo(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.todo_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.todo_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_todo:
                FragmentManager fm = getSupportFragmentManager();
                newTodoFragment = new NewTodoFragment();
                newTodoFragment.show(fm, ADD_TODO);
                return true;
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_item_customers:
                intent = new Intent(this, CustomerListActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Todo> todoList = todoRepo.get();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(todoList));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Todo> mValues;

        public SimpleItemRecyclerViewAdapter(List<Todo> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todo_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.setItem(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(TodoDetailFragment.ARG_ITEM_ID, holder.mItem.id.toString());
                        TodoDetailFragment fragment = new TodoDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.todo_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TodoDetailActivity.class);
                        intent.putExtra(TodoDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final View mView;
            private final TextView mIdView;
            private final TextView mContentView;
            private Todo mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            public void setItem(Todo item) {
                this.mItem = item;
                mIdView.setText(new DSDDate().toString());
                mContentView.setText("Description");
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(new DSDDate());
        dialog.setTargetFragment(newTodoFragment, DatePickerFragment.REQUEST_DATE);
        dialog.show(fm, DATE_PICKER);
    }

    public void getTime(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(new DSDTime(new Date()));
        dialog.setTargetFragment(newTodoFragment, TimePickerFragment.REQUEST_TIME);
        dialog.show(fm, TIME_PICKER);
    }

    public void getCustomer(View v) {
        FragmentManager fm = getSupportFragmentManager();
        CustomerPickerFragment dialog = CustomerPickerFragment.newInstance();
        dialog.setTargetFragment(newTodoFragment, CustomerPickerFragment.REQUEST_CUSTOMER);
        dialog.show(fm, CUSTOMER_PICKER);
    }

}
