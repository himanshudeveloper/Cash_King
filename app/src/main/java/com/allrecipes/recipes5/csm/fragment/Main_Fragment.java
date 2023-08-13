package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.AD_UNIT_DATA_GET;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.DAILY_CHECKIN_API;
import static com.allrecipes.recipes5.helper.Constatnt.DAILY_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.Main_Url;
import static com.allrecipes.recipes5.helper.Constatnt.SPIN_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;
import static com.allrecipes.recipes5.helper.Constatnt.WHEEL_URL;
import static com.allrecipes.recipes5.helper.PrefManager.check_n;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.allrecipes.recipes5.Activity_Login;
import com.allrecipes.recipes5.PrefUtils;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.AppsActivity;
import com.allrecipes.recipes5.csm.FragViewerActivity;
import com.allrecipes.recipes5.csm.GameActivity;
import com.allrecipes.recipes5.csm.OfferWallActivity;
import com.allrecipes.recipes5.csm.OffersActivity;
import com.allrecipes.recipes5.csm.RefTaskActivity;
import com.allrecipes.recipes5.csm.VideoActivity;
import com.allrecipes.recipes5.csm.VisitActivity;
import com.allrecipes.recipes5.csm.adapter.GameAdapter;
import com.allrecipes.recipes5.csm.adapter.OfferToro_Adapter;
import com.allrecipes.recipes5.csm.adapter.SliderAdapter;
import com.allrecipes.recipes5.csm.model.GameModel;
import com.allrecipes.recipes5.csm.model.OfferToro_Model;
import com.allrecipes.recipes5.csm.model.SliderItems;
import com.allrecipes.recipes5.csm.model.WebsiteModel;
import com.allrecipes.recipes5.csm.model.offers_model;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.Constatnt;
import com.allrecipes.recipes5.helper.CustomVolleyJsonRequest;
import com.allrecipes.recipes5.helper.Helper;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.helper.PrefManager;
import com.allrecipes.recipes5.luck_draw.Activity_Notification;
import com.allrecipes.recipes5.watchandearn.activity.MainActivity;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.ironsource.mediationsdk.IronSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class Main_Fragment extends Fragment {
    private View root_view;
    String wallCode = "n6aVrg", userId = "1";
    int count = 0;
    TextView points, c_sub, name;
    Dialog dialog;
    Integer progress = 0;
    String p, res_game, res_offer;
    Boolean daily = false;
    private ViewPager2 viewPager2;
    LinearLayout pro_lin, offerwall_btn, video,apps_btn,visit_btn,scratch_btn;
    ImageView wheel;
    Boolean is_offer = false, is_game = false;

    TextView game_t;
    ShimmerFrameLayout game_shim;
    RecyclerView game_list, offer_t;
    GameAdapter game_adapter;
    private List<GameModel> gameModel = new ArrayList<>();
    private List<OfferToro_Model> offerToro_model = new ArrayList<>();
    private OfferToro_Adapter offerToro_adapter;
    private Handler sliderHandler = new Handler();

    private List<offers_model> offers = new ArrayList<>();
    private List<SliderItems> sliderItems = new ArrayList<>();
    CircleImageView pro_img;

    Boolean is_offer_loaded = false, isAppsLoaded = false,isWebsiteLoaded=false;
    String offerwalls;
    LinearLayout spin, game_btn, task, game_more, more_offer;
    SwipeRefreshLayout refreshLayout;
    ShowAds showAds = new ShowAds();
    FrameLayout nativeAds1, nativeAds2, nativeAds3;
    RelativeLayout adViewTop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_main, container, false);
        points = root_view.findViewById(R.id.points);
        name = root_view.findViewById(R.id.name);
        points.setText("0");
        check_n(getContext(), getActivity());

        name.setText(AppController.getInstance().getFullname());
        TextView rank = root_view.findViewById(R.id.rank);
        rank.setText(AppController.getInstance().getRank());

        viewPager2 = root_view.findViewById(R.id.viewPagerImageSlider);
        game_shim = root_view.findViewById(R.id.game_shimmer);
        scratch_btn = root_view.findViewById(R.id.scratch_btn);
        pro_img = root_view.findViewById(R.id.pro_img);
        pro_lin = root_view.findViewById(R.id.pro_lin);
        offer_t = root_view.findViewById(R.id.offer_t);
        wheel = root_view.findViewById(R.id.wheel);
        offerwall_btn = root_view.findViewById(R.id.offerwall_btn);
        apps_btn = root_view.findViewById(R.id.apps_btn);
        visit_btn = root_view.findViewById(R.id.visit_btn);
        video = root_view.findViewById(R.id.video);
        spin = root_view.findViewById(R.id.spin);
        game_t = root_view.findViewById(R.id.game_t);
        game_btn = root_view.findViewById(R.id.game_btn);
        task = root_view.findViewById(R.id.task);
        game_list = root_view.findViewById(R.id.game);
        game_more = root_view.findViewById(R.id.game_more);
        refreshLayout = root_view.findViewById(R.id.refreshLayout);
        more_offer = root_view.findViewById(R.id.more_offer);

        nativeAds1 = root_view.findViewById(R.id.nativeAds1);
        nativeAds2 = root_view.findViewById(R.id.nativeAds2);
        nativeAds2 = root_view.findViewById(R.id.nativeAds3);
        adViewTop = root_view.findViewById(R.id.adView_top);

        Glide.with(getContext()).load(WHEEL_URL)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(wheel);

        Paper.init(getActivity());

        if (Paper.book().contains("ShouldShowScratchSection")) {
            if (Paper.book().read("ShouldShowScratchSection").equals("true")) {
                scratch_btn.setVisibility(View.VISIBLE);
            } else {
                scratch_btn.setVisibility(View.GONE);
            }
        }


        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VideoActivity.class);
                startActivity(i);
                showAds.showInterstitialAds(getActivity());
            }
        });

        scratch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(), FragmentLoadingActivity.class);
//                i.putExtra(FRAGMENT_TYPE, FRAGMENT_SCRATCH);
//                startActivity(i);
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        more_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_offer) {
                    Intent i = new Intent(getContext(), OffersActivity.class);
                    i.putExtra("res", res_offer);
                    startActivity(i);
                    showAds.showInterstitialAds(getActivity());
                } else {
                    Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        game_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_game) {
                    Intent i = new Intent(getContext(), GameActivity.class);
                    i.putExtra("res", res_game);
                    startActivity(i);
                    showAds.showInterstitialAds(getActivity());
                } else {
                    Toast.makeText(getContext(), "Game is loading...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), RefTaskActivity.class);
                startActivity(i);
                showAds.showInterstitialAds(getActivity());
            }
        });

        game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GameActivity.class);
                i.putExtra("res", res_game);
                startActivity(i);
                showAds.showInterstitialAds(getActivity());
            }
        });

        game_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), GameActivity.class);
                i.putExtra("res", res_game);
                startActivity(i);
                showAds.showInterstitialAds(getActivity());
            }
        });

        pro_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FragViewerActivity.class);
                startActivity(i);
                showAds.showInterstitialAds(getActivity());
            }
        });

        offerwall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_offer_loaded) {
                    Intent i = new Intent(getContext(), OfferWallActivity.class);
                    i.putExtra("array", offerwalls);
                    i.putExtra("type", "o");
                    startActivity(i);
                    showAds.showInterstitialAds(getActivity());
                } else {
                    Toast.makeText(getContext(), "Offers is loading please wait...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        apps_btn.setOnClickListener(view -> {
            if (isAppsLoaded) {
                startActivity(new Intent(getContext(), AppsActivity.class));
                showAds.showInterstitialAds(getActivity());
            } else {
                Toast.makeText(getContext(), "Apps is loading please wait...", Toast.LENGTH_SHORT).show();
            }
        });
        visit_btn.setOnClickListener(view -> {
            if (isWebsiteLoaded) {
                startActivity(new Intent(getContext(), VisitActivity.class));
                showAds.showInterstitialAds(getActivity());
            } else {
                Toast.makeText(getContext(), "Articles is loading please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_offer_loaded) {
                    Intent i = new Intent(getContext(), OfferWallActivity.class);
                    i.putExtra("array", offerwalls);
                    i.putExtra("type", "v");
                    startActivity(i);
                    showAds.showInterstitialAds(getActivity());
                } else {
                    Toast.makeText(getContext(), "Videos is loading please wait...", Toast.LENGTH_SHORT).show();
                }
            }
        });



        parseJsonFeedd();


        Glide.with(getContext()).load(AppController.getInstance().getProfile())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(pro_img);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000); // slide duration 2 seconds
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((com.allrecipes.recipes5.csm.MainActivity) getActivity()).reloadMainFragment();
            }
        });

        load_game();

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        progress = 0;
        daily_Point();

        RelativeLayout bell = root_view.findViewById(R.id.bell);

        bell.setOnClickListener(view -> {
            Intent i = new Intent(getContext(), Activity_Notification.class);
            startActivity(i);
        });


        TextView badge = root_view.findViewById(R.id.badge);

        try {
            int notification_count = Integer.parseInt(AppController.getInstance().getBadge());
            if (notification_count != 0) {
                badge.setText("" + notification_count);
                badge.setVisibility(View.VISIBLE);
            } else {
                badge.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        final HashMap<String, String> subids = new HashMap<String, String>();
        subids.put("s2", "my sub id");
        getAppsSettingsFromAdminPannel();
        getVisitSettingsFromAdminPannel();
        return root_view;
    }

    private void getVisitSettingsFromAdminPannel() {
        if (AppController.isConnected((AppCompatActivity) requireActivity())) {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_visit_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST,
                        Constatnt.WEBSITE_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
                                JSONArray jb = response.getJSONArray("data");
                                for (int i = 0; i < jb.length(); i++) {
                                    JSONObject visitObject = jb.getJSONObject(i);
                                    if (visitObject.getString("is_visit_enable").equalsIgnoreCase("true")) {
                                        WebsiteModel websiteModel = new WebsiteModel(
                                                visitObject.getString("id"),
                                                visitObject.getString("is_visit_enable"),
                                                visitObject.getString("visit_title"),
                                                visitObject.getString("visit_link"),
                                                visitObject.getString("visit_coin"),
                                                visitObject.getString("visit_timer"),
                                                visitObject.getString("browser"),
                                                null,
                                                null,
                                                null
                                        );
                                        websiteModelArrayList.add(websiteModel);
                                    }
                                }
                                if (!websiteModelArrayList.isEmpty()) {
                                    Gson gson = new Gson();

                                    // getting data from gson and storing it in a string.
                                    String json = gson.toJson(websiteModelArrayList);
                                    PrefManager.setString(getActivity(), Helper.WEBSITE_LIST, json);
                                }else {
                                    PrefManager.setString(getActivity(), Helper.WEBSITE_LIST, "");
                                }
                                isWebsiteLoaded = true;
                            } else {
                                isWebsiteLoaded = true;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            isWebsiteLoaded = true;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "Slow Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        isWebsiteLoaded = true;
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000*30,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Withdraw Settings: excption " + e.getMessage().toString());
            }
        } else {
            Toast.makeText(requireActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void getAppsSettingsFromAdminPannel() {
            try {
                String tag_json_obj = "json_login_req";
                Map<String, String> map = new HashMap<>();
                map.put("get_apps_settings", "any");
                CustomVolleyJsonRequest customVolleyJsonRequest = new CustomVolleyJsonRequest(Request.Method.POST,
                        Constatnt.APPS_SETTINGS, map, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean status = response.getBoolean("status");
                            if (status) {
                                ArrayList<WebsiteModel> websiteModelArrayList = new ArrayList<>();
                                JSONArray jb = response.getJSONArray("data");
                                PrefManager.setString(requireActivity(), Helper.TODAY_DATE , response.getString("date"));
                                for (int i = 0; i < jb.length(); i++) {
                                    JSONObject visitObject = jb.getJSONObject(i);
                                    if (visitObject.getString("is_enable").equalsIgnoreCase("true")) {
                                        WebsiteModel websiteModel = new WebsiteModel(
                                                visitObject.getString("id"),
                                                visitObject.getString("is_enable"),
                                                visitObject.getString("title"),
                                                visitObject.getString("link"),
                                                visitObject.getString("coin"),
                                                visitObject.getString("timer"),
                                                null,
                                                visitObject.getString("_desc"),
                                                visitObject.getString("logo"),
                                                visitObject.getString("pkg")
                                        );
                                        websiteModelArrayList.add(websiteModel);
                                    }
                                }
                                if (!websiteModelArrayList.isEmpty()) {
                                    Gson gson = new Gson();
                                    // getting data from gson and storing it in a string.
                                    String json = gson.toJson(websiteModelArrayList);
                                    PrefManager.setString(getActivity(), Helper.APPS_LIST, json);
                                    isAppsLoaded = true;
                                } else {
                                    isAppsLoaded = true;
                                    PrefManager.setString(getActivity(), Helper.APPS_LIST, "");
                                }
                            } else {
                                isAppsLoaded = true;
                                PrefManager.setString(requireActivity(), Helper.APPS_LIST, "");
                            }

                        } catch (Exception e) {
                            isAppsLoaded = true;
                            e.printStackTrace();
                            if (getActivity() !=null) {
                            PrefManager.setString(getActivity(), Helper.APPS_LIST, "");
                        }}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (getActivity() !=null) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(getActivity(), "Slow internet connection", Toast.LENGTH_SHORT).show();
                            }
                            isAppsLoaded = true;
                            PrefManager.setString(getActivity(), Helper.APPS_LIST, "");
                        }
                    }
                });
                customVolleyJsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        1000*30,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);

            } catch (Exception e) {
                Log.e("TAG", "Withdraw Settings: excption " + e.getMessage().toString());
            }
    }


    private void LoadRedeemList() {

        JsonArrayRequest request = new JsonArrayRequest(Main_Url + "offerswj.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                offers.clear();
                for (int i = 0; i < array.length(); i++) {
                    try {

                        offerwalls = array.toString();
                        is_offer_loaded = true;

                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("id").trim();
                        String image = object.getString("image").trim();
                        String title = object.getString("title").trim();
                        String sub = object.getString("sub").trim();
                        String offer_name = object.getString("offer_name").trim();
                        String status = object.getString("status").trim();
                        String type = object.getString("type").trim();
                        if (type.equals("1")) {
                            SliderItems itemm = new SliderItems("1", title, sub, sub, offer_name, image);
                            sliderItems.add(itemm);
                        }

                        offers_model item = new offers_model(id, image, title, sub, offer_name, status);
                        offers.add(item);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2, getContext()));
            }
        }, error -> Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    private void daily_Point() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null, response -> {
            try {
                if (response.getString("error").equalsIgnoreCase("false")) {
                    daily = true;
                    p = response.getString("points");
                    SliderItems item = new SliderItems("0", "Daily Bonus", "Claim your daily bonus", p, "true", ".");
                    sliderItems.add(item);
                    LoadRedeemList();
                } else {
                    p = response.getString("points");
                    SliderItems item = new SliderItems("0", "Daily Bonus", "Claim your daily bonus", p, "false", ".");
                    sliderItems.add(item);
                    LoadRedeemList();
                }
            } catch (Exception e) {
            }
        },
                error -> {
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(DAILY_CHECKIN_API, API);
                params.put(USERNAME, AppController.getInstance().getUsername());
                params.put(SPIN_TYPE, DAILY_TYPE);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    private void parseJsonFeedd() {
    }

    public void load_game() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, response -> {
            VolleyLog.d(TAG, "Response: " + response.toString());
            if (response != null) {
                load_offer();
                set_game(response);
            }
        }, error -> {
            if (getActivity()!=null) {
                load_offer();
                Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("game", "game");
                params.put("id", AppController.getInstance().getId());
                params.put("usser", AppController.getInstance().getUsername());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void set_game(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("data");
            res_game = feedArray.toString();
            is_game = true;
            gameModel.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Integer id = (feedObj.getInt("id"));
                String title = (feedObj.getString("title"));
                String image = (feedObj.getString("image"));
                String game_link = (feedObj.getString("game"));
                GameModel item = new GameModel(id, title, image, game_link);
                gameModel.add(item);
            }
            game_adapter = new GameAdapter(gameModel, getActivity(), 0);
            game_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            RelativeLayout lin_game_c = root_view.findViewById(R.id.lin_game_c);

            game_list.setAdapter(game_adapter);
            game_shim.setVisibility(View.GONE);
            lin_game_c.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        showAds.destroyBanner();
        IronSource.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
        user_points(points);
        IronSource.onResume(getActivity());
        showAds.showTopBanner(getActivity(), adViewTop);
        showAds.showNativeAds(getActivity(), nativeAds1);
        new Handler().postDelayed(() ->{
            showAds.showNativeAds(getActivity(), nativeAds2);
        }, 2000);
        new Handler().postDelayed(() ->{
            showAds.showNativeAds(getActivity(), nativeAds3);
        }, 2000);
    }

    Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    public void load_offer() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, response -> pass_offer(response), error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("get_offer_toro", "game");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void pass_offer(JSONObject response) {
        ShimmerFrameLayout offer_toro_shimmer = root_view.findViewById(R.id.offer_toro_shimmer);
        try {
            JSONObject offers_json = response.getJSONObject("response");
            JSONArray feedArray = offers_json.getJSONArray("offers");
            res_offer = feedArray.toString();
            is_offer = true;
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                String offer_id = (feedObj.getString("offer_id"));
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
                OfferToro_Model item = new OfferToro_Model(offer_id, offer_name, offer_desc, call_to_action, disclaimer,
                        offer_url, offer_url_easy, payout_type, amount, image_url, image_url_220x124);
                offerToro_model.add(item);
            }

            offerToro_adapter = new OfferToro_Adapter(offerToro_model, getContext(), 0);
            offer_t.setLayoutManager(new LinearLayoutManager(getContext()));

            offer_t.setAdapter(offerToro_adapter);
            offer_toro_shimmer.stopShimmer();
            offer_toro_shimmer.setVisibility(View.GONE);
            offer_t.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            root_view.findViewById(R.id.more_offer).setVisibility(View.GONE);
            offer_toro_shimmer.stopShimmer();
            offer_toro_shimmer.setVisibility(View.GONE);
        }
    }
}





