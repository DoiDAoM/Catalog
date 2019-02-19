package com.katalog.kozmetik.catalog.brands;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.katalog.kozmetik.catalog.Api;
import com.katalog.kozmetik.catalog.App;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.katalog.kozmetik.catalog.registiration.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.katalog.kozmetik.catalog.firebase.AdHelper.PREF_NAME_AD;

public class BrandsActivity extends BaseActivity {



    BrandsAdapter brandsGridViewAdapter;
    ListView lvBrands;
    ImageView ivGridviewCatalog, ivFullScreenCatalog;

    int rewardAdCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvBrands = findViewById(R.id.lvBrands);
        ivGridviewCatalog = findViewById(R.id.ivGridviewCatalog);
        ivFullScreenCatalog = findViewById(R.id.ivFullScreenCatalog);
        ivGridviewCatalog.setVisibility(View.GONE);
        ivFullScreenCatalog.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences(PREF_NAME_AD, MODE_PRIVATE);
        rewardAdCounter = prefs.getInt("rewarded_ad_counter", 0);

        brandsGridViewAdapter = new BrandsAdapter(this, getApp().brandList);
        lvBrands.setAdapter(brandsGridViewAdapter);
        getApp().selectedPage = 0;


        if (getApp().currentUser.getFirebaseId() != "") {
            getRequestFavorite(App.BASE_URL_BRANDS + Api.GET_FAVORITES + "user_id=" + getApp().currentUser.getFirebaseId());
        }


    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_brands;
    }


    private void getRequestFavorite(String url) {
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("page");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String url = jsonObject.getString("url");
                        getApp().currentUser.getFavouriteList().add(new Page(id, url, true));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error volley :" + error.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getApp().queue.add(jsonObjectRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getApp().selectedPage = 0;
    }
}
