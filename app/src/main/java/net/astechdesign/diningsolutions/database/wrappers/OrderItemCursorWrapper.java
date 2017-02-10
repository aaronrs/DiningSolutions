package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
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
        UUID id = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        String itemName = getString(getColumnIndex(OrderItemTable.PRODUCT_NAME));
        String itemBatch = getString(getColumnIndex(OrderItemTable.PRODUCT_BATCH));
        int itemQuantity = getInt(getColumnIndex(OrderItemTable.PRODUCT_QUANTITY));
        double itemPrice = getDouble(getColumnIndex(OrderItemTable.PRODUCT_PRICE));
        DSDDate deliveryDate = DSDDate.create(getLong(getColumnIndex(OrderItemTable.DELIVERY_DATE)));

        return new OrderItem(id, itemName, itemPrice, itemQuantity, itemBatch, deliveryDate);
    }
}
