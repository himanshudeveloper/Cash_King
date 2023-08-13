package com.allrecipes.recipes5.watchandearn.activity;


import static com.allrecipes.recipes5.watchandearn.feature.CommonMethods.openCustomTab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.databinding.ActivityStartBinding;
import com.allrecipes.recipes5.watchandearn.feature.ApiInterface;
import com.allrecipes.recipes5.watchandearn.feature.ApiWebServices;
import com.allrecipes.recipes5.watchandearn.feature.CommonMethods;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;

public class StartActivity extends AppCompatActivity {

    public static boolean checkInterstialIsCalled = true;
    ImageView startBtn, shareBtn, rateBtn;
    ShowAds showAds;
    ApiInterface apiInterface;
    //    List<BannerModel> bannerModels = new ArrayList<>();
//    ImageSlider slider;
//    List<SlideModel> slideModels = new ArrayList<>();
    ActivityStartBinding binding;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showAds = new ShowAds();
        apiInterface = ApiWebServices.getApiInterface();
        startBtn = findViewById(R.id.start_btn);
        shareBtn = findViewById(R.id.share_btn);
        rateBtn = findViewById(R.id.rate_btn);
//        slider = findViewById(R.id.image_slider);
//        setBanner();
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        startBtn.setAnimation(animation);
        showAds = new ShowAds();

        new Handler().postDelayed(() -> {

            startBtn.setOnClickListener(v -> {
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Start");
                mFirebaseAnalytics.logEvent("Clicked_On_Start_Button", bundle);

                findViewById(R.id.loading_indicator).setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
//                showAds.destroyBanner();
                    checkInterstialIsCalled = true;
                    startActivity(new Intent(this, MainActivity.class));
                    findViewById(R.id.loading_indicator).setVisibility(View.GONE);

                }, 2000);
            });

        }, 3000);


        shareBtn.setOnClickListener(v -> {
            CommonMethods.shareApp(this);
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Share");
            mFirebaseAnalytics.logEvent("Clicked_On_Share_Start", bundle);

        });
        rateBtn.setOnClickListener(v -> {
            CommonMethods.rateApp(this);
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Rate");
            mFirebaseAnalytics.logEvent("Clicked_On_Rate_Start", bundle);

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        setBanner();
        IronSource.onResume(this);
        new Handler().postDelayed(() -> {
            showAds.showTopBanner(this, binding.adViewTop);
            showAds.showBottomBanner(this, binding.adViewBottom);
            showAds.showNativeAds(this, binding.imageSlider);
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showAds.destroyBanner();
        IronSource.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (isInternetConnected(this)) {
//            new Handler().postDelayed(() -> {
//                showAds.showTopBanner(StartActivity.this, findViewById(R.id.adView_top));
//                showAds.showBottomBanner(StartActivity.this, findViewById(R.id.adView_bottom));
//            }, 2000);
//        } else {
//            Toast.makeText(this, "Please try to connect Internet", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.app_name).setIcon(R.mipmap.ic_launcher_round).setMessage("Do You Really Want To Exit?\nAlso Rate Us 5 Star.").setNeutralButton("CANCEL", (dialog, which) -> {
        });


        builder.setNegativeButton("RATE APP", (dialog, which) -> CommonMethods.rateApp(getApplicationContext())).setPositiveButton("OK!!", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            moveTaskToBack(true);
            System.exit(0);
        });
        builder.show();

    }

//    void setBanner() {
//        Call<List<BannerModel>> call = apiInterface.fetchBanners();
//
//        call.enqueue(new Callback<List<BannerModel>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<BannerModel>> call, @NonNull Response<List<BannerModel>> response) {
//                if (response.isSuccessful()) {
//                    bannerModels.clear();
//                    bannerModels.addAll(Objects.requireNonNull(response.body()));
//                    Collections.shuffle(bannerModels);
//                    slideModels.clear();
//                    for (BannerModel b : bannerModels) {
//                        slideModels.add(new SlideModel(ApiWebServices.base_url + "banner_images/" + b.getImage(), ScaleTypes.FIT));
//                    }
//                    slider.setImageList(slideModels);
//                    slider.setItemClickListener(i -> openWebPage(bannerModels.get(i).getUrl()));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<BannerModel>> call, @NonNull Throwable t) {
//                Log.e("onErrorResponse", t.getMessage());
//            }
//        });
//
//
//    }

    public void openWebPage(String url) {

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, url);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Banner");
        mFirebaseAnalytics.logEvent("Clicked_On_Start_Banner", bundle);
        CustomTabsIntent.Builder customTabIntent = new CustomTabsIntent.Builder();
        openCustomTab(StartActivity.this, customTabIntent.build(), url);

    }
}