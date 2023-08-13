package com.allrecipes.recipes5.csm;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_LOAD_WEB_VIEW;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_TYPE;
import static com.allrecipes.recipes5.helper.PrefManager.Add_Coins_;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.FragmentLoadingActivity;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.Refer_Activity;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivity extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    Float progress = 0f, maxpro = 60f;
    Boolean isover = false, limit = false, check_play = false;
    String url;
    CardView close, close3, play3, play2;
    LinearLayout loading;
    ImageView cut;
    TextView mints, coins, title;
    CountDownTimer countDownTimer;
    int sec = 0;
    int total_sec = 360; // in seconds
    int min = 60;
    //reward int
    int minuts, reward, time = 60;
    RelativeLayout claim, first, second, third, main_page, play;
    CircleImageView img;
    int pro = 0;
    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(PlayActivity.this);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        first = findViewById(R.id.progress);
        second = findViewById(R.id.claim_layout);
        third = findViewById(R.id.warn);
        mints = findViewById(R.id.minuts);
        coins = findViewById(R.id.coins);
        play = findViewById(R.id.play);
        close = findViewById(R.id.close);
        close3 = findViewById(R.id.close3);
        play3 = findViewById(R.id.play3);
        claim = findViewById(R.id.claim);
        play2 = findViewById(R.id.play2);
        loading = findViewById(R.id.loading);
        cut = findViewById(R.id.cut);
        main_page = findViewById(R.id.main_page);
        img = findViewById(R.id.img);
        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar);

        third.setVisibility(View.GONE);
        first.setVisibility(View.GONE);
        second.setVisibility(View.GONE);
        main_page.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);

        progressBar.setMax(total_sec);


        circularProgressBar = findViewById(R.id.PBar);
        circularProgressBar.setProgressWithAnimation(progress, 1000L); // =1s
        circularProgressBar.setProgressMax(maxpro);
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(-180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);

        getList();

        count();
        Intent i = getIntent();
        url = i.getStringExtra("url");

        Glide.with(this).load(i.getStringExtra("image"))
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(img);

        title.setText(i.getStringExtra("name"));

        AdsManager.loadInterstitalAd(this);

        cut.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                Common.openBackCustomTab(PlayActivity.this);
                finish();
            }
        });
        play.setOnClickListener(view -> {
            check_play = true;
            launchWebView();
            main_page.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        });
        play2.setOnClickListener(view -> {
            check_play = true;
            launchWebView();
            main_page.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

        });
        play3.setOnClickListener(view -> launchWebView());
        close.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                Common.openBackCustomTab(PlayActivity.this);
                finish();
            }

        });
        close3.setOnClickListener(view -> {
            if (AdsManager.isInterstitialLoaded()) {
                AdsManager.showInterstitalAd(PlayActivity.this);
                finish();
            } else {
                Common.openBackCustomTab(PlayActivity.this);
                finish();
            }
        });
    }

    private void count() {

        int total_sec_current = total_sec - sec;
        total_sec_current = total_sec_current * 1000;
        // Toast.makeText(PlayActivity.this, ""+total_sec_current, Toast.LENGTH_SHORT).show();

        countDownTimer = new CountDownTimer(total_sec_current, 1000) {
            public void onFinish() {
                // When timer is finished
                // Execute your code here
                isover = true;
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
                Log.e(TAG, "onTick: ");
                if (!(pro >= total_sec)) {
                    pro += 1;
                    progressBar.setProgress(pro);
                }
                if (!(sec >= min)) {
                    progress = progress + 1f;
                    circularProgressBar.setProgressWithAnimation(progress, 1000L); // =1s
                }
                sec = sec + 1;
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (limit) {
            if (check_play) {
                if (!isover) {
                    count();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.cancel();
        if (limit) {
            if (check_play) {
                check_play = false;
                if ((sec >= min)) {
                    main_page.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    minuts = sec / time;
                    reward = minuts * Integer.parseInt(getResources().getString(R.string.game_points));
                    mints.setText(minuts + " min");
                    coins.setText(reward + "");
                    claim.setOnClickListener(view -> Add_Coins_(PlayActivity.this, "" + reward, "GameZone", "true",null));
                } else {
                    main_page.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }


    public void getList() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, response -> {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }, error -> {
                    Toast.makeText(PlayActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("check_zone", AppController.getInstance().getUsername());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            if (response.getString("error").equals("false")) {
                limit = true;
                check_play = true;
                launchWebView();
            } else {
                limit = false;
                TextView text2 = findViewById(R.id.text2);
                text2.setText(response.getString("msg"));
                third.setVisibility(View.GONE);
                first.setVisibility(View.GONE);
                second.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                main_page.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(PlayActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void launchWebView() {
        Intent intent= new Intent(this, FragmentLoadingActivity.class);
        intent.putExtra(FRAGMENT_TYPE, FRAGMENT_LOAD_WEB_VIEW);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
