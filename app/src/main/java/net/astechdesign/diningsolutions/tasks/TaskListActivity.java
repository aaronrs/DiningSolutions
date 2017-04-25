package net.astechdesign.diningsolutions.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.admin.SettingsActivity;
import net.astechdesign.diningsolutions.customers.CustomerEditFragment;
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.orders.OrderActivity;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.reports.ReportsActivity;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderRepo;
import net.astechdesign.diningsolutions.repositories.TaskRepo;

import java.util.List;
import java.util.UUID;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.TimePickerFragment.TIME_PICKER;
import static net.astechdesign.diningsolutions.customers.CustomerEditFragment.EDIT_CUSTOMER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class TaskListActivity extends AppCompatActivity
        implements NewTaskFragment.NewTaskListener,
        CustomerEditFragment.CustomerEditListener {

    private NewTaskFragment newTaskFragment;
    private RecyclerView recyclerView;
    private List<Task> taskList;
    private TaskRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        taskList = TaskRepo.get(this).get();
        viewAdapter = new TaskRecyclerViewAdapter(this, taskList);
        recyclerView = (RecyclerView) findViewById(R.id.task_list);
        recyclerView.setAdapter(viewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_item_customers:
                intent = new Intent(this, CustomerListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_item_new_customer:
                FragmentManager fm = getSupportFragmentManager();
                CustomerEditFragment customerEditFragment = new CustomerEditFragment();
                customerEditFragment.setCustomer(Customer.newCustomer);
                customerEditFragment.show(fm, EDIT_CUSTOMER);
                return true;
            case R.id.action_reports:
                intent = new Intent(this, ReportsActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        setupRecyclerView(taskList);
//    }
//
//    private void setupRecyclerView(List<Task> taskList) {
//        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(this, taskList);
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
//    }
//
    @Override
    public void onNewTaskPositiveClick(DialogInterface dialog, DSDDate date, String title, String description) {
        TaskRepo taskRepo = TaskRepo.get(this);
        taskRepo.addOrUpdate(Task.create(date, title, description));
        taskList.clear();
        taskList.addAll(taskRepo.get());
        viewAdapter.notifyDataSetChanged();
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(newTaskFragment, DSDDate.create());
        dialog.show(fm, DATE_PICKER);
    }

    public void getTime(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(newTaskFragment, DSDDate.create());
        dialog.show(fm, TIME_PICKER);
    }

    public void addTask(View view) {
        FragmentManager fm = getSupportFragmentManager();
        newTaskFragment = new NewTaskFragment();
        Bundle args = new Bundle();
        args.putString(NewTaskFragment.HEADER, "New Task");
        newTaskFragment.setArguments(args);
        newTaskFragment.show(fm, ADD_TASK);
    }

    @Override
    public void onCustomerEditClick(DialogInterface dialog, Customer newCustomer) {
        UUID customerId = CustomerRepo.get(this).addOrUpdate(newCustomer);
        Customer customer = CustomerRepo.get(this).get(customerId);
        OrderRepo.get(this).add(Order.create(customer));
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(OrderActivity.CUSTOMER, customer);
        this.startActivity(intent);
    }
}
