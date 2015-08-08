package com.asfour.activities;

import android.content.Intent;
import android.os.Bundle;

import com.asfour.R;
import com.asfour.api.QuizzicalApi;
import com.asfour.api.params.CategoryParams;
import com.asfour.application.App;
import com.asfour.application.Configuration;
import com.asfour.managers.ObservablesManager;
import com.asfour.models.Categories;
import com.asfour.models.Category;
import com.asfour.viewmodels.CategoryListViewModel;
import com.asfour.viewmodels.CategoryListViewModel.OnCategorySelectedListener;
import com.asfour.viewmodels.impl.CategoryListViewModelImpl;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * An activity that displays a list of quiz activities.
 * Clicking on a category navigates to {@link QuizActivity}, where the quiz questions are presented.
 *
 * @author Waqqas
 */
public class CategoryListActivity extends BaseActivity {

    static final String TAG = CategoryListActivity.class.getSimpleName();

    private CategoryListViewModel mCategoryListViewModel;
    private Categories mCategories;
    private Subscription mCategoriesSubscription;

    @Inject
    public QuizzicalApi mQuizzicalApi;

    @Inject
    public Configuration mConfig;

    private OnCategorySelectedListener mCategorySelectedListener = new OnCategorySelectedListener() {

        @Override
        public void onCategorySelected(Category category) {

            Intent intent = new Intent(CategoryListActivity.this, QuizActivity.class);
            intent.putExtra(App.Extras.Category, category);

            startActivity(intent);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category_list);

        App.component().inject(this);

        if (savedInstanceState != null) {
            mCategories = savedInstanceState.getParcelable(App.Extras.Categories);
        }

        mCategoryListViewModel = new CategoryListViewModelImpl(this, findViewById(android.R.id.content), mConfig);
        mCategoryListViewModel.setOnCategorySelectedListener(mCategorySelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCategories != null) {
            mCategoryListViewModel.showCategories(mCategories);
        } else {
            loadCategories();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(App.Extras.Categories, mCategories);
    }

    private void loadCategories() {

        Observable<Categories> observable = null;
        if (ObservablesManager.getInstance().contains(App.Observables.Categories)) {
            observable = ObservablesManager.getInstance().getObservable(App.Observables.Categories);
        } else {
            observable = mQuizzicalApi.getCategories(new CategoryParams(this)).cache();
            ObservablesManager.getInstance().cacheObservable(App.Observables.Categories, observable);
        }

        mCategoryListViewModel.showProgressbar();
        mCategoriesSubscription = AppObservable.bindActivity(this, observable)
                        .subscribe(new Action1<Categories>() {

                            @Override
                            public void call(Categories categories) {

                                mCategories = categories;
                                mCategoryListViewModel.hideProgressbar();
                                mCategoryListViewModel.showCategories(categories);
                                mCompositeSubscription.remove(mCategoriesSubscription);
                            }

                        }, new Action1<Throwable>() {

                            @Override
                            public void call(Throwable throwable) {

                                mCategoryListViewModel.hideProgressbar();
                                mCategoryListViewModel.showError(getString(R.string.err_fetching_categories));
                                mCompositeSubscription.remove(mCategoriesSubscription);

                            }

                        });
        mCompositeSubscription.add(mCategoriesSubscription);
    }
}
