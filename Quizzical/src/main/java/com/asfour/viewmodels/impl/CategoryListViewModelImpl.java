package com.asfour.viewmodels.impl;

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
import com.asfour.viewmodels.CategoryListViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Waqqas on 02/07/15.
 */
public class CategoryListViewModelImpl implements CategoryListViewModel {

    private Context mContext;
    private View mView;
    private Configuration mConfig;

    private LinearLayout mProgressLayout;
    private ProgressBar mProgressBar;
    private TextView mProgressMessage;
    private TextView mTitleTextView;

    private ListView mCategoriesListView;
    private OnCategorySelectedListener mCategorySelectedListener;

    public CategoryListViewModelImpl(final Context context,
                                     final View view,
                                     final Configuration configuration) {

        this.mContext = context;
        this.mView = view;
        this.mConfig = configuration;

        initViews();
    }

    private void initViews() {

        mTitleTextView = (TextView) mView.findViewById(R.id.textview_title);
        mProgressLayout = (LinearLayout) mView.findViewById(R.id.layout_progress);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressbar);
        mProgressMessage = (TextView) mView.findViewById(R.id.textview_progress_message);

        mTitleTextView.setTypeface(
                Typeface.createFromAsset(mContext.getAssets(), "Bender-Inline.otf")
        );
        mTitleTextView.setText(mContext.getString(R.string.app_name));

        mCategoriesListView = (ListView) mView.findViewById(R.id.listview_categories);
        mCategoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mCategorySelectedListener != null) {
                    final Category category = (Category) adapterView.getAdapter().getItem(i);
                    mCategorySelectedListener.onCategorySelected(category);
                }

            }

        });

        loadAdsIfConfigured();
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
        mProgressLayout.setVisibility(View.GONE);
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

    public void loadAdsIfConfigured() {
        if (mConfig.isShowAds()) {
            AdView adView = (AdView) mView.findViewById(R.id.ad_view);
            AdRequest adRequest = AdMobUtils.newAdRequestBuilder().build();

            adView.loadAd(adRequest);
        }
    }
}
