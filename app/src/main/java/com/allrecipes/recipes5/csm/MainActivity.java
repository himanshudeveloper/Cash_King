package com.allrecipes.recipes5.csm;


import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;
import static com.allrecipes.recipes5.helper.PrefManager.CONSOLIADS_APP_SIGNATURE;
import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.BuildConfig;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.fragment.FragmentProfile;
import com.allrecipes.recipes5.csm.fragment.FragmentRefer;
import com.allrecipes.recipes5.csm.fragment.LeaderBoardFragment;
import com.allrecipes.recipes5.csm.fragment.Main_Fragment;
import com.allrecipes.recipes5.csm.fragment.RewardFragment;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.helper.PrefManager;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ayetstudios.publishersdk.AyetSdk;
import com.ayetstudios.publishersdk.interfaces.DeductUserBalanceCallback;
import com.ayetstudios.publishersdk.interfaces.UserBalanceCallback;
import com.ayetstudios.publishersdk.messages.SdkUserBalance;
import com.consoliads.sdk.ConsoliadsSdk;
import com.consoliads.sdk.SDKPlatform;
import com.consoliads.sdk.delegates.ConsoliadsSdkInitializationListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    //This is our viewPager
    FragmentRefer tournament_fragment;
    ChipNavigationBar chipNav;
    Boolean isBack = false;
    Dialog dialog;
    ShowAds showAds = new ShowAds();

    @Override
    public void onBackPressed() {
        backPressDialog();
    }

    private void backPressDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dlg_back_press);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        FrameLayout adLayout = dialog.findViewById(R.id.adLayout);
        Button noBtn = dialog.findViewById(R.id.noBtn);
        Button rateBtn = dialog.findViewById(R.id.rateBtn);
        Button yesBtn = dialog.findViewById(R.id.yesBtn);

        showAds.showNativeAds(MainActivity.this, adLayout);

        rateBtn.setOnClickListener(v ->{
            String PACKAGE_NAME = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
            Uri uri = Uri.parse(PACKAGE_NAME); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        yesBtn.setOnClickListener(v ->{
            finishAffinity();
        });

        noBtn.setOnClickListener(v ->{
            dialog.dismiss();
        });

        dialog.show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
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
        loadFragment(new Main_Fragment());
        chipNav = findViewById(R.id.chipNav);
        chipNav.setItemSelected(R.id.play, true);

        Paper.init(this);

        chipNav.getViewById(R.id.Rewards).setVisibility(View.GONE);

        if (Paper.book().contains("ShouldShowRewardsSection")) {
            if (Paper.book().read("ShouldShowRewardsSection").equals("true")) {
                chipNav.getViewById(R.id.Rewards).setVisibility(View.VISIBLE);
            } else {
                chipNav.getViewById(R.id.Rewards).setVisibility(View.GONE);
            }
        }

        AyetSdk.init((Application) getApplicationContext(), AppController.getInstance().getId(), new UserBalanceCallback() { // UserBalanceCallback is optional if you want to manage balances on your servers
            @Override
            public void userBalanceChanged(SdkUserBalance sdkUserBalance) {
                Log.d("AyetSdk", "userBalanceChanged - available balance: " + sdkUserBalance.getAvailableBalance()); // this is the new total available balance for the user
            }

            @Override
            public void userBalanceInitialized(SdkUserBalance sdkUserBalance) {
                Log.d("AyetSdk", "SDK initialization successful");
                Log.d("AyetSdk", "userBalanceInitialized - available balance: " + sdkUserBalance.getAvailableBalance()); // this is the total available balance for the user
                Log.d("AyetSdk", "userBalanceInitialized - spent balance: " + sdkUserBalance.getSpentBalance()); // this is the total amount spent with "AyetSdk.deductUserBalance(..)"
                Log.d("AyetSdk", "userBalanceInitialized - pending balance: " + sdkUserBalance.getPendingBalance()); // this is the amount currently pending for conversion (e.g. user still has offer requirements to meet)
            }

            @Override
            public void initializationFailed() {
                Log.d("AyetSdk", "initializationFailed - please check APP API KEY & internet connectivity");
            }
        });

        int amount = 100;
        AyetSdk.deductUserBalance(MainActivity.this, amount, new DeductUserBalanceCallback() {
            @Override
            public void success() {

            }

            @Override
            public void failed() {
                Log.d("AyetSdk", "deductUserBalance - failed");
                // this usually means that the user does not have sufficient balance in his account
            }
        });

        chipNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.play:
                        loadFragment(new Main_Fragment());
                        break;
                    case R.id.battle:
                        loadFragment(new LeaderBoardFragment());
                        break;
                    case R.id.tournament:
                        loadFragment(new FragmentRefer());
                        loadFragment(tournament_fragment);
                        break;
                    case R.id.profile:
                        loadFragment(new FragmentProfile());
                        break;
                    case R.id.Rewards:
                        loadFragment(new RewardFragment());
                        break;
                }
            }
        });

        cjeck();
        time_update();
        ConsoliadsSdk.getInstance().init(this, PrefManager.getSavedString(this, CONSOLIADS_APP_SIGNATURE)
                , false, false, SDKPlatform.Google, new ConsoliadsSdkInitializationListener() {
                    @Override
                    public void onInitializationSuccess() {

                    }

                    @Override
                    public void onInitializationError(String error) {

                    }
                });
        AdsManager.loadInterstitalAd(this);
    }

    public static void change(int position, ViewPager viewPager) {
        viewPager.setCurrentItem(position);

    }

    public void reloadMainFragment() {
        loadFragment(new Main_Fragment());
    }


    public void time_update() {
        int delay = 0; // delay for 0 sec.
        int period = 10000000; // repeat every 10 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                update_point();
            }
        }, delay, period);
    }

    private void update_point() {
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

                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("user_point_update", API);
                params.put("fcm_id", "FirebaseInstanceId.getInstance().getToken()");
                params.put(USERNAME, AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void cjeck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            // this will request for permission when permission is not true
        } else {
            // Download code here
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


//    @Override
//    public void onBackPressed() {
//
//
//        if (isBack.equals(false)) {
//            isBack = true;
//            Dialog dialog = new Dialog(this);
//            dialog.setContentView(R.layout.fragment_dialog);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable
//                    (Color.TRANSPARENT));
//
//            Button no = dialog.findViewById(R.id.no);
//            Button yes = dialog.findViewById(R.id.yes);
//
//            yes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                    System.exit(0);
//                }
//            });
//
//            no.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    isBack = false;
//                }
//            });
//            dialog.setCancelable(false);
//
//            dialog.show();
//        }
//    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
