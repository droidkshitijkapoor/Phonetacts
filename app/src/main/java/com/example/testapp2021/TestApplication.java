package com.example.testapp2021;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class TestApplication extends Application {
    public static final String APPLICATION_ID = "297C4B53-84C2-6828-FF80-FAA7EE445700";
    public static final String API_KEY = "D27EE815-ADFB-4CC0-8319-28C4AAC54655";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
        Backendless.setUrl(SERVER_URL);
    }
}
