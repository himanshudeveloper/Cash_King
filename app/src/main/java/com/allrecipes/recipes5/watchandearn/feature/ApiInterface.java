package com.allrecipes.recipes5.watchandearn.feature;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("fetch_urls.php")
    Call<UrlModel> getUrls(@Field("id") String id);

    @FormUrlEncoded
    @POST("fetch_ads_ids.php")
    Call<AdsModelList> fetchAds(@Field("id") String id);

    @POST("fetch_banners.php")
    Call<List<BannerModel>> fetchBanners();

    @FormUrlEncoded
    @POST("uploadWithdrawalRequest.php")
    Call<MessageModel> uploadWithdrawalRequest(@FieldMap Map<String, String> map);
    @FormUrlEncoded
    @POST("fetch_ownads.php")
    Call<List<OwnAdsModel>> fetchOwnAds(@Field("app_id") String appId);
}

