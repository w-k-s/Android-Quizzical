package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Created by Waqqas on 03/07/15.
 */
public class QuizScore implements Parcelable {

    public static final Parcelable.Creator<QuizScore> CREATOR
            = new Parcelable.Creator<QuizScore>() {
        public QuizScore createFromParcel(Parcel in) {
            return new QuizScore(in);
        }

        public QuizScore[] newArray(int size) {
            return new QuizScore[size];
        }
    };

    private int score;
    private int maxScore;

    private static final String[] GRADES = {"D","C","B","A","A+"};

    public QuizScore(int maxScore){
        this.score = 0;
        this.maxScore = maxScore;
    }

    public QuizScore(Parcel in){
        this.score = in.readInt();
        this.maxScore = in.readInt();
    }

    public void increment(){
        Preconditions.checkArgument(
                (score + 1) <= maxScore,
                "Score: %d can not exceed max: %d",score,maxScore
        );

        ++score;
    }

    public int getScore() {
        return score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public double getPercentage(){
        return 100.0 * ((double)this.score / (double)this.maxScore);
    }

    public String getGrade(){
        return GRADES[(int)getPercentage()/25];
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.score);
        parcel.writeInt(this.maxScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("score",this.score)
                .add("maxScore",this.maxScore)
                .toString();
    }
}
