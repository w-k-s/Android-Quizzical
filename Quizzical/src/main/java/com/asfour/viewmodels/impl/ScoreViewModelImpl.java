package com.asfour.viewmodels.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.asfour.R;
import com.asfour.models.QuizScore;
import com.asfour.viewmodels.ScoreViewModel;

/**
 * Created by Waqqas on 03/07/15.
 */
public class ScoreViewModelImpl implements ScoreViewModel {

    private Context mContext;
    private View mView;

    private static final long MILLISECONDS_PER_TICK = 50;

    private TextView mHeadingTextView;
    private TextView mScoreTextView;
    private TextView mGradeTextView;
    private int[] mColors;

    public ScoreViewModelImpl(Context context, View view){
        mContext = context;
        mView = view;

        mColors = mContext.getResources().getIntArray(R.array.grade_colors);

        initViews();
    }

    private void initViews(){

        mHeadingTextView = (TextView) mView.findViewById(R.id.textview_score_heading);
        mScoreTextView = (TextView) mView.findViewById(R.id.textview_score);
        mGradeTextView = (TextView) mView.findViewById(R.id.textview_grade);

        Typeface headingFont = Typeface.createFromAsset(mContext.getAssets(),
                "scribble_box_demo.ttf");

        mHeadingTextView.setTypeface(headingFont);
        mGradeTextView.setTypeface(headingFont);
    }

    @Override
    public void showScore(QuizScore score) {

        double percentage = score.getPercentage();
        if (percentage == 0) percentage = 0.5;

        ScoreAnimation animateScore = new ScoreAnimation(score,
                (long) (percentage * MILLISECONDS_PER_TICK * 1.4),
                MILLISECONDS_PER_TICK);
        animateScore.start();
    }

    public class ScoreAnimation extends CountDownTimer
    {
        private static final int MAX_PERCENTAGE = 100;
        private int counter = 0;
        private QuizScore mQuizScore;

        public ScoreAnimation( QuizScore score, long millisInFuture,
                              long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
            this.mQuizScore = score;
        }

        @Override
        public void onFinish()
        {

            mGradeTextView.setText(String.format("[ %s ]", mQuizScore.getGrade()));
            mGradeTextView.setVisibility(View.VISIBLE);
            Animation scoreAnimation = AnimationUtils.loadAnimation(mContext, R.anim.score);
            mGradeTextView.startAnimation(scoreAnimation);
        }

        /**
         * Increments a counter from 0 to earned percentage Sets textview to
         * display counter. Maps percentage to colour and comment
         */
        public void onTick(long millisUntilFinished)
        {
            // map percentage to colour.
            if(counter <= (int)mQuizScore.getPercentage()){
                mScoreTextView.setTextColor(mColors[counter / (MAX_PERCENTAGE/(mColors.length - 1))]);
                mScoreTextView.setText("" + counter + "%");
                counter++;

            }
        }

    }
}
