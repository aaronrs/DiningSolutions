package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Task;
import net.astechdesign.diningsolutions.model.TaskType;

public class TaskTable extends CMSTable<Task>{

    private static final String TABLE_NAME = "tasks";

    public static final String TASK_TYPE = "type";
    public static final String TASK_DATE = "date";
    public static final String TASK_TIME = "time";
    public static final String TASK_CUSTOMER_NAME = "customer_name";
    public static final String TASK_CUSTOMER_PHONE = "customer_phone";
    public static final String TASK_TITLE = "title";
    public static final String TASK_DESCRIPTION = "description";

    private static final String CREATE_TABLE =
            TASK_TYPE + " TEXT," +
            TASK_DATE + " DATE," +
            TASK_TIME + " TIME," +
            TASK_CUSTOMER_NAME + " TEXT," +
            TASK_CUSTOMER_PHONE + " TEXT," +
            TASK_TITLE + " TEXT," +
            TASK_DESCRIPTION + " TEXT";

    public TaskTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected ContentValues getInsertValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TASK_TYPE, task.type.name());
        values.put(TASK_DATE, task.date.dbFormat());
        values.put(TASK_TIME, task.time.toString());
        values.put(TASK_CUSTOMER_NAME, task.customerName);
        values.put(TASK_CUSTOMER_PHONE, task.customerPhone);
        values.put(TASK_TITLE, task.title);
        values.put(TASK_DESCRIPTION, task.description);
        return values;
    }

    public Cursor getTasks(SQLiteDatabase db) {
        return db.rawQuery(TASK_LIST, null);
    }

    public String TASK_LIST = "SELECT " +
            UUID_ID + "," +
            TASK_TYPE + ", " +
            TASK_DATE + "," +
            TASK_TIME + "," +
            TASK_CUSTOMER_NAME + "," +
            TASK_CUSTOMER_PHONE + "," +
            TASK_TITLE + "," +
            TASK_DESCRIPTION + "" +
            " FROM " + TABLE_NAME;
}
