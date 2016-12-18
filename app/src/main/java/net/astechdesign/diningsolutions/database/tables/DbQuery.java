package net.astechdesign.diningsolutions.database.tables;

public class DbQuery {

    private static String query = "SELECT * from %s WHERE %s = ?";

    public static String ORDER_LIST = String.format(query, OrderTable.TABLE_NAME, "customer_id");

    public static String ORDER_ITEM_LIST = String.format(query, OrderItemTable.TABLE_NAME, "order_id");
}
