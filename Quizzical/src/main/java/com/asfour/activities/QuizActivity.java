package com.asfour.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.asfour.R;
import com.asfour.api.QuizzicalApi;
import com.asfour.api.params.QuestionParameters;
import com.asfour.application.App;
import com.asfour.application.Configuration;
import com.asfour.models.Category;
import com.asfour.models.Question;
import com.asfour.models.Questions;
import com.asfour.models.Quiz;
import com.asfour.presenters.QuizPresenter;
import com.asfour.presenters.impl.QuizPresenterImpl;

import javax.inject.Inject;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;


public class QuizActivity extends BaseActivity implements QuizPresenter.OnAnswerSelectedListener {

    private static final String TAG = QuizActivity.class.getSimpleName();

    private QuizPresenter mQuizPresenter;
    private Quiz mQuiz;
    private Category mCategory;

    @Inject
    public QuizzicalApi mQuizzicalApi;

    @Inject
    public Configuration mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_question);

        App.component().inject(this);

        if (savedInstanceState != null) {

            mCategory = savedInstanceState.getParcelable(App.Extras.Category);
            mQuiz = savedInstanceState.getParcelable(App.Extras.Quiz);

        } else if (getIntent().getExtras() != null) {

            mCategory = getIntent().getExtras().getParcelable(App.Extras.Category);

        }

        if (mCategory == null) {
            finish();
        }

        mQuizPresenter = new QuizPresenterImpl(this, findViewById(android.R.id.content), mConfig);
        mQuizPresenter.setOnAnswerSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mQuiz != null) {
            resumeQuiz();
        } else {
            loadQuiz();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(App.Extras.Category, mCategory);
        outState.putParcelable(App.Extras.Quiz, mQuiz);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQuizPresenter.dismissProgress();
        mQuizPresenter.dismissDownlaodErrorDialog();

    }

    private void startQuiz() {
        mQuiz.shuffle();
        mQuizPresenter.showQuestion(mQuiz.next());
    }

    private void resumeQuiz() {

        mQuizPresenter.showQuestion(mQuiz.getCurrentQuestion());
    }

    private void loadQuiz() {

        mQuizPresenter.showProgress();

        Observable<Questions> questionsObservable = mQuizzicalApi.getQuestions(
                new QuestionParameters(mCategory,mConfig.getNumQuestionsInQuiz())
        );

        mCompositeSubscription.add(AppObservable.bindActivity(this, questionsObservable)
                .subscribe(new Action1<Questions>() {
                    @Override
                    public void call(Questions questions) {

                        mQuizPresenter.dismissProgress();
                        mQuiz = new Quiz(mCategory, questions);
                        startQuiz();
                    }

                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        Log.e(TAG, "" + throwable);

                        mQuizPresenter.dismissProgress();
                        mQuizPresenter.showDownloadingError(
                                getString(R.string.err_fetching_questions),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mQuizPresenter.dismissDownlaodErrorDialog();
                                        finish();
                                    }
                                }
                        );

                    }
                }));

    }

    @Override
    public void onAnswerSelected(String answer) {

        mQuizPresenter.showAnswers(mQuiz.getCurrentQuestion().getAnswer(), answer);

        if (mQuiz.getCurrentQuestion().checkAnswer(answer)) {
            mQuiz.incrementScore();
        }

        runAfterDelay(mConfig.getDelayBeforeNextQuestion(),mQuiz.hasNext()? mNextQuestionRunnable : mShowScoresRunnable);
    }

    private void runAfterDelay(long delay,Runnable runnable) {

        Handler handler = new Handler();
        handler.postDelayed(runnable, delay);
    }

    private Runnable mNextQuestionRunnable = new Runnable(){
        @Override
        public void run() {
            Question question = mQuiz.next();
            mQuizPresenter.showQuestion(question);
        }
    };

    private Runnable mShowScoresRunnable = new Runnable(){
        @Override
        public void run() {
            Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
            intent.putExtra(App.Extras.Score, mQuiz.getScore());

            startActivity(intent);
            finish();
        }
    };
}
