package com.asfour.api.params;

import android.content.Context;
import android.support.annotation.NonNull;

import com.asfour.models.Category;
import com.google.common.base.Preconditions;

import java.util.Map;

/**
 * Created by Waqqas on 08/08/15.
 */
public class QuestionParameters extends APIParameters {

    private Category category;
    private int limit;

    public QuestionParameters(@NonNull Category category, int limit) {

        super();

        Preconditions.checkNotNull(category);
        Preconditions.checkArgument(limit > 0);

        this.category = category;
        this.limit = limit;
    }

    @Override
    public Map<String, Object> getClaims() {

        Map<String, Object> claims = super.getClaims();

        claims.put("category", this.category.getName());
        claims.put("limit", this.limit);

        return claims;
    }
}