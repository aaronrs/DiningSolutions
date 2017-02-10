package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.model.TaskType;

public class TaskTable extends CMSTable<Task>{

    private static final String TABLE_NAME = "tasks";

    public static final String TASK_DATE = "date";
    public static final String TASK_TIME = "time";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";
    public static final String CUSTOMER_ID = "customer_id";

    private static final String CREATE_TABLE =
            TASK_DATE + " DATE," +
            TASK_TIME + " TIME," +
            TASK_TITLE + " TEXT," +
            TASK_DESCRIPTION + " TEXT," +
            CUSTOMER_ID + " TEXT";

    public TaskTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected ContentValues getInsertValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TASK_DATE, task.date == null ? null : task.date.dbFormat());
        values.put(TASK_TITLE, task.title);
        values.put(TASK_DESCRIPTION, task.description);
        values.put(CUSTOMER_ID, task.customerId.toString());
        return values;
    }

    public Cursor getTasks(SQLiteDatabase db) {
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getVisitTask(SQLiteDatabase db, String customerId) {
        return db.query(TABLE_NAME, null, CUSTOMER_ID + " = ?", new String[]{customerId}, null, null, null, "1");
    }
}
