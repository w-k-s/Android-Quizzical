package com.asfour.activities;

import android.app.Activity;
import android.os.Bundle;

import com.asfour.R;
import com.asfour.application.App;
import com.asfour.application.Configuration;
import com.asfour.models.QuizScore;
import com.asfour.viewmodels.ScoreViewModel;
import com.asfour.viewmodels.impl.ScoreViewModelImpl;

import javax.inject.Inject;


/**
 * Displays the users score as a percentage.
 *
 * @author Waqqas
 */
public class ScoreActivity extends Activity {
    private ScoreViewModel mScoreViewModel;
    private QuizScore mQuizScore;

    @Inject
    public Configuration mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_score);

        if (savedInstanceState != null) {
            mQuizScore = savedInstanceState.getParcelable(App.Extras.Score);
        } else if (getIntent().getExtras() != null) {
            mQuizScore = getIntent().getExtras().getParcelable(App.Extras.Score);
        }

        if (mQuizScore == null) {
            finish();
        } else {
            mScoreViewModel = new ScoreViewModelImpl(this, findViewById(android.R.id.content), mConfig);
            mScoreViewModel.showScore(mQuizScore);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(App.Extras.Score, mQuizScore);
    }
}
