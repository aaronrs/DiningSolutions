package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.TaskTable;
import net.astechdesign.diningsolutions.database.wrappers.TaskCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TaskRepo {

    private static TaskRepo repo;
    private final TaskTable mTaskTable;
    private final SQLiteDatabase mDatabase;
    private final Context mContext;

    public static TaskRepo get(Context context) {
        if (repo == null) {
            repo = new TaskRepo(context.getApplicationContext());
        }
        return repo;
    }

    public TaskRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        this.mTaskTable = DBHelper.getTaskTable();
    }

    public Task get(UUID id) {
        return new Task(id, new DSDDate(), "Test", "Data");
    }

    public List<Task> get() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = mTaskTable.getTasks(mDatabase);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tasks.add(new TaskCursorWrapper(cursor).getTask());
            cursor.moveToNext();
        }

        List<Order> orders = OrderRepo.get(mContext).get();
        for (Order order : orders) {
            List<OrderItem> items = order.getDeliveryItems();
            if (!items.isEmpty()) {
                Customer customer = CustomerRepo.get(mContext).get(order.customerId);
                tasks.add(Task.deliveryTask(customer, items));
            }
        }

        return tasks;
    }

    public void addOrUpdate(Task task) {
        mTaskTable.addOrUpdate(mDatabase, task);
    }

    public void addVisitDate(Customer customer, Calendar cal) {
        Task visit = new Task(null, DSDDate.create(cal), "Visit", "Customer visit : " + customer.name, customer.getId());
        addOrUpdate(visit);
    }

    public void addVisitTime(Customer customer, Calendar cal) {
        Task visit = new Task(null, DSDDate.create(cal), "Visit", "Customer visit : " + customer.name, customer.getId());
        addOrUpdate(visit);
    }
}
