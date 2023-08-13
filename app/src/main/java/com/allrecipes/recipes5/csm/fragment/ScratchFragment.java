package com.allrecipes.recipes5.csm.fragment;

import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.Add_Coins_;
import static com.allrecipes.recipes5.helper.PrefManager.SCRATCH_COUNT;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.allrecipes.recipes5.AdsManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.helper.PrefManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class ScratchFragment extends Fragment implements ScratchListener {

    private LinearLayout adLayout;
    private Activity activity;
    private TextView scratch_count_textView, points_textView, points_text, name_txt;
    boolean first_time = true, scratch_first = true;
    private final String TAG = "Silver Fragment";
    private String random_points;
    ScratchCardLayout scratchCardLayout;
    CircleImageView pro_img;
    int scratchCount = 0;

    public ScratchFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public static ScratchFragment newInstance() {
        ScratchFragment fragment = new ScratchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scratch, container, false);
        if (getActivity() != null) {
            activity = getActivity();
        }
        adLayout = view.findViewById(R.id.banner_container);
        points_text = view.findViewById(R.id.textView_points_show);
        points_textView = view.findViewById(R.id.points);
        scratch_count_textView = view.findViewById(R.id.limit_text);
        scratchCardLayout = view.findViewById(R.id.scratch_view_layout);
        scratchCardLayout.setScratchListener(this);
        name_txt = view.findViewById(R.id.name_txt);
        name_txt.setText(AppController.getInstance().getFullname());
        TextView rank = view.findViewById(R.id.rank);
        rank.setText(AppController.getInstance().getRank());
        pro_img = view.findViewById(R.id.pro_img);
        Glide.with(this).load(AppController.getInstance().getProfile())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(pro_img);
        try {
            ImageView back = view.findViewById(R.id.back);
            back.setOnClickListener(view1 -> {
                getActivity().onBackPressed();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        check_spinner();
        setPointsText();
        AdsManager.loadInterstitalAd(activity);
        AdsManager.loadBannerAd(activity, view.findViewById(R.id.banner_container));
        return view;
    }

    private void setPointsText() {
        if (points_textView != null) {
            user_points(points_textView);
        }
    }

    private void setScratchCount(String string) {
        if (string != null || !string.equalsIgnoreCase(""))
            scratch_count_textView.setText("Your Today Scratch Count left = " + string);
    }

    @Override
    public void onScratchComplete() {
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete" + random_points);
            int counter = getLeftScratchCount(scratch_count_textView.getText().toString());
            if (counter <= 0) {
                Toast.makeText(activity, "Today chance is over please come back tomorrow", Toast.LENGTH_SHORT).show();
            } else {
                scratchCount = scratchCount + 1;
                String showAd = "false";
                if (Integer.parseInt(PrefManager.getSavedString(activity, SCRATCH_COUNT)) <= scratchCount) {
                    scratchCount = 0;
                    showAd = "true";
                }
                Add_Coins_(activity, random_points, "Scratch & Win Reward", showAd, () -> {
                    setPointsText();
                    scratch_first = true;
                    first_time = true;
                    scratchCardLayout.resetScratch();
                    check_spinner();
                });
            }
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (scratch_first) {
            scratch_first = false;
            Log.e(TAG, "onScratchStarted: " + random_points);
            if (AppController.isConnected((AppCompatActivity) activity)) {
                random_points = "";
                Random random = new Random();
                String str = PrefManager.getSavedString(activity, PrefManager.SCRATCH_COUNT_BEETWEN);
                try {
                    String[] parts = str.split("-", 2);
                    int low = Integer.parseInt(parts[0]);
                    int high = Integer.parseInt(parts[1]);
                    int result = random.nextInt(high - low) + low;
                    random_points = String.valueOf(result);
                    if (random_points == null || random_points.equalsIgnoreCase("null")) {
                        random_points = String.valueOf(1);
                    }
                } catch (Exception e) {
                    random_points = "2";
                }
                points_text.setText(random_points);
                Log.e(TAG, "onScratchStarted: " + random_points);
            } else {
                Toast.makeText(activity, "No internet connection please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onScratchStarted() {
        if (getLeftScratchCount(scratch_count_textView.getText().toString()) <= 0) {
            Toast.makeText(activity, "Today chance is over please come back tomorrow", Toast.LENGTH_SHORT).show();
        }
    }

    public int getLeftScratchCount(String attemptLeftText) {
        int leftCount = 0;
        try {
            String[] count = attemptLeftText.split("=", 2);
            leftCount = Integer.parseInt(count[1].trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leftCount;
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
                                setScratchCount(String.valueOf(leftt));
                                if (left >= daily) {
                                    scratchCardLayout.setEnabled(false);
                                } else {
                                    scratchCardLayout.setEnabled(true);
                                }
                            } else {
                                Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(activity.getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("scratch", "scratch");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}