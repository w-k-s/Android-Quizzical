package com.asfour.presenters.impl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.asfour.R;
import com.asfour.presenters.QuizPresenter;

import butterknife.BindColor;

/**
 * This class has a very "primitive" and "un Object-Oriented" implementation.
 *
 * The reason for this is that the project started off as a very basic University assignment
 * which I then extended to a Quiz application (and publish on the App store).
 *
 * Created by Waqqas on 03/07/15.
 */
public class QuizPresenterImpl implements QuizPresenter {

    private Context mContext;
    private View mView;

    private OnAnswerSelectedListener mAnswerSelectedListener;

    @BindColor(R.color.green) int correctAnswerColor;
    @BindColor(R.color.red) int incorrectAnswerColor;

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    private View.OnClickListener mOptionSelectedListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (mAnswerSelectedListener != null) {
                //mAnswerSelectedListener.onAnswerSelected(getAnswerFromView(view));
            }

        }
    };

    public QuizPresenterImpl(final Context context,
                             final View view) {

        mContext = context;
        mView = view;

        initViews();
    }

    private void initViews() {

//        ButterKnife.bind(this,mView);
//        for (Button button : mOptionButtons) {
//            button.setOnClickListener(mOptionSelectedListener);
//        }
    }

    //TODO: Really really bad
//    private View getViewForAnswer(String answer) {
//        if (answer.equals(Question.OPTION_A)) {
//            return mOptionAButton;
//        } else if (answer.equals(Question.OPTION_B)) {
//            return mOptionBButton;
//        } else if (answer.equals(Question.OPTION_C)) {
//            return mOptionCButton;
//        } else if (answer.equals(Question.OPTION_D)) {
//            return mOptionDButton;
//        } else {
//            return null;
//        }
//    }
//
//    //TODO: Really really bad!
//    private String getAnswerFromView(View view) {
//
//        switch (view.getId()) {
//            case R.id.btn_A:
//                return Question.OPTION_A;
//
//            case R.id.btn_B:
//                return Question.OPTION_B;
//
//            case R.id.btn_C:
//                return Question.OPTION_C;
//
//            case R.id.btn_D:
//                return Question.OPTION_D;
//
//            default:
//                throw new IllegalStateException(
//                        String.format("Answer can not be determined for view: %s", view.getTag())
//                );
//        }
//
//    }

    private void setHighlightColorOnButtonWithAnswer(int color, String answer) {

//        Button answerButton = (Button) getViewForAnswer(answer);
//        if (answerButton != null) {
//            setHighlightColorOnButton(color, answerButton);
//        }
    }

    private void setHighlightColorOnButton(int color,Button button) {
        assert button != null;
        assert color > 0;

        button.setTextColor(color);
    }


    //TODO Really really have to make an 'Answer' class
    @Override
    public void showAnswers(String correctAnswer, String userAnswer) {

        setAnswerButtonsEnabled(false);

        setHighlightColorOnButtonWithAnswer(correctAnswerColor,correctAnswer);
        if (!correctAnswer.equals(userAnswer)) {
            setHighlightColorOnButtonWithAnswer(incorrectAnswerColor,userAnswer);
        }
    }

    @Override
    public void setOnAnswerSelectedListener(OnAnswerSelectedListener listener) {
        mAnswerSelectedListener = listener;
    }

    @Override
    public void showProgress() {

        if (mProgressDialog == null) {

            mProgressDialog = ProgressDialog.show(
                    mContext,
                    null,
                    mContext.getString(R.string.downloading_questions)
            );

        }

        mProgressDialog.show();
    }

    @Override
    public void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void showDownloadingError(String error, DialogInterface.OnClickListener listener) {

        dismissDownlaodErrorDialog();

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(mContext)
                    .setTitle(null)
                    .setMessage(error)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getString(android.R.string.ok), listener)
                    .create();
        }

        mAlertDialog.show();
    }

    @Override
    public void dismissDownlaodErrorDialog() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    private void setAnswerButtonsEnabled(boolean enable) {

//        for (Button button : mOptionButtons) {
//            button.setEnabled(enable);
//        }
    }

}
