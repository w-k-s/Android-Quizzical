package com.asfour.services;

import android.content.Context;

import com.asfour.api.QuizzicalApi;
import com.google.common.base.Preconditions;
import com.mobprofs.retrofit.converters.SimpleXmlConverter;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Waqqas on 26/06/15.
 */
public class QuizzicalService {

    private static final String QUIZZICAL_API_URL = "http://asfour-quizzical.appspot.com";

    private static com.asfour.services.QuizzicalService mInstance;
    private QuizzicalApi mQuizzicalApi;

    private QuizzicalService(final Context context) {
        Preconditions.checkNotNull(context);

        OkHttpClient okClient = ((com.asfour.application.App) context.getApplicationContext()).getOkHttpClent();

        RestAdapter mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(QUIZZICAL_API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new SimpleXmlConverter())
                .setClient(new OkClient(okClient))
                .build();

        mQuizzicalApi = mRestAdapter.create(QuizzicalApi.class);
    }

    public static QuizzicalApi getApi(final Context context) {
        Preconditions.checkNotNull(context);

        if (mInstance == null) {
            synchronized (com.asfour.services.QuizzicalService.class) {
                if (mInstance == null) {
                    mInstance = new com.asfour.services.QuizzicalService(context);
                }
            }
        }

        return mInstance.getQuizzicalApi();
    }

    private QuizzicalApi getQuizzicalApi() {
        return mQuizzicalApi;
    }

}
