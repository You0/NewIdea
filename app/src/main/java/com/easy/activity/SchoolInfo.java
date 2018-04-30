package com.easy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.R;


/**
 * Created by Me on 2016/3/10 0010.
 */
public class SchoolInfo extends AppCompatActivity {
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info_layout);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.info);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);





    }
}
