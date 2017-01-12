package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

public class RepoManager {

    private static CustomerRepo cr;
    private static OrderRepo or;
    private static ProductRepo pr;
    private static TodoRepo tr;

    public static CustomerRepo getCustomerRepo(Context context) {
        if (cr == null) {
            cr = new CustomerRepo(context);
        }
        return cr;
    }

    public static OrderRepo getOrderRepo(Context context) {
        if (or == null) {
            or = new OrderRepo(context);
        }
        return or;
    }

    public static ProductRepo getProductRepo(Context context) {
        if (pr == null) {
            pr = new ProductRepo(context);
        }
        return pr;
    }

    public static TodoRepo getTodoRepo(Context context) {
        if (tr == null) {
            tr = new TodoRepo(context);
        }
        return tr;
    }
}
