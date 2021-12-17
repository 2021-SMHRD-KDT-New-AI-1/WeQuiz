package com.hjh.wequiz;

import android.app.Application;

public class MyApplication extends Application {

    private String ip;

    @Override
    public void onCreate() {
        ip = "http://e206-210-223-239-152.ngrok.io";
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
