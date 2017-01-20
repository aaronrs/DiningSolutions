package net.astechdesign.diningsolutions.database;

import net.astechdesign.diningsolutions.database.tables.OrderItemTable;

public class DBQuery {

    private static String query = "SELECT * from %s WHERE %s = ?";

    public static String SELECT_WHERE = "SELECT * from ? WHERE ? = ?";

    public static String ORDER_ITEM_LIST = String.format(query, OrderItemTable.TABLE_NAME, "order_id");

    public static String getSelectWhere(String table, String idColumn) {
        return String.format(query, table, idColumn);
    }

}
