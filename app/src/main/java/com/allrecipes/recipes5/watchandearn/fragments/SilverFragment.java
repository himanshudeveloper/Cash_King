package com.allrecipes.recipes5.watchandearn.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.databinding.FragmentSilverBinding;
import com.allrecipes.recipes5.watchandearn.App;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.ironsource.mediationsdk.IronSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class SilverFragment extends Fragment implements ScratchListener {

    private final String TAG = "Silver Fragment";
    public int poiints = 0;
    boolean first_time = true, scratch_first = true;
    ScratchCardLayout scratchCardLayout;
    ShowAds showAds = new ShowAds();
    FragmentSilverBinding binding;
    private LinearLayout adLayout;
    private Activity activity;
    private Toolbar toolbar;
    private TextView scratch_count_textView, points_textView, points_text;
    private int scratch_count = 1;
    private int counter_dialog = 0;
    private String random_points;
    public boolean rewardShow = true, interstitialShow = true, isFullRewardShow = false, isRewadedVideo = false;


    public SilverFragment() {

    }

    public static SilverFragment newInstance() {
        SilverFragment fragment = new SilverFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
        binding = FragmentSilverBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        if (getActivity() != null) {
            activity = getActivity();
        }
        toolbar = view.findViewById(R.id.toolbar);
        points_text = view.findViewById(R.id.textView_points_show);
        scratch_count_textView = view.findViewById(R.id.limit_text);
        scratchCardLayout = view.findViewById(R.id.scratch_view_layout);
        scratchCardLayout.setScratchListener(this);
        try {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText("Silver Scratch");
            points_textView = toolbar.findViewById(R.id.points_text_in_toolbar);
            setPointsText();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));


        onInit();
        return view;
    }

    private void setPointsText() {
        if (points_textView != null) {
            String userPoints = Constant.getString(activity, Constant.USER_POINTS);
            if (userPoints.equalsIgnoreCase("")) {
                userPoints = "0";
            }
            points_textView.setText(userPoints);
        }
    }

    private void onInit() {


        String scratchCount = Constant.getString(activity, Constant.SCRATCH_COUNT_SILVER);
        if (scratchCount.equals("0")) {
            scratchCount = "";
            Log.e("TAG", "onInit: scratch card 0");
        }
        if (scratchCount.equals("")) {
            Log.e("TAG", "onInit: scratch card empty vala part");
            String currentDate = Constant.getString(activity, Constant.TODAY_DATE);
            Log.e("TAG", "onClick: Current Date" + currentDate);
            String last_date = Constant.getString(activity, Constant.LAST_DATE_SCRATCH_SILVER);
            Log.e("TAG", "Lat date" + last_date);
            if (last_date.equals("")) {
                Log.e("TAG", "onInit: last date empty part");
                setScratchCount(Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.SCRATCH_COUNT_SILVER, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                Constant.setString(activity, Constant.LAST_DATE_SCRATCH_SILVER, currentDate);
            } else {
                Log.e("TAG", "onInit: last date not empty part");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date current_date = sdf.parse(currentDate);
                    Date lastDate = sdf.parse(last_date);
                    long diff = current_date.getTime() - lastDate.getTime();
                    long difference_In_Days = (diff / (1000 * 60 * 60 * 24)) % 365;
                    Log.e("TAG", "onClick: Days Difference" + difference_In_Days);
                    if (difference_In_Days > 0) {
                        Constant.setString(activity, Constant.LAST_DATE_SCRATCH_SILVER, currentDate);
                        Constant.setString(activity, Constant.SCRATCH_COUNT_SILVER, Constant.getString(activity, Constant.DAILY_SCRATCH_COUNT));
                        setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_SILVER));
                        // Constant.addDate(activity, "silver", Constant.getString(activity, Constant.LAST_DATE_SCRATCH_SILVER));
                        Log.e("TAG", "onClick: today date added to preference" + currentDate);
                    } else {
                        setScratchCount("0");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "onInit: scracth card in preference part");
            setScratchCount(scratchCount);
        }
    }

    private void setScratchCount(String string) {
        if (string != null || !string.equalsIgnoreCase(""))
            scratch_count_textView.setText("Your Today Scratch Count left = " + string);
    }


    private void showDialogPoints(final int addOrNoAddValue, final String points, final int counter, boolean isShowDialog) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        if (Constant.isNetworkAvailable(activity)) {
            if (addOrNoAddValue == 1) {
                if (points.equals("0")) {
                    Log.e("TAG", "showDialogPoints: 0 points");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.better_luck));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else if (points.equals("no")) {
                    Log.e("TAG", "showDialogPoints: rewared cancel");
                    imageView.setImageResource(R.drawable.sad);
                    title_text.setText(getResources().getString(R.string.not_watch_full_video));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.okk));
                } else {
                    Log.e("TAG", "showDialogPoints: points");
                    imageView.setImageResource(R.drawable.congo);
                    title_text.setText(getResources().getString(R.string.you_won));
                    points_text.setVisibility(View.VISIBLE);
                    points_text.setText(points);
                    add_btn.setText(getResources().getString(R.string.add_to_wallet));
                }
            } else {
                Log.e("TAG", "showDialogPoints: chance over");
                imageView.setImageResource(R.drawable.donee);
                title_text.setText(getResources().getString(R.string.today_chance_over));
                points_text.setVisibility(View.GONE);
                add_btn.setText(getResources().getString(R.string.okk));
            }
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    first_time = true;
                    scratch_first = true;
                    scratchCardLayout.resetScratch();
                    poiints = 0;
                    if (addOrNoAddValue == 1) {
                        if (points.equals("0") || points.equalsIgnoreCase("no")) {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, current_counter);
                            setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM));
                            dialog.dismiss();
                        } else {
                            String current_counter = String.valueOf(counter - 1);
                            Constant.setString(activity, Constant.SCRATCH_COUNT_PLATINUM, current_counter);
                            setScratchCount(Constant.getString(activity, Constant.SCRATCH_COUNT_PLATINUM));
                            try {
                                int finalPoint;
                                if (points.equals("")) {
                                    finalPoint = 0;
                                } else {
                                    finalPoint = Integer.parseInt(points);
                                }
                                poiints = finalPoint;
                                Constant.addPoints(activity, finalPoint, 0);
                                setPointsText();
                            } catch (NumberFormatException ex) {
                                Log.e("TAG", "onScratchComplete: " + ex.getMessage());
                            }
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                    }
                    if (addOrNoAddValue == 1) {
                        if (scratch_count == Integer.parseInt(Constant.getString(activity, Constant.ADS_BEETWEEN))) {
                            if (rewardShow) {
                                Log.e(TAG, "onReachTarget: rewaded ads showing method");
                                if (Constant.getString(activity, Constant.AD_TYPE).equalsIgnoreCase("startapp")) {
                                    unityStartAppInterstitialShow();
                                } else {
                                    showDailog();
                                }
                                rewardShow = false;
                                interstitialShow = true;
                                scratch_count = 0;
                            } else if (interstitialShow) {
                                Log.e(TAG, "onReachTarget: interstital ads showing method");
                                unityStartAppInterstitialShow();
                                rewardShow = true;
                                interstitialShow = false;
                                scratch_count = 0;
                            }
                        } else {
                            scratch_count += 1;
                        }
                    }
                }
            });
        } else {
            Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
        }
        if (isShowDialog) {
            dialog.show();
        } else {
            rewardShow = false;
            interstitialShow = true;
            scratch_count = 0;
            showDailog();
        }
    }

    private void unityStartAppInterstitialShow() {
        showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);

//        isRewadedVideo = false;
//        String adType = Constant.getString(activity, Constant.AD_TYPE);
//        if (adType.equalsIgnoreCase("admob")) {
//            ShowAdmobInterstital();
//        } else if (adType.equalsIgnoreCase("startapp")) {
//            ShowStartappInterstital();
//        } else if (adType.equalsIgnoreCase("unity")) {
//            ShowInterstital();
//        } else if (adType.equalsIgnoreCase("applovin")) {
//            ShowApplovinInterstital();
//        }


    }
    public void showDailog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_points_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.points_image);
        TextView title_text = dialog.findViewById(R.id.title_text_points);
        TextView points_text = dialog.findViewById(R.id.points);
        AppCompatButton add_btn = dialog.findViewById(R.id.add_btn);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btn);
        cancel_btn.setVisibility(View.GONE);
        imageView.setImageResource(R.drawable.watched);
        add_btn.setText("Watch Video");
        title_text.setText("Watch Full Video");
        points_text.setText("To Unlock this Reward Points");

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAds.showInterstitialAds(requireActivity());
                unityStartAppRewardedShow();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (poiints != 0) {
                    String po = Constant.getString(activity, Constant.USER_POINTS);
                    if (po.equals("")) {
                        po = "0";
                    }
                    int current_Points = Integer.parseInt(po);
                    int finalPoints = current_Points - poiints;
                    Constant.setString(activity, Constant.USER_POINTS, String.valueOf(finalPoints));
//                    Intent serviceIntent = new Intent(activity, PointsService.class);
//                    serviceIntent.putExtra("points", Constant.getString(activity, Constant.USER_POINTS));
//                    activity.startService(serviceIntent);
                    App.db.getDao().update(Constant.getString(requireActivity(), Constant.USER_POINTS));

                    setPointsText();
                }
            }
        });

        dialog.show();
    }
    private void unityStartAppRewardedShow() {
//        isRewadedVideo = true;
//        isFullRewardShow = false;
//        String adType = Constant.getString(activity, Constant.AD_TYPE);
//        if (adType.equalsIgnoreCase("admob")) {
//            ShowAdmobRewaredVideo();
//        } else if (adType.equalsIgnoreCase("startapp")) {
//            ShowStartappInterstital();
//        } else if (adType.equalsIgnoreCase("unity")) {
//            ShowRewarded();
//        } else if (adType.equalsIgnoreCase("applovin")) {
//            ShowApplovinRewarded();
//        }
        showDialogPoints(1, points_text.getText().toString().trim(), counter_dialog, true);

    }


    @Override
    public void onScratchComplete() {
        if (first_time) {
            first_time = false;
            Log.e("onScratch", "Complete");
            Log.e("onScratch", "Complete" + random_points);
            String count = scratch_count_textView.getText().toString();
            String[] counteee = count.split("=", 2);
            String ran = counteee[1];
            Log.e(TAG, "onScratchComplete: " + ran);
            int counter = Integer.parseInt(ran.trim());
            counter_dialog = Integer.parseInt(ran.trim());
            if (counter <= 0) {
                showDialogPoints(0, "0", counter, true);
            } else {
                showDialogPoints(1, points_text.getText().toString(), counter, true);
            }
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        if (scratch_first) {
            scratch_first = false;
            Log.e(TAG, "onScratchStarted: " + random_points);
            if (Constant.isNetworkAvailable(activity)) {
                random_points = "";
                Random random = new Random();
                String str = Constant.getString(activity, Constant.SCRATCH_PRICE_COIN);
                String[] parts = str.split("-", 2);
                int low = Integer.parseInt(parts[0]);
                int high = Integer.parseInt(parts[1]);
                int result = random.nextInt(high - low) + low;
                random_points = String.valueOf(result);
                if (random_points == null || random_points.equalsIgnoreCase("null")) {
                    random_points = String.valueOf(1);
                }
                points_text.setText(random_points);
                Log.e(TAG, "onScratchStarted: " + random_points);
            } else {
                Constant.showInternetErrorDialog(activity, getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    @Override
    public void onScratchStarted() {
        if (Integer.parseInt(scratch_count_textView.getText().toString().trim()) <= 0) {
            showDialogPoints(0, "0", 0, true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        IronSource.onResume(requireActivity());
        showAds.showTopBanner(requireActivity(), binding.adViewTop);
        showAds.showBottomBanner(requireActivity(), binding.adViewBottom);
        showAds.showNativeAds(requireActivity(), binding.nativeAds);

    }

    @Override
    public void onPause() {
        super.onPause();
        showAds.destroyBanner();
        IronSource.onPause(requireActivity());
    }

}