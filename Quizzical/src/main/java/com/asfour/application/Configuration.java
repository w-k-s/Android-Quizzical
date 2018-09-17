package com.asfour.application;

import android.content.Context;

import com.asfour.R;

/**
 * Created by Waqqas on 08/08/15.
 */
public class Configuration {

    private  int mNumQuestionsInQuiz;
    private  long mDelayBeforeNextQuestion;

    public Configuration(Context context) {

        this.mNumQuestionsInQuiz = context.getResources().getInteger(R.integer.num_questions_per_quiz);
        this.mDelayBeforeNextQuestion = context.getResources().getInteger(R.integer.delay_before_next_question);
    }

    public  int getNumQuestionsInQuiz() {
        return mNumQuestionsInQuiz;
    }

    public  long getDelayBeforeNextQuestion() {
        return mDelayBeforeNextQuestion;
    }

}
