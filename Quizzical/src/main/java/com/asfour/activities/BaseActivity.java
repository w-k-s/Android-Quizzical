package com.asfour.activities;

import android.app.Activity;
import android.os.Bundle;

import com.asfour.api.QuizzicalApi;
import com.asfour.application.App;
import com.asfour.application.Configuration;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Waqqas on 03/07/15.
 */
public class BaseActivity extends Activity {

    @Inject QuizzicalApi mQuizzicalApi;
    @Inject Configuration mConfig;

    private CompositeSubscription mSubscriptions;

    public BaseActivity() {
        super();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.component().inject(this);
        mSubscriptions = new CompositeSubscription();
    }

    protected QuizzicalApi getQuizzicalApi() {
        return mQuizzicalApi;
    }

    protected Configuration getConfig() {
        return mConfig;
    }

    protected CompositeSubscription getSubscriptions(){
        return mSubscriptions;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
