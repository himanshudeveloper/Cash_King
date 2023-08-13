package com.allrecipes.recipes5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.csm.MainActivity;
import com.allrecipes.recipes5.spin.OtpEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;
import static com.allrecipes.recipes5.Activity_Login.dialogwindow;
import static com.allrecipes.recipes5.helper.AppController.hidepDialog;
import static com.allrecipes.recipes5.helper.AppController.initpDialog;
import static com.allrecipes.recipes5.helper.AppController.isConnected;
import static com.allrecipes.recipes5.helper.AppController.showpDialog;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.ACCOUNT_STATE_ENABLED;
import static com.allrecipes.recipes5.helper.Constatnt.ADD_SPIN;
import static com.allrecipes.recipes5.helper.Constatnt.ALPHA_NUMERIC_STRING;
import static com.allrecipes.recipes5.helper.Constatnt.API;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.Constatnt.GET_REFER_STATUS;
import static com.allrecipes.recipes5.helper.Constatnt.POINTS;
import static com.allrecipes.recipes5.helper.Constatnt.REFER_CODE;
import static com.allrecipes.recipes5.helper.Constatnt.REFER_CODE_STATUS;
import static com.allrecipes.recipes5.helper.Constatnt.REFER_STATUS;
import static com.allrecipes.recipes5.helper.Constatnt.REFER_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.SET_REFER_STATUS;
import static com.allrecipes.recipes5.helper.Constatnt.SPIN_TYPE;
import static com.allrecipes.recipes5.helper.Constatnt.USERNAME;
import static com.allrecipes.recipes5.helper.Constatnt.USER_LOGIN;


public class Activity_otp extends AppCompatActivity {//,View.OnClickListener {

    EditText username,email,name;
    OtpEditText otp;
    LinearLayout otp_holder;
    static ShowAds showAds = new ShowAds();
    LinearLayout username_holder;
    String account_status = "0";
    Button submit;
    String code,new_phone;
    String simple_otp = "1227";
    TextView resend,phone;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_otp);
        initpDialog(Activity_otp.this);
        otp = findViewById(R.id.otp);
        resend = findViewById(R.id.resend);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        otp_holder = findViewById(R.id.otp_holder);
        username_holder = findViewById(R.id.username_holder);
        submit = findViewById(R.id.submit);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        AppController.transparentStatusAndNavigation(Activity_otp.this);
        initpDialog(Activity_otp.this);
        if (getIntent().getExtras() != null) {
            simple_otp = getIntent().getStringExtra("otp");
            account_status = getIntent().getStringExtra("status");
            new_phone = getIntent().getStringExtra("phone");
            phone.setText("+91"+new_phone);
        }
        code = randomAlphaNumeric(8);
        timer();
        otp.setText(simple_otp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(account_status.contentEquals("1")) {
                    String u_opt = otp.getText().toString();
                    if(simple_otp.contentEquals(u_opt)){
                        signin(new_phone);
                    }else {
                        Toast.makeText(Activity_otp.this,"OTP Not Match",Toast.LENGTH_LONG).show();
                    }
                }else if(account_status.contentEquals("2")) {
                    String u_opt = otp.getText().toString();
                    if(simple_otp.contentEquals(u_opt)){
                        username_holder.setVisibility(View.VISIBLE);
                        otp_holder.setVisibility(View.GONE);
                        account_status = "0";
                    }else {
                        Toast.makeText(Activity_otp.this,"OTP Not Match",Toast.LENGTH_LONG).show();
                    }
                }else {
                    if(username.getText().length()>5){
                        if(name.getText().length()>0){
                            if(email.getText().toString().contains("@")){
                                register(new_phone,username.getText().toString(),name.getText().toString(),email.getText().toString());
                            }else {
                                email.setText("Enter Valid Email");
                            }
                        }else {
                            name.setError("Enter Name");
                        }

                    }else{
                        username.setError("Username Lenght Atleast 5 Digit");
                    }
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==3){
                    Toast.makeText(Activity_otp.this,"Otp send Blocked Try After some Time",Toast.LENGTH_LONG).show();
                }else {
                    rand();
                    count++;
                    timer();
                    otp.setText(""+simple_otp);
                    //otp_send(new_phone,Integer.parseInt(simple_otp),Activity_otp.this);
                }

            }
        });
      //  toast(Activity_otp.this,""+code);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            // | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    public void rand()
    {
        Random rand = new Random();
        int resRandom = rand.nextInt((9999 - 100) + 1) + 10;
        simple_otp = ""+resRandom;
        //Toast.makeText(Activity_Login.this,simple_otp+"",Toast.LENGTH_LONG).show();
    }
    public void timer(){
        CountDownTimer CountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                resend.setText("Resend in 0:"+seconds);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setText("Resend");
                resend.setEnabled(true);

            }
        };
        CountDownTimer.start();
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
    public static void refer(final int points, final Context context) {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response1) {
                        try {
                            if (response1.getString("error").equalsIgnoreCase("false")) {

                                if (response1.getString("message").equalsIgnoreCase("Points added to your wallet")) {

                                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View dialogView = inflater.inflate(R.layout.pay_congo, null);
                                    alert.setView(dialogView);
                                    dialogwindow = alert.create();
                                    dialogwindow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    Button ok = (Button) dialogView.findViewById(R.id.ok);
                                    TextView textView = (TextView) dialogView.findViewById(R.id.text);
                                    textView.setText(points + " " + context.getString(R.string.referal_reward));
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            //  System.out.println("status get**" + AppController.getInstance().getEmail());
                                                            try {
                                                                if (response.getString("error").toString().trim().equalsIgnoreCase("false")) {
                                                                    Intent i = new Intent(context, MainActivity.class);
                                                                    i.putExtra("new_user", "refer");
                                                                    context.startActivity(i);
                                                                    showAds.showInterstitialAds((Activity) context);
                                                                    dialogwindow.dismiss();
                                                                }else {
                                                                    Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                    Toast.makeText(context, context.getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                                                }
                                            }) {

                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put(ACCESS_KEY, ACCESS_Value);
                                                    params.put(SET_REFER_STATUS, API);
                                                    params.put(USERNAME, AppController.getInstance().getUsername());
                                                    params.put(REFER_STATUS, API);
                                                    return params;
                                                }
                                            };
                                            AppController.getInstance().getRequestQueue().getCache().clear();
                                            AppController.getInstance().addToRequestQueue(jsonReq);

                                        }
                                    });

                                    alert.setCancelable(false);
                                    dialogwindow.show();

                                }
                            } else {

                                Toast.makeText(context, response1.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

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
                params.put(ADD_SPIN, API);
                params.put("phone", AppController.getInstance().getPhone());
                params.put(USERNAME, AppController.getInstance().getUsername());
                params.put(POINTS, String.valueOf(points));
                params.put(SPIN_TYPE, REFER_TYPE);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

// User Login
    public void signin(final String enter_phone) {

        if (isConnected(Activity_otp.this)) {
            showpDialog();
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (AppController.getInstance().authorize(response)) {
                                if (AppController.getInstance().getState().equals(ACCOUNT_STATE_ENABLED)) {

                                    try {

                                        if (response.getString("error").equalsIgnoreCase("true")) {
                                            Toast.makeText(Activity_otp.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        }else if(response.getString("error").equalsIgnoreCase("false")){
                                            Toast.makeText(Activity_otp.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                            chkref(Activity_otp.this);
                                        }
                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }

                                } else {
                                     AppController.getInstance().logout(Activity_otp.this);
                                    Toast.makeText(Activity_otp.this, getText(R.string.msg_account_blocked), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "Something Gone Wrong", Toast.LENGTH_SHORT).show();
                            }

                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                    hidepDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put(USER_LOGIN, API);
                    params.put("phone", enter_phone);
                    AppController.getInstance().setPassword("12345");
                    return params;
                }
            };
            AppController.getInstance().getRequestQueue().getCache().clear();
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }
  //02. Register User
  public void register(final String enter_phone, final String username, final String name, final String email) {
      if (isConnected(Activity_otp.this)) {
          showpDialog();
          JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                  new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                                  try {
                                      if (response.getString("error").equalsIgnoreCase("true")) {
                                        Toast.makeText(Activity_otp.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                      }
                                      else if (response.getString("error").equalsIgnoreCase("false")){
                                          signin(enter_phone);
    //                                      Toast.makeText(Activity_Login.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                      }

                                  } catch (JSONException e) {
                                      e.printStackTrace();

                                  }
                          hidepDialog();
                      }
                  }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                  hidepDialog();
              }
          }) {
              @Override
              protected Map<String, String> getParams() {
                  Map<String, String> params = new HashMap<String, String>();
                  params.put(ACCESS_KEY, ACCESS_Value);
                  params.put("user_signup", API);
                  params.put("phone", enter_phone);
                  params.put("username",username );
                  params.put("name",name );
                  params.put("email",email );
                  params.put("refer", code);
                 return params;
              }
          };
          AppController.getInstance().addToRequestQueue(jsonReq);
      }
  }
     //End Register


    public static void chkref(final Context context) {

        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                if (response.getString("refer_status").equals("1")) {
                                    Intent i = new Intent(context, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    showAds.showInterstitialAds((Activity) context);
                                } else {
                                    refer_dialog(context);
                                }
                            }
                        } catch (Exception e) {
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
                params.put(GET_REFER_STATUS, API);
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put(EMAIL, AppController.getInstance().getEmail());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void refer_dialog(final Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dilog_refer, null);
        alert.setView(dialogView);
        alert.setCancelable(false);
        dialogwindow = alert.create();
        dialogwindow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView) dialogView.findViewById(R.id.top_title);
        title.setText("Refer & Earn");
        Button ok = (Button) dialogView.findViewById(R.id.claim);
        final EditText editText = (EditText) dialogView.findViewById(R.id.refer);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (editText.getText().length() == 0) {
                    Toast.makeText(context, "Please Enter Refer code", Toast.LENGTH_SHORT).show();
                } else {
                    showpDialog();
                    JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("refer_code_error").equalsIgnoreCase("false")) {
                                            dialogwindow.dismiss();
                                            int bonus =Integer.parseInt(response.getString("bonus"));
                                            refer(bonus, context);
                                            hidepDialog();
                                        } else if (response.getString(REFER_CODE_STATUS).equalsIgnoreCase("Invalid Referral Code")) {
                                            Toast.makeText(context, "Please Enter Valid Refer Code", Toast.LENGTH_SHORT).show();
                                            editText.setText("");
                                            Intent i = new Intent(context, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            hidepDialog();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println(e.getMessage());
                                        hidepDialog();
                                        Toast.makeText(context, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    hidepDialog();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(USER_LOGIN, API);
                            params.put(ACCESS_KEY, ACCESS_Value);
                            params.put("phone", AppController.getInstance().getEmail());
                            params.put("password", AppController.getInstance().getPassword());
                            params.put(REFER_CODE, editText.getText().toString().trim());
                            return params;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(stringRequest);


                }
            }
        });
        Button skip = (Button) dialogView.findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogwindow.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("new_user", "new");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                showAds.showInterstitialAds((Activity) context);
            }
        });
        alert.setCancelable(false);
        dialogwindow.show();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

}