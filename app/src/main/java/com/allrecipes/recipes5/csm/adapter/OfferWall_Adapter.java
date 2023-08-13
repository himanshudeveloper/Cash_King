package com.allrecipes.recipes5.csm.adapter;

import static com.allrecipes.recipes5.Just_base.addPoint;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_KEY;
import static com.allrecipes.recipes5.helper.Constatnt.ACCESS_Value;
import static com.allrecipes.recipes5.helper.Constatnt.Base_Url;
import static com.allrecipes.recipes5.helper.PrefManager.ADMOB_REWARDED_ID;
import static com.allrecipes.recipes5.helper.PrefManager.VUNGLE_REWARDED_ID;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.IsVideoLimitReach;
import com.allrecipes.recipes5.csm.model.OfferWall_Model;
import com.allrecipes.recipes5.helper.AppController;
import com.allrecipes.recipes5.helper.JsonRequest;
import com.allrecipes.recipes5.helper.PrefManager;
import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.adgatemedia.sdk.classes.AdGateMedia;
import com.adgatemedia.sdk.network.OnOfferWallLoadFailed;
import com.adgatemedia.sdk.network.OnOfferWallLoadSuccess;
import com.allrecipes.recipes5.watchandearn.utils.Constant;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.ayetstudios.publishersdk.AyetSdk;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chartboost.sdk.ads.Rewarded;
import com.chartboost.sdk.callbacks.RewardedCallback;
import com.chartboost.sdk.events.CacheError;
import com.chartboost.sdk.events.CacheEvent;
import com.chartboost.sdk.events.ClickError;
import com.chartboost.sdk.events.ClickEvent;
import com.chartboost.sdk.events.DismissEvent;
import com.chartboost.sdk.events.ImpressionEvent;
import com.chartboost.sdk.events.RewardEvent;
import com.chartboost.sdk.events.ShowError;
import com.chartboost.sdk.events.ShowEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.OfferwallListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.offertoro.sdk.interfaces.OfferWallListener;
import com.offertoro.sdk.sdk.OffersInit;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.error.VungleException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class OfferWall_Adapter extends RecyclerView.Adapter<OfferWall_Adapter.ViewHolder> {
    public List<OfferWall_Model> offer_data;
    public Context context;
    String ADGET_WALL_id;
    Boolean check = false;
    IUnityAdsLoadListener loadListener;
    IUnityAdsShowListener showListener;
    StartAppAd startAppAd;
    AdColonyInterstitial rewardAdColony;
    MaxRewardedAd maxRewardedAd;
    RewardedAd rewardedAd;
    Boolean isRewardLoaded = false, isChartBoostAdLoaded = false;
    AdColonyInterstitialListener rewardListener;
    AdColonyAdOptions rewardAdOptions;
    Rewarded chartboostRewarded;

    public OfferWall_Adapter(List<OfferWall_Model> offer_data, Context context) {

        this.offer_data = offer_data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offerwall, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OfferWall_Model offer = offer_data.get(position);

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Glide.with(context).load(offer.getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round)).into(holder.img);

        holder.title.setText(offer.getTitle());

        int randomNum = ThreadLocalRandom.current().nextInt(10, 20 + 1);
        holder.k.setText(randomNum + "k");
        if (offer.getType().equals("adget")) {
            settings();
        }
        if (offer.getType().equals("colony")) {
            holder.k.setText(context.getResources().getInteger(R.integer.adcolony_video_points) + "+");
            initRewardedAd();
        }
        if (offer.getType().equals("iron")) {
            holder.k.setText(context.getResources().getInteger(R.integer.iron_source_video_point) + "+");
            IronSource.loadRewardedVideo();
        }
        if (offer.getType().equals("startapp")) {
            holder.k.setText(context.getResources().getInteger(R.integer.startapp_video_point) + "+");
            loadStartAppRewardedVideo();
        }
        if (offer.getType().equals("vungle")) {
            loadVungleAd();
            holder.k.setText(context.getResources().getInteger(R.integer.vungle_video_point) + "+");
        }
        if (offer.getType().equals("admob")) {
            loadAdmobVideo();
            holder.k.setText(context.getResources().getInteger(R.integer.admob_video_point) + "+");
        }
        if (offer.getType().equals("yodo1")) {
            holder.k.setText(context.getResources().getInteger(R.integer.yodo1_video_point) + "+");
        }
        if (offer.getType().equals("applovin")) {
            loadApplovinAd();
            holder.k.setText(context.getResources().getInteger(R.integer.applovin_video_point) + "+");
        }
        if (offer.getType().equals("unity")) {
            holder.k.setText(context.getResources().getInteger(R.integer.unity_video_point) + "+");
        }
        if (offer.getType().equals("chartboost")) {
            holder.k.setText(context.getResources().getInteger(R.integer.chartboost_video_points) + "+");
            loadChartBoostAd();
        }

        if (offer.getType().equals("iron_offer")) {
            IronSource.setOfferwallListener(new OfferwallListener() {
                @Override
                public void onOfferwallAvailable(boolean isAvailable) {

                }

                @Override
                public void onOfferwallOpened() {
                }

                @Override
                public void onOfferwallShowFailed(IronSourceError error) {
                }

                @Override
                public boolean onOfferwallAdCredited(int credits, int totalCredits, boolean totalCreditsFlag) {
                    return true;
                }

                @Override
                public void onGetOfferwallCreditsFailed(IronSourceError error) {
                }

                @Override
                public void onOfferwallClosed() {
                }
            });

        }


        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offer.getType().equals("adget")) {
                    dialog.show();
                    final HashMap<String, String> subids = new HashMap<String, String>();
                    subids.put("s2", "my sub id");
                    AdGateMedia adGateMedia = AdGateMedia.getInstance();
                    adGateMedia.loadOfferWall(context, ADGET_WALL_id, AppController.getInstance().getId(), subids, new OnOfferWallLoadSuccess() {
                        @Override
                        public void onOfferWallLoadSuccess() {
                            dialog.dismiss();
                            // Here you can call adGateMedia.showOfferWall();
                            AdGateMedia.getInstance().showOfferWall(context, new AdGateMedia.OnOfferWallClosed() {
                                @Override
                                public void onOfferWallClosed() {
                                    // Here you handle the 'Offer wall has just been closed' event
                                }
                            });
                        }
                    }, new OnOfferWallLoadFailed() {
                        @Override
                        public void onOfferWallLoadFailed(String reason) {
                            // Here you handle the errors with provided reason
                            dialog.dismiss();

                        }
                    });
                } else if (offer.getType().equals("toro")) {
                    dialog.show();
                    OffersInit.getInstance().create((Activity) context);
                    OffersInit.getInstance().setOfferWallListener(new OfferWallListener() {
                        @Override
                        public void onOTOfferWallInitSuccess() {
                            OffersInit.getInstance().showOfferWall((Activity) context);
                            dialog.dismiss();
                        }

                        @Override
                        public void onOTOfferWallInitFail(String s) {
                            Toast.makeText(context, s.trim(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }

                        @Override
                        public void onOTOfferWallOpened() {

                        }

                        @Override
                        public void onOTOfferWallCredited(double v, double v1) {

                        }

                        @Override
                        public void onOTOfferWallClosed() {

                        }
                    });
                } else if (offer.getType().equals("ayet")) {
                    holder.k.setText("5+");
                    if (AyetSdk.isInitialized()) {
                        AyetSdk.showOfferwall((Application) context.getApplicationContext(), context.getString(R.string.AYET_ADSLOT));

                    } else {
                        Toast.makeText(context, "Failed to load offerwall try again later.", Toast.LENGTH_SHORT).show();
                    }
                    AyetSdk.showOfferwall((Application) context.getApplicationContext(), context.getString(R.string.AYET_ADSLOT));
                } else if (offer.getType().equals("applovin")) {
                    holder.k.setText(context.getResources().getInteger(R.integer.applovin_video_point) + "+");
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (maxRewardedAd.isReady()) {
                                    dialog.show();
                                    maxRewardedAd.setListener(new MaxRewardedAdListener() {
                                        @Override
                                        public void onRewardedVideoStarted(MaxAd ad) {

                                        }

                                        @Override
                                        public void onRewardedVideoCompleted(MaxAd ad) {

                                        }

                                        @Override
                                        public void onUserRewarded(MaxAd ad, MaxReward reward) {
                                        }

                                        @Override
                                        public void onAdLoaded(MaxAd ad) {
                                        }

                                        @Override
                                        public void onAdDisplayed(MaxAd ad) {

                                        }

                                        @Override
                                        public void onAdHidden(MaxAd ad) {
                                            dialog.dismiss();
                                            String point = String.valueOf(context.getResources().getInteger(R.integer.applovin_video_point));
                                            // Add_Coins_(context,point,"Video credit");
                                            addPoint(context, point, "Video credit Applovin");
                                            loadApplovinAd();
                                        }

                                        @Override
                                        public void onAdClicked(MaxAd ad) {

                                        }

                                        @Override
                                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                            dialog.dismiss();
                                        }
                                    });
                                    maxRewardedAd.showAd();
                                } else {
                                    Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                                    loadApplovinAd();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Applovin");
                } else if (offer.getType().equals("iron")) {
                    IronSource.setRewardedVideoListener(new RewardedVideoListener() {
                        @Override
                        public void onRewardedVideoAdOpened() {
                        }

                        /*Invoked when the RewardedVideo ad view is about to be closed.
                        Your activity will now regain its focus.*/
                        @Override
                        public void onRewardedVideoAdClosed() {
                            Toast.makeText(context, "Coins added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedVideoAvailabilityChanged(boolean available) {
                            //Change the in-app 'Traffic Driver' state according to availability.

                        }

                        @Override
                        public void onRewardedVideoAdRewarded(Placement placement) {
                            dialog.dismiss();
                            String point = String.valueOf(context.getResources().getInteger(R.integer.iron_source_video_point));
                            // Add_Coins_(context,point,"Video credit");
                            addPoint(context, point, "Video credit Iron");
                            IronSource.loadRewardedVideo();
                        }

                        @Override
                        public void onRewardedVideoAdShowFailed(IronSourceError error) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onRewardedVideoAdClicked(Placement placement) {
                        }

                        @Override
                        public void onRewardedVideoAdStarted() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onRewardedVideoAdEnded() {
                        }
                    });
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (IronSource.isRewardedVideoAvailable()) {
                                    dialog.show();
                                    IronSource.showRewardedVideo();
                                } else {
                                    Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                                    IronSource.loadRewardedVideo();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Iron");
                } else if (offer.getType().equals("iron_offer")) {
                    if (IronSource.isOfferwallAvailable()) {
                        IronSource.showOfferwall();
                    } else {
                        Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                    }
                } else if (offer.getType().equals("unity")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                dialog.show();
                                UnityAds.load(PrefManager.getSavedString(context, PrefManager.UNITY_REWARDED_ID), loadListener);
                                loadListener = new IUnityAdsLoadListener() {
                                    @Override
                                    public void onUnityAdsAdLoaded(String s) {
                                        UnityAds.show((Activity) context, PrefManager.getSavedString(context, PrefManager.UNITY_REWARDED_ID), new UnityAdsShowOptions(), showListener);
                                    }

                                    @Override
                                    public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {
                                        dialog.dismiss();
                                        Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                                    }
                                };

                                showListener = new IUnityAdsShowListener() {
                                    @Override
                                    public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                                        dialog.dismiss();
                                        Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onUnityAdsShowStart(String s) {

                                    }

                                    @Override
                                    public void onUnityAdsShowClick(String s) {

                                    }

                                    @Override
                                    public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                                        dialog.dismiss();
                                        check = true;
                                        if (check) {
                                            String point = String.valueOf(context.getResources().getInteger(R.integer.unity_video_point));
                                            addPoint(context, point, "Video credit Unity");
                                            check = false;
                                        }
                                    }
                                };
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Unity");
                } else if (offer.getType().equals("colony")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                displayRewardVideoAd();
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Adcolony");
                } else if (offer.getType().equals("admob")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (rewardedAd != null) {
                                    dialog.show();
                                    rewardedAd.show(((Activity) context), rewardItem -> {
                                        // Handle the reward.
                                        dialog.dismiss();
                                        rewardedAd = null;
                                        String point = String.valueOf(context.getResources().getInteger(R.integer.admob_video_point));
                                        addPoint(context, point, "Video credit Admob");
                                        loadAdmobVideo();
                                    });
                                } else {
                                    Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                    loadAdmobVideo();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Admob");
                } else if (offer.getType().equals("vungle")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (Vungle.canPlayAd(PrefManager.getSavedString(context, VUNGLE_REWARDED_ID))) {
                                    dialog.show();
                                    Vungle.playAd(PrefManager.getSavedString(context, VUNGLE_REWARDED_ID), null, new PlayAdCallback() {
                                        @Override
                                        public void creativeId(String creativeId) {

                                        }

                                        @Override
                                        public void onAdStart(String placementReferenceId) {
                                        }

                                        @Override
                                        public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                                        }

                                        @Override
                                        public void onAdEnd(String placementId) {
                                            Log.e("AD_END", "onAdEnd: ");
                                            String point = String.valueOf(context.getResources().getInteger(R.integer.vungle_video_point));
                                            try {
                                                dialog.dismiss();
                                                addPoint(context, point, "Video credit Vungle");
                                                loadVungleAd();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onAdClick(String placementId) {

                                        }

                                        @Override
                                        public void onAdRewarded(String placementId) {
                                        }

                                        @Override
                                        public void onAdLeftApplication(String placementId) {

                                        }

                                        @Override
                                        public void onError(String placementReferenceId, VungleException exception) {
                                            dialog.dismiss();
                                            Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onAdViewed(String placementId) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                    loadVungleAd();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Vungle");
                } else if (offer.getType().equals("startapp")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (startAppAd.isReady()) {
                                    dialog.show();
                                    startAppAd.showAd(new AdDisplayListener() {
                                        @Override
                                        public void adHidden(Ad ad) {
                                            String point = String.valueOf(context.getResources().getInteger(R.integer.startapp_video_point));
                                            try {
                                                dialog.dismiss();
                                                addPoint(context, point, "Video credit Startapp");
                                                loadStartAppRewardedVideo();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void adDisplayed(Ad ad) {

                                        }

                                        @Override
                                        public void adClicked(Ad ad) {

                                        }

                                        @Override
                                        public void adNotDisplayed(Ad ad) {
                                            dialog.dismiss();
                                            Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Startapp");
                } else if (offer.getType().equals("yodo1")) {

                } else if (offer.getType().equals("chartboost")) {
                    isVideoLimitNotReach((videoLimitReach, isError) -> {
                        if (!isError) {
                            if (!videoLimitReach) {
                                if (isChartBoostAdLoaded) {
                                    chartboostRewarded.show();
                                } else {
                                    Toast.makeText(context, "Ad is not ready please try again after sometime", Toast.LENGTH_SHORT).show();
                                    loadChartBoostAd();
                                }
                            } else {
                                Toast.makeText(context, "Today limit is reach for this video", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something went wrong try again...", Toast.LENGTH_SHORT).show();
                        }
                    }, "Video credit Chartboost");
                }
            }
        });
    }

    private void loadChartBoostAd() {
        isChartBoostAdLoaded = false;
        chartboostRewarded = new Rewarded("location", new RewardedCallback() {
            @Override
            public void onRewardEarned(@NonNull RewardEvent rewardEvent) {
                String points = String.valueOf(context.getResources().getInteger(R.integer.chartboost_video_points));
                addPoint(context, points, "Video credit Chartboost");
                loadChartBoostAd();
            }

            @Override
            public void onAdDismiss(@NonNull DismissEvent dismissEvent) {

            }

            @Override
            public void onAdLoaded(@NonNull CacheEvent cacheEvent, @Nullable CacheError cacheError) {
// after this is successful ad can be shown
                isChartBoostAdLoaded = true;
            }

            @Override
            public void onAdRequestedToShow(@NonNull ShowEvent showEvent) {

            }

            @Override
            public void onAdShown(@NonNull ShowEvent showEvent, @Nullable ShowError showError) {

            }

            @Override
            public void onAdClicked(@NonNull ClickEvent clickEvent, @Nullable ClickError clickError) {

            }

            @Override
            public void onImpressionRecorded(@NonNull ImpressionEvent impressionEvent) {

            }
        }, null);
        chartboostRewarded.cache();
    }

    private void loadApplovinAd() {
        maxRewardedAd = MaxRewardedAd.getInstance(PrefManager.getSavedString(context, PrefManager.APPLOVIN_REWARDED_ID), ((Activity) context));
        maxRewardedAd.loadAd();
    }

    private void loadVungleAd() {
        Vungle.loadAd(PrefManager.getSavedString(context, VUNGLE_REWARDED_ID), new LoadAdCallback() {
            @Override
            public void onAdLoad(String placementReferenceId) {

            }

            @Override
            public void onError(String placementReferenceId, VungleException exception) {
            }
        });
    }

    private void loadAdmobVideo() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(context, PrefManager.getSavedString(context, ADMOB_REWARDED_ID), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                OfferWall_Adapter.this.rewardedAd = rewardedAd;
            }
        });
    }

    private void loadStartAppRewardedVideo() {
        startAppAd = new StartAppAd(context);
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
    }

    @Override
    public int getItemCount() {
        return offer_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView title, k;
        LinearLayout click;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            k = itemView.findViewById(R.id.k);
            click = itemView.findViewById(R.id.click);

        }
    }


    public void settings() {
        // showpDialog();
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(ContentValues.TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);


                    // Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                //  hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("settings", "settings");
                return params;
            }
        };
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("set");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                //Toast.makeText(MainActivity.this,feedObj.getString("check_p"),Toast.LENGTH_LONG).show();
                String AG_WALLCODE = (feedObj.getString("AG_WALLCODE"));
                String check_ag = (feedObj.getString("check_ag"));
                if (check_ag.equals("0")) {
                    ADGET_WALL_id = AG_WALLCODE;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //  listView.setVisibility(View.GONE);
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

        }
    }


    private void initRewardedAd() {
        rewardListener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial adReward) {
// Ad passed back in request filled callback, ad can now be shown
                rewardAdColony = adReward;
                isRewardLoaded = true;
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                super.onOpened(ad);
            }

            @Override
            public void onClosed(AdColonyInterstitial ad) {
                super.onClosed(ad);
                String point = String.valueOf(context.getResources().getInteger(R.integer.adcolony_video_points));
                Toast.makeText(context.getApplicationContext(), context.getResources().getInteger(R.integer.adcolony_video_points) + " coins added to your wallet!", Toast.LENGTH_SHORT).show();
                addPoint(context, point, "Video credit Adcolony");
                check = false;
                AdColony.requestInterstitial(PrefManager.getSavedString(context, PrefManager.ADCOLONY_INTERSTITAL_ID), rewardListener, rewardAdOptions);
            }

            @Override
            public void onClicked(AdColonyInterstitial ad) {
                super.onClicked(ad);
            }

            @Override
            public void onLeftApplication(AdColonyInterstitial ad) {
                super.onLeftApplication(ad);
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                super.onExpiring(ad);
            }
        };
// Ad specific options to be sent with request
        rewardAdOptions = new AdColonyAdOptions().enableConfirmationDialog(false).enableResultsDialog(false);
        AdColony.requestInterstitial(PrefManager.getSavedString(context, PrefManager.ADCOLONY_INTERSTITAL_ID), rewardListener, rewardAdOptions);
    }

    public void displayRewardVideoAd() {
        if (rewardAdColony != null && isRewardLoaded) {
            rewardAdColony.show();
            isRewardLoaded = false;
        } else {
            Toast.makeText(context.getApplicationContext(), "Reward Ad Is Not Loaded Yet or Request Not Filled", Toast.LENGTH_SHORT).show();
        }
    }

    private void isVideoLimitNotReach(IsVideoLimitReach isVideoLimitReach, String videoType) {
        JsonRequest stringRequest = new JsonRequest(Request.Method.POST, Base_Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("RESPONSE", "onResponse: "+response);
                            if (response.getString("error").equalsIgnoreCase("false")) {
                                int daily = response.getInt("daily");
                                int left = response.getInt("left");
                                if (left >= daily) {
                                    isVideoLimitReach.onVideoLimitReach(true, false);
                                } else {
                                    isVideoLimitReach.onVideoLimitReach(false, false);
                                }
                            } else {
                                isVideoLimitReach.onVideoLimitReach(false, false);
                            }
                        } catch (Exception e) {
                            isVideoLimitReach.onVideoLimitReach(false, true);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isVideoLimitReach.onVideoLimitReach(false, true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ACCESS_KEY, ACCESS_Value);
                params.put("usernamee", AppController.getInstance().getUsername());
                params.put("video", "video");
                params.put("videoType", videoType);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}