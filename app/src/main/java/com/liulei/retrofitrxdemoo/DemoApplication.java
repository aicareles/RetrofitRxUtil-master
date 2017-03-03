package com.liulei.retrofitrxdemoo;

import android.app.Application;
import android.content.Context;

public class DemoApplication extends Application {

    private static DemoApplication instance;

    public static Context getAppContext(){
        return instance == null ? null : instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
