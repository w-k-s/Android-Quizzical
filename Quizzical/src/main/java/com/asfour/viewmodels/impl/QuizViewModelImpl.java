package com.asfour.viewmodels.impl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asfour.R;
import com.asfour.application.Configuration;
import com.asfour.models.Question;
import com.asfour.viewmodels.QuizViewModel;

/**
 * Created by Waqqas on 03/07/15.
 */
public class QuizViewModelImpl implements QuizViewModel {

    private Context mContext;
    private View mView;
    private Configuration mConfig;

    private OnAnswerSelectedListener mAnswerSelectedListener;

    private TextView mQuestionTextView;
    private Button mOptionAButton, mOptionBButton, mOptionCButton, mOptionDButton;
    private Button[] mOptionButtons;

    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;

    private View.OnClickListener mOptionSelectedListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (mAnswerSelectedListener != null) {
                mAnswerSelectedListener.onAnswerSelected(getAnswerFromView(view));
            }

        }
    };

    public QuizViewModelImpl(final Context context,
                             final View view,
                             final Configuration configuration) {

        mContext = context;
        mView = view;
        mConfig = configuration;

        initViews();
    }

    private void initViews() {

        mQuestionTextView = (TextView) mView.findViewById(R.id.tv_question);

        mOptionAButton = (Button) mView.findViewById(R.id.btn_A);
        mOptionBButton = (Button) mView.findViewById(R.id.btn_B);
        mOptionCButton = (Button) mView.findViewById(R.id.btn_C);
        mOptionDButton = (Button) mView.findViewById(R.id.btn_D);

        mOptionButtons = new Button[]{mOptionAButton, mOptionBButton, mOptionCButton, mOptionDButton};

        for (Button button : mOptionButtons) {
            button.setOnClickListener(mOptionSelectedListener);
        }
    }

    private View getViewForAnswer(String answer) {
        if (answer.equals(Question.OPTION_A)) {
            return mOptionAButton;
        } else if (answer.equals(Question.OPTION_B)) {
            return mOptionBButton;
        } else if (answer.equals(Question.OPTION_C)) {
            return mOptionCButton;
        } else if (answer.equals(Question.OPTION_D)) {
            return mOptionDButton;
        } else {
            return null;
        }
    }

    private String getAnswerFromView(View view) {

        switch (view.getId()) {
            case R.id.btn_A:
                return Question.OPTION_A;

            case R.id.btn_B:
                return Question.OPTION_B;

            case R.id.btn_C:
                return Question.OPTION_C;

            case R.id.btn_D:
                return Question.OPTION_D;

            default:
                throw new IllegalStateException(
                        String.format("Answer can not be determined for view: %s", view.getTag())
                );
        }

    }

    @Override
    public void showQuestion(Question question) {

        setButtonTextColor(mContext.getResources().getColor(R.color.white));
        toggleAnswerButtonsEnabled(true);

        mQuestionTextView.setText(question.getText());
        mOptionAButton.setText(question.getA());
        mOptionBButton.setText(question.getB());
        mOptionCButton.setText(question.getC());
        mOptionDButton.setText(question.getD());


    }

    private void highlightButtonWithAnswer(String answer, int color) {

        Button answerButton = (Button) getViewForAnswer(answer);
        if (answerButton != null) {
            highlightButton(answerButton, color);
        }
    }

    private void highlightButton(Button button, int color) {
        assert button != null;
        assert color > 0;

        button.setTextColor(color);
    }


    @Override
    public void showAnswers(String correct, String userAnswer) {

        toggleAnswerButtonsEnabled(false);

        highlightButtonWithAnswer(correct, mContext.getResources().getColor(R.color.green));
        if (!correct.equals(userAnswer)) {
            highlightButtonWithAnswer(userAnswer, mContext.getResources().getColor(R.color.red));
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

    private void toggleAnswerButtonsEnabled(boolean enable) {

        for (Button button : mOptionButtons) {
            button.setEnabled(enable);
        }
    }

    private void setButtonTextColor(int color) {

        assert color > 0;

        for (Button button : mOptionButtons) {
            button.setTextColor(color);
        }

    }
}
