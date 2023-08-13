package com.allrecipes.recipes5.csm.fragment;

import static android.content.ContentValues.TAG;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_CHANGE_PASSWORD;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_TYPE;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.user_points;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allrecipes.recipes5.BuildConfig;
import com.allrecipes.recipes5.FragmentLoadingActivity;
import com.allrecipes.recipes5.watchandearn.activity.PolicyActivity;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.allrecipes.recipes5.Activity_Login;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.TransActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {
    private View root_view;
    LinearLayout p_logout,history,rate,t_of_s,p_policy,cont,change_password_btn;
    CardView fb,tg;

    TextView txt2,txt1;

    RoundedImageView pro;
    TextView points,rank,total,redeem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_profile, container, false);

        cont = root_view.findViewById(R.id.cont);
        //txt2 = root_view.findViewById(R.id.txt2);
        pro = root_view.findViewById(R.id.profile);
        history = root_view.findViewById(R.id.history);
        txt1 = root_view.findViewById(R.id.txt1);
        rank = root_view.findViewById(R.id.rank);
        txt1.setText(AppController.getInstance().getFullname());
        points = root_view.findViewById(R.id.points);
        total = root_view.findViewById(R.id.total);
        redeem = root_view.findViewById(R.id.redeem);
        change_password_btn = root_view.findViewById(R.id.change_password_btn);
        points.setText("0");
        user_points(points);
        TextView rank = root_view.findViewById(R.id.rank);
        rank.setText(AppController.getInstance().getRank());
        //total.setText(AppController.getInstance().getTotal_points());
        //redeem.setText(AppController.getInstance().getRedeem());

        Glide.with(getContext()).load(AppController.getInstance().getProfile())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(pro);

        //rank.setText(AppController.getInstance().getRank());

        settings();

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+getString(R.string.contect_email))));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TransActivity.class);
                startActivity(i);
            }
        });



      //  txt2.setText(AppController.getInstance().getEmail());
        p_logout = root_view.findViewById(R.id.log_out);
        p_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().removeData();
                AppController.getInstance().setId("0");
                AppController.getInstance().setUsername("0");
                AppController.getInstance().readData();
                Toast.makeText(getActivity(), "Logout Successfully..!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), Activity_Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        rate = root_view.findViewById(R.id.rate);
        p_policy = root_view.findViewById(R.id.p_policy);
        t_of_s = root_view.findViewById(R.id.t_of_s);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PACKAGE_NAME = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
                Uri uri = Uri.parse(PACKAGE_NAME); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        p_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse(getString(R.string.privacy_policy));
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

                try {
                    Intent policyintent = new Intent(getActivity(), PolicyActivity.class);
                    policyintent.putExtra("type", "Privacy Policy");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(getActivity(), e.getMessage());
                }
            }
        });

        t_of_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse(getString(R.string.terms_of_service));
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

                try {
                    Intent policyintent = new Intent(getActivity(), PolicyActivity.class);
                    policyintent.putExtra("type", "Terms & Conditions");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(getActivity(), e.getMessage());
                }
            }
        });

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), FragmentLoadingActivity.class);
                intent.putExtra(FRAGMENT_TYPE,FRAGMENT_CHANGE_PASSWORD);
                startActivity(intent);
            }
        });



        return root_view;
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
                message+"  Install APP from this link and enter my Code: "+AppController.getInstance().getRefercodee()+" to free coins. "+"https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID
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

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                //  hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("reward_count", AppController.getInstance().getId());
                params.put("user", AppController.getInstance().getUsername());
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void parseJsonFeed(JSONObject response) {
        try {
            TextView re = root_view.findViewById(R.id.re);
            re.setText(response.getString("re"));
            TextView total = root_view.findViewById(R.id.total);
            total.setText(response.getString("earn"));




        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            Toast.makeText(getContext(), AppController.getInstance().getUsername(), Toast.LENGTH_SHORT).show();

            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();

        }
    }

}