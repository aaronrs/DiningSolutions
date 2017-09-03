package net.astechdesign.diningsolutions.app;

import android.content.Context;

public class AppManager {

    public static void setContext(Context context) {
        CustomerManager.setContext(context);
        OrderManager.setContext(context);
        ReportManager.setContext(context);
    }
}
