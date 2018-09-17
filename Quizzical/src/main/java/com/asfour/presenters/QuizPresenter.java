package com.asfour.presenters;

import android.content.DialogInterface;

/**
 * Created by Waqqas on 03/07/15.
 */
public interface QuizPresenter {
    interface OnAnswerSelectedListener {
        void onAnswerSelected(String answer);
    }

    void showAnswers(String correct, String userAnswer);

    void setOnAnswerSelectedListener(OnAnswerSelectedListener listener);

    void showProgress();

    void dismissProgress();

    void showDownloadingError(String error, DialogInterface.OnClickListener listener);

    void dismissDownlaodErrorDialog();
}
