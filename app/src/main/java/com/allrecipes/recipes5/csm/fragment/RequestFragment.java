package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.adapter.r_Adapter;
import com.allrecipes.recipes5.csm.model.r_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestFragment extends Fragment {
    View root_view;
    RecyclerView game_list;
    r_Adapter R_adapter;
    private List<r_Model> gameModel = new ArrayList<>();
    NestedScrollView scroll;
    LinearLayout no;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_request, container, false);
        game_list = root_view.findViewById(R.id.list);
        scroll = root_view.findViewById(R.id.scroll);
        no = root_view.findViewById(R.id.no);
        getlist();

        return root_view;
    }

    public void getlist() {
        // showpDialog();
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
                params.put("redeem_check", AppController.getInstance().getId());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("data");
            if (!(feedArray.length() ==0))
            {
                scroll.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
            }
            gameModel.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Integer id = (feedObj.getInt("id"));
                String u_id=(feedObj.getString("u_id"));
                String package_name = (feedObj.getString("package_name"));
                String p_details = (feedObj.getString("p_details"));
                String coins_used = (feedObj.getString("coins_used"));
                String symbol = (feedObj.getString("symbol"));
                String amount = (feedObj.getString("amount"));
                String time = (feedObj.getString("time"));
                String date = (feedObj.getString("date"));
                String status = (feedObj.getString("status"));
                String package_id = (feedObj.getString("package_id"));
                String image = (feedObj.getString("image"));

                r_Model item = new r_Model(package_name,p_details,coins_used,symbol,amount,date,time,status,package_id,image);
                gameModel.add(item);
            }
            R_adapter = new r_Adapter(gameModel, getContext());


            game_list.setLayoutManager(new LinearLayoutManager(getContext()));
            game_list.setAdapter(R_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            //  Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

        }
    }
}