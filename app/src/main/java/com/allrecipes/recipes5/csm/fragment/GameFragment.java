package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.adapter.GameAdapter;
import com.allrecipes.recipes5.csm.model.GameModel;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
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

public class GameFragment extends Fragment {
    View root_view;
    RecyclerView game_list;
    GameAdapter game_adapter;
    private List<GameModel> gameModel = new ArrayList<>();
    Integer total_post = 0, done_post = 0;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_game, container, false);
        game_list = root_view.findViewById(R.id.games_list);
        progressBar = root_view.findViewById(R.id.progressBar);
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                //  hidepDialog();
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

    private void parseJsonFeed(JSONObject response) {
        try {
            progressBar.setMax(response.getInt("limit"));
            progressBar.setProgress(response.getInt("played"));

            TextView task = root_view.findViewById(R.id.task);
            task.setText(response.getInt("played") + "/" + response.getInt("limit"));

            JSONArray feedArray = response.getJSONArray("data");
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
            game_adapter = new GameAdapter(gameModel, getActivity(), 1);


            game_list.setLayoutManager(new GridLayoutManager(getContext(), 4));
            game_list.setAdapter(game_adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getlist();
    }
}