package io.morgan.Void;

import android.app.Application;
import android.content.Context;

/**
 * Created by mobrown on 7/11/13.
 */
public class App extends Application {
    public static final String SHARED_PREFERENCES_NAME = "Void";

    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}
