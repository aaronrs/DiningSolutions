package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.Product;

import java.util.UUID;

public class ProductsCursorWrapper extends CursorWrapper {

    public ProductsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        UUID id = UUID.fromString(getString(getColumnIndex(ProductsTable.ID)));
        String name = getString(getColumnIndex(ProductsTable.PRODUCT_NAME));
        String description = getString(getColumnIndex(ProductsTable.PRODUCT_DESCRIPTION));
        Double price = getDouble(getColumnIndex(ProductsTable.PRODUCT_PRICE));
        String barcode = getString(getColumnIndex(ProductsTable.PRODUCT_BARCODE));
        int deleted = getInt(getColumnIndex(ProductsTable.PRODUCT_DESCRIPTION));
        return new Product(id, name, description, price, barcode, deleted);
    }
}
