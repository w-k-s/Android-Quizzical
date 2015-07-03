package com.asfour.viewmodels;

import android.widget.AdapterView;

import com.asfour.models.Categories;
import com.asfour.models.Category;

/**
 * Created by Waqqas on 02/07/15.
 */
public interface CategoryListViewModel {
    public static interface OnCategorySelectedListener{
        void onCategorySelected(Category category);
    }

    public void showProgressbar();
    public void hideProgressbar();
    public void showCategories(Categories categories);
    public void showError(String message);
    public void setOnCategorySelectedListener(OnCategorySelectedListener listener);

}
