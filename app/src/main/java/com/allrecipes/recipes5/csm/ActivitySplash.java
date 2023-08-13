package com.allrecipes.recipes5.csm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.PrefManager;
import com.allrecipes.recipes5.watchandearn.App;
import com.allrecipes.recipes5.watchandearn.utils.BaseUrl;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.allrecipes.recipes5.watchandearn.utils.CustomVolleyJsonRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.startapp.sdk.adsbase.StartAppAd;
import com.allrecipes.recipes5.R;


import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.allrecipes.recipes5.helper.Constatnt.AD_UNIT_DATA_GET;
import static com.allrecipes.recipes5.helper.PrefManager.A;
import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;


public class ActivitySplash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.splace));
        }
        setContentView( R.layout.activity_splash);
        View overlay = findViewById(R.id.mylayout);
        StartAppAd.disableSplash();
        ConnectivityManager ConnectionManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        Paper.init(this);


            if (networkInfo != null && networkInfo.isConnected()) {
                new Handler().postDelayed(new Runnable() {@Override public void run() {
                    A(ActivitySplash.this);
                }}, SPLASH_TIME_OUT);
            } else {
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "No Network connection..!!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = getIntent();intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);}});snackbar.show();
            }


        if (ActivityCompat.checkSelfPermission(ActivitySplash.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivitySplash.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 111333);
            return;
        }

        getSettingsFromAdminPannel();
        getAdsData();
    }


    protected void onResume() {
        super.onResume();
    }

    public boolean vpn() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if ( iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void getSettingsFromAdminPannel() {
        Activity activity = ActivitySplash.this;
        if (Constant.isNetworkAvailable(activity)) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.ADMIN_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "onResponse: " + response);
                            boolean status = response.getBoolean("status");
                            if (status) {
                                JSONObject jb = response.getJSONObject("0");
                                Constant.setString(activity, Constant.ADS_BEETWEEN, jb.getString("ads_between"));
                                Constant.setString(activity, Constant.DAILY_SCRATCH_COUNT, jb.getString("daily_scratch_limit"));
                                Constant.setString(activity, Constant.DAILY_SPIN_COUNT, jb.getString("daily_spin_limit"));
                                Constant.setString(activity, Constant.DAILY_CHECK_IN_POINTS, jb.getString("daily_check_in_points"));
                                Constant.setString(activity, Constant.COIN_TO_RUPEE, jb.getString("coin_to_rupee_text"));
                                Constant.setString(activity, Constant.DAILY_CAPTCHA_COUNT, jb.getString("daily_captcha_limit"));
                                Constant.setString(activity, Constant.MINIMUM_REDEEM_POINTS, jb.getString("minimum_redeem_points"));
                                Constant.setString(activity, Constant.AD_TYPE, jb.getString("ad_type"));

                                Constant.setString(activity, Constant.ADMOB_BANNER_ID, jb.getString("admob_banner_id"));
                                Constant.setString(activity, Constant.ADMOB_INTERSTITAL_ID, jb.getString("admob_interstital_id"));
                                Constant.setString(activity, Constant.ADMOB_REWARDED_ID, jb.getString("admob_rewarded_id"));

                                Constant.setString(activity, Constant.UNITY_BANNER_ID, jb.getString("unity_banner_id"));
                                Constant.setString(activity, Constant.UNITY_INTERSTITAL_ID, jb.getString("unity_interstital_id"));
                                Constant.setString(activity, Constant.UNITY_REWARDED_ID, jb.getString("unity_rewarded_id"));

                                Constant.setString(activity, Constant.STARTAPP_BANNER_ID, jb.getString("startapp_banner_id"));
                                Constant.setString(activity, Constant.STARTAPP_INTERSTITAL_ID, jb.getString("startapp_interstital_id"));
                                Constant.setString(activity, Constant.REFER_TEXT, jb.getString("refer_text"));


                                Constant.setString(activity, Constant.SPIN_PRICE_COIN, jb.getString("spin_price_coins"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN, jb.getString("scratch_price_coins"));
                                Constant.setString(activity, Constant.CAPTCHA_PRICE_COIN, jb.getString("captcha_price_coins"));
                                Constant.setString(activity, Constant.SIGNUP_BOUNUS, jb.getString("signup_points"));
                                Constant.setString(activity, Constant.UNITY_GAME_ID, jb.getString("unity_game_id"));
                                Constant.setString(activity, Constant.STARTAPP_APP_ID, jb.getString("startapp_app_id"));
                                Constant.setString(activity, Constant.ADMOB_APP_ID, jb.getString("admob_app_id"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN_PLATINUM, jb.getString("scratch_coin_platinum"));
                                Constant.setString(activity, Constant.SCRATCH_PRICE_COIN_GOLD, jb.getString("scratch_coin_gold"));
                                Constant.setString(activity, Constant.APPLOVIN_BANNER_ID, jb.getString("applovin_banner_id"));
                                Constant.setString(activity, Constant.APPLOVIN_INTERSTITAL_ID, jb.getString("applovin_interstital_id"));
                                Constant.setString(activity, Constant.APPLOVIN_REWARDED_ID, jb.getString("applovin_rewarded_id"));


                            } else {
                                Constant.showToastMessage(activity, "No Settings Found In Admin Pannel");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.showToastMessage(activity, "Something Went Wrong Try Again");
                        }
                    }
                }, error -> {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Constant.showToastMessage(activity, getResources().getString(R.string.slow_internet_connection));
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000 * 20,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                App.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Admin Settings: excption " + e.getMessage().toString());
            }
        } else {
            Constant.showToastMessage(activity, "No internet Connection");
        }
    }

    public void getAdsData() {
        Activity activity = ActivitySplash.this;
        StringRequest request = new StringRequest(Request.Method.GET, AD_UNIT_DATA_GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    PrefManager.setString(activity, PrefManager.UNITY_BANNER_ID, jsonObject.getString("unity_banner_id"));
                    PrefManager.setString(activity, PrefManager.APPLOVIN_BANNER_ID, jsonObject.getString("applovin_banner_id"));
                    PrefManager.setString(activity, PrefManager.ADCOLONY_BANNER_ID, jsonObject.getString("adcolony_banner_id"));
                    PrefManager.setString(activity, PrefManager.VUNGLE_BANNER_ID, jsonObject.getString("vungle_banner_id"));

                    PrefManager.setString(activity, PrefManager.BANNER_AD_TYPE, jsonObject.getString("banner_ad_type"));
                    PrefManager.setString(activity, PrefManager.INTERSTITAL_AD_TYPE, jsonObject.getString("interstital_ad_type"));
                    PrefManager.setString(activity, PrefManager.REWARDED_AD_TYPE, jsonObject.getString("rewarded_ad_type"));

                    PrefManager.setString(activity, PrefManager.APPLOVIN_INTERSTITAL_ID, jsonObject.getString("applovin_interstital_id"));
                    PrefManager.setString(activity, PrefManager.UNITY_INTERSTITAL_ID, jsonObject.getString("unity_interstital_id"));
                    PrefManager.setString(activity, PrefManager.ADCOLONY_INTERSTITAL_ID, jsonObject.getString("adcolony_interstital_id"));
                    PrefManager.setString(activity, PrefManager.VUNGLE_INTERSTITAL_ID, jsonObject.getString("vungle_interstital_id"));

                    PrefManager.setString(activity, PrefManager.ADMOB_REWARDED_ID, jsonObject.getString("admob_rewarded_id"));
                    PrefManager.setString(activity, PrefManager.VUNGLE_REWARDED_ID, jsonObject.getString("vungle_rewarded_id"));
                    PrefManager.setString(activity, PrefManager.APPLOVIN_REWARDED_ID, jsonObject.getString("applovin_rewarded_id"));
                    PrefManager.setString(activity, PrefManager.UNITY_REWARDED_ID, jsonObject.getString("unity_rewarded_id"));
                    PrefManager.setString(activity, PrefManager.ADCOLONY_REWARDED_ID, jsonObject.getString("adcolony_rewarded_id"));

                    PrefManager.setString(activity, PrefManager.IRON_SOURCE_APP_KEY, jsonObject.getString("iron_source_app_key"));
                    PrefManager.setString(activity, PrefManager.YODO_APP_KEY, jsonObject.getString("yodo_app_key"));
                    PrefManager.setString(activity, PrefManager.CHARTBOOST_APP_ID, jsonObject.getString("chartboost_app_id"));
                    PrefManager.setString(activity, PrefManager.CHARTBOOST_APP_SIGNATURE, jsonObject.getString("chartboost_app_signature"));
                    PrefManager.setString(activity, PrefManager.VUNGLE_APP_ID, jsonObject.getString("vungle_app_id"));
                    PrefManager.setString(activity, PrefManager.ADCOLONY_APP_ID, jsonObject.getString("adcolony_app_id"));
                    PrefManager.setString(activity, PrefManager.UNITY_GAME_ID, jsonObject.getString("unity_game_id"));
                    PrefManager.setString(activity, PrefManager.START_IO_APP_ID, jsonObject.getString("start_io_app_id"));
                    PrefManager.setString(activity, PrefManager.ADMOB_APP_ID, jsonObject.getString("admob_app_id"));

                    PrefManager.setString(activity, PrefManager.APPLOVIN_NATIVE_ID, jsonObject.getString("applovin_native_id"));

                    Paper.book().write("ShouldShowScratchSection", jsonObject.getString("show_scratch_section"));
                    Paper.book().write("BackPressUrl", jsonObject.getString("back_press_url"));
                    Paper.book().write("ShouldShowRewardsSection", jsonObject.getString("show_rewards_section"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

}

