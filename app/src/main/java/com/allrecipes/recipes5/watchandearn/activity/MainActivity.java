package com.allrecipes.recipes5.watchandearn.activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.Common;
import com.allrecipes.recipes5.csm.PlayActivity;
import com.allrecipes.recipes5.databinding.ActivityMainBinding;
import com.allrecipes.recipes5.watchandearn.feature.ApiInterface;
import com.allrecipes.recipes5.watchandearn.feature.ApiWebServices;
import com.allrecipes.recipes5.watchandearn.feature.CommonMethods;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.allrecipes.recipes5.watchandearn.feature.UrlModel;
import com.allrecipes.recipes5.watchandearn.fragments.HomeFragment;
import com.allrecipes.recipes5.watchandearn.rooms.CoinDatabase;
import com.allrecipes.recipes5.watchandearn.rooms.Coins;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final float END_SCALE = 0.7f;
    public static Coins coins;
    public static boolean checkInterstialIsCalled = true;
    ShowAds showAds = new ShowAds();
    String fileUrl, contactUsUrl;
    ApiInterface apiInterface;
    CoinDatabase db;
    private DrawerLayout drawer;
    private MainActivity activity;
    private NavigationView mNav;
    private TextView points_textView;
    private LottieAnimationView whatsAppLottie;
    private RelativeLayout contentView;
    ActivityResultLauncher<String> permissionResultLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        apiInterface = ApiWebServices.getApiInterface();

        // check for the notification permission
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Toast.makeText(MainActivity.this, "Notifications granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Notifications Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission();
            }
        }

        db = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin.db").allowMainThreadQueries().build();


        if (StartActivity.checkInterstialIsCalled) {
            Paper.init(this);
            showAds.showInterstitialAds(this);
            StartActivity.checkInterstialIsCalled = false;
        }

        fetchProWallUrl();
        fetchContactUsUrl();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText(getResources().getString(R.string.app_name));
            points_textView = toolbar.findViewById(R.id.points_text_in_toolbar);
            ImageView backBtn = toolbar.findViewById(R.id.backBtn);

            backBtn.setOnClickListener(v ->{
                Common.openBackCustomTab(MainActivity.this);
                finish();
            });

            whatsAppLottie = toolbar.findViewById(R.id.lottie_whatsapp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPointsText();
        contentView = findViewById(R.id.contentView);
        drawer = findViewById(R.id.drawer_layout);
        mNav = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        mNav.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, HomeFragment.newInstance()).commit();
        onClick();
        animateNavigationDrawer();
    }

    private void onClick() {
        checkIsBlocked();
        View header = mNav.getHeaderView(0);
        showAds.showNativeAds(this, header.findViewById(R.id.nativeAds));
        whatsAppLottie.setOnClickListener(v -> {
            if (contactUsUrl != null) {
                openWebPage(contactUsUrl, MainActivity.this);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissionResultLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    public void setPointsText() {
        if (points_textView != null) {
            String userPoints = Constant.getString(activity, Constant.USER_POINTS);
            if (userPoints.equalsIgnoreCase("")) {
                userPoints = "0";
            }
            coins = db.getDao().getCoins("scratch");
            Log.d(TAG, String.valueOf(coins.coin));
            points_textView.setText(String.valueOf(coins.coin));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPointsText();

        onClick();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                startActivity(new Intent(this, StartActivity.class));
                Common.openBackCustomTab(MainActivity.this);
                finish();
                break;
            case R.id.menu_activity_send:
                if (fileUrl != null) openWebPage(fileUrl, MainActivity.this);
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu_Send");
                mFirebaseAnalytics.logEvent("Clicked_On_Send", bundle);
                break;

            case R.id.nav_share:
                CommonMethods.shareApp(this);
                break;
            case R.id.nav_rate:
                CommonMethods.rateApp(this);
                break;

            case R.id.nav_policy:
                try {
                    Intent policyintent = new Intent(activity, PolicyActivity.class);
                    policyintent.putExtra("type", "Privacy Policy");
                    startActivity(policyintent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
                break;

            case R.id.nav_exit:
                showExitDialog();
                break;
            case R.id.nav_refer:
                try {
                    checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "refer");
                    startActivity(policyintent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }

                break;

            case R.id.nav_wallet:
                try {
                    checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "wallet");
                    startActivity(policyintent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }

                break;

            case R.id.nav_about:
                try {
                    checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, PolicyActivity.class);
                    policyintent.putExtra("type", "About Us");
                    startActivity(policyintent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
                break;
            case R.id.nav_contact:
                if (contactUsUrl != null) openWebPage(contactUsUrl, MainActivity.this);


                break;
            case R.id.nav_faq:
                try {
                    checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, PolicyActivity.class);
                    policyintent.putExtra("type", "Instruction");
                    startActivity(policyintent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
                break;
            case R.id.disclaimer:
                checkInterstialIsCalled = true;
                Intent policyintent = new Intent(activity, PolicyActivity.class);
                policyintent.putExtra("type", "disclaimer");
                startActivity(policyintent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

        }
        animateNavigationDrawer();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
        Common.openBackCustomTab(MainActivity.this);
        finish();
    }

    private void animateNavigationDrawer() {
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }


    private void checkIsBlocked() {
        if (Constant.getString(activity, Constant.USER_BLOCKED).equals("true")) {
            Constant.showBlockedDialog(activity, getResources().getString(R.string.you_are_blocked));
            return;
        }
    }

    private void showExitDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);
        AppCompatButton yes_btn = dialog.findViewById(R.id.add_btn);

        imageView.setImageResource(R.drawable.ic_error);
        title_text.setText(getResources().getString(R.string.exit_text));
        points_text.setVisibility(View.GONE);
        cancel_btn.setVisibility(View.VISIBLE);
        cancel_btn.setText(getResources().getString(R.string.no));
        yes_btn.setText(getResources().getString(R.string.yes));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Common.openBackCustomTab(MainActivity.this);
                finish();
            }
        });
        dialog.show();
    }


    private void fetchProWallUrl() {
        Call<UrlModel> call = apiInterface.getUrls("earning_quiz2");
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {

                if (response.isSuccessful()) {
                    fileUrl = response.body().getUrl();
                    Log.d("checkIds", response.body().getUrl());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {
                Log.d("ggggggggg", t.getMessage());
            }
        });
    }

    private void fetchContactUsUrl() {
        Call<UrlModel> call = apiInterface.getUrls("earning_quiz_contactUs");
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {

                if (response.isSuccessful()) {
                    contactUsUrl = response.body().getUrl();
                    Log.d("checkIds", response.body().getUrl());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {
                Log.d("ggggggggg", t.getMessage());
            }
        });
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebPage(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}