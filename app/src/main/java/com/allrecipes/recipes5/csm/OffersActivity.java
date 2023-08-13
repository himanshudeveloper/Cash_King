package com.allrecipes.recipes5.csm;

import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.Refer_Activity;
import com.allrecipes.recipes5.csm.adapter.OfferToro_Adapter;
import com.allrecipes.recipes5.csm.model.OfferToro_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {
    ImageView back;
    private OfferToro_Adapter offerToro_adapter;
    private List<OfferToro_Model> offerToro_model = new ArrayList<>();
    RecyclerView rv_game;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(OffersActivity.this);
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
        setContentView(R.layout.activity_offers);
        TextView points = findViewById(R.id.points);
        user_points(points);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openBackCustomTab(OffersActivity.this);
                finish();
            }
        });

        rv_game = findViewById(R.id.rv_offer);

        Intent i = getIntent();
        try {
            JSONArray array = new JSONArray(i.getStringExtra("res"));
            for (int index=0; index < array.length(); index++)
            {   JSONObject feedObj = (JSONObject) array.get(index);
                String offer_id=(feedObj.getString("offer_id"));
                String offer_name = (feedObj.getString("offer_name"));
                String offer_desc = (feedObj.getString("offer_desc"));
                String call_to_action = (feedObj.getString("call_to_action"));
                String disclaimer = (feedObj.getString("disclaimer"));
                String offer_url = (feedObj.getString("offer_url"));
                String offer_url_easy = (feedObj.getString("offer_url_easy"));
                String payout = (feedObj.getString("payout"));
                String payout_type = (feedObj.getString("payout_type"));
                String amount = (feedObj.getString("amount"));
                String image_url = (feedObj.getString("image_url"));
                String image_url_220x124 = (feedObj.getString("image_url_220x124"));
                OfferToro_Model item = new OfferToro_Model(offer_id,offer_name,offer_desc,call_to_action,disclaimer,
                        offer_url,offer_url_easy,payout_type,amount,image_url,image_url_220x124);
                offerToro_model.add(item);
            }

            offerToro_adapter = new OfferToro_Adapter(offerToro_model,OffersActivity.this,1);
            rv_game.setLayoutManager(new LinearLayoutManager(OffersActivity.this));
            rv_game.setAdapter(offerToro_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Common.openBackCustomTab(OffersActivity.this);
            finish();
        }




    }
}