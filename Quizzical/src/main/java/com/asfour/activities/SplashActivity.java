package com.asfour.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.asfour.api.QuizzicalApi;
import com.asfour.models.Categories;
import com.asfour.R;
import com.asfour.services.QuizzicalService;

import rx.android.app.AppObservable;
import rx.functions.Action1;

/**Splashscreen displayed when application is first launched.
 * 
 * @author Waqqas
 *
 */
public class SplashActivity extends Activity {

	private static final int SPLASH_DURATION = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// get full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// set layout
		setContentView(R.layout.layout_splash);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		//when app is paused, finish this activity.
		this.finish();
	}

	
}
