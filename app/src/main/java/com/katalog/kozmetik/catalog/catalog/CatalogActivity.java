package com.katalog.kozmetik.catalog.catalog;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
import com.katalog.kozmetik.catalog.brands.Page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class  CatalogActivity extends BaseActivity {

    public int brandPosition;

    ImageView ivGridviewCatalog, ivFullScreenCatalog;
    Toolbar toolbar;
    CardView cvFavoriesEmpty;
    TextView tvFavoriesEmpty;
    ViewPagerFragment pagerCatalogFragment;
    GridviewFragment gridviewFragment;
    private boolean isFavorite;
    public FrameLayout frLayout;

    LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews2();
        setupListeners();

        isFavorite = getIntent().getExtras().getBoolean("is_favorite");

        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("acrobatics.json");
        animationView.loop(true);
        animationView.playAnimation();

        brandPosition = getIntent().getExtras().getInt("brand_position");
        gridviewFragment = new GridviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("brand_position", brandPosition);
        gridviewFragment.setArguments(bundle);

        if (!isFavorite) {
            toolbar.setVisibility(View.VISIBLE);
            frLayout.setVisibility(View.VISIBLE);
            cvFavoriesEmpty.setVisibility(View.GONE);
            getRequestCatalog(App.BASE_URL_BRANDS + "book/" + getApp().brandList.get(brandPosition).getId());
        } else {

            toolbar.setVisibility(View.VISIBLE);
            frLayout.setVisibility(View.VISIBLE);
            cvFavoriesEmpty.setVisibility(View.GONE);
            if (getApp().currentUser.getFirebaseId() != null)
            getRequestFavorite(App.BASE_URL_BRANDS + Api.GET_FAVORITES + "user_id=" + getApp().currentUser.getFirebaseId());
        }

        pagerCatalogFragment = ViewPagerFragment.newInstance(brandPosition, getApp().selectedPage);

    }

    public int getLayoutId() {return R.layout.activity_catalog;}


    private void initViews2() {
        ivGridviewCatalog = findViewById(R.id.ivGridviewCatalog);
        ivFullScreenCatalog = findViewById(R.id.ivFullScreenCatalog);
        frLayout = findViewById(R.id.fragment_container);
        cvFavoriesEmpty = findViewById(R.id.cvFavoriesEmpty);
        tvFavoriesEmpty = findViewById(R.id.tvFavoriesEmpty);
        toolbar = findViewById(R.id.toolBar);
        animationView = findViewById(R.id.lottieLoadingCatalogs);
        ivGridviewCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_gridview_32dp));
        ivFullScreenCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_screen_activated_32dp));
    }

    private void setupListeners() {
        ivGridviewCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApp().analyticsHelper.sendEvent("grid_button", "clicked");

                replaceFragment(frLayout, gridviewFragment, false);
                ivGridviewCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_gridview_activated_32dp));
                ivFullScreenCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_screen_32dp));
            }
        });

        ivFullScreenCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getApp().analyticsHelper.sendEvent("full_screen_button", "clicked");

                replaceFragment(frLayout, pagerCatalogFragment, false);
                ivGridviewCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_gridview_32dp));
                ivFullScreenCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_screen_activated_32dp));

            }
        });
    }

    private void getRequestCatalog(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                List<Page> pageList = new ArrayList<>();
                try {
                    jsonArray = response.getJSONArray("pages");
                    for (int i = 0 ; i < jsonArray.length() ; i++) {
                        boolean isFavorite = false;
                        for (int j = 0; j < getApp().currentUser.getFavouriteList().size(); j++) {
                            if (jsonArray.getJSONObject(i).getString("id").equals(getApp().currentUser.getFavouriteList().get(j).getId())) {
                                isFavorite = true;
                                break;
                            }
                        }
                        pageList.add(new Page(jsonArray.getJSONObject(i).getString("id"),jsonArray.getJSONObject(i).getString("url"), isFavorite));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                animationView.setVisibility(View.GONE);
                animationView.pauseAnimation();


                getApp().brandList.get(brandPosition).setPageList(pageList);
                replaceFragment(frLayout, pagerCatalogFragment, false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                animationView.setVisibility(View.GONE);
                animationView.pauseAnimation();
                showSnackbar(R.string.check_internet_connection, R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getRequestCatalog(App.BASE_URL_BRANDS + "book/" + getApp().brandList.get(brandPosition).getId());
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
    private void getRequestFavorite(String url) {
        getApp().currentUser.getFavouriteList().clear();

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

                animationView.setVisibility(View.GONE);
                animationView.pauseAnimation();
                replaceFragment(frLayout, pagerCatalogFragment, false);

                ivGridviewCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_gridview_32dp));
                ivFullScreenCatalog.setImageDrawable(getResources().getDrawable(R.drawable.ic_full_screen_activated_32dp));

                if (getApp().currentUser.getFavouriteList().size() == 0) {
                    toolbar.setVisibility(View.GONE);
                    frLayout.setVisibility(View.GONE);
                    cvFavoriesEmpty.setVisibility(View.VISIBLE);
                    tvFavoriesEmpty.setText("Favori listeniz boş.");
                    animationView.setVisibility(View.GONE);
                    animationView.pauseAnimation();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackbar(R.string.check_internet_connection, R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animationView.setVisibility(View.GONE);
                        animationView.pauseAnimation();
                        getRequestFavorite(App.BASE_URL_BRANDS + Api.GET_FAVORITES + "user_id=" + getApp().currentUser.getFirebaseId());
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


    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        System.out.println(count + "wwwe");

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }


}
