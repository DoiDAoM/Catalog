package com.katalog.kozmetik.catalog.firebase;

import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.katalog.kozmetik.catalog.App;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

/**
 * Created by user on 15.03.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    RequestQueue queue;
    public static final String PREF_NAME_FIREBASE = "pref_firebase";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        queue = Volley.newRequestQueue(this);
        ((App)getApplication()).currentUser.setFirebaseId(refreshedToken);

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(PREF_NAME_FIREBASE, MODE_PRIVATE).edit();
        editor.putString("firebase_id", refreshedToken);
        editor.apply();


        sendRegistrationToServer(App.BASE_URL_BRANDS + "user/create_user?user_id=" + refreshedToken);
    }

    private void sendRegistrationToServer(String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);

    }
}
