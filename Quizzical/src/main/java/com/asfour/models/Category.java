package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Waqqas on 02/07/15.
 */
@Root(strict = false)
public class Category implements Parcelable {

    public static final Parcelable.Creator<Category> CREATOR
            = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Element(name = "Name")
    private String name;

    private Category() {
        this.name = "";
    }

    public Category(String name) {
        this.name = name;
    }

    public Category(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
