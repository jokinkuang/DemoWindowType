package com.jokin.framework.cwindow;

import android.app.Application;

/**
 * Created by jokin on 2018/7/10 11:54.
 */

public class CApplication extends Application {
    public static CApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static CApplication getInstance() {
        return instance;
    }
}
