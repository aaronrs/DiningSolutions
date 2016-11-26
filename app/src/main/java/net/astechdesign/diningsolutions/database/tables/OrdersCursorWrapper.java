package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.util.List;
import java.util.UUID;

public class OrdersCursorWrapper extends CursorWrapper {

    public OrdersCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Order getOrder(List<OrderItem> orderItems) {
        UUID id = UUID.fromString(getString(getColumnIndex(OrdersTable.ID)));
        UUID customerId = UUID.fromString(getString(getColumnIndex(OrdersTable.CUSTOMER_ID)));
        DSDDate orderDate = new DSDDate(getString(getColumnIndex(OrdersTable.ORDER_DATE)));
        String invoiceNumber = getString(getColumnIndex(OrdersTable.INVOICE_NO));
        Order order = new Order(id, customerId, orderDate, invoiceNumber, orderItems);
        return order;
    }
}
