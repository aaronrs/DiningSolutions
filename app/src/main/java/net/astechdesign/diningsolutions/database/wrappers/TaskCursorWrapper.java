package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.TaskTable;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Task;

import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        UUID id = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        DSDDate date = new DSDDate(getString(getColumnIndex(TaskTable.TASK_DATE)));
        String title = getString(getColumnIndex(TaskTable.TASK_TITLE));
        String description = getString(getColumnIndex(TaskTable.TASK_DESCRIPTION));
        String type = getString(getColumnIndex(TaskTable.TASK_TYPE));
        String customerName = getString(getColumnIndex(CustomerTable.CUSTOMER_NAME));
        String customerPhone = getString(getColumnIndex(CustomerTable.CUSTOMER_PHONE));
        return new Task(id, type, date, null, customerName, customerPhone, title, description);
    }
}