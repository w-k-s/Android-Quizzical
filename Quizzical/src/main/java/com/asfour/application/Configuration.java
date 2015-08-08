package com.asfour.application;

import android.content.Context;

import com.asfour.R;

/**
 * Created by Waqqas on 08/08/15.
 */
public class Configuration {

    private static int mNumQuestionsInQuiz;
    private static long mDelayBeforeNextQuestion;
    private static boolean mShowAds;
    private static int mDelayBeforeDisplayInterstitialAds;
    private static long mSplashScreenDuration;

    public Configuration(Context context) {

        this.mNumQuestionsInQuiz = context.getResources().getInteger(R.integer.num_questions_per_quiz);
        this.mDelayBeforeNextQuestion = context.getResources().getInteger(R.integer.delay_before_next_question);
        this.mShowAds = context.getResources().getBoolean(R.bool.show_ads);
        this.mDelayBeforeDisplayInterstitialAds = context.getResources().getInteger(R.integer.delay_before_displaying_interstitial_ads);
        this.mSplashScreenDuration = context.getResources().getInteger(R.integer.splash_screen_duration);
    }

    public static int getNumQuestionsInQuiz() {
        return mNumQuestionsInQuiz;
    }

    public static long getDelayBeforeNextQuestion() {
        return mDelayBeforeNextQuestion;
    }

    public static boolean isShowAds() {
        return mShowAds;
    }

    public static int getDelayBeforeDisplayInterstitialAds() {
        return mDelayBeforeDisplayInterstitialAds;
    }

    public static long getSplashScreenDuration() {
        return mSplashScreenDuration;
    }
}
