package com.cevs.studosh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by TOSHIBA on 03.12.2016..
 */

public class FoiLogIn extends AppCompatActivity {
    final static String url ="https://login.foi.hr/login?service=https%3A%2F%2Felf.foi.hr%2Flogin%2Findex.php";
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(getBaseContext());
        setContentView(webView);

        final ProgressDialog progressDialog = ProgressDialog.show(FoiLogIn.this,"Foi","log in screen",true);
        progressDialog.show();


        webView.setWebViewClient( new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        webView.loadUrl(url);


    }


}
