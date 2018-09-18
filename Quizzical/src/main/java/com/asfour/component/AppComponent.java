package com.asfour.component;

import com.asfour.modules.DataSourceModule;
import com.asfour.modules.DatabaseModule;
import com.asfour.modules.RepositoryModule;
import com.asfour.ui.base.BaseActivity;
import com.asfour.modules.ApiModule;
import com.asfour.modules.AppModule;
import com.asfour.ui.categories.CategoryListActivity;
import com.asfour.ui.quiz.QuizActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Waqqas on 15/07/15.
 */
@Singleton
@Component(modules = {
        ApiModule.class,
        AppModule.class,
        DataSourceModule.class,
        RepositoryModule.class,
        DatabaseModule.class
})
public interface AppComponent {
    void inject(CategoryListActivity categoryListActivity);
    void inject(QuizActivity categoryListActivity);
}
