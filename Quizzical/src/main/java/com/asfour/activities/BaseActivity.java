package com.asfour.activities;

import android.app.Activity;

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
        mCompositeSubscription.unsubscribe();
    }
}
