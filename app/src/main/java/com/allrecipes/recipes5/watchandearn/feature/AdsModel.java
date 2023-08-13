package com.allrecipes.recipes5.watchandearn.feature;

public class AdsModel {
    String id;
    String appId;
    String appLovinSdkKey;
    String bannerTopNetworkName;
    String bannerTop;
    String bannerBottomNetworkName;
    String bannerBottom;
    String interstitialNetwork;
    String interstitialAds;
    String nativeAdsNetworkName;
    String nativeAds;
    String openAdsNetworkName;
    String openAds;


    public AdsModel(String id, String appId, String appLovinSdkKey, String bannerTopNetworkName, String bannerTop, String bannerBottomNetworkName, String bannerBottom, String interstitialNetwork, String interstitialAds, String nativeAdsNetworkName, String nativeAds, String openAdsNetworkName, String openAds) {
        this.id = id;
        this.appId = appId;
        this.appLovinSdkKey = appLovinSdkKey;
        this.bannerTopNetworkName = bannerTopNetworkName;
        this.bannerTop = bannerTop;
        this.bannerBottomNetworkName = bannerBottomNetworkName;
        this.bannerBottom = bannerBottom;
        this.interstitialNetwork = interstitialNetwork;
        this.interstitialAds = interstitialAds;
        this.nativeAdsNetworkName = nativeAdsNetworkName;
        this.nativeAds = nativeAds;
        this.openAdsNetworkName = openAdsNetworkName;
        this.openAds = openAds;
    }

    public String getId() {
        return id;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppLovinSdkKey() {
        return appLovinSdkKey;
    }

    public String getBannerTop() {
        return bannerTop;
    }

    public String getBannerTopNetworkName() {
        return bannerTopNetworkName;
    }

    public String getBannerBottom() {
        return bannerBottom;
    }

    public String getBannerBottomNetworkName() {
        return bannerBottomNetworkName;
    }

    public String getInterstitialAds() {
        return interstitialAds;
    }

    public String getInterstitialNetwork() {
        return interstitialNetwork;
    }

    public String getNativeAds() {
        return nativeAds;
    }

    public String getNativeAdsNetworkName() {
        return nativeAdsNetworkName;
    }

    public String getOpenAdsNetworkName() {
        return openAdsNetworkName;
    }

    public String getOpenAds() {
        return openAds;
    }
}


//CREATE TABLE `ads_table`
//      (`id` varchar(255),
//      `appId` varchar(255),
//      `appLovinSdkKey` varchar(255),
//      `bannerTop` varchar(255),
//      `bannerTopNetworkName` varchar(255),
//      `bannerBottom` varchar(255),
//      `bannerBottomNetworkName` varchar(255),
//      `interstitialAds` varchar(255),
//      `interstitialNetwork` varchar(255),
//      `nativeAds` varchar(255),
//      `nativeAdsNetworkName` varchar(255))
//      ;