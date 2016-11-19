package net.astechdesign.diningsolutions.database.tables;

import android.provider.BaseColumns;

import static net.astechdesign.dsdcms.database.tables.InvoicesTable.*;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.CUSTOMER_NAME;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.DELIVERY_DATE;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.INVOICE_NUMBER;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.ORDER_DATE;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.PRODUCT_BATCH;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.PRODUCT_NAME;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.PRODUCT_PRICE;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.PRODUCT_QUANTITY;
import static net.astechdesign.dsdcms.database.tables.InvoicesTable.TABLE_NAME;

public class InvoicesQueries {

    public static final String GET_INVOICES =
            "SELECT DISTINCT " + BaseColumns._ID + ", "
                    + INVOICE_NUMBER + ", "
                    + CUSTOMER_NAME + ", "
                    + ORDER_DATE
                    + " FROM " + TABLE_NAME
                    + " GROUP BY "
                    + INVOICE_NUMBER + ", "
                    + CUSTOMER_NAME + ", "
                    + ORDER_DATE
            ;


    public static final String GET_INVOICE_DETAILS =
            "SELECT " + BaseColumns._ID + ", "
                    + CUSTOMER_NAME + ", "
                    + ORDER_DATE + ", "
                    + INVOICE_NUMBER + ", "
                    + PRODUCT_NAME + ", "
                    + PRODUCT_BATCH + ", "
                    + PRODUCT_QUANTITY + ", "
                    + PRODUCT_PRICE + ", "
                    + DELIVERY_DATE
                    + " FROM " + TABLE_NAME;


}
