package com.felixyan.wifiproxy;

import android.app.Application;

/**
 * Created by yanfei on 2017/07/09.
 */

public class App extends Application {
    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }
}
