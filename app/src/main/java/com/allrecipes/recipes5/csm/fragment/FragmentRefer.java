package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allrecipes.recipes5.BuildConfig;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.RefTaskActivity;
import com.allrecipes.recipes5.csm.adapter.Task_adapter;
import com.allrecipes.recipes5.csm.model.Task_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentRefer extends Fragment {
    private View root_view;
    TextView ref;
    private Task_adapter task_adapter;
    RecyclerView task_list;
    RelativeLayout send;
    String msg,bonus;
    RelativeLayout copy;
    RelativeLayout show;

    private List<Task_model> task_item = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_refer, container, false);

        ref=root_view.findViewById(R.id.ref);

        task_list = root_view.findViewById(R.id.task_list);
        send = root_view.findViewById(R.id.send);
        copy = root_view.findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getContext(),AppController.getInstance().getRefercodee());
            }
        });
        show = root_view.findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), RefTaskActivity.class);
                startActivity(i);
            }
        });



        ref.setText(AppController.getInstance().getRefercodee());
        //get();
        getquizlist();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(msg);
            }
        });
        return root_view;
    }



    public void getquizlist() {
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
                params.put(TASK, TASK);
                params.put("id", AppController.getInstance().getId());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    private void parseJsonFeed(JSONObject response) {
        try {
            msg = response.getString("msg");
            bonus = response.getString("bonus");
            JSONArray feedArray = response.getJSONArray("data");
            task_item.clear();
            int count = 0;
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                count++;

                String id = (feedObj.getString("id"));
                Integer invites=(feedObj.getInt("invites"));
                String points = (feedObj.getString("points"));
                String check = (feedObj.getString("check"));
                String title = ""+count;
                Integer reff = response.getInt("ref");


                Task_model item = new Task_model(id, invites, points,check,title,reff);
                task_item.add(item);

            }

            task_adapter = new Task_adapter(task_item, getActivity(),"0");

            task_list.setHasFixedSize(true);
            task_list.setLayoutManager(new LinearLayoutManager(getContext()));
            task_list.setAdapter(task_adapter);
            ShimmerFrameLayout loading = root_view.findViewById(R.id.loading);
            LinearLayout lin_list = root_view.findViewById(R.id.lin_list);
            loading.stopShimmer();
            loading.setVisibility(View.GONE);
            lin_list.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
          //  listView.setVisibility(View.GONE);
            Toast.makeText(getContext(),"error",Toast.LENGTH_LONG).show();

        }
    }

    private void sendMessage(String message)
    {

        // Creating new intent
        Intent intent
                = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");

        // Give your message here
        intent.putExtra(
                Intent.EXTRA_TEXT,
                message+"  Install APP from this link and enter my Code: "+AppController.getInstance().getRefercodee()+" to get "+bonus+" coins. "+"https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID
        );
        // Checking whether Whatsapp
        // is installed or not
        if (intent
                .resolveActivity(
                        getActivity().getPackageManager())
                == null) {
            Toast.makeText(
                    getContext(),
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Starting Whatsapp
        startActivity(intent);
    }

    private void sendtg(String message)
    {

        // Creating new intent
        Intent intent
                = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.setPackage("org.telegram.messenger");

        // Give your message here
        intent.putExtra(
                Intent.EXTRA_TEXT,
                message+"  Install APP from this link and enter my Code: "+AppController.getInstance().getRefercodee()+" to get "+bonus+" coins. "+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID
        );

        // Checking whether Whatsapp
        // is installed or not
        if (intent
                .resolveActivity(
                        getActivity().getPackageManager())
                == null) {
            Toast.makeText(
                    getContext(),
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Starting Whatsapp
        startActivity(intent);
    }

    private void sendfb(String message)
    {

        // Creating new intent
        Intent intent
                = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.setPackage("com.facebook.orca");

        // Give your message here
        intent.putExtra(
                Intent.EXTRA_TEXT,
                message+"  Install APP from this link and enter my Code: "+AppController.getInstance().getRefercodee()+" to get "+bonus+" coins. "+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID
        );

        // Checking whether Whatsapp
        // is installed or not
        if (intent
                .resolveActivity(
                        getActivity().getPackageManager())
                == null) {
            Toast.makeText(
                    getContext(),
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Starting Whatsapp
        startActivity(intent);
    }

    private void send(String message)
    {

        // Creating new intent
        Intent intent
                = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        //intent.setPackage("com.facebook.orca");

        // Give your message here
        intent.putExtra(
                Intent.EXTRA_TEXT,
                message+"  Install APP from this link and enter my Code: "+AppController.getInstance().getRefercodee()+" to get "+bonus+" coins. "+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID
        );


        // Checking whether Whatsapp
        // is installed or not
        if (intent
                .resolveActivity(
                        getActivity().getPackageManager())
                == null) {
            Toast.makeText(
                    getContext(),
                    "Please install whatsapp first.",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Starting Whatsapp
        startActivity(intent);
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();

        }
    }



}