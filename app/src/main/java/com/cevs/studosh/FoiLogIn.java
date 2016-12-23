package com.cevs.studosh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by TOSHIBA on 03.12.2016..
 */

public class FoiLogIn extends AppCompatActivity {
    String url = "https://login.foi.hr/login?service=http%3A%2F%2Fnastava.foi.hr%2Fj_spring_cas_security_check";
    WebView webView;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //CookieManager.setAcceptFileSchemeCookies(true);
        webView = new WebView(getBaseContext());
        setContentView(webView);
        progressDialog = ProgressDialog.show(FoiLogIn.this, "Redirecting to Foi", "Please wait...", true);
        progressDialog.show();


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("URL", url);
                if (url.contains("ajaxSuccess")){
                    String sessionId = url.split("=")[1];
                    Log.d("SessionId",sessionId);
                    webView.setVisibility(View.GONE);
                    Intent output = new Intent();
                    output.putExtra("SESSIONID",sessionId);
                    setResult(RESULT_OK, output);
                    finish();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if(progressDialog!=null)
                    progressDialog.dismiss();
            }

        });
        webView.loadUrl(url);

    }


    /*When back button is pressed, finish this activity and return RESULT_CANCELED
    so that Main Activity know to not start downloading data from server*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent output = new Intent();
        setResult(RESULT_CANCELED,output);
        finish();
    }
}
