package com.example.seventimer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShowHTML extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_html);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        Intent intent = getIntent();
        String latitude = intent.getStringExtra("Latitude");
        String longitude = intent.getStringExtra("Longitude");
        int index = intent.getIntExtra("index",1);
        /*WebView webView = (WebView)findViewById(R.id.web_view);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar) ;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new WebViewClient());*/
        init();
        String webURL = "";
        if(index == 1){
            webURL = "http://www.7timer.info/bin/civil.php?lon=" + longitude + "&lat=" + latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0";
            webView.loadUrl(webURL);
        }
        else if (index == 2){
            webURL = "http://www.7timer.info/bin/civillight.php?lon=" + longitude + "&lat=" + latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0";
            //webView.setRotation(90.0f);
            webView.loadUrl(webURL);
        }
        else if (index == 3){
            webURL = "http://www.7timer.info/bin/two.php?lon=" + longitude + "&lat=" + latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0";
            webView.loadUrl(webURL);
        }
        else if (index == 4){
            webURL = "http://www.7timer.info/bin/astro.php?lon=" + longitude + "&lat=" + latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0";
            //webView.setRotation(90.0f);
            webView.loadUrl(webURL);
        }
        else if (index == 5){
            webURL = "http://www.7timer.info/bin/meteo.php?lon=" + longitude + "&lat=" + latitude + "&lang=zh-CN&ac=0&unit=metric&tzshift=0";
            webView.loadUrl(webURL);
        }
        else
            Toast.makeText(MyApplication.getContext(),"打开页面数据出错",Toast.LENGTH_LONG).show();
    }

    private void init() {
        webView=findViewById(R.id.web_view);
        progressBar=findViewById(R.id.progressBar);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        webView.clearCache(true);
        webView.clearHistory();
        webView.destroy();
        webView = null;
        progressBar = null;
        super.onDestroy();
    }
}