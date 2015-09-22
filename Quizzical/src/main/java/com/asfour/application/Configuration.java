package com.asfour.application;

import android.content.Context;

import com.asfour.R;

/**
 * Created by Waqqas on 08/08/15.
 */
public class Configuration {

    private  int mNumQuestionsInQuiz;
    private  long mDelayBeforeNextQuestion;
    private  boolean mShowAds;
    private  int mDelayBeforeDisplayInterstitialAds;
    private  long mSplashScreenDuration;

    public Configuration(Context context) {

        this.mNumQuestionsInQuiz = context.getResources().getInteger(R.integer.num_questions_per_quiz);
        this.mDelayBeforeNextQuestion = context.getResources().getInteger(R.integer.delay_before_next_question);
        this.mShowAds = context.getResources().getBoolean(R.bool.show_ads);
        this.mDelayBeforeDisplayInterstitialAds = context.getResources().getInteger(R.integer.delay_before_displaying_interstitial_ads);
        this.mSplashScreenDuration = context.getResources().getInteger(R.integer.splash_screen_duration);
    }

    public  int getNumQuestionsInQuiz() {
        return mNumQuestionsInQuiz;
    }

    public  long getDelayBeforeNextQuestion() {
        return mDelayBeforeNextQuestion;
    }

    public  boolean showAds() {
        return mShowAds;
    }

    public  int getDelayBeforeDisplayInterstitialAds() {
        return mDelayBeforeDisplayInterstitialAds;
    }

    public  long getSplashScreenDuration() {
        return mSplashScreenDuration;
    }
}
