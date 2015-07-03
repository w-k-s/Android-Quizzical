package com.asfour.managers;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by Waqqas on 26/06/15.
 */
public class ObservablesManager {

    private static ObservablesManager mInstance;
    private Map<String,Observable> mObservablesCache;

    private ObservablesManager(){
        mObservablesCache = new HashMap<String, Observable>();
    }

    public static ObservablesManager getInstance(){

        if ( mInstance == null ){

            synchronized(ObservablesManager.class){
                if (mInstance == null){
                    mInstance = new ObservablesManager();
                }
            }

        }

        return mInstance;
    }

    public Observable cacheObservable(final String key, final Observable observable){
        return mObservablesCache.put(key,observable);
    }

    public Observable getObservable(final String key){
        return mObservablesCache.get(key);
    }

    public boolean contains(final String key){
        return mObservablesCache.containsKey(key);
    }

    public void clear(){
        mObservablesCache.clear();
    }
}
