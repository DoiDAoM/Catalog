package com.katalog.kozmetik.catalog.registiration;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.katalog.kozmetik.catalog.App;
import com.katalog.kozmetik.catalog.Const;
import com.katalog.kozmetik.catalog.R;
import com.katalog.kozmetik.catalog.bases.BaseActivity;
import com.katalog.kozmetik.catalog.brands.Brand;
import com.katalog.kozmetik.catalog.brands.BrandsActivity;
import com.katalog.kozmetik.catalog.firebase.MyFirebaseInstanceIDService;

import org.json.JSONArray;
import org.json.JSONException;

public class SplashActivity extends BaseActivity {

    public final static String API_ALL_BRANDS = "book/brand_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LottieAnimationView animationView2 = findViewById(R.id.lottieLoadingSplash2);
        LottieAnimationView animationView3 = findViewById(R.id.lottieLoadingSplash3);
        animationView2.setAnimation("acrobatics.json");
        animationView2.loop(true);
        animationView2.playAnimation();
        animationView3.setAnimation("background.json");
        animationView3.loop(true);
        animationView3.playAnimation();


        getRequest(getApp().BASE_URL_BRANDS + API_ALL_BRANDS + "?user_id=" + getApp().currentUser.getFirebaseId());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    private void getRequest(String url) {
        getApp().brandList.clear();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Const.AD_SHOWN_LIMIT = response.getJSONObject(0).getInt("popup_ad_count");
                    Const.REWARDED_AD_LIMIT = response.getJSONObject(0).getInt("rewarded_ad_count");
                    FreeRegisterActivity.ORIFLAME_URL = response.getJSONObject(0).getString("oriflame_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i = 0 ; i < response.length() ; i++) {
                    String id;
                    String brandName;
                    String title;
                    String coverPageUrl;
                    int brandImage;
                    int pageCount;
                    int order;

                    try {
                        id = response.getJSONObject(i).getString("id");
                        brandName = response.getJSONObject(i).getString("brand");
                        title = response.getJSONObject(i).getString("title");
                        coverPageUrl = response.getJSONObject(i).getString("image_url");
                        pageCount = response.getJSONObject(i).getInt("pageCount");
                        brandImage = setupBrandImage(brandName);
                        order = response.getJSONObject(i).getInt("order");


                        getApp().brandList.add(new Brand(id, brandName, brandImage, title, coverPageUrl, pageCount, order));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Intent intent = new Intent(SplashActivity.this, BrandsActivity.class);
                startActivity(intent);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackbar(R.string.check_internet_connection, R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRequest(App.BASE_URL_BRANDS+ API_ALL_BRANDS);
                    }
                });
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getApp().queue.add(jsonObjectRequest);

    }

    private int setupBrandImage(String brandName) {
        int resource = R.drawable.logo;
     /*   if (brandName.equals("avon")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("oriflame")) {
            resource = R.drawable.image_oriflame;
        } else if (brandName.equalsIgnoreCase("gratis")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("farmasi")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("huncalife")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("cosmetica")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("pierre")) {
            resource = R.drawable.image_avon;
        } else if (brandName.equalsIgnoreCase("watsons")) {
            resource = R.drawable.image_avon;
        } else {
            resource = R.drawable.image_avon;
        }*/
        return resource;
    }

}
