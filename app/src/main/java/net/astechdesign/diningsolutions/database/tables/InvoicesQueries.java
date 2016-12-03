package net.astechdesign.diningsolutions.database.tables;

import android.provider.BaseColumns;

import static net.astechdesign.diningsolutions.database.tables.InvoiceTable.*;

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
