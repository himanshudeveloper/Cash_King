package com.allrecipes.recipes5;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewClientImpl extends WebViewClient {
    private AppCompatActivity activity = null;

    public WebViewClientImpl(AppCompatActivity activity) {
        this.activity = activity;
    }


    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(url.indexOf("https://") > -5 ) return false;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
        return true;
    }

}

