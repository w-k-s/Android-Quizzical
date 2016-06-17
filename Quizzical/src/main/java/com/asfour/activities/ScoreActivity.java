package com.asfour.activities;

import android.app.Activity;
import android.os.Bundle;

import com.asfour.Extras;
import com.asfour.R;
import com.asfour.application.App;
import com.asfour.application.Configuration;
import com.asfour.models.QuizScore;
import com.asfour.presenters.ScorePresenter;
import com.asfour.presenters.impl.ScorePresenterImpl;

import javax.inject.Inject;


/**
 * Displays the users score as a percentage.
 *
 * @author Waqqas
 */
public class ScoreActivity extends BaseActivity {
    private ScorePresenter mScorePresenter;
    private QuizScore mQuizScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_score);

        if (savedInstanceState != null) {
            mQuizScore = savedInstanceState.getParcelable(Extras.Score);
        } else if (getIntent().getExtras() != null) {
            mQuizScore = getIntent().getExtras().getParcelable(Extras.Score);
        }

        if (mQuizScore == null) {
            finish();
        } else {
            mScorePresenter = new ScorePresenterImpl(this, findViewById(android.R.id.content));
            mScorePresenter.setShowAds(getConfig().showAds());
            mScorePresenter.setMillisecondsDelayBeforeDisplayingAd(getConfig().getDelayBeforeDisplayInterstitialAds());
            mScorePresenter.showScore(mQuizScore);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Extras.Score, mQuizScore);
    }
}
