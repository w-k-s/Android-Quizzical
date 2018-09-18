package com.asfour.ui.splash

import android.content.Intent
import android.os.Bundle

import com.asfour.R
import com.asfour.ui.base.BaseActivity
import com.asfour.ui.categories.CategoryListActivity

/**
 * Created by waqqas on 6/17/16.
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        startActivity(Intent(this@SplashActivity, CategoryListActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
