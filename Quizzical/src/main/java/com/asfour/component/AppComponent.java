package com.asfour.component;

import com.asfour.activities.BaseActivity;
import com.asfour.modules.ApiModule;
import com.asfour.modules.AppModule;
import com.asfour.modules.ConfigModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Waqqas on 15/07/15.
 */
@Singleton
@Component(modules = {ApiModule.class, AppModule.class, ConfigModule.class})
public interface AppComponent {

    void inject(BaseActivity categoryListActivity);
}
