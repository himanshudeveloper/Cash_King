package com.allrecipes.recipes5.csm;

import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.Add_Coins_;
import static com.allrecipes.recipes5.helper.PrefManager.EXTRA_SPIN_COUNT;
import static com.allrecipes.recipes5.helper.PrefManager.SPIN_COUNT;
import static com.allrecipes.recipes5.helper.PrefManager.SPIN_COUNT_PER_DAY;
import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.topsheet.ChanceOverDialog;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.helper.PrefManager;
import com.allrecipes.recipes5.luckywheel.LuckyWheelView;
import com.allrecipes.recipes5.luckywheel.model.LuckyItem;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class VideoActivity extends AppCompatActivity {
    LuckyWheelView luckyWheelView;
    List<LuckyItem> data = new ArrayList<>();
    LinearLayout spin_btn;
    LinearLayout back,extra_spin_btn;
    TextView spins, name_txt, spin_text;
    CircleImageView pro_img;
    Boolean isSpin = false;
    int spinCount = 0;
    boolean isChanceOver = false, isUserAdViewed = false;
    private MediaPlayer spinSound;
    private VideoActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_video);
        activity = this;
        AdsManager.loadInterstitalAd(activity);
        AdsManager.loadBannerAd(activity, findViewById(R.id.banner_ad_container));
        luckyWheelView = findViewById(R.id.viewLuckyWheel);
        spin_btn = findViewById(R.id.spin_btn);
        extra_spin_btn = findViewById(R.id.extra_spin_btn);
        spinSound = MediaPlayer.create(this, R.raw.wheelsound);
        back = findViewById(R.id.pro_lin);
        spins = findViewById(R.id.spins);
        pro_img = findViewById(R.id.pro_img);
        name_txt = findViewById(R.id.name_txt);
        name_txt.setText(AppController.getInstance().getFullname());
        spin_text = findViewById(R.id.spin_text);
        TextView points = findViewById(R.id.points);
        user_points(points);
        check_spinner();
        Glide.with(this).load(AppController.getInstance().getProfile())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(pro_img);
        TextView rank = findViewById(R.id.rank);
        rank.setText(AppController.getInstance().getRank());
        spin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChanceOver) {
                    // Show video ad for 1 spin
                    if (isUserAdViewed) {
                        StartSpin();
                        isUserAdViewed = false;
                    } else {
                        extra_spin_btn.performClick();
                    }
                } else {
                    StartSpin();
                }
            }
        });

        extra_spin_btn.setOnClickListener(v-> {
            if (Integer.parseInt(PrefManager.getSavedString(VideoActivity.this, SPIN_COUNT_PER_DAY)) >= getExtraSpinCount()) {
                if (!AdsManager.isRewardedVideoAdLoaded(VideoActivity.this)) {
                    AdsManager.loadRewardedVideoAd(VideoActivity.this);
                    Toast.makeText(VideoActivity.this, "Loading video ad please wait a moment and click again for extra spin!", Toast.LENGTH_SHORT).show();
                } else {
                    AdsManager.showRewardedVideo(() -> {
                        increaseDailySpinCounter();
                        isUserAdViewed = true;
                        spin_text.setText("Spin");
                        showSpinButtonAndHideExtraSpinButton();
                        showExtraSpinDialog();
                    }, VideoActivity.this);
                }
            } else {
                Toast.makeText(this, "Today chance is over please come back tomorrow", Toast.LENGTH_SHORT).show();
            }
        });

        spin_btn.setEnabled(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSpin) {
                    Toast.makeText(VideoActivity.this, "Wait for the spinner to finish!", Toast.LENGTH_SHORT).show();
                } else {
                    Common.openBackCustomTab(VideoActivity.this);
                    finish();
                }
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                spinSound.stop();
                spinCount = spinCount + 1;
                String poss = "" + index;
                spin_btn.setEnabled(true);
                isSpin = false;
                String DAILY_TYPE = "Spin & Win Reward";
                if (isChanceOver) {
                    DAILY_TYPE = "Spin & Win";
                }
                String showAd = "false";
                if (Integer.parseInt(PrefManager.getSavedString(VideoActivity.this, SPIN_COUNT)) <= spinCount) {
                    spinCount = 0;
                    showAd = "true";
                }
                switch (poss) {
                    case "0":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_1), DAILY_TYPE, showAd,null);
                        break;
                    case "1":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_2), DAILY_TYPE, showAd,null);
                        break;
                    case "2":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_3), DAILY_TYPE, showAd,null);
                        break;
                    case "3":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_4), DAILY_TYPE, showAd,null);
                        break;
                    case "4":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_5), DAILY_TYPE, showAd,null);
                        break;
                    case "5":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_6), DAILY_TYPE, showAd,null);
                        break;
                    case "6":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_7), DAILY_TYPE, showAd,null);
                        break;
                    case "7":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_8), DAILY_TYPE, showAd,null);
                        break;
                    case "8":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_9), DAILY_TYPE, showAd,null);
                        break;
                    case "9":
                        Add_Coins_(VideoActivity.this, getString(R.string.spin_coin_10), DAILY_TYPE, showAd,null);
                        break;
                    default:
                        Toast.makeText(VideoActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                if (!isChanceOver) {
                    check_spinner();
                    showSpinButtonAndHideExtraSpinButton();
                } else {
                    showExtraSpinButtonAndHideSpinButton();
                    spin_text.setText("Get 1 Spin");
                }
                user_points(points);
            }
        });

        // luckyWheelView.setData();
        create(0, getString(R.string.spin_coin_1) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_1), new int[]{10}, 1, ContextCompat.getColor(this, R.color.wheel_b));
        create(1, getString(R.string.spin_coin_2) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_2), new int[]{10}, 2, ContextCompat.getColor(this, R.color.wheel_b));
        create(2, getString(R.string.spin_coin_3) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_3), new int[]{10}, 3, ContextCompat.getColor(this, R.color.wheel_b));
        create(3, getString(R.string.spin_coin_4) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_4), new int[]{10}, 20, ContextCompat.getColor(this, R.color.wheel_b));
        create(4, getString(R.string.spin_coin_5) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_5), new int[]{10}, 30, ContextCompat.getColor(this, R.color.wheel_b));
        create(5, getString(R.string.spin_coin_6) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_6), new int[]{10}, 40, ContextCompat.getColor(this, R.color.wheel_b));
        create(6, getString(R.string.spin_coin_7) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_7), new int[]{10}, 50, ContextCompat.getColor(this, R.color.wheel_b));
        create(7, getString(R.string.spin_coin_8) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_8), new int[]{10}, 60, ContextCompat.getColor(this, R.color.wheel_b));
        create(8, getString(R.string.spin_coin_9) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_9), new int[]{10}, 70, ContextCompat.getColor(this, R.color.wheel_b));
        create(9, getString(R.string.spin_coin_10) + " coins   ", R.drawable.rupee, ContextCompat.getColor(this, R.color.wheel_8), new int[]{10}, 80, ContextCompat.getColor(this, R.color.wheel_b));
        luckyWheelView.setData(data);
        spin_btn.setEnabled(false);
    }

    private void increaseDailySpinCounter() {
        PrefManager.setInt(
                this,
                EXTRA_SPIN_COUNT,
                (PrefManager.getSavedInt(this,EXTRA_SPIN_COUNT) + 1)
        );
    }

    private int getExtraSpinCount() {
        return PrefManager.getSavedInt(this, EXTRA_SPIN_COUNT);
    }

    private void showExtraSpinDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChanceOverDialog newFragment = new ChanceOverDialog();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private void StartSpin() {
        spin_btn.setEnabled(false);
        isSpin = true;
        spinSound = null;
        spinSound = MediaPlayer.create(VideoActivity.this, R.raw.wheelsound);
        final int random = new Random().nextInt((9 - 0) + 1) + 0;
        spinSound.start();
        luckyWheelView.startLuckyWheelWithTargetIndex(random);
    }


    private void showChanceOverDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChanceOverDialog newFragment = new ChanceOverDialog();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    public void create(int index, String partTitle, int partIcon, int partColor, int[] itemChance, int itemReward, int bColor) {
        LuckyItem item = new LuckyItem(partTitle, partIcon, partColor, itemChance, itemReward, bColor);
        data.add(item);
    }

    public void check_spinner() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                int daily = response.getInt("daily");
                                int left = response.getInt("left");
                                int leftt = daily - left;
                                PrefManager.setString(VideoActivity.this, SPIN_COUNT, String.valueOf(leftt));
                                spins.setText(String.valueOf(leftt));
                                if (left >= daily) {
                                    spin_btn.setEnabled(true);
                                    isChanceOver = true;
                                    showExtraSpinButtonAndHideSpinButton();
                                    spin_text.setText("Get 1 Spin");
                                } else {
                                    PrefManager.setInt(
                                            VideoActivity.this,
                                            EXTRA_SPIN_COUNT,
                                            0
                                    );
                                    isChanceOver = false;
                                    spin_btn.setEnabled(true);
                                    showSpinButtonAndHideExtraSpinButton();
                                }
                            } else {
                                Toast.makeText(VideoActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

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
                params.put("usernamee", AppController.getInstance().getUsername());
                params.put("spinner", "spinner");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void showExtraSpinButtonAndHideSpinButton() {
        extra_spin_btn.setVisibility(View.VISIBLE);
        spin_btn.setVisibility(View.GONE);
        spins.setVisibility(View.GONE);
    }

    public void showSpinButtonAndHideExtraSpinButton() {
        extra_spin_btn.setVisibility(View.GONE);
        spin_btn.setVisibility(View.VISIBLE);
        if (!isChanceOver) {
            spins.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSpin) {
            Toast.makeText(this, "Wait for the spinner to finish!", Toast.LENGTH_SHORT).show();
        } else {
            Common.openBackCustomTab(VideoActivity.this);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        spinSound = null;
        data.clear();
        activity = null;
        super.onDestroy();
    }
}