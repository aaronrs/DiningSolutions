package net.astechdesign.diningsolutions.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.tables.OrderItemTable;

public class QueryBuilder {

    private static String query = "SELECT * from %s WHERE %s = ?";

    public static String SELECT_WHERE = "SELECT * from ? WHERE ? = ?";

    public static String ORDER_ITEM_LIST = String.format(query, OrderItemTable.TABLE_NAME, "order_id");
    private SQLiteDatabase db;
    private String tableName;

    public QueryBuilder(SQLiteDatabase db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    public static String getSelectWhere(String table, String idColumn) {
        return String.format(query, table, idColumn);
    }

    public Cursor execute() {
        return db.query(false, null, null, null, null, null, null, null, null, null);
    }
    static QueryBuilder builder(SQLiteDatabase db, String tableName) {
        return new QueryBuilder(db, tableName);
    }

}
