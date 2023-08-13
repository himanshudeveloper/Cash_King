package com.allrecipes.recipes5.csm;

import static com.allrecipes.recipes5.Just_base.addPoint;
import static com.allrecipes.recipes5.helper.Helper.VISIT_REQUEST_CODE;
import static com.allrecipes.recipes5.helper.PrefManager.BANNER_AD_TYPE;
import static com.allrecipes.recipes5.helper.PrefManager.CHARTBOOST_AD_TYPE;
import static com.allrecipes.recipes5.helper.PrefManager.INTERSTITAL_AD_TYPE;
import static com.allrecipes.recipes5.helper.PrefManager.getSavedString;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.OnVideoAdEnded;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.UpdateListener;
import com.allrecipes.recipes5.csm.adapter.WebsiteAdapter;
import com.allrecipes.recipes5.csm.model.WebsiteModel;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.Helper;
import com.allrecipes.recipes5.helper.PrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VisitActivity extends AppCompatActivity implements UpdateListener {

    VisitActivity activity;
    TextView user_points_text_view, noWebsiteFound;
    private Handler timerHandler, pointsHandler;
    public int poiints = 0;
    long soundTime = 10000;

    CountDownTimer collectClickTimer;
    int collectClickCounter = 0, visit_index = 0;
    boolean collectClickTimerTrue = false, isWebsiteVisited = false;

    String visit1_link, visit_time, visit_browser, visit_id;
    private WebsiteAdapter websiteAdapter;
    private ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
    private RecyclerView websiteRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        activity = this;
        noWebsiteFound = findViewById(R.id.noWebsiteFound);
        ImageView back = findViewById(R.id.back);
        user_points_text_view = findViewById(R.id.points);
        websiteRv = findViewById(R.id.websiteRv);
        websiteRv.setLayoutManager(new LinearLayoutManager(activity));
        String json = PrefManager.getSavedString(activity, Helper.WEBSITE_LIST);
        if (!json.equalsIgnoreCase("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WebsiteModel>>() {
            }.getType();
            ArrayList<WebsiteModel> websiteModels = gson.fromJson(json, type);
            for (int i = 0; i < websiteModels.size(); i++) {
                if (!PrefManager.getSavedString(activity, Helper.VISITED_DATE + websiteModels.get(i).getId()).equalsIgnoreCase(PrefManager.getSavedString(activity, Helper.TODAY_DATE))) {
                    websiteModelArrayList.add(websiteModels.get(i));
                }
            }
            websiteAdapter = new WebsiteAdapter(websiteModelArrayList, activity, "website", activity);
            websiteRv.setAdapter(websiteAdapter);
        }
        setWebsiteData();
        collectTimer();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (getSavedString(this, BANNER_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE) &&
                getSavedString(this, INTERSTITAL_AD_TYPE).equalsIgnoreCase(CHARTBOOST_AD_TYPE)
        ) {

        } else {
            AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
        }
        AdsManager.loadRewardedVideoAd(this);
    }

    private void setWebsiteData() {
        if (websiteModelArrayList.isEmpty()) {
            websiteRv.setVisibility(View.GONE);
            noWebsiteFound.setVisibility(View.VISIBLE);
        } else {
            websiteRv.setVisibility(View.VISIBLE);
            noWebsiteFound.setVisibility(View.GONE);
        }
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_points(user_points_text_view);
    }

    private void showDialogPoints(final int addNoAddValueVideo, final String points) {
        SweetAlertDialog sweetAlertDialog;

        if (PrefManager.isConnected(activity)) {
            if (addNoAddValueVideo == 1) {
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

            sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
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
                addPoint(activity, String.valueOf(poiints), "visit");
                user_points(user_points_text_view);
                sweetAlertDialog1.dismiss();
                if (AdsManager.isRewardedVideoAdLoaded(activity)) {
                    AdsManager.showRewardedVideo(new OnVideoAdEnded() {
                        @Override
                        public void videoWatched() {

                        }
                    }, activity);
                }
                websiteModelArrayList.remove(visit_index);
                websiteAdapter.notifyItemRemoved(visit_index);
                setWebsiteData();
                putDate(Helper.VISITED_DATE + visit_id, visit_id);
            }).show();
        } else {
            Toast.makeText(activity, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void putDate(String watched_date, String scratchType) {
        if (AppController.isConnected(activity)) {
            String currentDateWatched = PrefManager.getSavedString(activity, Helper.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDateWatched);
            String last_date_watched = PrefManager.getSavedString(activity, watched_date);
            if (last_date_watched.equalsIgnoreCase("0")) {
                last_date_watched = "";
            }
            Log.e("TAG", "onClick: last_date Date" + last_date_watched);
            if (last_date_watched.equals("")) {
                PrefManager.setString(activity, watched_date, currentDateWatched);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date_watched);
                    Date currentDAte = sdf.parse(currentDateWatched);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        PrefManager.setString(activity, watched_date, currentDateWatched);
                    } else {

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
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
        Common.openBackCustomTab(VisitActivity.this);
        finish();
    }


    private void visitLinks() {
        if (visit_browser.equalsIgnoreCase("external")) {
            openLink(visit1_link);
            timerHandler = new Handler();
            timerHandler.postDelayed(() -> {
                pointsHandler = new Handler();
                pointsHandler.postDelayed(() -> {
                    timerHandler = null;
                    pointsHandler = null;
                    showDialogPoints(1, String.valueOf(poiints));
                }, soundTime);
            }, TimeUnit.MINUTES.toMillis(Long.parseLong(visit_time)));
        } else {
            Intent intent = new Intent(activity, VisitLinksActivity.class);
            intent.putExtra("type", "website");
            intent.putExtra("websiteModal", websiteModelArrayList.get(visit_index));
            startActivityForResult(intent, VISIT_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VISIT_REQUEST_CODE && resultCode == RESULT_OK) {
            showDialogPoints(1, String.valueOf(poiints));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pointsHandler != null) {
            pointsHandler = null;
        }
        if (timerHandler != null) {
            timerHandler = null;
        }
    }

    @Override
    public void UpdateListener(
            @Nullable String coin,
            @Nullable String time,
            @Nullable String link,
            @Nullable String browser,
            @Nullable String id,
            int index,
            String description,
            String logo,
            String packages) {
        visit1_link = link;
        visit_time = time;
        visit_browser = browser;
        visit_id = id;
        visit_index = index;
        poiints = Integer.parseInt(coin);
        isWebsiteVisited = false;
        visitLinks();
    }
}