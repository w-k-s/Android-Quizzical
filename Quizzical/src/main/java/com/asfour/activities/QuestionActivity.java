package com.asfour.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.asfour.R;
import com.asfour.application.App;
import com.asfour.managers.ObservablesManager;
import com.asfour.models.Category;
import com.asfour.models.Question;
import com.asfour.models.Questions;
import com.asfour.models.Quiz;
import com.asfour.services.QuizzicalService;
import com.asfour.viewmodels.QuizViewModel;
import com.asfour.viewmodels.impl.QuizViewModelImpl;

import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * Displays unique random question to users. Highlights answers. Updates scores.
 * Starts ScoreActivity when user has answered all questions.
 * 
 * @author Waqqas
 * 
 */
public class QuestionActivity extends BaseActivity implements QuizViewModel.OnAnswerSelectedListener
{

	private static final long QUESTION_TRANSITION_PAUSE = 750;
    private static final String TAG = QuestionActivity.class.getSimpleName();

	private QuizViewModel mQuizViewModel;
	private Quiz mQuiz;
	private Category mCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question);

		if (savedInstanceState != null){

			mCategory = savedInstanceState.getParcelable(App.Extras.Category);
			mQuiz = savedInstanceState.getParcelable(App.Extras.Quiz);

		}else if(getIntent().getExtras() != null){

			mCategory = getIntent().getExtras().getParcelable(App.Extras.Category);

		}

		if (mCategory == null){
			finish();
		}

		mQuizViewModel = new QuizViewModelImpl(this,findViewById(android.R.id.content));
		mQuizViewModel.setOnAnswerSelectedListener(this);
    }

	@Override
	protected void onResume() {
		super.onResume();

		if (mQuiz != null){
			resumeQuiz();
		}else{
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
    protected void onPause()
    {
		super.onPause();
		mQuizViewModel.dismissProgress();
		mQuizViewModel.dismissDownlaodErrorDialog();

    }

	private void startQuiz(){
		mQuiz.shuffle();
		mQuizViewModel.showQuestion(mQuiz.next());
	}

	private void resumeQuiz(){

		mQuizViewModel.showQuestion(mQuiz.getCurrentQuestion());
	}

	private void loadQuiz(){

		Observable<Questions> observable = null;
		if(ObservablesManager.getInstance().contains(App.Observables.Questions)){
			observable = ObservablesManager.getInstance().getObservable(App.Observables.Questions);
		}else{
			observable = QuizzicalService.getApi(this).getQuestions(mCategory.getName()).cache();
			ObservablesManager.getInstance().cacheObservable(App.Observables.Questions,observable);
		}

		mQuizViewModel.showProgress();

		mCompositeSubscription.add(AppObservable.bindActivity(this,observable)
		.subscribe(new Action1<Questions>() {
			@Override
			public void call(Questions questions) {

				mQuizViewModel.dismissProgress();

				mQuiz = new Quiz(mCategory,questions);
				startQuiz();
			}
		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {

				Log.e(TAG,""+throwable);

				mQuizViewModel.dismissProgress();
				mQuizViewModel.showDownloadingError(
						getString(R.string.err_fetching_questions),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								mQuizViewModel.dismissDownlaodErrorDialog();
								finish();
							}
						}
				);

			}
		}));

	}

	@Override
	public void onAnswerSelected(String answer) {

		mQuizViewModel.showAnswers(mQuiz.getCurrentQuestion().getAnswer(),answer);

		if(mQuiz.getCurrentQuestion().checkAnswer(answer)){
			mQuiz.incrementScore();
		}

		postNextAction();
	}

	private void postNextAction(){

		Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				if (mQuiz.hasNext()) {

					Question question = mQuiz.next();
					mQuizViewModel.showQuestion(question);

				}else{

					Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
					intent.putExtra(App.Extras.Score,mQuiz.getScore());

					startActivity(intent);
					finish();
				}

			}
		}, QUESTION_TRANSITION_PAUSE);

	}


}
