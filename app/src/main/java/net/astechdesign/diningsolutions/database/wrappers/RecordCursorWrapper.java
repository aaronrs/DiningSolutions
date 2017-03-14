package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.Record;

import java.util.HashMap;
import java.util.Map;

public class RecordCursorWrapper extends CursorWrapper {

    public RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Record getRecord() {
        Map<String, Object> data = new HashMap<>();
        data.put("date", getString(getColumnIndex(OrderTable.ORDER_DATE)));
        data.put("product", getString(getColumnIndex(OrderItemTable.PRODUCT_NAME)));
        data.put("quantity", getInt(getColumnIndex(OrderItemTable.PRODUCT_QUANTITY)));
        return new Record(getColumnNames(), data);
    }
}
