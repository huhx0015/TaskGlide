package com.devhack.taskglide.interfaces;

import com.devhack.taskglide.activities.MainActivity;
import com.devhack.taskglide.module.ApplicationModule;
import com.devhack.taskglide.module.NetworkModule;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by Michael Yoon Huh on 2/11/2017.
 */

@Singleton
@Component(modules={ApplicationModule.class, NetworkModule.class})
public interface ApiComponent {
    void inject(MainActivity activity);
}