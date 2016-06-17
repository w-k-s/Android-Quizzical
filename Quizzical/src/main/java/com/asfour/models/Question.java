package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Multiple Choice Question Objects.
 *
 * @author Waqqas
 */
public class Question implements Parcelable {
    public static final Parcelable.Creator<Question> CREATOR
            = new Parcelable.Creator<Question>() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public static final String OPTION_A = "A";
    public static final String OPTION_B = "B";
    public static final String OPTION_C = "C";
    public static final String OPTION_D = "D";

    @SerializedName("Question")
    private String text;

    @SerializedName("Answer")
    private String answer;

    @SerializedName("A")
    private String a;

    @SerializedName("B")
    private String b;

    @SerializedName("C")
    private String c;

    @SerializedName("D")
    private String d;

    private Question() {
        this("", "", "", "", "", "");
    }

    public Question(String text,
                    String a,
                    String b,
                    String c,
                    String d,
                    String answer) {
        this.text = text;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;

    }

    public Question(Parcel in) {

        this.text = in.readString();
        this.a = in.readString();
        this.b = in.readString();
        this.c = in.readString();
        this.d = in.readString();
        this.answer = in.readString();

    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public String getA() {
        return a;
    }

    public String getB() {
        return b;
    }

    public String getC() {
        return c;
    }

    public String getD() {
        return d;
    }

    public boolean checkAnswer(String answer) {
        return this.answer.equalsIgnoreCase(answer);
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.text);
        parcel.writeString(this.a);
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.answer);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", answer='" + answer + '\'' +
                ", a='" + a + '\'' +
                ", b='" + b + '\'' +
                ", c='" + c + '\'' +
                ", d='" + d + '\'' +
                '}';
    }
}
