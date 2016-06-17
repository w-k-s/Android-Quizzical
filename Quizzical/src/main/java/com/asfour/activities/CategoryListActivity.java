package com.asfour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.asfour.Extras;
import com.asfour.R;
import com.asfour.api.QuizzicalApi;
import com.asfour.application.App;
import com.asfour.application.Configuration;
import com.asfour.models.ApiResponse;
import com.asfour.models.Category;
import com.asfour.presenters.CategoryListPresenter;
import com.asfour.presenters.CategoryListPresenter.OnCategorySelectedListener;
import com.asfour.presenters.impl.CategoryListPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
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
public class CategoryListActivity extends BaseActivity implements OnCategorySelectedListener{

    static final String TAG = CategoryListActivity.class.getSimpleName();

    private CategoryListPresenter mCategoryListPresenter;
    private List<Category> mCategories;
    private Subscription mCategoriesSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_category_list);

        App.component().inject(this);

        if (savedInstanceState != null) {
            mCategories = savedInstanceState.getParcelableArrayList(Extras.Categories);
        }
        mCategoryListPresenter = new CategoryListPresenterImpl(this, findViewById(android.R.id.content));
        mCategoryListPresenter.setOnCategorySelectedListener(this);
        mCategoryListPresenter.setShowAds(getConfig().showAds());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCategories != null) {
            mCategoryListPresenter.showCategories(mCategories);
        } else {
            loadCategories();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mCategories != null) {
            outState.putParcelableArrayList(Extras.Categories, new ArrayList<Parcelable>(mCategories));
        }
    }

    private void loadCategories() {

        mCategoryListPresenter.showProgressbar();

        Observable<ApiResponse<List<Category>>> categoriesObservable = getQuizzicalApi().getCategories();

        mCategoriesSubscription  = AppObservable.bindActivity(this, categoriesObservable)
                .subscribe(new Action1<ApiResponse<List<Category>>>() {

                    @Override
                    public void call(ApiResponse<List<Category>> categories) {

                        mCategories = categories.getData();
                        mCategoryListPresenter.hideProgressbar();
                        mCategoryListPresenter.showCategories(mCategories);
                        getSubscriptions().remove(mCategoriesSubscription);

                    }

                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {

                        String message = getString(R.string.err_fetching_categories);

                        if ((throwable instanceof RetrofitError)
                                && getStatusCode((RetrofitError) throwable) == 401) {

                            message = getString(R.string.user_message_token_expired);
                        }

                        mCategoryListPresenter.showError(message);
                        getSubscriptions().remove(mCategoriesSubscription);

                    }

                });
        getSubscriptions().add(mCategoriesSubscription);
    }

    private int getStatusCode(RetrofitError error){
        assert error != null;
        return error.getResponse().getStatus();
    }

    @Override
    public void onCategorySelected(Category category) {

        Intent intent = new Intent(CategoryListActivity.this, QuizActivity.class);
        intent.putExtra(Extras.Category, category);

        startActivity(intent);

    }
}
