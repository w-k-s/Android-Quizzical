package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import java.util.Collections;
import java.util.Iterator;

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
    private Questions questions;
    private QuizScore score;
    private int currentQuestionIndex;

    public Quiz(Category category, Questions questions) {

        this.category = category;
        this.questions = questions;
        this.score = new QuizScore(questions.getQuestions().size());
        this.currentQuestionIndex = -1;

    }

    public Quiz(final Parcel in) {

        category = in.readParcelable(Category.class.getClassLoader());
        questions = in.readParcelable(Question.class.getClassLoader());
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
        Collections.shuffle(questions.getQuestions());
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex == -1) {
            next();
        }

        return this.questions.getQuestions().get(currentQuestionIndex);
    }

    @Override
    public boolean hasNext() {
        return (this.currentQuestionIndex + 1) < this.questions.getQuestions().size();
    }

    @Override
    public Question next() {
        return this.questions.getQuestions().get(++currentQuestionIndex);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Quiz does not support removing questions");
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(category, flags);
        parcel.writeParcelable(questions, flags);
        parcel.writeParcelable(score, flags);
        parcel.writeInt(currentQuestionIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("category", this.category)
                .add("questions", this.questions)
                .add("score", score)
                .add("currentQuestionIndex", currentQuestionIndex)
                .toString();
    }
}
