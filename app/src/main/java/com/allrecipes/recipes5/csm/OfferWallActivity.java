package com.allrecipes.recipes5.csm;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.Activity_otp.randomAlphaNumeric;
import static com.allrecipes.recipes5.helper.AppController.getInstance;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.IRON_SOURCE_APP_KEY;
import static com.allrecipes.recipes5.helper.PrefManager.getSavedString;
import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.Refer_Activity;
import com.allrecipes.recipes5.csm.adapter.OfferWall_Adapter;
import com.allrecipes.recipes5.csm.model.OfferWall_Model;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ironsource.mediationsdk.IronSource;
import com.offertoro.sdk.OTOfferWallSettings;
import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.callback.PollfishOpenedListener;
import com.pollfish.callback.PollfishUserNotEligibleListener;
import com.pollfish.callback.PollfishUserRejectedSurveyListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferWallActivity extends AppCompatActivity {
    private OfferWall_Adapter offerWall_adapter;
    private List<OfferWall_Model> offers = new ArrayList<>();
    RecyclerView rv_offer;
    Params params;
    TextView title;
    ImageView back;
    TextView points;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(OfferWallActivity.this);
        finish();
    }

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
        setContentView(R.layout.activity_offer_wall);
        if (AdsManager.isInterstitialLoaded()) {
            AdsManager.showInterstitalAd(this);
        }
        points = findViewById(R.id.points);
        rv_offer = findViewById(R.id.rv_offer);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openBackCustomTab(OfferWallActivity.this);
                finish();
            }
        });


        settings();


        Intent i = getIntent();

        try {
            JSONArray array = new JSONArray(i.getStringExtra("array"));
            for (int index = 0; index < array.length(); index++) {
                JSONObject feedObj = (JSONObject) array.get(index);
                switch (i.getStringExtra("type")) {
                    case "o":
                        title.setText("OfferWalls");
                        if (feedObj.getString("offer_name").equals("adget") || feedObj.getString("offer_name").equals("toro") ||
                                feedObj.getString("offer_name").equals("ayet") ||
                                feedObj.getString("offer_name").equals("iron_offer")
                        ) {
                            OfferWall_Model item = new OfferWall_Model(feedObj.getString("title"), feedObj.getString("sub"),
                                    feedObj.getString("image"), feedObj.getString("offer_name"));
                            offers.add(item);
                        }
                        break;
                    case "v":
                        title.setText("Videos");
                        Log.e(TAG, "onCreate: " + feedObj.getString("offer_name"));
                        if (feedObj.getString("offer_name").equals("fb") ||
                                feedObj.getString("offer_name").equals("iron") ||
                                feedObj.getString("offer_name").equals("unity") ||
                                feedObj.getString("offer_name").equals("start")
                                || feedObj.getString("offer_name").equals("colony")
                                || feedObj.getString("offer_name").equals("applovin")
                                || feedObj.getString("offer_name").equals("admob")
                                || feedObj.getString("offer_name").equals("vungle")
                                || feedObj.getString("offer_name").equals("startapp")
                                || feedObj.getString("offer_name").equals("chartboost")
                            // || feedObj.getString("offer_name").equals("yodo1")
                        ) {
                            OfferWall_Model item = new OfferWall_Model(feedObj.getString("title"), feedObj.getString("sub"),
                                    feedObj.getString("image"), feedObj.getString("offer_name"));
                            offers.add(item);
                        }
                }
            }

            if (title.getText().toString().trim().equalsIgnoreCase("Videos")) {
                IronSource.init(
                        OfferWallActivity.this,
                        getSavedString(AppController.getInstance(), IRON_SOURCE_APP_KEY),
                        IronSource.AD_UNIT.REWARDED_VIDEO
                );
            }

            offerWall_adapter = new OfferWall_Adapter(offers, OfferWallActivity.this);
            rv_offer.setLayoutManager(new LinearLayoutManager(OfferWallActivity.this));
            rv_offer.setAdapter(offerWall_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Common.openBackCustomTab(OfferWallActivity.this);
            finish();
        }

    }

    public void settings() {
        // showpDialog();
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);


                    // Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OfferWallActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                //  hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("settings", "settings");
                return params;
            }
        };
        // Adding request to volley request queue
        getInstance().addToRequestQueue(stringRequest);
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("set");

            //Toast.makeText(this,":"+feedArray.length() , Toast.LENGTH_SHORT).show();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                //Toast.makeText(MainActivity.this,feedObj.getString("check_p"),Toast.LENGTH_LONG).show();

                String OT_APP_ID = (feedObj.getString("OT_APP_ID"));
                String OT_KEY = (feedObj.getString("OT_KEY"));
                String PF_ID = (feedObj.getString("PF_ID"));
                String AG_WALLCODE = (feedObj.getString("AG_WALLCODE"));
                String check_ag = (feedObj.getString("check_ag"));
                String check_ot = (feedObj.getString("check_ot"));
                String check_p = (feedObj.getString("check_p"));

                if (check_ot.equals("0")) {
                    ot(OT_APP_ID, OT_KEY);
                }

                if (check_p.equals("0")) {
                    poll(PF_ID);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            Toast.makeText(OfferWallActivity.this, e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    public void ot(String OT_APP_ID, String OT_KEY) {
        if (!OT_KEY.equalsIgnoreCase("") && !OT_KEY.equalsIgnoreCase("0") && !OT_KEY.equalsIgnoreCase("null")) {
            OTOfferWallSettings.getInstance().configInit(OT_APP_ID,
                    OT_KEY, getInstance().getUsername());
        } else {
            int position = -1;
            for (int i = 0; i < offers.size(); i++) {
                OfferWall_Model model = offers.get(i);
                if (model.getType().equalsIgnoreCase("toro")) {
                    position = i;
                }
            }
            if (position != -1) {
                offers.remove(position);
                offerWall_adapter.notifyItemRemoved(position);
            }
        }
    }

    public void poll(String poll) {
        String code = randomAlphaNumeric(15);
        params = new Params.Builder(poll)
                .requestUUID(getInstance().getId())
                .rewardMode(true)
                .offerwallMode(true)
                .releaseMode(false)
                .pollfishSurveyNotAvailableListener(() -> Toast.makeText(OfferWallActivity.this, "not available", Toast.LENGTH_SHORT).show())
                .pollfishUserNotEligibleListener(new PollfishUserNotEligibleListener() {
                    @Override
                    public void onUserNotEligible() {
                        Toast.makeText(OfferWallActivity.this, "not available", Toast.LENGTH_SHORT).show();

                    }
                })
                .pollfishOpenedListener(new PollfishOpenedListener() {
                    @Override
                    public void onPollfishOpened() {
                        Toast.makeText(OfferWallActivity.this, "not available", Toast.LENGTH_SHORT).show();

                    }
                })
                .pollfishUserRejectedSurveyListener(new PollfishUserRejectedSurveyListener() {
                    @Override
                    public void onUserRejectedSurvey() {
                        Toast.makeText(OfferWallActivity.this, "not available", Toast.LENGTH_SHORT).show();

                    }
                })
                .signature(code)
                .build();

        Pollfish.initWith(this, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_points(points);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}