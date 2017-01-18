package net.astechdesign.diningsolutions.database.tables;

public class DbQuery {

    private static String query = "SELECT * from %s WHERE %s = ?";

    public static String SELECT_WHERE = "SELECT * from ? WHERE ? = ?";

    public static String ORDER_ITEM_LIST = String.format(query, OrderItemTable.TABLE_NAME, "order_id");

    public static String getSelectWhere(String table, String idColumn) {
        return String.format(query, table, idColumn);
    }

    public static String CUSTOMER_ORDERS = "SELECT " +
            OrderTable.CUSTOMER_ID + ", " +
            OrderTable.INVOICE_NO + ", " +
            OrderTable.ORDER_DATE + ", " +
            OrderItemTable.ORDER_ID + ", " +
            OrderItemTable.PRODUCT_NAME + ", " +
            OrderItemTable.PRODUCT_BATCH + ", " +
            OrderItemTable.PRODUCT_PRICE + ", " +
            OrderItemTable.PRODUCT_QUANTITY + ", " +
            OrderItemTable.DELIVERY_DATE + " " +
            "from " + OrderTable.TABLE_NAME +
            " JOIN " + OrderItemTable.TABLE_NAME +
            " WHERE " + OrderTable.CUSTOMER_ID + " = ? " +
            " AND " + OrderTable._ID + " = " + OrderItemTable.ORDER_ID +
            "";
}
