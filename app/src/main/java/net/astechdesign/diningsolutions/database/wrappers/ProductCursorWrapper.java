package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.model.Product;

import java.util.UUID;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        UUID id = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        String name = getString(getColumnIndex(ProductTable.PRODUCT_NAME));
        String description = getString(getColumnIndex(ProductTable.PRODUCT_DESCRIPTION));
        Double price = getDouble(getColumnIndex(ProductTable.PRODUCT_PRICE));
        String barcode = getString(getColumnIndex(ProductTable.PRODUCT_BARCODE));
        return new Product(id, name, description, price, barcode);
    }
}
