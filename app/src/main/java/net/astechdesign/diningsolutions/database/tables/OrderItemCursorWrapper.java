package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;
import java.util.UUID;

public class OrderItemCursorWrapper extends CursorWrapper {

    public OrderItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public OrderItem getOrderItem() {
        UUID id = UUID.fromString(getString(getColumnIndex(OrderItemTable.ID)));
        UUID orderId = UUID.fromString(getString(getColumnIndex(OrderItemTable.ORDER_ID)));
        String itemName = getString(getColumnIndex(OrderItemTable.PRODUCT_NAME));
        String itemBatch = getString(getColumnIndex(OrderItemTable.PRODUCT_BATCH));
        int itemQuantity = getInt(getColumnIndex(OrderItemTable.PRODUCT_QUANTITY));
        double itemPrice = getDouble(getColumnIndex(OrderItemTable.PRODUCT_PRICE));
        DSDDate deliveryDate = new DSDDate(getString(getColumnIndex(OrderTable.ORDER_DATE)));

        return new OrderItem(id, orderId, itemName, itemPrice, itemQuantity, itemBatch, deliveryDate);
    }
}
