package com.katalog.kozmetik.catalog.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.katalog.kozmetik.catalog.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static android.content.Context.MODE_PRIVATE;
import static com.katalog.kozmetik.catalog.Const.AD_SHOWN_LIMIT;

/**
 * Created by user on 18.03.2018.
 */

public class AdHelper {

    public static final String PREF_NAME_AD = "pref_ad";


    private Context mContext;
    private InterstitialAd mInterstitialAd;
    private int mAdCounter;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPrefs;

    public AdHelper(Context context) {
        this.mContext = context;
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.gecis_ad_id));

        loadAd();

        mPrefs = mContext.getSharedPreferences(PREF_NAME_AD, MODE_PRIVATE);
        mAdCounter = mPrefs.getInt("ad_counter", 0);
        System.out.println(mAdCounter + "wwee");
    }

    public void countAd() {
        mAdCounter++;
        mEditor = mPrefs.edit();
        mEditor.putInt("ad_counter", mAdCounter);
        mEditor.apply();

        showInterstitialAd();

    }

    private void showInterstitialAd() {


        if (mInterstitialAd.isLoaded() && mAdCounter >= AD_SHOWN_LIMIT) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    mAdCounter = 22;
                    loadAd();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    mAdCounter = 0;
                    loadAd();
                }

            });
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void loadAd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }






}
