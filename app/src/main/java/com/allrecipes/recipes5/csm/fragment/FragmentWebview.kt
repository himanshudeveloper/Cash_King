package com.allrecipes.recipes5.csm.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.allrecipes.recipes5.R

class FragmentWebview(
    private var targetUrl: String
) : Fragment() {

    private var webView: WebView? = null

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("url", targetUrl)
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)
        // Inflate the layout for this fragment
        savedInstanceState?.let {
            targetUrl = it.getString("url").toString()
        }
        webView = view.findViewById(R.id.webView)
        webView?.apply {
            loadUrl(targetUrl)

            // this will enable the javascript.

            // this will enable the javascript.
            settings.javaScriptEnabled = true

            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.

            // WebViewClient allows you to handle
            // onPageFinished and override Url loading.
            webViewClient = MyBrowser()
        }
        return view
    }

    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.destroy()
        webView = null
    }
}