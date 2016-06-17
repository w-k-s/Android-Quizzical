package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rx.android.internal.Preconditions;

/**
 * Created by Waqqas on 03/07/15.
 */
public class Quiz implements Parcelable, Iterator<Question> {

    public static final Parcelable.Creator<Quiz> CREATOR
            = new Parcelable.Creator<Quiz>() {
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    private Category category;
    private List<Question> questions;
    private QuizScore score;
    private int currentQuestionIndex;

    public Quiz(@NonNull final Category category, @NonNull final List<Question> questions) {
        Preconditions.checkNotNull(category, "Category must not be null");
        Preconditions.checkNotNull(questions,"Questions must not be null.");

        this.category = category;
        this.questions = questions;
        this.score = new QuizScore(questions.size());
        this.currentQuestionIndex = -1;

    }

    public Quiz(final Parcel in) {

        category = in.readParcelable(Category.class.getClassLoader());
        questions = new ArrayList<>();
        in.readList(questions,Question.class.getClassLoader());
        score = in.readParcelable(QuizScore.class.getClassLoader());
        currentQuestionIndex = in.readInt();
    }

    public Category getCategory() {
        return category;
    }

    public void incrementScore() {
        score.increment();
    }

    public QuizScore getScore() {
        return score;
    }

    public void reset() {
        currentQuestionIndex = -1;
    }

    public void shuffle() {
        reset();
        Collections.shuffle(questions);
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex == -1) {
            next();
        }

        return this.questions.get(currentQuestionIndex);
    }

    @Override
    public boolean hasNext() {
        return (this.currentQuestionIndex + 1) < this.questions.size();
    }

    @Override
    public Question next() {
        return this.questions.get(++currentQuestionIndex);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Quiz does not support removing questions");
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(category, flags);
        parcel.writeList(questions);
        parcel.writeParcelable(score, flags);
        parcel.writeInt(currentQuestionIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "category=" + category +
                ", questions=" + questions +
                ", score=" + score +
                ", currentQuestionIndex=" + currentQuestionIndex +
                '}';
    }
}
