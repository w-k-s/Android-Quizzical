package com.asfour.presenters.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.asfour.R;
import com.asfour.data.quiz.QuizScore;
import com.asfour.presenters.ScorePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Waqqas on 03/07/15.
 */
public class ScorePresenterImpl implements ScorePresenter {

    private static final int MAX_PERCENTAGE = 100;

    private Context mContext;
    private View mView;
    private boolean mShowAds;
    private long mAdDelayMillis;

    private static final long MILLISECONDS_PER_TICK = 50;

    @BindView(R.id.textview_score_heading) TextView mHeadingTextView;
    @BindView(R.id.textview_score) TextView mScoreTextView;
    @BindView(R.id.textview_grade) TextView mGradeTextView;
    private int[] mColors;

    public ScorePresenterImpl(Context context, View view) {
        mContext = context;
        mView = view;

        mColors = mContext.getResources().getIntArray(R.array.grade_colors);

        initViews();
    }

    private void initViews() {

        ButterKnife.bind(this,mView);

        Typeface headingFont = Typeface.createFromAsset(mContext.getAssets(), "scribble_box_demo.ttf");

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

    public class ScoreAnimation extends CountDownTimer {

        private int counter = 0;
        private QuizScore mQuizScore;

        public ScoreAnimation(QuizScore score, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

            this.mQuizScore = score;
        }

        @Override
        public void onFinish() {

            animateGradeStamp(mQuizScore.getGrade());
        }

        public void onTick(long millisUntilFinished) {

            if (counter <= (int) mQuizScore.getPercentage()) {
                showPercentageWithColor(counter);
                counter++;
            }
        }
    }

    private void showPercentageWithColor(int percentage){
        mScoreTextView.setTextColor(mColors[percentage / (MAX_PERCENTAGE / (mColors.length - 1))]);
        mScoreTextView.setText("" + percentage + "%");
    }

    private void animateGradeStamp(String grade){

        mGradeTextView.setText(String.format("[ %s ]", grade));
        mGradeTextView.setVisibility(View.VISIBLE);

        Animation scoreAnimation = AnimationUtils.loadAnimation(mContext, R.anim.score);
        scoreAnimation.setAnimationListener(mGradeStampAnimationListener);

        mGradeTextView.startAnimation(scoreAnimation);

    }

    private Animation.AnimationListener mGradeStampAnimationListener = new Animation.AnimationListener(){

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) { }
    };

}
