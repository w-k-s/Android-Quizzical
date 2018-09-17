package com.asfour.activities;

import android.os.Bundle;

import com.asfour.Extras;
import com.asfour.R;
import com.asfour.data.quiz.QuizScore;
import com.asfour.presenters.ScorePresenter;
import com.asfour.presenters.impl.ScorePresenterImpl;


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
            mScorePresenter.showScore(mQuizScore);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Extras.Score, mQuizScore);
    }
}
