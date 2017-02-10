package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;
import java.util.UUID;

public class OrderCursorWrapper extends CursorWrapper {

    public OrderCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Order getOrder() {
        UUID id = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        UUID customerId = UUID.fromString(getString(getColumnIndex(OrderTable.CUSTOMER_ID)));
        DSDDate orderDate = DSDDate.create(getLong(getColumnIndex(OrderTable.ORDER_DATE)));
        String invoiceNumber = getString(getColumnIndex(OrderTable.INVOICE_NO));

        Order order = new Order(id, customerId, orderDate, invoiceNumber);
        return order;
    }
}
