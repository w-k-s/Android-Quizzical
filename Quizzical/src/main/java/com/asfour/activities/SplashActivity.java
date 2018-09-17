package com.asfour.activities;

import android.content.Intent;
import android.os.Bundle;

import com.asfour.R;
import com.asfour.ui.categories.CategoryListActivity;

/**
 * Created by waqqas on 6/17/16.
 */
public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);

        startActivity(new Intent(SplashActivity.this, CategoryListActivity.class));
    }

}
