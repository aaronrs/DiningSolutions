package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.net.Uri;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Distributor;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

public class EmailTemplate {

    private static String INVOICE_NUMBER = "#INVOICE_NUMBER";
    private static String INVOICE_DATE = "#DATE";
    private static String CUSTOMER_NAME = "#CUSTOMER_NAME";
    private static String CUSTOMER_ADDRESS = "#ADDRESS";
    private static String CUSTOMER_POSTCODE = "#POSTCODE";
    private static String CUSTOMER_EMAIL = "#EMAIL";
    private static String DISTRIBUTOR_NUMBER = "#DISTRIBUTOR_NUMBER";
    private static String DISTRIBUTOR_NAME = "#DISTRIBUTOR_NAME";
    private static String DISTRIBUTOR_MOBILE = "#DISTRIBUTOR_MOBILE";
    private static String ORDER_ITEMS = "#ORDER_ITEMS";

    private String template;
    private Customer customer;
    private Order order;
    private Context context;

    public EmailTemplate(Context context, String template, Customer customer, Order order) {
        this.context = context;
        this.template = template;
        this.customer = customer;
        this.order = order;
    }

    public String toString() {
        Distributor distributor = Distributor.get(context);
        template = template.replaceAll("#TAB", "/t");
        template = template.replace(INVOICE_NUMBER, order.invoiceNumber);
        template = template.replace(INVOICE_DATE, order.created.getDisplayDate());
        template = template.replace(CUSTOMER_NAME, customer.name);
        template = template.replace(CUSTOMER_ADDRESS, customer.address.toString());
        template = template.replace(CUSTOMER_POSTCODE, customer.address.postcode);
        template = template.replace(CUSTOMER_EMAIL, customer.email.address);
        template = template.replace(DISTRIBUTOR_NUMBER, distributor.number);
        template = template.replace(DISTRIBUTOR_NAME, distributor.name);
        template = template.replace(DISTRIBUTOR_MOBILE, distributor.mobile);

        StringBuilder sb = new StringBuilder();
        for (OrderItem item : order.orderItems) {
            sb.append(String.format(text2, item.name, item.batch, item.quantity, item.cost()));
        }
        sb.append(String.format(text3, order.totalCost()));
        template = template.replace(ORDER_ITEMS, sb.toString());

        String mailto = "mailto:" + customer.email.address +
                "?cc=" + distributor.email +
                "&subject=" + Uri.encode("Dining Solutions Direct - Invoice : " + order.invoiceNumber) +
                "&body=" + Uri.encode(template);

        return mailto;

    }

    private static String newLine = System.getProperty("line.separator");
    private static String text2 = "%1$-60s %2$-9s %3$8s £%4$8s " + newLine;
    private static String text3 = "                            Total : %s" + newLine;
}
