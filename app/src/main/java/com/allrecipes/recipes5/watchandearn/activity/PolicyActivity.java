package com.allrecipes.recipes5.watchandearn.activity;

import static com.allrecipes.recipes5.watchandearn.activity.MainActivity.coins;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allrecipes.recipes5.PrefManager;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.ironsource.mediationsdk.IronSource;

public class PolicyActivity extends AppCompatActivity {

    ShowAds showAds = new ShowAds();
    TextView points_textView;
    private String type = "", url = "";
    private PolicyActivity activity;
    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        activity = this;
        swipeRefreshLayout = findViewById(R.id.swipe);
        webView = findViewById(R.id.webView);
        type = getIntent().getStringExtra("type");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showAds.showInterstitialAds(this);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            TextView titleText = toolbar.findViewById(R.id.toolbarText);
            titleText.setText(getResources().getString(R.string.app_name));
            points_textView = toolbar.findViewById(R.id.points_text_in_toolbar);
            points_textView.setText(String.valueOf(coins.coin));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type != null) {
            switch (type) {
                case "Privacy Policy":
                case "About Us":
                    webView.loadUrl("https://gedgetsworld.in/watch_and_earn/privacy_policy.html");
                    break;
                case "Terms & Conditions":
                    webView.loadUrl("https://gedgetsworld.in/watch_and_earn/terms_and_conditions.html");
                    break;
                case "Instruction":
                    webView.loadData("You Can Earn 10 Points Simply Daily Check in \n" + "\n" + "Scratch All Sliver Card\n" + "\n" + "Scratch All Platinum Card\n" + "\n" + "Scratch All Gold Card\n" + "\n" + "If you have any kind of issue or facing any kind of problems You can freely contact us  \n" + "scratch.cashapp786@gmail.com", "text/html; charset=utf-8", "UTF-8");

                    break;
                case "disclaimer":
                    webView.loadData(getString(R.string.disclaimer_text), "text/html; charset=utf-8", "UTF-8");

                    break;

            }

        } else {
            webView.loadUrl("file:///android_asset/privacy_policy.html");
        }

    }

//    private void onClick() {
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                LoadPage(url);
//            }
//        });
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                LoadPage(url);
//            }
//        });
//    }

//    @SuppressLint("SetJavaScriptEnabled")
//    public void LoadPage(String Url) {
//        webView.setWebViewClient(new MyWebViewClient());
//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                swipeRefreshLayout.setRefreshing(progress != 100);
//            }
//        });
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.loadUrl(Url);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        showAds.showTopBanner(this, findViewById(R.id.adView_top));
        showAds.showBottomBanner(this, findViewById(R.id.adView_bottom));
        IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showAds.destroyBanner();
        IronSource.onPause(this);

    }

//    private static class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String myUrl) {
//            view.loadUrl(myUrl);
//            return false;
//        }
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}