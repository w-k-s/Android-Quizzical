package com.asfour.api.params;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Waqqas on 07/08/15.
 */
public class ApiParams extends JWTParams {

    public static final String DEFAULT_RESPONSE_FORMAT = "xml";

    public ApiParams(@NonNull Context context) {

        super(context);

    }

    @Override
    public Map<String, Object> getClaims() {

        Map<String, Object> claims = new HashMap<String, Object>();

        claims.put("format", DEFAULT_RESPONSE_FORMAT);

        return claims;
    }
}
