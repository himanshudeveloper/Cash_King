package com.allrecipes.recipes5.csm.fragment;

import static com.allrecipes.recipes5.helper.AppController.hidepDialog;
import static com.allrecipes.recipes5.helper.AppController.initpDialog;
import static com.allrecipes.recipes5.helper.AppController.isConnected;
import static com.allrecipes.recipes5.helper.AppController.showpDialog;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_CHANGE_PASSWORD;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_FORGOT_PASSWORD;
import static com.allrecipes.recipes5.helper.Helper.FRAGMENT_TYPE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.allrecipes.recipes5.Activity_Login;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.Constatnt;
import com.allrecipes.recipes5.helper.CustomVolleyJsonRequest;
import com.allrecipes.recipes5.helper.Helper;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class ForgotFragment extends Fragment {

    private TextInputEditText email_EditText, change_password_edit_text;
    private AppCompatButton reset_btn, change_password_btn;
    private Context mContext;
    private TextView header_text, resend_text;
    private OtpTextView otp_edit_text;
    private String type;
    private RelativeLayout baseLyt;
    private LinearLayout password_change_lyt;
    private String OTP;
    private TextInputLayout emailLyt;

    public ForgotFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static ForgotFragment newInstance() {
        ForgotFragment fragment = new ForgotFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot, container, false);
        initpDialog((AppCompatActivity) requireActivity());
        email_EditText = view.findViewById(R.id.reset_email_edit_text);
        reset_btn = view.findViewById(R.id.reset_password_btn);
        header_text = view.findViewById(R.id.title);
        otp_edit_text = view.findViewById(R.id.otpEditText);
        resend_text = view.findViewById(R.id.resend_otp);
        baseLyt = view.findViewById(R.id.otp_base_lyt);
        emailLyt = view.findViewById(R.id.email_lyt);

        password_change_lyt = view.findViewById(R.id.reset_password_lyt);
        change_password_edit_text = view.findViewById(R.id.new_password);
        baseLyt = view.findViewById(R.id.otp_base_lyt);
        change_password_btn = view.findViewById(R.id.change_password_btn);
        if (getActivity().getIntent() != null) {
            type = getActivity().getIntent().getStringExtra(FRAGMENT_TYPE);
            if (type != null) {
                if (type.equals(FRAGMENT_FORGOT_PASSWORD)) {
                    header_text.setText(getResources().getString(R.string.forgot_password));
                } else if (type.equals(FRAGMENT_CHANGE_PASSWORD)){
                    header_text.setText(getResources().getString(R.string.change_password));
                }
            }
        }
        onClick();

        return view;
    }

    private void onClick() {
        resend_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Random rnd = new Random();
                int number = rnd.nextInt(999999);
                resetPasswordEmail(email_EditText.getText().toString(), String.format("%06d", number));
            }
        });

        otp_edit_text.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {


            }

            @Override
            public void onOTPComplete(String otp) {
                if (otp.equalsIgnoreCase(OTP)) {
                    baseLyt.setVisibility(View.GONE);
                    password_change_lyt.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mContext, "Enter Correct Otp...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change_password_edit_text.getText().toString().length() == 0) {
                    change_password_edit_text.setError("Enter password");
                    change_password_edit_text.requestFocus();
                } else if (change_password_edit_text.getText().toString().length() < 6) {
                    change_password_edit_text.setError("Enter at least 6 digit password");
                    change_password_edit_text.requestFocus();
                } else {
                    changePasswordMethod(change_password_edit_text.getText().toString());
                }
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected((AppCompatActivity) requireActivity())) {
                    String Email = email_EditText.getText().toString();
                    if (Email.length() == 0) {
                        email_EditText.setError(getResources().getString(R.string.enter_email));
                        email_EditText.requestFocus();
                    } else if (!Helper.isValidEmail(Email).equalsIgnoreCase("ok")) {
                        email_EditText.setError("Enter correct email address");
                        email_EditText.requestFocus();
                    } else {
                        reset_btn.setEnabled(false);
                        showProgressDialog();
                        Random rnd = new Random();
                        int number = rnd.nextInt(999999);
                        resetPasswordEmail(Email, String.format("%06d", number));
                    }
                } else {
                    Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void changePasswordMethod(String password) {
        showProgressDialog();
        String email = email_EditText.getText().toString().trim();
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("rest_pass", "any");
        params.put("email", email);
        params.put("pass", password);
        Log.e("{PASSWORD}", "changePasswordMethod: "+params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Constatnt.RESET_PASSWORD, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        hideProgressDialog();
                        Toast.makeText(mContext, "Password Reset now please login", Toast.LENGTH_SHORT).show();
                        if (getActivity() == null) {
                            return;
                        }
                        startActivity(new Intent(getActivity(), Activity_Login.class));
                    } else {
                        Toast.makeText(mContext, "Password Not Updated Please Check your email", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mContext, "Slow internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 30,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void resetPasswordEmail(String email, final String otp) {
        showProgressDialog();

        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("recover", "anything");
        params.put("email", email);
        params.put("otp", otp);
        Log.e("TAG", "resetPasswordEmail: " + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Constatnt.FORGOT_PASSWORD, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    hideProgressDialog();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        hideProgressDialog();
                        Toast.makeText(mContext, "Otp Sent to your email check your email", Toast.LENGTH_SHORT).show();
                        emailLyt.setVisibility(View.GONE);
                        resend_text.setVisibility(View.VISIBLE);
                        reset_btn.setEnabled(false);
                        OTP = otp;
                    } else {
                        reset_btn.setEnabled(true);
                        Toast.makeText(mContext, "This Email is Not Registered", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                hideProgressDialog();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(mContext, "Slow internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 30,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void showProgressDialog() {
        showpDialog();
    }

    public void hideProgressDialog() {
        hidepDialog();
    }
}