package com.asfour.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.asfour.R;
import com.asfour.application.App;
import com.asfour.managers.ObservablesManager;
import com.asfour.models.Categories;
import com.asfour.models.Category;
import com.asfour.services.QuizzicalService;
import com.asfour.viewmodels.CategoryListViewModel;
import com.asfour.viewmodels.CategoryListViewModel.OnCategorySelectedListener;
import com.asfour.viewmodels.impl.CategoryListViewModelImpl;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Downloads categories, displays them to the user as a list.
 * 
 * @author Waqqas
 * 
 */
public class CategoryListActivity extends BaseActivity
{
    static final String TAG = CategoryListActivity.class.getSimpleName();

	private CategoryListViewModel mCategoryListViewModel;
    private Categories mCategories;

	private OnCategorySelectedListener mCategorySelectedListener = new OnCategorySelectedListener() {

		@Override
		public void onCategorySelected(Category category) {

			Intent intent = new Intent(CategoryListActivity.this,QuestionActivity.class);
			intent.putExtra(App.Extras.Category,category);

			startActivity(intent);
		}

	};



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_category_list);

		if (savedInstanceState != null){
			mCategories = savedInstanceState.getParcelable(App.Extras.Categories);
		}

		mCategoryListViewModel = new CategoryListViewModelImpl(this,findViewById(android.R.id.content));
    	mCategoryListViewModel.setOnCategorySelectedListener(mCategorySelectedListener);
	}

    @Override
    protected void onResume()
    {
        super.onResume();

		if (mCategories != null){
			mCategoryListViewModel.showCategories(mCategories);
		}else{
			loadCategories();
		}
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable(App.Extras.Categories, mCategories);
	}

	private void loadCategories(){

		Observable<Categories> observable = null;
		if (ObservablesManager.getInstance().contains(App.Observables.Categories)){
			observable = ObservablesManager.getInstance().getObservable(App.Observables.Categories);
		}else{
			observable = QuizzicalService.getApi(this).getCategories().cache();
			ObservablesManager.getInstance().cacheObservable(App.Observables.Categories,observable);
		}

		mCategoryListViewModel.showProgressbar();
		mCompositeSubscription.add(AppObservable.bindActivity(this, observable)
				.subscribe(new Action1<Categories>() {

					@Override
					public void call(Categories categories) {

						mCategories = categories;
						mCategoryListViewModel.hideProgressbar();
						mCategoryListViewModel.showCategories(categories);

					}

				}, new Action1<Throwable>() {

					@Override
					public void call(Throwable throwable) {

						mCategoryListViewModel.hideProgressbar();
						mCategoryListViewModel.showError(getString(R.string.err_fetching_categories));

					}

				}));


	}


}
