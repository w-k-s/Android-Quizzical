package com.asfour.presenters;

import com.asfour.models.Category;

import java.util.List;

/**
 * Created by Waqqas on 02/07/15.
 */
public interface CategoryListPresenter {
    public static interface OnCategorySelectedListener {
        void onCategorySelected(Category category);
    }

    public void setShowAds(boolean showAds);

    public boolean showAds();

    public void showProgressbar();

    public void hideProgressbar();

    public void showCategories(List<Category> categories);

    public void showError(String message);

    public void setOnCategorySelectedListener(OnCategorySelectedListener listener);

}
