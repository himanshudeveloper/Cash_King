package com.allrecipes.recipes5.watchandearn.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.databinding.FragmentHomeBinding;
import com.allrecipes.recipes5.watchandearn.activity.MainActivity;
import com.allrecipes.recipes5.watchandearn.activity.ReferActivity;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.ironsource.mediationsdk.IronSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {


    ShowAds showAds = new ShowAds();
    FragmentHomeBinding binding;
    private CardView referCardView, walletCardView, dailyCheckIn, silverCardView, platinumCardView, goldCardView;
    private Context activity;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        referCardView = view.findViewById(R.id.refer_cardView);
        walletCardView = view.findViewById(R.id.walletCardView);
        dailyCheckIn = view.findViewById(R.id.daily_check_in);
        silverCardView = view.findViewById(R.id.silverCardView);
        platinumCardView = view.findViewById(R.id.platinumCardView);
        goldCardView = view.findViewById(R.id.goldCardView);
        onClick();
        return view;
    }

    private void onClick() {
        referCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "refer");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
            }
        });

        walletCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "wallet");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
            }
        });

        platinumCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "Platinum Scratch");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }
            }
        });


        dailyCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAds.showInterstitialAds(requireActivity());
                checkDaily();
            }
        });

        silverCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.checkInterstialIsCalled = true;

                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "Silver Scratch");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }

            }
        });

        goldCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.checkInterstialIsCalled = true;
                    Intent policyintent = new Intent(activity, ReferActivity.class);
                    policyintent.putExtra("type", "Gold Scratch");
                    startActivity(policyintent);
                } catch (Exception e) {
                    Constant.showToastMessage(activity, e.getMessage());
                }

            }
        });
    }

    private void checkDaily() {
        if (Constant.isNetworkAvailable(activity)) {
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            Constant.setString(activity, Constant.TODAY_DATE, timeStamp);
//            Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)), 0);
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE);
            Log.e("TAG", "onClick: last_date Date" + last_date);
            if (last_date.isEmpty()) {
                Constant.setString(activity, Constant.LAST_DATE, currentDate);
                Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)), 0);
                showDialogOfPoints(Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)));
                if (getActivity() == null) {
                    return;
                }
                ((MainActivity) getActivity()).setPointsText();
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date pastDAte = sdf.parse(last_date);
                    Date currentDAte = sdf.parse(timeStamp);
                    long diff = currentDAte.getTime() - pastDAte.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Diffrernce" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE, currentDate);
                        Constant.addPoints(activity, Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)), 0);
                        showDialogOfPoints(Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS)));
                        if (getActivity() == null) {
                            return;
                        }
                        ((MainActivity) getActivity()).setPointsText();
                    } else {
                        showDialogOfPoints(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
    }

    public void showDialogOfPoints(int points) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);

        int dailyPoints = 0;
        if (!Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS).equals("")) {
            dailyPoints = Integer.parseInt(Constant.getString(activity, Constant.DAILY_CHECK_IN_POINTS));
        }
        if (points == dailyPoints) {
            imageView.setImageResource(R.drawable.congo);
            title_text.setText(getResources().getString(R.string.you_won));
            points_text.setVisibility(View.VISIBLE);
            points_text.setText(String.valueOf(points));
            add_btn.setText(getResources().getString(R.string.add_to_wallet));
        } else {
            imageView.setImageResource(R.drawable.donee);
            title_text.setText(getResources().getString(R.string.today_checkin_over));
            points_text.setVisibility(View.GONE);
            add_btn.setText(getResources().getString(R.string.okk));
        }
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(requireActivity());
        showAds.showBottomBanner(requireActivity(), binding.adViewBottom);
        showAds.showTopBanner(requireActivity(), binding.adViewTop);
    }

    @Override
    public void onPause() {
        super.onPause();
        showAds.destroyBanner();
        IronSource.onPause(requireActivity());
    }
}