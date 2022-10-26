package com.acepay.DownloadPackage;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.R;

public class ShowWebview extends AppCompatActivity {
    private WebView webView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webview);
        webView = (WebView) findViewById(R.id.webView);
        /*webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setBackgroundColor(getResources().getColor(R.color.light_gray));
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        Log.e("url-->",getIntent().getStringExtra("id"));

        if (getIntent() != null)
        {
            Log.e("url-->","inside");
            webView.loadUrl(getIntent().getStringExtra("id"));
        }*/

        String myPdfUrl = getIntent().getStringExtra("id");
        String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        Log.e("pdf", "Opening PDF: " + url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    }

    @Override
    public void onBackPressed() {

        /*if (!doubleBackToExitPressedOnce) {
            finish();
            Log.e("Biding","once");
        }*/

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
//            super.onBackPressed();
            Log.e("Biding","once");
//            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 1000);
    }

}
