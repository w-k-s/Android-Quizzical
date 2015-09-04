package com.asfour.utils;

import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import retrofit.RetrofitError;

/**
 * Created by Waqqas on 04/09/15.
 */
public class ApiUtils {

    public static final String readApiErrorDescription(Throwable t){

        String description = "(null)";

        if(t instanceof RetrofitError) {
            try {
                InputStream in = ((RetrofitError) t).getResponse().getBody().in();
                description = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
                Closeables.closeQuietly(in);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return description;
    }

}
