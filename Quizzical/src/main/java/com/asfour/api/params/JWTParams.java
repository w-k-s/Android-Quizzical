package com.asfour.api.params;

import android.content.Context;

import com.asfour.api.Keys;
import com.google.common.base.Preconditions;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Waqqas on 07/08/15.
 */
public abstract class JWTParams {

    private final static int TOKEN_EXPIRES_AFTER_MINUTES = 5;

    private Context context;

    public JWTParams(Context context) {

        Preconditions.checkNotNull(context);

        this.context = context;
    }

    protected String getIssuer() {
        return context.getApplicationInfo().packageName;
    }

    protected int getExpirationTimeMinutesInTheFuture() {
        return TOKEN_EXPIRES_AFTER_MINUTES;
    }

    public String getJwtId() {
        return null;
    }

    public abstract Map<String, Object> getClaims();

    private Map<String, Object> getClaimsOrEmpty() {

        Map<String, Object> claims = getClaims();
        if (claims == null) {
            return Collections.emptyMap();
        }

        return claims;
    }

    private JwtClaims getJWTClaims() {

        JwtClaims claims = new JwtClaims();
        claims.setIssuer(getIssuer());
        claims.setExpirationTimeMinutesInTheFuture(getExpirationTimeMinutesInTheFuture());
        claims.setClaim("exp", System.currentTimeMillis() + (2 * 60));
        claims.setClaim("iat", System.currentTimeMillis());

        String jwtId = getJwtId();
        if (jwtId == null || jwtId.isEmpty()) {
            claims.setGeneratedJwtId();
            ;
        }

        for (Map.Entry<String, Object> entry : getClaimsOrEmpty().entrySet()) {
            claims.setClaim(entry.getKey(), entry.getValue());
        }

        return claims;
    }

    public String generateJsonWebToken() {

        JsonWebSignature jws = new JsonWebSignature();

        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setPayload(getJWTClaims().toJson());
        jws.setKey(new HmacKey(Keys.JSON_HMAC_256_KEY.getBytes()));

        try {
            return jws.getCompactSerialization();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {

        return generateJsonWebToken();
    }
}
