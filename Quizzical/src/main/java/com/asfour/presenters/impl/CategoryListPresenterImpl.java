package com.asfour.presenters.impl;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asfour.R;
import com.asfour.application.Configuration;
import com.asfour.models.Categories;
import com.asfour.models.Category;
import com.asfour.utils.AdMobUtils;
import com.asfour.presenters.CategoryListPresenter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Waqqas on 02/07/15.
 */
public class CategoryListPresenterImpl implements CategoryListPresenter {

    private Context mContext;
    private View mView;
    private Configuration mConfig;

    @Bind(R.id.layout_progress) LinearLayout mProgressLayout;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;
    @Bind(R.id.textview_progress_message) TextView mProgressMessage;
    @Bind(R.id.textview_title) TextView mTitleTextView;
    @Bind(R.id.listview_categories) ListView mCategoriesListView;

    private OnCategorySelectedListener mCategorySelectedListener;

    public CategoryListPresenterImpl(final Context context,
                                     final View view,
                                     final Configuration configuration) {

        this.mContext = context;
        this.mView = view;
        this.mConfig = configuration;

        initViews();

        if (mConfig.showAds()){
            loadAds();
        }
    }

    private void initViews() {

        ButterKnife.bind(this,mView);

        mTitleTextView.setText(mContext.getString(R.string.app_name));
        mTitleTextView.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "Bender-Inline.otf")
        );

        mCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mCategorySelectedListener != null) {
                    final Category category = (Category) adapterView.getAdapter().getItem(i);
                    mCategorySelectedListener.onCategorySelected(category);
                }

            }

        });
    }

    @Override
    public void showProgressbar() {
        mCategoriesListView.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressMessage.setText(mContext.getString(R.string.downloading_categories));
    }

    @Override
    public void hideProgressbar() {
        mCategoriesListView.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void showError(final String message) {
        mCategoriesListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressMessage.setText(message);
    }

    @Override
    public void showCategories(final Categories categories) {

        mCategoriesListView.setAdapter(new ArrayAdapter<Category>(
                mContext,
                android.R.layout.simple_list_item_1,
                categories.getCategories()) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);

                TextView tv = ((TextView) view.findViewById(android.R.id.text1));
                tv.setText(getItem(position).getName());
                tv.setTextColor(mContext.getResources().getColor(R.color.white));

                return view;
            }
        });
    }

    @Override
    public void setOnCategorySelectedListener(OnCategorySelectedListener listener) {
        mCategorySelectedListener = listener;
    }

    public void loadAds() {

            AdView adView = (AdView) mView.findViewById(R.id.ad_view);
            AdRequest adRequest = AdMobUtils.newAdRequestBuilder().build();

            adView.loadAd(adRequest);

    }
}
