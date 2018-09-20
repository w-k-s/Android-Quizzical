package com.asfour.ui.quiz

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
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
import com.asfour.utils.asVisibility
import kotlinx.android.synthetic.main.layout_question.*
import javax.inject.Inject

class QuizActivity : BaseActivity(), QuizContract.View {

    private val adapter = QuestionAdapter(onChoiceClicked = { choice ->
        if (selectionEnabled) {
            selectionEnabled = false
            quizPresenter.onQuestionAnswered(choice)
        }
    })

    @Inject lateinit var questionsRepository: QuestionsRepository
    lateinit var quizPresenter: QuizPresenter
    var selectionEnabled : Boolean = false

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
        quizPresenter = QuizPresenter(this, category!!, questionsRepository)

        quizPresenter.startQuiz()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateSpanCount()
    }

    fun updateSpanCount() {
        val layoutManager = (choicesRecycler.layoutManager as GridLayoutManager)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.spanCount = 2
        } else {
            layoutManager.spanCount = 1
        }
    }

    override fun setProgressIndicator(visible: Boolean) {
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

    fun showEmptyChoices(){
        selectionEnabled = false
        (choicesRecycler.adapter as QuestionAdapter).question = Question()
    }

    override fun showScore(quizScore: QuizScore) {
        val intent = Intent(this,ScoreActivity::class.java)
        intent.putExtra(Extras.Score, quizScore)

        startActivity(intent)
        finish()
    }

    override fun showError(message: String) {
        selectionEnabled = false
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        progressTextView.visibility = View.VISIBLE
        progressTextView.text = message
    }

    override fun onDestroy() {
        quizPresenter.dropView()
        super.onDestroy()
    }


    override fun showQuestion(question: Question) {
        selectionEnabled = true
        questionTextView.text = question.title
        adapter.question = question
    }
}

class QuestionAdapter(question: Question? = null, private val onChoiceClicked: (Choice) -> Unit) : RecyclerView.Adapter<ChoicesViewHolder>() {

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
            holder.bind(it.choices[position], onChoiceClicked)
        }
    }
}

class ChoicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val choiceTextView = itemView.findViewById<TextView>(R.id.choiceTextView)

    fun bind(choice: Choice, onChoiceClicked: (Choice) -> Unit) {
        choiceTextView.setText(choice.title)
        choiceTextView.setOnClickListener { onChoiceClicked(choice) }
    }
}
