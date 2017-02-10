package net.astechdesign.diningsolutions.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import net.astechdesign.diningsolutions.customers.CustomerListActivity;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.products.ProductListActivity;
import net.astechdesign.diningsolutions.repositories.OrderRepo;
import net.astechdesign.diningsolutions.repositories.TaskRepo;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.TimePickerFragment.TIME_PICKER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class TaskListActivity extends AppCompatActivity implements NewTaskFragment.NewTaskListener {

    public static final String TASK = "task";

    private NewTaskFragment newTaskFragment;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.task_list);
        setupRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_task:
                FragmentManager fm = getSupportFragmentManager();
                newTaskFragment = new NewTaskFragment();
                Bundle args = new Bundle();
                args.putString(NewTaskFragment.HEADER, "New Task");
                newTaskFragment.setArguments(args);
                newTaskFragment.show(fm, ADD_TASK);
                return true;
            case R.id.menu_item_products:
                Intent intent = new Intent(this, ProductListActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_item_customers:
                intent = new Intent(this, CustomerListActivity.class);
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

    private void setupRecyclerView() {
        List<Task> taskList = TaskRepo.get(this).get();
        recyclerView.setAdapter(new TaskRecyclerViewAdapter(this, taskList));
    }

    @Override
    public void onNewTaskPositiveClick(DialogInterface dialog, DSDDate date, String title, String description) {
        TaskRepo.get(this).addOrUpdate(Task.create(date, title, description));
        setupRecyclerView();
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(DSDDate.create());
        dialog.setTargetFragment(newTaskFragment, DatePickerFragment.REQUEST_DATE);
        dialog.show(fm, DATE_PICKER);
    }

    public void getTime(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(DSDDate.create());
        dialog.setTargetFragment(newTaskFragment, TimePickerFragment.REQUEST_TIME);
        dialog.show(fm, TIME_PICKER);
    }

}
