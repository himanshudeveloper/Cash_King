package com.allrecipes.recipes5;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.topsheet.Coins_Dialog;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.ADD_SPIN;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.FCM_ID;
import static com.allrecipes.recipes5.helper.Constatnt.POINTS;
import static com.allrecipes.recipes5.helper.Constatnt.SPIN_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.UPDATE_FCM;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;


public class Just_base extends AppCompatActivity {
   // public RewardedVideoAd mRewardedVideoAd;

    public static AppCompatActivity activity_main;


    SharedPreferences prefs = null;
    SharedPreferences prefs_daily = null;


    private WebView webView = null;
    ProgressBar progressBar;
    String rotation,current_page_url = "";
    FrameLayout frameLayout;
    @SuppressLint({"SourceLockedOrientationActivity", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_just_base);
        frameLayout = findViewById(R.id.container);
        Intent i = getIntent();
        current_page_url = i.getStringExtra("url");
        rotation = i.getStringExtra("rotation");
        if(rotation.matches("1")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//Set Landscape
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
        }
        this.webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);


        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
              WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(current_page_url);



        if (AppController.getInstance().readFCM() == true) {
            JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            AppController.getInstance().saveFCM(false);
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
                    params.put(UPDATE_FCM, API);
                    params.put(USERNAME, AppController.getInstance().getUsername());
                    params.put(FCM_ID, FirebaseInstanceId.getInstance().getToken());

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(stringRequest);


        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public static void addPoint(final Context context, final String points, final String type) {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("error").equalsIgnoreCase("false")) {

                                if (response.getString("message").equalsIgnoreCase("Points added to your wallet")) {
                                    //setPoint();

                                    if (!(type =="Deposit"))
                                    {
                                       // Toast.makeText(context, "Coins Added Successfully!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

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
                params.put(ADD_SPIN, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                params.put(POINTS, points);
                params.put(SPIN_TYPE, type);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static boolean task_claim(Context context, String task_id, Button click)
    {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable
                (Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();


        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("error").equalsIgnoreCase("false")) {
                                String p = response.getString("p");
                                click.setText("Done");
                                click.setEnabled(false);
                                if (dialog.isShowing()){
                                    dialog.dismiss();}
                                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                                Coins_Dialog newFragment = new Coins_Dialog();
                                Bundle args = new Bundle();
                                args.putString("coins", p);
                                args.putString("from", "referral task");
                                newFragment.setArguments(args);
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
                               // Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();




                            } else {
                                if (dialog.isShowing()){
                                    dialog.dismiss();}
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            if (dialog.isShowing()){
                                dialog.dismiss();}
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (dialog.isShowing()){
                            dialog.dismiss();}
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("id", AppController.getInstance().getId());
                params.put("task_claim", task_id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

        return true;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setPoint();

    }


}
