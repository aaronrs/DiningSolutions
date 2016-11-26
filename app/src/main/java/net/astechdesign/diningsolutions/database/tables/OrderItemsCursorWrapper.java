package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.UUID;

public class OrderItemsCursorWrapper extends CursorWrapper {

    public OrderItemsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public OrderItem getOrderItem() {
        UUID id = UUID.fromString(getString(getColumnIndex(OrderItemsTable.ID)));
        String name = getString(getColumnIndex(OrderItemsTable.PRODUCT_NAME));
        double price = getDouble(getColumnIndex(OrderItemsTable.PRODUCT_PRICE));
        int quantity = getInt(getColumnIndex(OrderItemsTable.PRODUCT_QUANTITY));
        String batch = getString(getColumnIndex(OrderItemsTable.PRODUCT_BATCH));
        DSDDate deliveryDate = new DSDDate(getString(getColumnIndex(OrderItemsTable.DELIVERY_DATE)));
        return new OrderItem(id, name, price, quantity, batch, deliveryDate);
    }
}
