package com.asfour.ui.categories

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.asfour.Extras
import com.asfour.R
import com.asfour.application.App
import com.asfour.data.categories.Categories
import com.asfour.data.categories.Category
import com.asfour.data.categories.source.CategoriesRepository
import com.asfour.ui.base.BaseActivity
import com.asfour.ui.quiz.QuizActivity
import com.asfour.utils.ConnectivityAssistant
import com.asfour.utils.asVisibility
import kotlinx.android.synthetic.main.layout_category_list.*
import javax.inject.Inject

/**
 * An activity that displays a list of quiz activities.
 * Clicking on a category navigates to [QuizActivity], where the quiz questions are presented.
 *
 * @author Waqqas
 */
class CategoryListActivity : BaseActivity(), CategoriesContract.View {

    @Inject
    lateinit var categoriesRepository: CategoriesRepository

    @Inject
    lateinit var connectionAssistant: ConnectivityAssistant

    private var categoriesPresenter: CategoriesContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_category_list)

        App.component().inject(this)

        categoriesPresenter = CategoriesPresenter(this, categoriesRepository,connectionAssistant)

        initViews()
    }


    private fun initViews() {
        titleTextView.text = getString(R.string.app_name)
        categoriesRecyclerView.adapter = CategoriesAdapter(onCategorySelected = { category ->
            categoriesPresenter?.onCategorySelected(category)
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateSpanCount()
    }

    fun updateSpanCount(){
        val layoutManager = (categoriesRecyclerView.layoutManager as GridLayoutManager)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager.spanCount = 3
        }else{
            layoutManager.spanCount = 1
        }
    }

    override fun onResume() {
        super.onResume()
        categoriesPresenter?.loadCategories()
    }

    override fun onDestroy() {
        categoriesPresenter?.dropView()
        super.onDestroy()
    }

    override fun showCategories(categories: Categories) {
        (categoriesRecyclerView.adapter as CategoriesAdapter).categories = categories
    }

    override fun startQuiz(category: Category) {

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(Extras.Category, category)

        startActivity(intent)
    }

    override fun setProgressIndicator(visible: Boolean) {

        categoriesRecyclerView.visibility = (!visible).asVisibility()
        progressLayout.visibility = (visible).asVisibility()

        progressTextView.text = if (visible) {
            getString(R.string.downloading_categories)
        } else {
            ""
        }
    }

    override fun showError(message: String) {
        categoriesRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE
        progressTextView.text = message
    }
}

class CategoriesAdapter(categories: Categories = Categories(),
                        private val onCategorySelected: (Category) -> Unit) : RecyclerView.Adapter<CategoryViewHolder>() {

    var categories: Categories = categories
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) = holder.bind(categories[position], onCategorySelected)

}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: Category, onCategorySelected: (Category) -> Unit) {
        (itemView as TextView).let {
            it.text = category.title
            it.typeface = ResourcesCompat.getFont(itemView.context, R.font.architects_daughter)
            it.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            it.setOnClickListener({
                onCategorySelected(category)
            })
        }
    }

}
