package com.allrecipes.recipes5.helper;

import static com.allrecipes.recipes5.Just_base.addPoint;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.ACCOUNT_STATE_ENABLED;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.DAILY_CHECKIN_API;
import static com.allrecipes.recipes5.helper.Constatnt.DAILY_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.GET_USER;
import static com.allrecipes.recipes5.helper.Constatnt.ID;
import static com.allrecipes.recipes5.helper.Constatnt.SPIN_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.allrecipes.recipes5.OnScratchComplete;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.WelcomeActivity;
import com.allrecipes.recipes5.csm.ActivitySplash;
import com.allrecipes.recipes5.csm.MainActivity;
import com.allrecipes.recipes5.csm.topsheet.Coins_Dialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class PrefManager {
    public static final String SCRATCH_COUNT_BEETWEN = "scratch_count_beetween";
    public static final String DAILY_VIDEO_LIMIT = "daily_video_limit";
    static Context context;
    static AppCompatActivity activity;
    public static final String SETTING_PREF = "settings_pref";
    public static final String UNITY_AD_TYPE = "unity";
    public static final String APPLOVIN_AD_TYPE = "applovin";
    public static final String START_APP_AD_TYPE = "start_io";
    public static final String ADCOLONY_AD_TYPE = "adcolony";
    public static final String VUNGLE_AD_TYPE = "vungle";
    public static final String CONSOLIADS_TYPE = "consoliads";
    public static final String CHARTBOOST_AD_TYPE = "chartboost";
    public static final String IRON_SOURCE_AD_TYPE = "iron_source";
    public static final String UNITY_BANNER_ID = "unity_banner_id";
    public static final String APPLOVIN_BANNER_ID = "applovin_banner_id";
    public static final String ADCOLONY_BANNER_ID = "adcolony_banner_id";
    public static final String VUNGLE_BANNER_ID = "vungle_banner_id";
    public static final String BANNER_AD_TYPE = "banner_ad_type";
    public static final String INTERSTITAL_AD_TYPE = "interstital_ad_type";
    public static final String USER_MULTIPLE_ACCOUNT = "use_multiple_account";
    public static final String NATIVE_AD_TYPE = "native_ad_type";
    public static final String APPLOVIN_INTERSTITAL_ID = "applovin_interstital_id";
    public static final String UNITY_INTERSTITAL_ID = "unity_interstital_id";
    public static final String ADCOLONY_INTERSTITAL_ID = "adcolony_interstital_id";
    public static final String VUNGLE_INTERSTITAL_ID = "vungle_interstital_id";
    public static final String ADMOB_REWARDED_ID = "admob_rewarded_id";
    public static final String VUNGLE_REWARDED_ID = "vungle_rewarded_id";
    public static final String APPLOVIN_REWARDED_ID = "applovin_rewarded_id";
    public static final String APPLOVIN_NATIVE_ID = "applovin_native_id";
    public static final String UNITY_REWARDED_ID = "unity_rewarded_id";
    public static final String ADCOLONY_REWARDED_ID = "adcolony_rewarded_id";
    public static final String SPIN_COUNT = "spin_count";
    public static final String SCRATCH_COUNT = "scratch_count";
    public static final String CONSOLIADS_APP_SIGNATURE = "consoliads_app_signature";
    public static final String IRON_SOURCE_APP_KEY = "iron_source_app_key";
    public static final String YODO_APP_KEY = "yodo_app_key";
    public static final String CHARTBOOST_APP_ID = "chartboost_app_id";
    public static final String CHARTBOOST_APP_SIGNATURE = "chartboost_app_signature";
    public static final String VUNGLE_APP_ID = "vungle_app_id";
    public static final String ADCOLONY_APP_ID = "adcolony_app_id";
    public static final String NATIVE_COUNT = "native_count";
    public static final String UNITY_GAME_ID = "unity_game_id";
    public static final String START_IO_APP_ID = "start_io_app_id";
    public static final String ADMOB_APP_ID = "admob_app_id";
    public static final String REWARDED_AD_TYPE = "rewarded_ad_type";
    public static final String SPIN_COUNT_PER_DAY = "spin_count_per_day";
    public static final String EXTRA_SPIN_COUNT = "extra_spin_count";

    public PrefManager(Context context) {
        this.context = context;
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void saveAdmob(String ads_id, String banner, String full, String reward) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ads_id", ads_id);
        editor.putString("banner", banner);
        editor.putString("full", full);
        editor.putString("reward", reward);

        editor.commit();
    }

    public static void setString(Context context, String key, String prefString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, prefString);
        editor.apply();
    }

    public static String getSavedString(Context context, String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(pref, "hahahhahaahahahhahaah");
    }

    public static void setInt(Context context, String key, int prefString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, prefString);
        editor.apply();
    }

    public static int getSavedInt(Context context, String pref) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTING_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(pref, 0);
    }

    public static String getAds_id(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ads_id", "");
    }

    public static String getBanner(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        return sharedPreferences.getString("banner", "fsdafda");
    }

    public static String getFull(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        return sharedPreferences.getString("full", "");
    }

    public static String getReward(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        return sharedPreferences.getString("reward", "");
    }

    public String getStatus() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        return sharedPreferences.getString("status", "");
    }

    public boolean reset_admob(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Admob", Context.MODE_PRIVATE);
        boolean isAds_idEmpty = sharedPreferences.getString("ads_id", "").isEmpty();
        boolean isBannerEmpty = sharedPreferences.getString("banner", "").isEmpty();
        boolean isFullEmpty = sharedPreferences.getString("full", "").isEmpty();
        boolean isRewardEmpty = sharedPreferences.getString("reward", "").isEmpty();
        boolean isStatusEmpty = sharedPreferences.getString("status", "").isEmpty();

        return isAds_idEmpty || isBannerEmpty || isFullEmpty || isRewardEmpty || isStatusEmpty;
    }

    public static void Add_Coins_(Context context, String coins, String from, String showAd, OnScratchComplete onScratchComplete) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Coins_Dialog newFragment = new Coins_Dialog();
        Bundle args = new Bundle();
        args.putString("coins", coins);
        args.putString("from", from);
        args.putString("show_ad", showAd);
        newFragment.setArguments(args);
        newFragment.setOnScratchCompleteListener(onScratchComplete);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        addPoint(context, coins, from);
    }

    public static void user_points(TextView t) {
        final String[] s = {"0"};
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                t.setText(response.getString("points"));

                            } else {

                            }
                        } catch (Exception e) {
                            Toast.makeText(t.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("get_points", AppController.getInstance().getId());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    public static void algoratham(Context context) {
//        System.out.println("1st : " + data);
//        try {
//            setString(context, UNITY_BANNER_ID, data.getString("unity_banner_id"));
//            setString(context, APPLOVIN_BANNER_ID, data.getString("applovin_banner_id"));
//            setString(context, ADCOLONY_BANNER_ID, data.getString("adcolony_banner_id"));
//            setString(context, VUNGLE_BANNER_ID, data.getString("vungle_banner_id"));
//            setString(context, BANNER_AD_TYPE, data.getString("banner_ad_type"));
//            setString(context, INTERSTITAL_AD_TYPE, data.getString("interstital_ad_type"));
//            setString(context, USER_MULTIPLE_ACCOUNT, data.getString("use_multiple_account"));
//            setString(context, NATIVE_AD_TYPE, data.getString("native_ad_type"));
//            setString(context, APPLOVIN_INTERSTITAL_ID, data.getString("applovin_interstital_id"));
//            setString(context, UNITY_INTERSTITAL_ID, data.getString("unity_interstital_id"));
//            setString(context, ADCOLONY_INTERSTITAL_ID, data.getString("adcolony_interstital_id"));
//            setString(context, VUNGLE_INTERSTITAL_ID, data.getString("vungle_interstital_id"));
//            setString(context, ADMOB_REWARDED_ID, data.getString("admob_rewarded_id"));
//            setString(context, VUNGLE_REWARDED_ID, data.getString("vungle_rewarded_id"));
//            setString(context, APPLOVIN_REWARDED_ID, data.getString("applovin_rewarded_id"));
//            setString(context, APPLOVIN_NATIVE_ID, data.getString("applovin_native_id"));
//            setString(context, UNITY_REWARDED_ID, data.getString("unity_rewarded_id"));
//            setString(context, ADCOLONY_REWARDED_ID, data.getString("adcolony_rewarded_id"));
//            setString(context, SPIN_COUNT, data.getString("spin_count"));
//            setString(context, SCRATCH_COUNT, data.getString("scratch_count"));
//            setString(context, SCRATCH_COUNT_BEETWEN, data.getString("scratch_count_beetween"));
//            setString(context, DAILY_VIDEO_LIMIT, data.getString(DAILY_VIDEO_LIMIT));
//            setString(context, CONSOLIADS_APP_SIGNATURE, data.getString("consoliads_app_signature"));
//            setString(context, IRON_SOURCE_APP_KEY, data.getString("iron_source_app_key"));
//            setString(context, YODO_APP_KEY, data.getString("yodo_app_key"));
//            setString(context, CHARTBOOST_APP_ID, data.getString("chartboost_app_id"));
//            setString(context, CHARTBOOST_APP_SIGNATURE, data.getString("chartboost_app_signature"));
//            setString(context, VUNGLE_APP_ID, data.getString("vungle_app_id"));
//            setString(context, ADCOLONY_APP_ID, data.getString("adcolony_app_id"));
//            setString(context, NATIVE_COUNT, data.getString("native_count"));
//            setString(context, UNITY_GAME_ID, data.getString("unity_game_id"));
//            setString(context, START_IO_APP_ID, data.getString("start_io_app_id"));
//            setString(context, ADMOB_APP_ID, data.getString("admob_app_id"));
//            setString(context, REWARDED_AD_TYPE, data.getString("rewarded_ad_type"));
//            setString(context, SPIN_COUNT_PER_DAY, data.getString(SPIN_COUNT_PER_DAY));
//            AppController.initializeAdNetworks();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (AppController.getInstance().isConnected((AppCompatActivity) context) && !(AppController.getInstance().getId().equals("0"))) {
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("2nd : " + response);

                            if (AppController.getInstance().authorize(response)) {

                                if (AppController.getInstance().getState().equalsIgnoreCase(ACCOUNT_STATE_ENABLED)) {
                                    System.out.println("MainActivity");
                                    Intent i = new Intent(context, MainActivity.class);
                                    i.putExtra("new_user", "old");
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    ((Activity) context).finish();


                                } else {
                                    System.out.println("Welcome");
                                    Intent i = new Intent(context, WelcomeActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    AppController.getInstance().logout((AppCompatActivity) context);
                                    ((Activity) context).finish();
                                }

                            } else {
                                System.out.println("Welcome");
                                Intent i = new Intent(context, WelcomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(i);
                                ((Activity) context).finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("3rd : " + error.getMessage());
                    Intent i = new Intent(context, WelcomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(i);
                    ((Activity) context).finish();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put(GET_USER, API);
                    params.put(ID, "" + AppController.getInstance().getId());

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(jsonReq);

        } else {
            System.out.println("Welcome");
            Intent i = new Intent(context, WelcomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
        }
    }

    public static void A(Context context) {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("VPN response : " + response);
                        try {
                            if (response.getString("error").equalsIgnoreCase("1")) {
                                if (response.getString("vpn").equals("1")) {
                                    if (vpn()) {
                                        Toast.makeText(context, "Please disconnect the vpn and reopen the app!", Toast.LENGTH_SHORT).show();
                                        ((Activity) context).finish();
                                    } else {
                                        System.out.println("Algoratham going 1");
                                        algoratham(context);
                                        //response.getJSONObject("data")
                                    }
                                } else {
                                    System.out.println("Algoratham going 2");
                                    algoratham(context);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("VPN : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("c", DAILY_TYPE);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public static boolean vpn() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }


    public static void check_n(Context context, Activity activity) {
        if (isConnected(context)) {

        } else {
            // Create the object of
            // AlertDialog Builder class
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(context);

            // Set the message show for the Alert time
            builder.setMessage("Please check your internet.");

            // Set Alert Title
            builder.setTitle("No connection!");
            builder.setCancelable(false);

            // Set Cancelable false
            // for when the user clicks on the outside
            // the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the positive button with yes name
            // OnClickListener method is use of
            // DialogInterface interface.

            builder
                    .setPositiveButton(
                            "Retry",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // When the user click yes button
                                    // then app will close
                                    dialog.dismiss();
                                    Intent i = new Intent(context, ActivitySplash.class);
                                    context.startActivity(i);
                                    activity.finish();

                                }
                            });

            // Set the Negative button with No name
            // OnClickListener method is use
            // of DialogInterface interface.
            builder
                    .setNegativeButton(
                            "Exit",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    activity.finishAffinity();
                                    System.exit(0);

                                }
                            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }
    }

    public static boolean isConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


    public static void redeem_package(final Context contextt, String package_id, String p_details, String amount_id) {
        Dialog dialogg = new Dialog(contextt);
        dialogg.setContentView(R.layout.loading);
        dialogg.getWindow().setBackgroundDrawable(new ColorDrawable
                (Color.TRANSPARENT));
        dialogg.setCancelable(false);
        dialogg.show();
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                if (response.getString("message").equals("Not enough points available to redeem")) {
                                    dialogg.dismiss();
                                    Toast.makeText(contextt, response.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    dialogg.dismiss();
                                   /* FragmentManager fragmentManager = ((FragmentActivity)contextt).getSupportFragmentManager();
                                    Done newFragment = new Done();
                                    Bundle args = new Bundle();
                                    args.putString("symbol", symbol);
                                    args.putString("amount", ""+ amount);
                                    newFragment.setArguments(args);
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();*/
                                    Toast.makeText(contextt, response.getString("message"), Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                dialogg.dismiss();
                                Toast.makeText(contextt, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            dialogg.dismiss();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(contextt, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("redeem", package_id);
                params.put("id", AppController.getInstance().getId());
                params.put("p_details", p_details);
                params.put("amount_id", amount_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public static void claim_points(Context context) {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                //daily = true;
                                String p = response.getString("points");
                                // c_sub.setText("Claim your daily "+p+" coins for free");
                                Add_Coins_(context, p, "Daily checkin bonus", "true",null);
                            } else {
                                Toast.makeText(context, "You've already claim your daily bonus!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(DAILY_CHECKIN_API, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                // params.put(POINTS, "" + DAILY_POINT);
                params.put(SPIN_TYPE, DAILY_TYPE);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

}
