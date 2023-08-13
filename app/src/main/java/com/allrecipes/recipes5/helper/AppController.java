package com.allrecipes.recipes5.helper;

import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.AD_UNIT_DATA_GET;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.EMAIL;
import static com.allrecipes.recipes5.helper.Constatnt.GET_USER;
import static com.allrecipes.recipes5.helper.Constatnt.ID;
import static com.allrecipes.recipes5.helper.Constatnt.NAME;
import static com.allrecipes.recipes5.helper.Constatnt.POINTS;
import static com.allrecipes.recipes5.helper.Constatnt.REFER_CODE;
import static com.allrecipes.recipes5.helper.Constatnt.STATTUS;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;
import static com.allrecipes.recipes5.helper.PrefManager.ADCOLONY_APP_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.watchandearn.App;
import com.adcolony.sdk.AdColony;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.chartboost.sdk.Chartboost;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.vungle.warren.InitCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Hetal on 05-Jan-18.
 */

public class AppController extends App {

    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    public static final String TAG = AppController.class.getSimpleName();


    private SharedPreferences sharedPref;

    private String id, status, fcm_id;
    private String username, fullname, accessToken, email, ip_addr, points, phone, password, refercodee, badge, image, rank;
    public static ProgressDialog pDialog;
    private ImageLoader mImageLoader;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPref = this.getSharedPreferences(getString(R.string.settings_file), Context.MODE_PRIVATE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.one_signal_app_id));
        this.readData();
        FirebaseApp.initializeApp(this);
    }

    public static void initializeAdNetworks() {
        UnityAds.initialize(getInstance(), PrefManager.getSavedString(getInstance(), PrefManager.UNITY_GAME_ID),
                getInstance().getResources().getBoolean(R.bool.test_mode), new IUnityAdsInitializationListener() {
                    @Override
                    public void onInitializationComplete() {

                    }

                    @Override
                    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

                    }
                });
        AudienceNetworkAds.initialize(getInstance());
        AppLovinSdk.getInstance(getInstance()).setMediationProvider("max");
        AppLovinSdk.initializeSdk(getInstance(), configuration -> {
            AppLovinSdk.getInstance(getInstance()).getSettings().setTestDeviceAdvertisingIds(Arrays.asList("2f7bcab5-934e-4455-9e3b-10calad1abe1"));
        });
        StartAppSDK.init(getInstance(), PrefManager.getSavedString(getInstance(), PrefManager.START_IO_APP_ID), true);
        StartAppAd.disableSplash();
        StartAppSDK.setUserConsent(getInstance(),
                "pas",
                System.currentTimeMillis(),
                false);

        AdColony.configure(getInstance(), PrefManager.getSavedString(getInstance(), ADCOLONY_APP_ID));

        Vungle.init(PrefManager.getSavedString(getInstance(), PrefManager.VUNGLE_APP_ID), getInstance(), new InitCallback() {
            @Override
            public void onSuccess() {
                // SDK has successfully initialized
            }

            @Override
            public void onError(VungleException exception) {
                // SDK has failed to initialize
            }

            @Override
            public void onAutoCacheAdAvailable(String placementId) {
                // Ad has become available to play for a cache optimized placement
            }
        });

        Chartboost.startWithAppId(getInstance(), PrefManager.getSavedString(getInstance(), PrefManager.CHARTBOOST_APP_ID),
                PrefManager.getSavedString(getInstance(),
                        PrefManager.CHARTBOOST_APP_SIGNATURE),
                startError -> {
                    if (startError != null) {
                        Log.e(TAG, "initializeAdNetworks: " + startError.getException());
                    }
                });
        MobileAds.initialize(getInstance(), initializationStatus -> {

        });
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public Boolean signup_authorize(JSONObject authObj) {
        try {
            Boolean ret = false;
            if (authObj.getString("error").toString().equalsIgnoreCase("true")) {

                return false;
            }
            if (authObj.has("id")) {
                this.setId(authObj.getString(ID));

                this.saveData();


                JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if (AppController.getInstance().authorize(response)) {

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();


                        System.out.println("send**" + AppController.getInstance().getId());
                        params.put(ACCESS_KEY, ACCESS_Value);
                        params.put(GET_USER, API);
                        params.put(ID, "" + AppController.getInstance().getId());

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(jsonReq);


                return true;
            } else {

                return false;

            }


        } catch (JSONException e) {

            e.printStackTrace();
            return false;
        }
    }


    public Boolean authorize(JSONObject authObj) {

        try {

            if (authObj.getString("error").equalsIgnoreCase("true")) {

                return false;
            }
            if (!authObj.has("data")) {

                return false;

            } else {
                JSONObject accountObj = authObj.getJSONObject("data");


                this.setId(accountObj.getString(ID));
                this.setFullname(accountObj.getString(NAME));
                this.setUsername(accountObj.getString(USERNAME));
                this.setEmail(accountObj.getString(EMAIL));
                this.setIp_addr(accountObj.getString("image"));
                this.setState(accountObj.getString(STATTUS));
                this.setPoints(accountObj.getString(POINTS));
                this.setRefer(accountObj.getString(REFER_CODE));
                this.setPhone(accountObj.getString("phone"));
                this.setBadge(accountObj.getString("badge"));
                this.setRank(authObj.getString("rank"));
                this.saveData();
                return true;
            }


        } catch (JSONException e) {

            e.printStackTrace();
            return false;
        }
    }


    public static Boolean isConnected(final AppCompatActivity activity) {
        Boolean check = false;
        ConnectivityManager ConnectionManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            check = true;
        } else {
            Snackbar snackbar = Snackbar
                    .make(activity.findViewById(android.R.id.content), "No Network connection..!!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = activity.getIntent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                        }
                    });
            snackbar.show();
        }
        return check;
    }

    public void logout(AppCompatActivity activity) {

        AppController.getInstance().removeData();
        AppController.getInstance().readData();
    }

    public static void initpDialog(AppCompatActivity activity) {

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(activity.getString(R.string.msg_loading));
        pDialog.setCancelable(true);
    }

    public static void showpDialog() {
        try {
            if (!pDialog.isShowing())
                pDialog.show();
        } catch (Exception e) {
        }
    }

    public static void hidepDialog() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

        }

    }

    public void readData() {
        this.setId(sharedPref.getString(getString(R.string.settings_account_id), "0"));
    }

    public void saveData() {
        sharedPref.edit().putString(getString(R.string.settings_account_id), this.getId()).apply();
    }

    public Boolean readFCM() {


        return sharedPref.getBoolean("FCM", false);
    }

    public void saveFCM(Boolean fcm) {

        sharedPref.edit().putBoolean("FCM", fcm).apply();
    }

    public String readToken() {

        return sharedPref.getString("TOKEN", "");
    }

    public void saveToken(String fcm) {

        sharedPref.edit().putString("TOKEN", fcm).apply();
    }

    public void removeData() {

        sharedPref.edit().putString(getString(R.string.settings_account_id), "0").apply();

    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setState(String state) {

        this.status = state;
    }

    public String getState() {

        return this.status;
    }

    public String getPoints() {

        return this.points;
    }

    public void setPoints(String username) {

        this.points = username;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public void setBadge(String badge) {

        this.badge = badge;
    }

    public String getPhone() {

        return this.phone;
    }

    public String getBadge() {

        return this.badge;
    }

    public String getUsername() {
        return PrefManager.getSavedString(getInstance(), "USER_NAME");
    }

    public void setUsername(String username) {
        PrefManager.setString(getInstance(), "USER_NAME", username);
        this.username = username;
    }

    public void setFullname(String fullname) {
        PrefManager.setString(getInstance(), "FULL_NAME", fullname);
        this.fullname = fullname;
    }

    public String getFullname() {
        return PrefManager.getSavedString(getInstance(), "FULL_NAME");
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getEmail() {

        return this.email;
    }

    public void setIp_addr(String ip_addr) {

        this.image = ip_addr;
    }

    public String getProfile() {

        return this.image;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    public String getRefercodee() {
        return refercodee;
    }

    public void setRefer(String password) {
        this.refercodee = password;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }


    public static void transparentStatusAndNavigation(AppCompatActivity context) {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true, context);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            context.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false, context);
            context.getWindow().setStatusBarColor(Color.TRANSPARENT);
            context.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private static void setWindowFlag(final int bits, boolean on, AppCompatActivity context) {
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
