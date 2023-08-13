package com.allrecipes.recipes5

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.allrecipes.recipes5.helper.AppController
import com.allrecipes.recipes5.helper.PrefManager.*
import com.adcolony.sdk.*
import com.applovin.mediation.*
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.sdk.AppLovinSdkUtils
import com.chartboost.sdk.ads.Interstitial
import com.chartboost.sdk.ads.Rewarded
import com.chartboost.sdk.callbacks.BannerCallback
import com.chartboost.sdk.callbacks.InterstitialCallback
import com.chartboost.sdk.callbacks.RewardedCallback
import com.chartboost.sdk.events.*
import com.consoliads.sdk.ConsoliadsSdk
import com.consoliads.sdk.PlaceholderName
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerAdListener
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerSize
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerView
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.ironsource.mediationsdk.sdk.RewardedVideoListener
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds.*
import com.unity3d.ads.UnityAdsShowOptions
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import com.vungle.warren.*
import com.vungle.warren.error.VungleException


object AdsManager {
    private var maxRewardedAd: MaxRewardedAd? = null
    private var maxInterstitialAd: MaxInterstitialAd? = null
    private var isUnityAdLoaded = false

    private var startAppAd: StartAppAd? = null
    private var adColonyInterstitialAd: AdColonyInterstitial? = null
    private var chartboostInterstitial: Interstitial? = null
    private var chartboostRewarded: Rewarded? = null
    private var onVideoAdEnded: OnVideoAdEnded? = null
    private var rewardAdColony: AdColonyInterstitial? = null
    private var rewardAdOptions: AdColonyAdOptions? = null
    private var rewardListener: AdColonyInterstitialListener? = null

    @JvmStatic
    fun loadBannerAd(context: Context, linearLayout: LinearLayout) {
        when (getSavedString(context, BANNER_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                // Stretch to the width of the screen for banners to be fully functional
                val width = ViewGroup.LayoutParams.MATCH_PARENT

                // Banner height on phones and tablets is 50 and 90, respectively
                val isTablet = AppLovinSdkUtils.isTablet(context)
                val heightPx = AppLovinSdkUtils.dpToPx(context, if (isTablet) 90 else 50)
                val maxAdView = MaxAdView(getSavedString(context, APPLOVIN_BANNER_ID), context)
                val frame = FrameLayout.LayoutParams(width, heightPx, Gravity.TOP)
                maxAdView.layoutParams = frame
                maxAdView.apply {
                    layoutParams = FrameLayout.LayoutParams(width, heightPx)
                    setListener(object : MaxAdViewAdListener {
                        override fun onAdLoaded(ad: MaxAd?) {
                            linearLayout.apply {
                                visibility = VISIBLE
                                removeAllViews()
                                addView(maxAdView)
                            }
                        }

                        override fun onAdDisplayed(ad: MaxAd?) {

                        }

                        override fun onAdHidden(ad: MaxAd?) {

                        }

                        override fun onAdClicked(ad: MaxAd?) {

                        }

                        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                            Log.d("APPLOVIN", "onAdLoadFailed: ${error.toString()}")
                        }

                        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                        }

                        override fun onAdExpanded(ad: MaxAd?) {

                        }

                        override fun onAdCollapsed(ad: MaxAd?) {

                        }

                    })
                    // Load the ad
                    loadAd()
                    startAutoRefresh()
                }
                // end applovin banner
            }
            UNITY_AD_TYPE -> {
                val unityBanner = BannerView(
                    context as Activity?,
                    getSavedString(context, UNITY_BANNER_ID),
                    UnityBannerSize(320, 50)
                )
                unityBanner.apply {
                    listener = object : BannerView.IListener {
                        override fun onBannerLoaded(bannerAdView: BannerView?) {
                            linearLayout.apply {
                                visibility = VISIBLE
                                removeAllViews()
                                addView(bannerAdView)
                            }
                        }

                        override fun onBannerClick(bannerAdView: BannerView?) {

                        }

                        override fun onBannerFailedToLoad(
                            bannerAdView: BannerView?, errorInfo: BannerErrorInfo?
                        ) {

                        }

                        override fun onBannerLeftApplication(bannerView: BannerView?) {

                        }

                    }
                    load()
                    // end unity banner
                }
            }
            START_APP_AD_TYPE -> {
                val banner = Banner(context as Activity)
                val bannerParameters = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL)
                bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                linearLayout.apply {
                    visibility = VISIBLE
                    removeAllViews()
                    addView(banner, bannerParameters)
                }
                // end start app banner
            }
            ADCOLONY_AD_TYPE -> {
                AdColony.requestAdView(
                    getSavedString(context, ADCOLONY_BANNER_ID), object : AdColonyAdViewListener() {
                        override fun onRequestFilled(p0: AdColonyAdView?) {
                            linearLayout.apply {
                                visibility = VISIBLE
                                removeAllViews()
                                addView(p0)
                            }
                        }
                    }, AdColonyAdSize.BANNER, AdColonyAdOptions()
                )
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                IronSource.init(
                    context as Activity,
                    getSavedString(AppController.getInstance(), IRON_SOURCE_APP_KEY),
                    /* IronSource.AD_UNIT.OFFERWALL,
                     IronSource.AD_UNIT.REWARDED_VIDEO,*/
                    IronSource.AD_UNIT.BANNER
                )
                val banner = IronSource.createBanner(context as Activity, ISBannerSize.BANNER)
                val layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.addView(banner, 0, layoutParams)
                banner.bannerListener = object : BannerListener {
                    override fun onBannerAdLoaded() {
                        // Called after a banner ad has been successfully loaded
                        linearLayout.apply {
                            visibility = VISIBLE
                        }
                    }

                    override fun onBannerAdLoadFailed(error: IronSourceError?) {
                        // Called after a banner has attempted to load an ad but failed.
                        context.runOnUiThread(Runnable {
                            linearLayout.removeAllViews()
                        })
                    }

                    override fun onBannerAdClicked() {
                        // Called after a banner has been clicked.
                    }

                    override fun onBannerAdScreenPresented() {
                        // Called when a banner is about to present a full screen content.
                    }

                    override fun onBannerAdScreenDismissed() {
                        // Called after a full screen content has been dismissed
                    }

                    override fun onBannerAdLeftApplication() {
                        // Called when a user would be taken out of the application context.
                    }
                }
                IronSource.loadBanner(banner)
            }
            VUNGLE_AD_TYPE -> {
                val bannerAdConfig = BannerAdConfig()
                bannerAdConfig.adSize = AdConfig.AdSize.BANNER
                Banners.loadBanner(getSavedString(context, VUNGLE_BANNER_ID),
                    bannerAdConfig,
                    object : LoadAdCallback {
                        override fun onAdLoad(placementId: String?) {
                            val vungleBanner = Banners.getBanner(
                                getSavedString(context, VUNGLE_BANNER_ID), bannerAdConfig, null
                            )
                            linearLayout.apply {
                                visibility = VISIBLE
                                removeAllViews()
                                addView(vungleBanner)
                            }
                        }

                        override fun onError(placementId: String?, exception: VungleException?) {

                        }

                    })
            }
            CHARTBOOST_AD_TYPE -> {
                val chartboostBanner = com.chartboost.sdk.ads.Banner(
                    context,
                    "location",
                    com.chartboost.sdk.ads.Banner.BannerSize.STANDARD,
                    object : BannerCallback {
                        override fun onAdRequestedToShow(event: ShowEvent) {}
                        override fun onAdShown(
                            event: ShowEvent, error: ShowError?
                        ) {
                        }

                        override fun onAdClicked(
                            event: ClickEvent, error: ClickError?
                        ) {
                        }

                        override fun onAdLoaded(event: CacheEvent, error: CacheError?) {
                            event.ad.show()
                        }

                        override fun onImpressionRecorded(event: ImpressionEvent) {}
                    },
                    null
                )
                linearLayout.apply {
                    visibility = VISIBLE
                    removeAllViews()
                    addView(chartboostBanner)
                }
                chartboostBanner.cache()
            }
            CONSOLIADS_TYPE -> {
                val bannerView = ConsoliadsSdkBannerView(context)
                ConsoliadsSdk().showBanner(context as Activity,
                    ConsoliadsSdkBannerSize.Banner,
                    bannerView,
                    object : ConsoliadsSdkBannerAdListener {
                        override fun onBannerAdLoaded(placeholderName: PlaceholderName?) {
                            linearLayout.apply {
                                visibility = VISIBLE
                                removeAllViews()
                                addView(bannerView)
                            }
                        }

                        override fun onBannerAdFailedToLoad(
                            placeholderName: PlaceholderName?, reason: String?
                        ) {

                        }

                        override fun onBannerAdRefreshed(placeholderName: PlaceholderName?) {

                        }

                        override fun onBannerAdClicked(
                            placeholderName: PlaceholderName?, ProductId: String?
                        ) {

                        }

                        override fun onBannerAdClosed(placeholderName: PlaceholderName?) {

                        }

                    })
            }
        }
    }

    @JvmStatic
    fun isInterstitialLoaded(): Boolean {
        when (getSavedString(AppController.getInstance(), INTERSTITAL_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                return maxInterstitialAd?.isReady == true
            }
            UNITY_AD_TYPE -> {
                return isUnityAdLoaded
            }
            START_APP_AD_TYPE -> {
                return startAppAd?.isReady == true
                // end start app banner
            }
            ADCOLONY_AD_TYPE -> {
                return adColonyInterstitialAd != null && adColonyInterstitialAd?.isExpired == false
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                return IronSource.isInterstitialReady()
            }
            VUNGLE_AD_TYPE -> {
                return Vungle.canPlayAd(
                    getSavedString(
                        AppController.getInstance(), VUNGLE_INTERSTITAL_ID
                    )
                )
            }
            CHARTBOOST_AD_TYPE -> {
                return chartboostInterstitial?.isCached() == true
            }
            CONSOLIADS_TYPE -> {
                return ConsoliadsSdk().isInterstitialAvailable
            }
            else -> {
                return false
            }
        }
    }

    @JvmStatic
    fun showInterstitalAd(activity: Activity) {
        when (getSavedString(AppController.getInstance(), INTERSTITAL_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                maxInterstitialAd?.showAd()
            }
            UNITY_AD_TYPE -> {
                isUnityAdLoaded = false
                show(activity,
                    getSavedString(activity, UNITY_INTERSTITAL_ID),
                    UnityAdsShowOptions(),
                    object : IUnityAdsShowListener {
                        override fun onUnityAdsShowFailure(
                            placementId: String, error: UnityAdsShowError, message: String
                        ) {

                        }

                        override fun onUnityAdsShowStart(placementId: String) {

                        }

                        override fun onUnityAdsShowClick(placementId: String) {

                        }

                        override fun onUnityAdsShowComplete(
                            placementId: String, state: UnityAdsShowCompletionState
                        ) {
                            loadInterstitalAd(activity)
                        }
                    })
            }
            START_APP_AD_TYPE -> {
                startAppAd?.showAd(object : AdDisplayListener {
                    override fun adHidden(p0: Ad?) {
                        loadInterstitalAd(activity)
                    }

                    override fun adDisplayed(p0: Ad?) {

                    }

                    override fun adClicked(p0: Ad?) {

                    }

                    override fun adNotDisplayed(p0: Ad?) {

                    }
                })
                // end start app banner
            }
            ADCOLONY_AD_TYPE -> {
                adColonyInterstitialAd?.show()
                loadInterstitalAd(activity)
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                IronSource.showInterstitial()
            }
            VUNGLE_AD_TYPE -> {
                Vungle.playAd(getSavedString(
                    AppController.getInstance(), APPLOVIN_INTERSTITAL_ID
                ), null, object : PlayAdCallback {
                    override fun creativeId(creativeId: String?) {

                    }

                    override fun onAdStart(placementReferenceId: String) {}
                    override fun onAdEnd(
                        placementId: String?, completed: Boolean, isCTAClicked: Boolean
                    ) {

                    }

                    override fun onAdEnd(placementId: String?) {
                        loadInterstitalAd(activity)
                    }

                    override fun onAdClick(placementId: String?) {

                    }

                    override fun onAdRewarded(placementId: String?) {

                    }

                    override fun onAdLeftApplication(placementId: String?) {

                    }

                    override fun onError(
                        placementReferenceId: String, exception: VungleException
                    ) {
                    }

                    override fun onAdViewed(placementId: String?) {

                    }
                })
            }
            CHARTBOOST_AD_TYPE -> {
                chartboostInterstitial?.show()
                loadInterstitalAd(activity)
            }
            CONSOLIADS_TYPE -> {
                ConsoliadsSdk().showInterstitial(activity)
                loadInterstitalAd(activity)
            }
            else -> {

            }
        }
    }

    @JvmStatic
    fun loadInterstitalAd(activity: Activity) {
        when (getSavedString(activity, INTERSTITAL_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                maxInterstitialAd = MaxInterstitialAd(
                    getSavedString(activity, APPLOVIN_INTERSTITAL_ID), activity
                )
                maxInterstitialAd?.setListener(object : MaxAdListener {
                    override fun onAdLoaded(ad: MaxAd?) {

                    }

                    override fun onAdDisplayed(ad: MaxAd?) {

                    }

                    override fun onAdHidden(ad: MaxAd?) {
                        maxInterstitialAd?.loadAd()
                    }

                    override fun onAdClicked(ad: MaxAd?) {

                    }

                    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {

                    }

                    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {

                    }
                })
                // Load the first ad
                maxInterstitialAd?.loadAd()
                // end applovin interstital
            }
            UNITY_AD_TYPE -> {
                load(
                    getSavedString(activity, UNITY_INTERSTITAL_ID),
                    object : IUnityAdsLoadListener {
                        override fun onUnityAdsAdLoaded(placementId: String) {
                            isUnityAdLoaded = true
                        }

                        override fun onUnityAdsFailedToLoad(
                            placementId: String, error: UnityAdsLoadError, message: String
                        ) {
                            isUnityAdLoaded = false
                        }
                    })
            }
            START_APP_AD_TYPE -> {
                if (startAppAd == null || startAppAd?.isReady == false) {
                    startAppAd = StartAppAd(activity)
                    startAppAd?.loadAd(StartAppAd.AdMode.AUTOMATIC)
                    // end start app banner
                }
            }
            ADCOLONY_AD_TYPE -> {
                AdColony.requestInterstitial(
                    getSavedString(activity, ADCOLONY_INTERSTITAL_ID),
                    object : AdColonyInterstitialListener() {
                        override fun onRequestFilled(p0: AdColonyInterstitial?) {
                            adColonyInterstitialAd = p0
                        }

                    },
                    AdColonyAdOptions()
                )
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                IronSource.init(
                    activity,
                    getSavedString(AppController.getInstance(), IRON_SOURCE_APP_KEY),
                    IronSource.AD_UNIT.INTERSTITIAL
                )
                IronSource.setInterstitialListener(object: InterstitialListener {
                    override fun onInterstitialAdReady() {

                    }

                    override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {

                    }

                    override fun onInterstitialAdOpened() {

                    }

                    override fun onInterstitialAdClosed() {
                        IronSource.loadInterstitial()
                    }

                    override fun onInterstitialAdShowSucceeded() {

                    }

                    override fun onInterstitialAdShowFailed(p0: IronSourceError?) {

                    }

                    override fun onInterstitialAdClicked() {

                    }

                })
                IronSource.loadInterstitial()
            }
            VUNGLE_AD_TYPE -> {
                if (Vungle.isInitialized()) {
                    Vungle.loadAd(getSavedString(activity, VUNGLE_INTERSTITAL_ID),
                        object : LoadAdCallback {
                            override fun onAdLoad(placementId: String?) {

                            }

                            override fun onError(
                                placementId: String?, exception: VungleException?
                            ) {

                            }
                        })
                }
            }
            CHARTBOOST_AD_TYPE -> {
                chartboostInterstitial = Interstitial("location", object : InterstitialCallback {
                    override fun onAdRequestedToShow(@NonNull showEvent: ShowEvent) {}
                    override fun onAdShown(
                        @NonNull showEvent: ShowEvent, @Nullable showError: ShowError?
                    ) {
                    }

                    override fun onAdClicked(
                        @NonNull clickEvent: ClickEvent, @Nullable clickError: ClickError?
                    ) {
                    }

                    override fun onAdDismiss(event: DismissEvent) {

                    }

                    override fun onAdLoaded(event: CacheEvent, error: CacheError?) {

                    }

                    override fun onImpressionRecorded(@NonNull impressionEvent: ImpressionEvent) {}
                }, null)
                chartboostInterstitial?.cache()
            }
            CONSOLIADS_TYPE -> {
                ConsoliadsSdk().loadInterstitial()
            }
        }
    }

    @JvmStatic
    fun loadApplovinNativeAd(context: Context): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.applovin_native_ad_layout)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
//                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build()
        return MaxNativeAdView(binder, context)
    }

    @JvmStatic
    fun loadRewardedVideoAd(activity: Activity) {
        when (getSavedString(activity, REWARDED_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                maxRewardedAd = MaxRewardedAd.getInstance(
                    getSavedString(activity, APPLOVIN_REWARDED_ID),
                    activity
                )
                maxRewardedAd?.loadAd()
            }
            UNITY_AD_TYPE -> {
                val loadListener = object : IUnityAdsLoadListener {
                    override fun onUnityAdsAdLoaded(s: String) {
                        isUnityAdLoaded = true
                    }

                    override fun onUnityAdsFailedToLoad(
                        s: String,
                        unityAdsLoadError: UnityAdsLoadError,
                        s1: String
                    ) {
                        isUnityAdLoaded = false
                    }
                }
                try {
                    load(getSavedString(activity, UNITY_REWARDED_ID), loadListener)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            START_APP_AD_TYPE -> {
                if (startAppAd == null || startAppAd?.isReady == false) {
                    startAppAd = StartAppAd(activity)
                    startAppAd?.loadAd(StartAppAd.AdMode.AUTOMATIC)
                    // end start app banner
                }
            }
            ADCOLONY_AD_TYPE -> {
                rewardListener = object : AdColonyInterstitialListener() {
                    override fun onRequestFilled(adReward: AdColonyInterstitial) { // Ad passed back in request filled callback, ad can now be shown
                        rewardAdColony = adReward
                    }

                    override fun onRequestNotFilled(zone: AdColonyZone) {}
                    override fun onOpened(ad: AdColonyInterstitial) {
                        super.onOpened(ad)
                    }

                    override fun onClosed(ad: AdColonyInterstitial) {
                        super.onClosed(ad)
                        onVideoAdEnded?.videoWatched()
                        rewardAdColony = null
                        rewardListener?.let {
                            AdColony.requestInterstitial(
                                getSavedString(
                                    activity,
                                    ADCOLONY_REWARDED_ID
                                ), it, rewardAdOptions
                            )
                        }
                    }

                    override fun onClicked(ad: AdColonyInterstitial) {
                        super.onClicked(ad)
                    }

                    override fun onLeftApplication(ad: AdColonyInterstitial) {
                        super.onLeftApplication(ad)
                    }

                    override fun onExpiring(ad: AdColonyInterstitial) {
                        super.onExpiring(ad)
                    }
                }
                // Ad specific options to be sent with request
                // Ad specific options to be sent with request
                rewardAdOptions =
                    AdColonyAdOptions().enableConfirmationDialog(false).enableResultsDialog(false)
                AdColony.requestInterstitial(
                    getSavedString(activity, ADCOLONY_INTERSTITAL_ID),
                    rewardListener as AdColonyInterstitialListener,
                    rewardAdOptions
                )

                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                IronSource.init(
                    activity,
                    getSavedString(AppController.getInstance(), IRON_SOURCE_APP_KEY),
                    IronSource.AD_UNIT.REWARDED_VIDEO
                )
                IronSource.setRewardedVideoListener(object : RewardedVideoListener {
                    override fun onRewardedVideoAdOpened() {

                    }

                    override fun onRewardedVideoAdClosed() {

                    }

                    override fun onRewardedVideoAvailabilityChanged(p0: Boolean) {

                    }

                    override fun onRewardedVideoAdStarted() {

                    }

                    override fun onRewardedVideoAdEnded() {

                    }

                    override fun onRewardedVideoAdRewarded(p0: Placement?) {
                        loadRewardedVideoAd(activity)
                        onVideoAdEnded?.videoWatched()
                    }

                    override fun onRewardedVideoAdShowFailed(p0: IronSourceError?) {

                    }

                    override fun onRewardedVideoAdClicked(p0: Placement?) {

                    }
                })
                IronSource.loadRewardedVideo()
            }
            VUNGLE_AD_TYPE -> {
                if (Vungle.isInitialized()) {
                    Vungle.loadAd(getSavedString(activity, VUNGLE_REWARDED_ID),
                        object : LoadAdCallback {
                            override fun onAdLoad(placementId: String?) {

                            }

                            override fun onError(
                                placementId: String?, exception: VungleException?
                            ) {

                            }
                        })
                }
            }
            CHARTBOOST_AD_TYPE -> {
                chartboostRewarded = Rewarded("location", object : RewardedCallback {
                    override fun onAdClicked(event: ClickEvent, error: ClickError?) {

                    }

                    override fun onAdDismiss(event: DismissEvent) {

                    }

                    override fun onAdLoaded(event: CacheEvent, error: CacheError?) {

                    }

                    override fun onAdRequestedToShow(event: ShowEvent) {

                    }

                    override fun onAdShown(event: ShowEvent, error: ShowError?) {

                    }

                    override fun onImpressionRecorded(event: ImpressionEvent) {

                    }

                    override fun onRewardEarned(event: RewardEvent) {
                        onVideoAdEnded?.videoWatched()
                        loadRewardedVideoAd(activity)
                    }

                })
                chartboostRewarded?.cache()
            }
            CONSOLIADS_TYPE -> {
                ConsoliadsSdk().loadRewardedVideoAd()
            }
        }
    }

    @JvmStatic
    fun isRewardedVideoAdLoaded(activity: Activity): Boolean {
        when (getSavedString(AppController.getInstance(), REWARDED_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                return maxRewardedAd?.isReady == true
            }
            UNITY_AD_TYPE -> {
                return isUnityAdLoaded
            }
            START_APP_AD_TYPE -> {
                return startAppAd?.isReady == true
                // end start app banner
            }
            ADCOLONY_AD_TYPE -> {
                return rewardAdColony != null && rewardAdColony?.isExpired == false
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                return IronSource.isRewardedVideoAvailable()
            }
            VUNGLE_AD_TYPE -> {
                return Vungle.canPlayAd(
                    getSavedString(
                        activity, VUNGLE_REWARDED_ID
                    )
                )
            }
            CHARTBOOST_AD_TYPE -> {
                return chartboostRewarded?.isCached() == true
            }
            CONSOLIADS_TYPE -> {
                return ConsoliadsSdk().isRewardedVideoAvailable
            }
            else -> {
                return false
            }
        }
    }

    @JvmStatic
    fun showRewardedVideo(onVideoAdEnded: OnVideoAdEnded, activity: Activity) {
        this.onVideoAdEnded = onVideoAdEnded
        when (getSavedString(AppController.getInstance(), REWARDED_AD_TYPE)) {
            APPLOVIN_AD_TYPE -> {
                maxRewardedAd?.setListener(object : MaxRewardedAdListener {
                    override fun onRewardedVideoStarted(ad: MaxAd) {}
                    override fun onRewardedVideoCompleted(ad: MaxAd) {}
                    override fun onUserRewarded(ad: MaxAd, reward: MaxReward) {}
                    override fun onAdLoaded(ad: MaxAd) {}
                    override fun onAdDisplayed(ad: MaxAd) {}
                    override fun onAdHidden(ad: MaxAd) {
                        maxRewardedAd?.loadAd()
                        onVideoAdEnded.videoWatched()
                    }

                    override fun onAdClicked(ad: MaxAd) {}
                    override fun onAdLoadFailed(adUnitId: String, error: MaxError) {

                    }

                    override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {

                    }
                })
                maxRewardedAd?.showAd()
            }
            UNITY_AD_TYPE -> {
                val showListener = object : IUnityAdsShowListener {
                    override fun onUnityAdsShowFailure(
                        s: String,
                        unityAdsShowError: UnityAdsShowError,
                        s1: String
                    ) {
                    }

                    override fun onUnityAdsShowStart(s: String) {}
                    override fun onUnityAdsShowClick(s: String) {}
                    override fun onUnityAdsShowComplete(
                        s: String,
                        unityAdsShowCompletionState: UnityAdsShowCompletionState
                    ) {
                        isUnityAdLoaded = false
                        loadRewardedVideoAd(activity)
                        onVideoAdEnded.videoWatched()
                    }
                }
                show(
                    activity,
                    getSavedString(activity, UNITY_REWARDED_ID),
                    UnityAdsShowOptions(),
                    showListener
                )
            }
            START_APP_AD_TYPE -> {
                startAppAd?.showAd(object : AdDisplayListener {
                    override fun adHidden(p0: Ad?) {
                        loadRewardedVideoAd(activity)
                        onVideoAdEnded.videoWatched()
                    }

                    override fun adDisplayed(p0: Ad?) {

                    }

                    override fun adClicked(p0: Ad?) {

                    }

                    override fun adNotDisplayed(p0: Ad?) {

                    }
                })
                // end start app banner
            }
            ADCOLONY_AD_TYPE -> {
                rewardAdColony?.let {
                    rewardAdColony?.show()
                }
                // end ad colony banner
            }
            IRON_SOURCE_AD_TYPE -> {
                IronSource.showRewardedVideo()
                onVideoAdEnded.videoWatched()
            }
            VUNGLE_AD_TYPE -> {
                Vungle.playAd(
                    getSavedString(activity, VUNGLE_REWARDED_ID),
                    null,
                    object : PlayAdCallback {
                        override fun creativeId(creativeId: String?) {

                        }

                        override fun onAdStart(placementReferenceId: String) {}
                        override fun onAdEnd(
                            placementReferenceId: String,
                            completed: Boolean,
                            isCTAClicked: Boolean
                        ) {
                        }

                        override fun onAdEnd(placementId: String?) {

                        }

                        override fun onAdClick(placementId: String?) {

                        }

                        override fun onAdRewarded(placementId: String?) {
                            onVideoAdEnded.videoWatched()
                            loadRewardedVideoAd(activity)
                        }

                        override fun onAdLeftApplication(placementId: String?) {

                        }

                        override fun onError(
                            placementReferenceId: String,
                            exception: VungleException
                        ) {
                        }

                        override fun onAdViewed(placementId: String?) {

                        }
                    })
            }
            CHARTBOOST_AD_TYPE -> {
                chartboostRewarded?.show()
                onVideoAdEnded.videoWatched()
            }
        }
    }

}