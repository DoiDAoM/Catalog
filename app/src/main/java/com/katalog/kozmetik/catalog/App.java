package com.katalog.kozmetik.catalog;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.katalog.kozmetik.catalog.brands.Brand;
import com.katalog.kozmetik.catalog.brands.Page;
import com.katalog.kozmetik.catalog.firebase.AdHelper;
import com.katalog.kozmetik.catalog.firebase.MyFirebaseInstanceIDService;
import com.katalog.kozmetik.catalog.picasso.ImageLoader;
import com.katalog.kozmetik.catalog.firebase.AnalyticsHelper;
import com.katalog.kozmetik.catalog.registiration.User;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 1.12.2017.
 */

public class App extends Application {


    public List<Brand> brandList;
    public List<Page> favoriteList;
    public int selectedPage;
    public User currentUser;
    public AnalyticsHelper analyticsHelper;
    public RequestQueue queue;
    public AdHelper adHelper;
    public RewardedVideoAd rewardedVideoAd;

    public FirebaseAuth mAuth;

    public static final String BASE_URL = "http://cryptic-badlands-51221.herokuapp.com/book/oriflame_list";
    public static final String BASE_URL_BRANDS = "https://sleepy-lowlands-83171.herokuapp.com/";

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }



    @Override
    public void onCreate() {
        super.onCreate();


        Fabric.with(this, new Crashlytics());
        queue = Volley.newRequestQueue(this);
        currentUser = new User();
        brandList = new ArrayList<>();
        favoriteList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(this, getResources().getString(R.string.user_id_ad));

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.loadAd(getResources().getString(R.string.rewarded_ad_id), new AdRequest.Builder().build());

        SharedPreferences prefs = getSharedPreferences(MyFirebaseInstanceIDService.PREF_NAME_FIREBASE, MODE_PRIVATE);
        String userFirebaseID = prefs.getString("firebase_id", "");
        if (!userFirebaseID.equals("")) {
            currentUser.setFirebaseId(userFirebaseID);
            rewardedVideoAd.setUserId(userFirebaseID);
        }
        analyticsHelper = new AnalyticsHelper(this);
        adHelper = new AdHelper(getApplicationContext());

        selectedPage = 0;

        ImageLoader.getInstance(this);



    }


}
