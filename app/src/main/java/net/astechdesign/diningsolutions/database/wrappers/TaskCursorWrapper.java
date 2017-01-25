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
        String dateString = getString(getColumnIndex(TaskTable.TASK_DATE));
        DSDDate date = dateString != null ? new DSDDate(dateString) : null;
        String title = getString(getColumnIndex(TaskTable.TASK_TITLE));
        String description = getString(getColumnIndex(TaskTable.TASK_DESCRIPTION));
        return new Task(id, date, null, title, description);
    }
}
