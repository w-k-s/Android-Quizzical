package com.asfour.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by waqqas on 6/16/16.
 */
public class Token {
    @SerializedName("Token")
    private String token;

    public String getToken() {
        return token;
    }
}
