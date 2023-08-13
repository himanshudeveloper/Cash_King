package com.allrecipes.recipes5.watchandearn.activity;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.Common;
import com.allrecipes.recipes5.watchandearn.App;
import com.allrecipes.recipes5.watchandearn.utils.BaseUrl;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.allrecipes.recipes5.watchandearn.utils.CustomVolleyJsonRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    public static final int RC_APP_UPDATE = 101;
    SplashActivity activity;
    String user_name = null;
    private AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("TAG", "onCreate:if part activarte ");
            appUpdateManager = AppUpdateManagerFactory.create(this);
            UpdateApp();
        } else {
            Log.e("TAG", "onCreate:else part activarte ");
            onInit();
        }
    }

    private void onInit() {
        if (Constant.isNetworkAvailable(activity)) {

            if (Constant.getString(activity, Constant.USER_BLOCKED).equals("0")) {
                Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
                return;
            }
//                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//                Constant.setString(activity, Constant.TODAY_DATE, currentDate);
//                Log.e("TAG", "onInit: else part of no login");
//                Constant.GotoNextActivity(activity, LoginActivity.class, "");
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//                finish();

            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, StartActivity.class));
                Common.openBackCustomTab(SplashActivity.this);
                finish();
            }, 4000);
            getSettingsFromAdminPannel();

        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
    }

    public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                        Log.e("TAG", "onCreate:startUpdateFlowForResult part activarte ");
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("TAG", "onCreate:startUpdateFlowForResult else part activarte ");
                    activity.onInit();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                Log.e("TAG", "onCreate:addOnFailureListener else part activarte ");
                activity.onInit();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                onInit();
            } else {
                onInit();
            }
        }
    }

    private void getSettingsFromAdminPannel() {
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


}