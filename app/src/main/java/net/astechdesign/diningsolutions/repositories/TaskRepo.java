package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.TaskTable;
import net.astechdesign.diningsolutions.database.wrappers.TaskCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private TaskRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        this.mTaskTable = DBHelper.getTaskTable();
    }

    public List<Task> get() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = mTaskTable.getTasks(mDatabase);
        while (!cursor.isAfterLast()) {
            tasks.add(new TaskCursorWrapper(cursor).getTask());
            cursor.moveToNext();
        }

        List<Order> orders = OrderRepo.get(mContext).get();
        for (Order order : orders) {
            List<OrderItem> items = order.getDeliveryItems();
            if (!items.isEmpty()) {
                Customer customer = CustomerRepo.get(mContext).get(order.customerId);
                Map<String, List<OrderItem>> itemsMap = new HashMap<>();
                for (OrderItem item : items) {
                    if (!itemsMap.containsKey(item.deliveryDate.getDisplayDate())) {
                        itemsMap.put(item.deliveryDate.getDisplayDate(), new ArrayList<OrderItem>());
                    }
                    itemsMap.get(item.deliveryDate.getDisplayDate()).add(item);
                }
                for (List<OrderItem> itemList : itemsMap.values()) {
                    tasks.add(Task.deliveryTask(customer, itemList));
                }
            }
        }
        List<Customer> customers = CustomerRepo.get(mContext).get();
        for (Customer customer : customers) {
            if (customer.visit.isCurrent()) {
                tasks.add(Task.visitTask(customer));
            }
        }

        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.date.compareTo(rhs.date);
            }
        });
        return tasks;
    }

    public void addOrUpdate(Task task) {
        mTaskTable.addOrUpdate(mDatabase, task);
    }
}
