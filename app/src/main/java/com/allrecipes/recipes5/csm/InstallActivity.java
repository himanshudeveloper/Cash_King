package com.allrecipes.recipes5.csm;

import static com.allrecipes.recipes5.Just_base.addPoint;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.OnVideoAdEnded;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.Refer_Activity;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.Helper;
import com.allrecipes.recipes5.helper.PrefManager;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InstallActivity extends AppCompatActivity {
    InstallActivity activity;
    TextView user_points_text_view;
    TextView appName, appDesc, appCoin;
    Button installBtn;
    RoundedImageView appLogo;
    Boolean isUse = false, isComplete = false;
    public int poiints = 0, counter_dialog = 0;

    String logo, name, desc, link, pkg, coin, timer;
    String isEqual;
    long soundTime = 10000;
    private Handler timerHandler, pointsHandler;
    CountDownTimer collectClickTimer;
    int collectClickCounter = 0;
    boolean collectClickTimerTrue = false;

    boolean isInstallTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        activity = this;
        user_points_text_view = findViewById(R.id.points);
        appName = findViewById(R.id.appName);
        appDesc = findViewById(R.id.appDesc);
        appCoin = findViewById(R.id.appCoin);
        installBtn = findViewById(R.id.installBtn);
        appLogo = findViewById(R.id.appLogo);

        logo = getIntent().getStringExtra("logo");
        name = getIntent().getStringExtra("name");
        desc = getIntent().getStringExtra("desc");
        link = getIntent().getStringExtra("link");
        pkg = getIntent().getStringExtra("pkg");
        coin = getIntent().getStringExtra("coin");
        timer = getIntent().getStringExtra("timer");
        isEqual = getIntent().getStringExtra("isEqual");
       ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            onBackPressed();
        });
        Glide.with(activity)
                .load(logo)
                .placeholder(R.drawable.loading)
                .into(appLogo);
        appName.setText(name);
        appDesc.setText(Html.fromHtml(desc));
        appCoin.setText(coin);
        installBtn.setOnClickListener(v -> {
            boolean isPkgAvailable = isAppInstalled(activity, pkg);
            if (isComplete) {
                if (isPkgAvailable) {
                    showDialogPoints(1, coin);
                }
            } else {
                if (isUse) {
                    Toast.makeText(activity, "Now Use for " + timer + " minutes", Toast.LENGTH_SHORT).show();
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(pkg);
                    startActivity(launchIntent);
                    timerHandler = new Handler();
                    timerHandler.postDelayed(() -> {
                        pointsHandler = new Handler();
                        pointsHandler.postDelayed(() -> {
                            timerHandler = null;
                            pointsHandler = null;
                            isComplete = true;
                            installBtn.setText(getResources().getString(R.string.collect));
                        }, soundTime);
                    }, TimeUnit.MINUTES.toMillis(Long.parseLong(timer)));
                } else {
                    isInstallTrue = true;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(intent);
                }
            }
        });
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
        AdsManager.loadRewardedVideoAd(this);
        collectTimer();
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onResume() {
        user_points(user_points_text_view);
        boolean isPkgAvailable = isAppInstalled(activity, pkg);
        if (isPkgAvailable) {
            isUse = true;
            if (isComplete) {
                installBtn.setText(getResources().getString(R.string.collect));
            } else {
                installBtn.setText("Now Use for " + timer + " minutes");
            }
        }
        super.onResume();
    }

    private void showDialogPoints(final int addNoAddValueApps, final String points) {
        SweetAlertDialog sweetAlertDialog;

        if (AppController.isConnected(activity)) {
            if (addNoAddValueApps == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText("Oops!");
                    sweetAlertDialog.setContentText(getResources().getString(R.string.better_luck));
                    sweetAlertDialog.setConfirmText("Ok");
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText(getResources().getString(R.string.you_won));
                    sweetAlertDialog.setContentText(points);
                    sweetAlertDialog.setConfirmText("Collect");
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                sweetAlertDialog = new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setCanceledOnTouchOutside(false);
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setTitleText("Today chance is over");
                sweetAlertDialog.setConfirmText("Ok");
            }

            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    collectClickTimer.start();
                    if (collectClickTimerTrue) {
                        collectClickCounter++;
                    }
                    int finalPoint;
                    if (points.equals("")) {
                        finalPoint = 0;
                    } else {
                        finalPoint = Integer.parseInt(points);
                    }
                    poiints = finalPoint;
                    addPoint(activity, String.valueOf(poiints), "apps");
                    user_points(user_points_text_view);
                    sweetAlertDialog.dismiss();
                    installBtn.setVisibility(View.GONE);
                    if (AdsManager.isRewardedVideoAdLoaded(activity)) {
                        AdsManager.showRewardedVideo(new OnVideoAdEnded() {
                            @Override
                            public void videoWatched() {

                            }
                        }, activity);
                    }
                    isInstallTrue = false;
                    PrefManager.setString(activity, Helper.APP_DATE + isEqual, PrefManager.getSavedString(activity, Helper.TODAY_DATE));
                }
            }).show();
        } else {
            Toast.makeText(activity, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    void collectTimer() {
        collectClickTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                collectClickTimerTrue = true;
            }

            @Override
            public void onFinish() {
                collectClickTimerTrue = false;
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(InstallActivity.this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
        if (pointsHandler != null) {
            pointsHandler = null;
        }
        if (timerHandler != null) {
            timerHandler = null;
        }
    }
}