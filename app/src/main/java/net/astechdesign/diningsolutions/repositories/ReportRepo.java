package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.wrappers.RecordCursorWrapper;
import net.astechdesign.diningsolutions.model.Record;

import java.util.ArrayList;
import java.util.List;

public class ReportRepo {

    private static ReportRepo repo;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final OrderTable orderTable;

    private ReportRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        orderTable = DBHelper.getOrderTable();
    }

    public static ReportRepo get(Context context) {
        if (repo == null) {
            repo = new ReportRepo(context);
        }
        return repo;
    }

    public List<Record> getSales() {
        List<Record> records = new ArrayList<>();
        Cursor cursor = orderTable.getOrderSummary(mDatabase);
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            records.add(new RecordCursorWrapper(cursor).getRecord());
            cursor.moveToNext();
        }
        return records;
    }
}
