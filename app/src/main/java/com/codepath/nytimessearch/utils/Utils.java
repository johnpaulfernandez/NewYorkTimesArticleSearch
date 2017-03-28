package com.codepath.nytimessearch.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class Utils {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
