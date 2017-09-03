package net.astechdesign.diningsolutions.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.app.AppManager;
import net.astechdesign.diningsolutions.app.OrderManager;
import net.astechdesign.diningsolutions.app.SourceMode;
import net.astechdesign.diningsolutions.app.TaskManager;
import net.astechdesign.diningsolutions.app.flow.OptionsActivity;
import net.astechdesign.diningsolutions.customers.CustomerEditFragment;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.orders.OrderActivity;

import static net.astechdesign.diningsolutions.DatePickerFragment.DATE_PICKER;
import static net.astechdesign.diningsolutions.TimePickerFragment.TIME_PICKER;
import static net.astechdesign.diningsolutions.tasks.NewTaskFragment.ADD_TASK;

public class TaskListActivity extends OptionsActivity
        implements CustomerEditFragment.CustomerEditListener {

    private NewTaskFragment newTaskFragment;
    private TaskRecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.setContext(this.getApplicationContext());
        TaskManager.setActivity(this);

        setContentView(R.layout.activity_task_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.task_list);
        viewAdapter = new TaskRecyclerViewAdapter(this, TaskManager.getTasks());
        recyclerView.setAdapter(viewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getDate(View v) {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(SourceMode.OTHER, newTaskFragment, DSDDate.create());
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

    public void deleteTask(View view) {
        final Task deleteTask = (Task) view.getTag();
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Delete task \"" + deleteTask.title + "\" ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        TaskManager.delete(deleteTask);
                        TaskManager.updateView();
                    }}).show();
    }

    @Override
    public void onCustomerEditClick(DialogInterface dialog) {
        OrderManager.create();
        Intent intent = new Intent(this, OrderActivity.class);
        this.startActivity(intent);
    }

    public void updateView() {
        viewAdapter.notifyDataSetChanged();
    }
}
