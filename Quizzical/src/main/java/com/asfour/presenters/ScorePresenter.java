package com.asfour.presenters;

import com.asfour.models.QuizScore;

/**
 * Created by Waqqas on 03/07/15.
 */
public interface ScorePresenter {

    boolean showAds();
    void setShowAds(boolean showAds);
    void setMillisecondsDelayBeforeDisplayingAd(long millisecondsDelay);
    long getMillisecondsDelayBeforeDisplayingAd();
    void showScore(QuizScore score);
}
