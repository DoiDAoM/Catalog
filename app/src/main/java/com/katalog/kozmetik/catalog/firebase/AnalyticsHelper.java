package com.katalog.kozmetik.catalog.firebase;

import android.content.Context;
import android.os.Bundle;

import com.katalog.kozmetik.catalog.App;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by user on 21.03.2018.
 */

public class AnalyticsHelper {

    private FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsHelper(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        if (((App)context).currentUser.getFirebaseId() != null) {
            mFirebaseAnalytics.setUserId(((App)context).currentUser.getFirebaseId());
        }

    }

    public void sendEvent(String itemName, String contentType) {
        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void sendEvent(String brandName, String itemName, String contentType) {
        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, brandName);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void setUserId(String userID) {
        mFirebaseAnalytics.setUserId(userID);
    }


}
