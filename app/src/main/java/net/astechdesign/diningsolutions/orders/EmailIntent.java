package net.astechdesign.diningsolutions.orders;

import android.content.Intent;
import android.net.Uri;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

public class EmailIntent extends Intent {

    public static String text(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(text1, order.invoiceNumber));
        for (OrderItem item : order.orderItems) {
            sb.append(String.format(text2, item.name, item.batch, item.quantity, item.cost()));
        }
        return sb.append(String.format(text3, order.totalCost())).toString();
    }

    private static String newLine = System.getProperty("line.separator");
    private static String text1 = "Invoice Number: %s" + newLine +
            "Produce Purchased                                          Batch No. Quantity Cost" + newLine;
    private static String text2 = "%1$-60s %2$-9s %3$8s £%4$8s " + newLine + newLine;
    private static String text3 = "Total : %s" + newLine;
}
