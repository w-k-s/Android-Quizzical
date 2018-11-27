package com.asfour.ui.quiz

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.asfour.Extras
import com.asfour.R
import com.asfour.application.App
import com.asfour.data.categories.Category
import com.asfour.data.questions.Choice
import com.asfour.data.questions.Question
import com.asfour.data.questions.source.QuestionsRepository
import com.asfour.data.quiz.QuizScore
import com.asfour.ui.base.BaseActivity
import com.asfour.ui.score.ScoreActivity
import com.asfour.utils.ConnectivityAssistant
import com.asfour.utils.asVisibility
import kotlinx.android.synthetic.main.layout_question.*
import javax.inject.Inject

class QuizActivity : BaseActivity() {

    private val adapter = QuestionAdapter(onChoiceClicked = { choice ->
        if (selectionEnabled) {
            selectionEnabled = false
            quizViewModel.onQuestionAnswered(choice)
        }
    })

    @Inject
    lateinit var questionsRepository: QuestionsRepository
    @Inject
    lateinit var connectivityAssistant: ConnectivityAssistant
    private lateinit var quizViewModel: QuizViewModel

    private var selectionEnabled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_question)
        updateSpanCount()

        val category = intent.extras?.getParcelable(Extras.Category) as? Category
        if (category == null) {
            finish()
        }

        App.component().inject(this)

        choicesRecycler.adapter = adapter

        quizViewModel = ViewModelProviders
                .of(this, QuizViewModelFactory(application, category!!, questionsRepository, connectivityAssistant))
                .get(QuizViewModel::class.java)

        setupViewModel()

        quizViewModel.startQuiz()
    }

    private fun setupViewModel() {

        quizViewModel.loading.observe(
                this@QuizActivity,
                Observer { setProgressIndicator(it == true) }
        )
        quizViewModel.loadingError.observe(
                this@QuizActivity,
                Observer { it?.let { error -> showError(error) } }
        )
        quizViewModel.question.observe(
                this@QuizActivity,
                Observer { it?.let { question -> showQuestion(question) } }
        )
        quizViewModel.score.observe(
                this@QuizActivity,
                Observer { quizScore -> quizScore?.let { showScore(it) } }
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateSpanCount()
    }

    private fun updateSpanCount() {
        val layoutManager = (choicesRecycler.layoutManager as GridLayoutManager)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.spanCount = 2
        } else {
            layoutManager.spanCount = 1
        }
    }

    private fun setProgressIndicator(visible: Boolean) {
        progressLayout.visibility = visible.asVisibility()
        progressBar.visibility = visible.asVisibility()
        progressTextView.visibility = visible.asVisibility()
        questionTextView.visibility = (!visible).asVisibility()

        choicesRecycler.isEnabled = !visible
        if (visible) {
            showEmptyChoices()
            progressTextView.text = getString(R.string.downloading_questions)
        }
    }

    private fun showEmptyChoices() {
        selectionEnabled = false
        (choicesRecycler.adapter as QuestionAdapter).question = Question()
    }

    private fun showScore(quizScore: QuizScore) {
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra(Extras.Score, quizScore)

        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        selectionEnabled = false
        questionTextView.visibility = View.VISIBLE
        questionTextView.text = message
    }


    private fun showQuestion(question: Question) {
        selectionEnabled = true
        questionTextView.text = question.title
        adapter.question = question
    }
}

class QuestionAdapter(question: Question? = null, private val onChoiceClicked: (Choice) -> Unit) : RecyclerView.Adapter<ChoicesViewHolder>() {

    private var lockedForAnimation = false

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
        val context = holder.choiceTextView.context

        question?.let { it ->
            val choice = it.choices[position]
            val background = if (choice.correct) {
                R.drawable.bg_transition_correct
            } else {
                R.drawable.bg_transition_incorrect
            }

            holder.choiceTextView.background = ContextCompat.getDrawable(context, background)

            holder.bind(choice) {
                lockedForAnimation = true

                val itemBackground = (holder.choiceTextView.background as TransitionDrawable)
                itemBackground.startTransition(200)

                holder.choiceTextView.postDelayed({
                    lockedForAnimation = false
                    onChoiceClicked(it)
                }, 400)

            }
        }
    }
}

class ChoicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val choiceTextView : TextView = itemView.findViewById(R.id.choiceTextView)

    fun bind(choice: Choice, onChoiceClicked: (Choice) -> Unit) {
        choiceTextView.text = choice.title
        choiceTextView.setOnClickListener { onChoiceClicked(choice) }
    }
}
