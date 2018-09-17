package com.asfour.ui.categories

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.asfour.Extras
import com.asfour.R
import com.asfour.activities.BaseActivity
import com.asfour.activities.QuizActivity
import com.asfour.application.App
import com.asfour.data.categories.Categories
import com.asfour.data.categories.Category
import retrofit.RetrofitError
import rx.Subscription

/**
 * An activity that displays a list of quiz activities.
 * Clicking on a category navigates to [QuizActivity], where the quiz questions are presented.
 *
 * @author Waqqas
 */
class CategoryListActivity : BaseActivity(), CategoriesContract.View {

    @BindView(R.id.layout_progress)
    internal var mProgressLayout: LinearLayout? = null
    @BindView(R.id.progressbar)
    internal var mProgressBar: ProgressBar? = null
    @BindView(R.id.textview_progress_message)
    internal var mProgressMessage: TextView? = null
    @BindView(R.id.textview_title)
    internal var mTitleTextView: TextView? = null
    @BindView(R.id.listview_categories)
    internal var mCategoriesListView: ListView? = null

    private var mCategoryListPresenter: CategoryListPresenter? = null
    private var mCategories: Categories? = null
    private var mCategoriesSubscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_category_list)

        App.component().inject(this)

        if (savedInstanceState != null) {
            mCategories = savedInstanceState.getParcelable(Extras.Categories)
        }
        mCategoryListPresenter = CategoriesPresenter(this, findViewById<View>(android.R.id.content))
        mCategoryListPresenter!!.setOnCategorySelectedListener(this)
    }

    override fun onResume() {
        super.onResume()

        if (mCategories != null) {
            mCategoryListPresenter!!.showCategories(mCategories!!.categories)
        } else {
            loadCategories()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (mCategories != null) {
            outState.putParcelable(Extras.Categories, mCategories)
        }
    }



    private fun getStatusCode(error: RetrofitError): Int {
        assert(error != null)
        return error.response.status
    }

    override fun onCategorySelected(category: Category) {

        val intent = Intent(this@CategoryListActivity, QuizActivity::class.java)
        intent.putExtra(Extras.Category, category)

        startActivity(intent)

    }

    companion object {

        internal val TAG = CategoryListActivity::class.java.simpleName
    }

    init {

        initViews()
    }

    private fun initViews() {

        ButterKnife.bind(this, mView)

        mTitleTextView!!.text = mContext.getString(R.string.app_name)
        mTitleTextView!!.typeface = Typeface.createFromAsset(mContext.assets, "Bender-Inline.otf")

        mCategoriesListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if (mCategorySelectedListener != null) {
                val category = adapterView.adapter.getItem(i) as Category
                mCategorySelectedListener!!.onCategorySelected(category)
            }
        }
    }

    fun showProgressbar() {
        mCategoriesListView!!.visibility = View.GONE
        mProgressLayout!!.visibility = View.VISIBLE
        mProgressMessage!!.text = mContext.getString(R.string.downloading_categories)
    }

    fun hideProgressbar() {
        mCategoriesListView!!.visibility = View.VISIBLE
        mProgressLayout!!.visibility = View.GONE
    }

    fun showError(message: String) {
        mCategoriesListView!!.visibility = View.GONE
        mProgressBar!!.visibility = View.GONE
        mProgressLayout!!.visibility = View.VISIBLE
        mProgressMessage!!.text = message
    }

    fun showCategories(categories: List<Category>) {

        mCategoriesListView!!.adapter = object : ArrayAdapter<Category>(
                mContext,
                android.R.layout.simple_list_item_1,
                categories) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view = super.getView(position, convertView, parent)

                val tv = view.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.title
                tv.setTextColor(mContext.resources.getColor(R.color.white))

                return view
            }
        }
    }

    fun setOnCategorySelectedListener(listener: OnCategorySelectedListener) {
        mCategorySelectedListener = listener
    }
}
