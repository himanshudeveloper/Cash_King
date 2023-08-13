package com.allrecipes.recipes5.watchandearn;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.room.Room;

import com.allrecipes.recipes5.watchandearn.activity.MainActivity;
import com.allrecipes.recipes5.watchandearn.activity.SplashActivity;
import com.allrecipes.recipes5.watchandearn.feature.AdsModel;
import com.allrecipes.recipes5.watchandearn.feature.AdsModelList;
import com.allrecipes.recipes5.watchandearn.feature.ApiInterface;
import com.allrecipes.recipes5.watchandearn.feature.ApiWebServices;
import com.allrecipes.recipes5.watchandearn.feature.OwnAdsModel;
import com.allrecipes.recipes5.watchandearn.feature.Prevalent;
import com.allrecipes.recipes5.watchandearn.rooms.CoinDatabase;
import com.allrecipes.recipes5.watchandearn.rooms.Coins;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;
import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    private static final String ONESIGNAL_APP_ID = "769865bd-7d6d-48dd-b0a7-2c1969e1a23b";
    public static CoinDatabase db;
    public static List<OwnAdsModel> ownAdsModels;
    private static App mInstance;
    SharedPreferences.Editor editor;
    private ApiInterface apiInterface;
    private RequestQueue mRequestQueue;

    public static Context getContext() {
        return mInstance;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        apiInterface = ApiWebServices.getApiInterface();
        Paper.init(this);
        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        db = Room.databaseBuilder(getApplicationContext(),
                CoinDatabase.class, "coin.db").allowMainThreadQueries().build();

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if (!sh.getBoolean("newUser", false)) {
            db.getDao().insert(new Coins("scratch", 100));
            editor.putBoolean("newUser", true);
            editor.commit();
            db.getDao().getCoins("scratch");
            Constant.addPoints(this, db.getDao().getCoins("scratch").getCoin(), 0);
        }

        fetchAds();
        fetchOwnAds();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    private void fetchAds() {
        apiInterface = ApiWebServices.getApiInterface();
        Call<AdsModelList> call = apiInterface.fetchAds("earning_quiz13");
        call.enqueue(new Callback<AdsModelList>() {
            @Override
            public void onResponse(@NonNull Call<AdsModelList> call, @NonNull Response<AdsModelList> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData() != null) {
                        for (AdsModel ads : response.body().getData()) {
                            Log.d("checkIds",
                                    ads.getId()
                                            + "\n" + ads.getAppId()
                                            + "\n" + ads.getAppLovinSdkKey()
                                            + "\n" + ads.getBannerTop()
                                            + "\n" + ads.getBannerTopNetworkName()
                                            + "\n" + ads.getBannerBottom()
                                            + "\n" + ads.getBannerBottomNetworkName()
                                            + "\n" + ads.getInterstitialAds()
                                            + "\n" + ads.getInterstitialNetwork()
                                            + "\n" + ads.getNativeAds()
                                            + "\n" + ads.getNativeAdsNetworkName()
                            );

                            Paper.book().write(Prevalent.id, ads.getId());
                            Paper.book().write(Prevalent.appId, ads.getAppId());
                            Paper.book().write(Prevalent.appLovinId, ads.getAppLovinSdkKey());
                            Paper.book().write(Prevalent.bannerTop, ads.getBannerTop());
                            Paper.book().write(Prevalent.bannerTopNetworkName, ads.getBannerTopNetworkName());
                            Paper.book().write(Prevalent.bannerBottom, ads.getBannerBottom());
                            Paper.book().write(Prevalent.bannerBottomNetworkName, ads.getBannerBottomNetworkName());
                            Paper.book().write(Prevalent.interstitialAds, ads.getInterstitialAds());
                            Paper.book().write(Prevalent.interstitialNetwork, ads.getInterstitialNetwork());
                            Paper.book().write(Prevalent.nativeAds, ads.getNativeAds());
                            Paper.book().write(Prevalent.nativeAdsNetworkName, ads.getNativeAdsNetworkName());

                            try {
                                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                                Bundle bundle = ai.metaData;
                                String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                                Log.d(TAG, "Name Found: " + myApiKey);
                                ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", Paper.book().read(Prevalent.appId));//you can replace your key APPLICATION_ID here
                                String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                                Log.d(TAG, "ReNamed Found: " + ApiKey);
                            } catch (PackageManager.NameNotFoundException e) {
                                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                            } catch (NullPointerException e) {
                                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                            }

                            try {
                                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                                Bundle bundle = ai.metaData;
                                String myApiKey = bundle.getString("applovin.sdk.key");
                                Log.d(TAG, "Name Found: " + myApiKey);
                                ai.metaData.putString("applovin.sdk.key", Paper.book().read(Prevalent.appLovinId));     //you can replace your key APPLICATION_ID here
                                String ApiKey = bundle.getString("applovin.sdk.key");
                                Log.d(TAG, "ReNamed Found: " + ApiKey);
                            } catch (PackageManager.NameNotFoundException e) {
                                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                            } catch (NullPointerException e) {
                                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                            }
                        }
                    }
                } else {
                    Log.e("adsError", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdsModelList> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });
    }

    private void fetchOwnAds() {
        ownAdsModels = new ArrayList<>();
        Call<List<OwnAdsModel>> call = apiInterface.fetchOwnAds("earning_quiz2");
        call.enqueue(new Callback<List<OwnAdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<OwnAdsModel>> call, @NonNull Response<List<OwnAdsModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ownAdsModels.addAll(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OwnAdsModel>> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            JSONObject data = result.getNotification().getAdditionalData();
            String activityToBeOpened, imgPos;

            if (data != null) {
                activityToBeOpened = data.optString("action", null);
                imgPos = data.optString("pos", null);
                editor.putString("pos", imgPos);
                editor.putString("action", activityToBeOpened);
                editor.apply();
                switch (activityToBeOpened) {
                    case "news":
                    case "yojana":
                    case "others": {
                        Intent intent = new Intent(App.this, MainActivity.class);
                        intent.putExtra("action", activityToBeOpened);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    }
                }
            } else {
                Intent intent = new Intent(App.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


        }
    }

}
