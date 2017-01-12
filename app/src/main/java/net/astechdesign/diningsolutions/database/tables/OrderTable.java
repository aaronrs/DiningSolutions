package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;
import net.astechdesign.diningsolutions.repositories.OrderAssets;
import net.astechdesign.diningsolutions.repositories.RepoManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderTable extends CMSTable<Order> {

    public static final String TABLE_NAME = "orders";

    public static final String CUSTOMER_ID = "customer_id";
    public static final String ORDER_DATE = "order_date";
    public static final String INVOICE_NO = "invoice_no";

    private static String CREATE_TABLE = "" +
            "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CUSTOMER_ID + " INTEGER, " +
            ORDER_DATE + " DATE, " +
            INVOICE_NO + " TEXT " +
            ")";
    private final CustomerRepo customerRepo;
    private Context context;

    public OrderTable(Context context) {
        super(TABLE_NAME, CREATE_TABLE);
        this.context = context;
        customerRepo = RepoManager.getCustomerRepo(context);
    }

    @Override
    public void create(SQLiteDatabase db) {
        super.create(db);
        OrderItemTable orderItemTable = new OrderItemTable();
        List<Order> productList = OrderAssets.getOrders(context, customerRepo.get().get(0).id);
        for (Order order : productList) {
            ContentValues insertValues = getInsertValues(order);
            db.insert(TABLE_NAME, null, insertValues);
            for (OrderItem item : order.orderItems) {
                insertValues = orderItemTable.getInsertValues(item);
                db.insert(orderItemTable.TABLE_NAME, null, insertValues);
            }
        }
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ContentValues getInsertValues(Order order) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.CUSTOMER_ID, order.customerId);
        values.put(OrderTable.INVOICE_NO, order.invoiceNumber);
        values.put(OrderTable.ORDER_DATE, order.created.dbFormat());
        return values;
    }

    public List<Order> getOrders(SQLiteDatabase mDatabase, Customer customer) {
        List<Order> orders = new ArrayList<>();
        Cursor orderCursor = mDatabase.rawQuery(DbQuery.getSelectWhere(TABLE_NAME, CUSTOMER_ID), new String[]{Integer.toString(customer.getId())});
        orderCursor.moveToFirst();
        while (!orderCursor.isAfterLast()) {
            OrderCursorWrapper cursorWrapper = new OrderCursorWrapper(orderCursor);
            orders.add(cursorWrapper.getOrder());
            orderCursor.moveToNext();
        }
        return orders;
    }
//
//    public List<Order> getOrders(SQLiteDatabase mDatabase) {
//        List<Order> orders = new ArrayList<>();
//        Cursor orderCursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, ORDER_DATE);
//        orderCursor.moveToFirst();
//        while (!orderCursor.isAfterLast()) {
//            OrderCursorWrapper cursorWrapper = new OrderCursorWrapper(orderCursor);
//            orders.add(cursorWrapper.getOrder());
//            orderCursor.moveToNext();
//        }
//        return orders;
//    }

    public int newInvoiceNumber(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{INVOICE_NO}, null, null, null, null, INVOICE_NO + " DESC", "1");
        cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0)) + 1;
    }
}
