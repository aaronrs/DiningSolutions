package net.astechdesign.diningsolutions.database.tables;

public class DbQuery {
    public static final String CUSTOMER_LIST = "" +
            " SELECT * from " + CustomerTable.TABLE_NAME + ", " + AddressTable.TABLE_NAME +
            " WHERE " + CustomerTable.TABLE_NAME + "." + CustomerTable.ID + " = " + AddressTable.TABLE_NAME + "." + AddressTable.CUSTOMER_ID;
}
