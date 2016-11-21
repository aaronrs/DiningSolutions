package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.OrderItem;

public class OrderItemsCursorWrapper extends CursorWrapper {

    public OrderItemsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public OrderItem getOrderItem() {
        String name = getString(getColumnIndex(OrdersTable.PRODUCT_NAME));
        double price = getDouble(getColumnIndex(OrdersTable.PRODUCT_PRICE));
        int quantity = getInt(getColumnIndex(OrdersTable.PRODUCT_QUANTITY));
        String batch = getString(getColumnIndex(OrdersTable.PRODUCT_BATCH));
        DSDDate deliveryDate = new DSDDate(getString(getColumnIndex(OrdersTable.DELIVERY_DATE)));
        return new OrderItem(name, price, quantity, batch, deliveryDate);
    }
}
