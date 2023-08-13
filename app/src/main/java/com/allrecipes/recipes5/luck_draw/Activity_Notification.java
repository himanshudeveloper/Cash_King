package com.allrecipes.recipes5.luck_draw;


import static com.allrecipes.recipes5.helper.AppController.hidepDialog;
import static com.allrecipes.recipes5.helper.AppController.initpDialog;
import static com.allrecipes.recipes5.helper.AppController.showpDialog;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.setWindowFlag;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.Common;
import com.allrecipes.recipes5.csm.PlayActivity;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.luck_draw.Dialog.Dialog_Activity;
import com.allrecipes.recipes5.luck_draw.adapter.ActivityItem;
import com.allrecipes.recipes5.luck_draw.adapter.ActivityListAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Activity_Notification extends AppCompatActivity {

    private RecyclerView listView;
    ActivityListAdapter adapter;
    private List<ActivityItem> historyList = new ArrayList<>();
    private View root_view;
    TextView nodata;
    int RecyclerViewItemPosition;
    View view;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog Asycdialog;
    ArrayList<String> get_date;
    ArrayList<String> get_title;
    ArrayList<String> get_message;
    AppCompatActivity getContext;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        Window window = this.getWindow();
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
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_ad_container));
        listView = (RecyclerView) findViewById(R.id.listview);
        linearLayoutManager = new LinearLayoutManager(getContext);
//        Asycdialog = new ProgressDialog(getContext);
        nodata = (TextView) findViewById(R.id.nodata);
        get_date = new ArrayList<>();
        get_title = new ArrayList<>();
        get_message = new ArrayList<>();
        listView.setLayoutManager(linearLayoutManager);
        initpDialog(this);
        ActivityData();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openBackCustomTab(Activity_Notification.this);
                finish();
            }
        });

        listView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getContext, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (view != null && gestureDetector.onTouchEvent(motionEvent)) {
                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);
                    full_details1(get_title.get(RecyclerViewItemPosition), get_message.get(RecyclerViewItemPosition), get_date.get(RecyclerViewItemPosition));
                    // Showing RecyclerView Clicked Item value using Toast.
                    // Toast.makeText(getActivity(), Game_url_arrey.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


    }

    public void full_details1(String get_title, String get_message, String get_date) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Dialog_Activity newFragment = new Dialog_Activity();
        Bundle bundle = new Bundle();
        bundle.putString("get_title", get_title);
        bundle.putString("get_message", get_message);
        bundle.putString("get_date", get_date);
        newFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    public void ActivityData() {
        showpDialog();
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("TAG", "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //t.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("activity", API);
                return params;
            }

        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            if (response.getJSONArray("feed") == response.getJSONArray("feed")) {
                historyList.clear();
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    get_title.add(feedObj.getString("title"));
                    get_date.add(feedObj.getString("date"));
                    get_message.add(feedObj.getString("message"));
                    String title = feedObj.getString("title");
                    String message = feedObj.getString("message");
                    String date = feedObj.getString("date");
                    ActivityItem model = new ActivityItem(title, message, date);
                    historyList.add(model);
                }
                adapter = new ActivityListAdapter(historyList);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                hidepDialog();
            } else {

                listView.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            hidepDialog();
        }
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Common.openBackCustomTab(Activity_Notification.this);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}




