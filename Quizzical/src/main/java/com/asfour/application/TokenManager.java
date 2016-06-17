package com.asfour.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.asfour.models.Token;

import java.util.prefs.Preferences;

import rx.android.internal.Preconditions;

/**
 * Created by waqqas on 6/17/16.
 */
public class TokenManager {

    public static final String PREFERENCES_TOKEN = "com.asfour.quizzical.token";
    public static final String KEY_TOKEN = "token";

    public static void saveToken(final Context context, final @NonNull String token){
        Preconditions.checkNotNull(token, "Token must not be null");
        getPreferences(context).edit().putString(KEY_TOKEN, token).apply();
    }

    public static @Nullable String getToken(@NonNull final Context context){
        return getPreferences(context).getString(KEY_TOKEN,null);
    }

    private static final SharedPreferences getPreferences(@NonNull final Context context){
        return context.getSharedPreferences(PREFERENCES_TOKEN,Context.MODE_PRIVATE);
    }
}
