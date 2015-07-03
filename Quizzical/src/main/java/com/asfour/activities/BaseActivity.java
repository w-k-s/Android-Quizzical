package com.asfour.activities;

import android.app.Activity;

import com.asfour.managers.ObservablesManager;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Waqqas on 03/07/15.
 */
public class BaseActivity extends Activity {

    protected CompositeSubscription mCompositeSubscription;

    public BaseActivity() {
        super();

        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ObservablesManager.getInstance().clear();
        mCompositeSubscription.clear();
    }
}
