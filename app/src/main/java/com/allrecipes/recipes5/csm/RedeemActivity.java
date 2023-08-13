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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.adapter.Redeem_adapter;
import com.allrecipes.recipes5.csm.adapter.Redeem_model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {
    private List<Redeem_model> model = new ArrayList<>();
    RecyclerView list;
    Redeem_adapter adapter;
    ImageView back;
    LinearLayout history;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(RedeemActivity.this);
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
        setContentView(R.layout.activity_redeem);

        list = findViewById(R.id.list);
        back = findViewById(R.id.back);
        history = findViewById(R.id.history);
        TextView points = findViewById(R.id.points);
        user_points(points);


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RedeemActivity.this, TransActivity.class);
                startActivity(i);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openBackCustomTab(RedeemActivity.this);
                finish();
            }
        });

        Intent i = getIntent();

        try {
            JSONArray array = new JSONArray(i.getStringExtra("arry"));
            for (int index=0; index < array.length(); index++)
            {
                JSONObject feedObj = (JSONObject) array.get(index);
              //  Integer id = (feedObj.getInt("id"));
                String coins=(feedObj.getString("coins"));
                String amount=(feedObj.getString("amount"));
                String amount_id=(feedObj.getString("id"));
                String image = i.getStringExtra("image");
                String input = i.getStringExtra("input");
                String hint = i.getStringExtra("hint");
                String title = i.getStringExtra("title");
                String symb = i.getStringExtra("symb");
                String id = i.getStringExtra("id");
                String type = i.getStringExtra("type");
                String details = i.getStringExtra("details");
                //GameModel item = new GameModel(id,title,image,game_link);
                //gameModel.add(item);
                Redeem_model item = new Redeem_model(image, coins, amount,symb,input,hint,title,id,type,details,amount_id);
                model.add(item);
            }
                adapter = new Redeem_adapter(model,RedeemActivity.this);
                list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
                list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));

    }
}