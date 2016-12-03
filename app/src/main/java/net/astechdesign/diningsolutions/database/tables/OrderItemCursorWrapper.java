package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.UUID;

public class OrderItemCursorWrapper extends CursorWrapper {

    public OrderItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public OrderItem getOrderItem() {
        UUID id = UUID.fromString(getString(getColumnIndex(OrderItemTable.ID)));
        String name = getString(getColumnIndex(OrderItemTable.PRODUCT_NAME));
        double price = getDouble(getColumnIndex(OrderItemTable.PRODUCT_PRICE));
        int quantity = getInt(getColumnIndex(OrderItemTable.PRODUCT_QUANTITY));
        String batch = getString(getColumnIndex(OrderItemTable.PRODUCT_BATCH));
        DSDDate deliveryDate = new DSDDate(getString(getColumnIndex(OrderItemTable.DELIVERY_DATE)));
        return new OrderItem(id, name, price, quantity, batch, deliveryDate);
    }
}
