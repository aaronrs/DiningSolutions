package net.astechdesign.diningsolutions.app;

import android.content.Context;

public class ReportManager {

    private static ReportManager instance = new ReportManager();

    private Context context;

    public static void setContext(Context context) {
        instance.context = context;
    }
}
