package com.acepay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;


/**
 * Created by pro22 on 21/12/17.
 */

public class NewPay extends AppCompatActivity
{

    TextView tool_text;
    private WebView terms;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_udetails);
      //  progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        terms=(WebView)findViewById(R.id.webView);

        terms.getSettings().setJavaScriptEnabled(true);

        terms.loadUrl("http://18.216.88.154/metaPay/homes/payu_mobile/"+ PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
        terms.getSettings().setBuiltInZoomControls(true);
        terms.getSettings().setSupportZoom(false);
        terms.clearCache(true);
        terms.setWebViewClient(new MyWebViewClient());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        terms.destroy();
        finish();
    }

    class MyWebViewClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
           // progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
          //  progressBar.setVisibility(View.VISIBLE);
            Log.e("url-->",url);
            view.loadUrl(url);
            if (url.contains("payment"))
            {
               // progressBar.setVisibility(View.VISIBLE);
            }else if (url.contains("review"))
            {
               // progressBar.setVisibility(View.VISIBLE);
            }else if (url.contains("invoice") || url.contains("responseForGateway"))
            {
                //progressBar.setVisibility(View.VISIBLE);

            }else if (url.contains("successResponse"))
            {
                CountDownTimer x =new CountDownTimer(6600,1000)
                {
                    public void onTick(long millisUntilFinished)
                    {
                    }
                    public void onFinish()
                    {
                        if(getIntent().hasExtra("make_pay"))
                        {
                            Intent intent=new Intent();
                            setResult(101,intent);
                            finish();
                        }else if (getIntent().hasExtra("new_pay"))
                        {
                            Intent intent=new Intent();
                            setResult(201,intent);
                            finish();
                        }else if (getIntent().hasExtra("history"))
                        {
                            Intent intent=new Intent();
                            setResult(301,intent);
                            finish();
                        }
                    }
                };
                x.start();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
           // progressBar.setVisibility(View.GONE);
        }
    }
}

