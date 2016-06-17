package com.asfour.activities;

import android.content.Intent;
import android.os.Bundle;

import com.asfour.R;
import com.asfour.api.QuizzicalApi;
import com.asfour.application.TokenManager;
import com.asfour.models.ApiResponse;
import com.asfour.models.Category;
import com.asfour.models.Token;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * Created by waqqas on 6/17/16.
 */
public class SplashActivity extends BaseActivity{

    private Subscription mTokenSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        acquireToken();
    }


    private void acquireToken(){
        mTokenSubscription = AppObservable.bindActivity(this,getQuizzicalApi().getSessionToken("-"))
                .subscribe(new Action1<ApiResponse<Token>>() {
                    @Override
                    public void call(ApiResponse<Token> tokenApiResponse) {
                        TokenManager.saveToken(SplashActivity.this,tokenApiResponse.getData().getToken());
                        getSubscriptions().remove(mTokenSubscription);
                        startActivity(new Intent(SplashActivity.this, CategoryListActivity.class));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        getSubscriptions().remove(mTokenSubscription);
                        //Todo.
                    }
                });
        getSubscriptions().add(mTokenSubscription);
    }
}
