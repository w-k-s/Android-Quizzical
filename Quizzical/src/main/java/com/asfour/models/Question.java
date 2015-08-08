package com.asfour.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.MoreObjects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Multiple Choice Question Objects.
 *
 * @author Waqqas
 */
@Root(strict = false)
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

    @Element(name = "ask")
    private String text;

    @Attribute(name = "correct")
    private String answer;

    @Element(name = "A")
    private String a;

    @Element(name = "B")
    private String b;

    @Element(name = "C")
    private String c;

    @Element(name = "D")
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
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("answer", answer)
                .add("a", a)
                .add("b", b)
                .add("c", c)
                .add("d", d)
                .toString();
    }
}
