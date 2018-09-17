package com.asfour.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.asfour.Extras
import com.asfour.R
import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.quiz.Quiz
import com.asfour.presenters.QuizPresenter
import com.asfour.presenters.impl.QuizPresenterImpl
import kotlinx.android.synthetic.main.layout_question.*
import rx.Subscription
import rx.android.app.AppObservable

class QuizActivity : BaseActivity(), QuizPresenter.OnAnswerSelectedListener {

    private var mQuizPresenter: QuizPresenter? = null
    private var mQuiz: Quiz? = null
    private var mCategory: Category? = null
    private var mQuestionsSubscription: Subscription? = null

    private val adapter = QuestionAdapter(onChoiceClicked = { question, choice ->
        val question = mQuiz!!.next()
        showQuestion(question)
    })

    private val mNextQuestionRunnable = Runnable {
        val question = mQuiz!!.next()
        showQuestion(question)
    }

    private val mShowScoresRunnable = Runnable {
        val intent = Intent(this@QuizActivity, ScoreActivity::class.java)
        intent.putExtra(Extras.Score, mQuiz!!.score)

        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_question)
        updateSpanCount()

        if (savedInstanceState != null) {

            mCategory = savedInstanceState.getParcelable(Extras.Category)
            mQuiz = savedInstanceState.getParcelable(Extras.Quiz)

        } else if (intent.extras != null) {

            mCategory = intent.extras!!.getParcelable(Extras.Category)

        }

        if (mCategory == null) {
            finish()
        }

        choicesRecycler.adapter = adapter

        mQuizPresenter = QuizPresenterImpl(this, findViewById<View>(android.R.id.content))
        mQuizPresenter!!.setOnAnswerSelectedListener(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateSpanCount()
    }

    fun updateSpanCount(){
        val layoutManager = (choicesRecycler.layoutManager as GridLayoutManager)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager.spanCount = 2
        }else{
            layoutManager.spanCount = 1
        }
    }

    override fun onResume() {
        super.onResume()

        if (mQuiz != null) {
            resumeQuiz()
        } else {
            loadQuiz()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(Extras.Category, mCategory)
        outState.putParcelable(Extras.Quiz, mQuiz)
    }

    override fun onPause() {
        super.onPause()
        mQuizPresenter!!.dismissProgress()
        mQuizPresenter!!.dismissDownlaodErrorDialog()

    }

    private fun startQuiz() {
        mQuiz!!.shuffle()
        showQuestion(mQuiz!!.next())
    }

    private fun resumeQuiz() {

        showQuestion(mQuiz!!.currentQuestion)
    }

    private fun loadQuiz() {

        mQuizPresenter!!.showProgress()

        val questionsObservable = quizzicalApi.getQuestions(
                mCategory!!.title, config.numQuestionsInQuiz
        )

        mQuestionsSubscription = AppObservable.bindActivity(this, questionsObservable)
                .subscribe({ (data) ->
                    subscriptions.remove(mQuestionsSubscription)

                    mQuizPresenter!!.dismissProgress()
                    mQuiz = Quiz(mCategory!!, data)
                    startQuiz()
                }) { throwable ->
                    subscriptions.remove(mQuestionsSubscription)
                    Log.e(TAG, "" + throwable)

                    mQuizPresenter!!.dismissProgress()
                    mQuizPresenter!!.showDownloadingError(
                            getString(R.string.err_fetching_questions)
                    ) { dialogInterface, i ->
                        mQuizPresenter!!.dismissDownlaodErrorDialog()
                        finish()
                    }
                }
        subscriptions.add(mQuestionsSubscription!!)
    }

    override fun onAnswerSelected(answer: String) {

        //        mQuizPresenter.showAnswers(mQuiz.getCurrentQuestion().getAnswer(), answer);
        //
        //        if (mQuiz.getCurrentQuestion().checkAnswer(answer)) {
        //            mQuiz.incrementScore();
        //        }

        runAfterDelay(config.delayBeforeNextQuestion, if (mQuiz!!.hasNext()) mNextQuestionRunnable else mShowScoresRunnable)
    }

    private fun runAfterDelay(delay: Long, runnable: Runnable) {

        val handler = Handler()
        handler.postDelayed(runnable, delay)
    }

    fun showQuestion(question: Question) {
        questionTextView.text = question.title
        adapter.question = question
    }

    companion object {

        private val TAG = QuizActivity::class.java.simpleName
    }
}

class QuestionAdapter(question: Question? = null, private val onChoiceClicked: (Question, Choice) -> Unit) : RecyclerView.Adapter<ChoicesViewHolder>() {

    var question: Question? = question
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ChoicesViewHolder(layoutInflater.inflate(R.layout.layout_choice_item, parent, false))
    }

    override fun getItemCount(): Int = question?.choices?.count() ?: 0

    override fun onBindViewHolder(holder: ChoicesViewHolder, position: Int) {
        question?.let {
            holder.bind(it, it.choices[position], onChoiceClicked)
        }
    }
}

class ChoicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val choiceTextView = itemView.findViewById<TextView>(R.id.choiceTextView)

    fun bind(question: Question, choice: Choice, onChoiceClicked: (Question, Choice) -> Unit) {
        choiceTextView.setText(choice.title)
        choiceTextView.setOnClickListener { onChoiceClicked(question, choice) }
    }
}
