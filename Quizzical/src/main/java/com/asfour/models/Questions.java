package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Waqqas on 02/07/15.
 */
@Root(strict = false)
public class Questions implements Parcelable {

    public static final Parcelable.Creator<Questions> CREATOR
            = new Parcelable.Creator<Questions>() {
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };

    @ElementList(inline = true, entry = "Question")
    private List<Question> questions;

    private Questions() {
        questions = new ArrayList<Question>();
    }

    public Questions(List<Question> questions) {
        this.questions = questions;
    }

    public Questions(final Parcel in) {
        this.questions = in.readArrayList(Question.class.getClassLoader());
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeList(questions);

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("questions", questions)
                .toString();
    }
}
