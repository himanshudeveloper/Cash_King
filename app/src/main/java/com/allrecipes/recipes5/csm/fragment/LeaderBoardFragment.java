package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.LEADER;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.adapter.LeaderboardAdapter;
import com.allrecipes.recipes5.csm.model.Leaderboard_model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardFragment extends Fragment {
    View root_view;
    RecyclerView list;
    List<Leaderboard_model> model = new ArrayList<>();
    LeaderboardAdapter adapter;

    CircleImageView leader_one_img,leader_two_img,leader_three_img;
    TextView tag_1,tag_2,tag_3,text1,text2,text3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        leader_one_img = root_view.findViewById(R.id.leader_one_img);
        leader_two_img = root_view.findViewById(R.id.leader_two_img);
        leader_three_img = root_view.findViewById(R.id.leader_three_img);
        tag_1 = root_view.findViewById(R.id.tag_1);
        tag_2 = root_view.findViewById(R.id.tag_2);
        tag_3 = root_view.findViewById(R.id.tag_3);
        text1 = root_view.findViewById(R.id.text1);
        text2 = root_view.findViewById(R.id.text2);
        text3 = root_view.findViewById(R.id.text3);
        list = root_view.findViewById(R.id.list);

        getquizlist();


        return root_view;
    }

    public void getquizlist() {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST,
                Base_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                    //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                //  hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(LEADER, LEADER);
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void parseJsonFeed(JSONObject response) {
        try {



            JSONArray feedArray = response.getJSONArray("data");
            model.clear();
            int count = 0;
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                count++;
                String name = (feedObj.getString("name"));
                String image=(feedObj.getString("image"));
                String points = (feedObj.getString("points"));
                String tag = "#"+count;

                if (getActivity() == null) {
                    return;
                }else {
                    if (count==1)
                    {
                        Glide.with(getContext()).load(image)
                                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                                .into(leader_one_img);
                        tag_1.setText(tag);
                        text1.setText(name);
                    }
                    else if (count==2)
                    {
                        Glide.with(getContext()).load(image)
                                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                                .into(leader_two_img);
                        tag_2.setText(tag);
                        text2.setText(name);
                    }
                    else if (count==3)
                    {
                        Glide.with(getContext()).load(image)
                                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                                .into(leader_three_img);
                        tag_3.setText(tag);
                        text3.setText(name);
                    }
                    else
                    {
                        Leaderboard_model item = new Leaderboard_model(name, points, image,tag);
                        model.add(item);
                    }
                }



            }

            adapter = new LeaderboardAdapter(model, getContext());

            ShimmerFrameLayout shimmer = root_view.findViewById(R.id.shimmer);
            NestedScrollView listt = root_view.findViewById(R.id.listt);

            list.setHasFixedSize(true);
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            list.setAdapter(adapter);

            shimmer.hideShimmer();
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            listt.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    private void setLeader(TextView tag, CircleImageView circleImageView,TextView name_txt,String name,String img,String tage)
    {
        Glide.with(getContext()).load(img)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(circleImageView);
        tag.setText(tage);
        name_txt.setText(name);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}