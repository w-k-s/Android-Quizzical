package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Waqqas on 02/07/15.
 */
@Root(strict = false)
public class Categories implements Parcelable {

    public static final Parcelable.Creator<Categories> CREATOR = new Parcelable.Creator<Categories>() {

        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

    @ElementList(inline = true, entry = "Category")
    private List<Category> categories;

    private Categories() {
        this.categories = new ArrayList<Category>();
    }

    public Categories(List<Category> categories) {
        this.categories = categories;
    }

    public Categories(Parcel in) {
        categories = in.readArrayList(Categories.class.getClassLoader());
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categories=" + categories +
                '}';
    }
}
