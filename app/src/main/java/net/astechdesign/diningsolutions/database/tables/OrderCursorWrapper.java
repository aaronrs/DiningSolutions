package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

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
        UUID id = UUID.fromString(getString(getColumnIndex(OrderTable.ID)));
        UUID customerId = UUID.fromString(getString(getColumnIndex(OrderTable.CUSTOMER_ID)));
        DSDDate orderDate = new DSDDate(getString(getColumnIndex(OrderTable.ORDER_DATE)));
        String invoiceNumber = getString(getColumnIndex(OrderTable.INVOICE_NO));

        Order order = new Order(id, customerId, orderDate, invoiceNumber, null);
        return order;
    }
}
