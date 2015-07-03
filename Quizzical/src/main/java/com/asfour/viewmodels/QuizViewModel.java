package com.asfour.viewmodels;

import android.content.DialogInterface;

import com.asfour.models.Question;

/**
 * Created by Waqqas on 03/07/15.
 */
public interface QuizViewModel {
    public static interface OnAnswerSelectedListener {
        public void onAnswerSelected(String answer);
    }

    public void showQuestion(Question question);

    public void showAnswers(String correct, String userAnswer);

    public void setOnAnswerSelectedListener(OnAnswerSelectedListener listener);

    public void showProgress();

    public void dismissProgress();

    public void showDownloadingError(String error, DialogInterface.OnClickListener listener);

    public void dismissDownlaodErrorDialog();
}
