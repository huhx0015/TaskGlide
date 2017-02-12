package com.devhack.taskglide.application;

import android.app.Application;
import com.devhack.taskglide.constants.TaskGlideConstants;
import com.devhack.taskglide.interfaces.ApiComponent;
import com.devhack.taskglide.interfaces.DaggerApiComponent;
import com.devhack.taskglide.module.ApplicationModule;
import com.devhack.taskglide.module.NetworkModule;

public class TaskGlideApplication extends Application {

    private static final String LOG_TAG = TaskGlideApplication.class.getSimpleName();

    private ApiComponent apiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // DAGGER 2:
        apiComponent = DaggerApiComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(TaskGlideConstants.SANDBOX_URL))
                .build();

        //initPubNub();
    }

    public ApiComponent getApiComponent() {
        return apiComponent;
    }
}