package com.asfour.utils;

import com.asfour.BuildConfig;
import com.google.android.gms.ads.AdRequest;

/**
 * Created by Waqqas on 08/08/15.
 */
public class AdMobUtils {

    public static AdRequest.Builder newAdRequestBuilder() {
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        if (BuildConfig.DEBUG) {
            adRequestBuilder.addTestDevice("4021242AD07CBAE3C06EE3211B5B3C11");
        }

        return adRequestBuilder;
    }
}
