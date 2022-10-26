package com.acepay;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NotificationDetails extends AppCompatActivity implements RetrofitResponse {
    TextView txName, txNext,tvdate,tvTitle,tvdescription;
    ImageView ivarrow,imtype;
    boolean doubleBackToExitPressedOnce = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        imtype = (ImageView) findViewById(R.id.imtype);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvdescription = (TextView) findViewById(R.id.tvdescription);
        txName = (TextView) findViewById(R.id.txName);
        tvdate = (TextView) findViewById(R.id.tvdate);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Notification Details");

        callservice();

        tvdate.setText(getIntent().getStringExtra("date"));
        tvdescription.setText(getIntent().getStringExtra("message"));
        tvTitle.setText(getIntent().getStringExtra("title"));

        if(getIntent().getStringExtra("type").equals("Alert"))
        {
            imtype.setBackground(getResources().getDrawable(R.drawable.alert));
            imtype.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

        }

        if(getIntent().getStringExtra("type").equals("Update")){

            imtype.setBackground(getResources().getDrawable(R.drawable.message));
            imtype.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

        }

        if(getIntent().getStringExtra("type").equals("Notification")){

            imtype.setBackground(getResources().getDrawable(R.drawable.bell));
            imtype.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

        }

        ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

                Intent intent=new Intent(NotificationDetails.this,NotificationPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            Log.e("Biding","once");
        }

        this.doubleBackToExitPressedOnce = true;


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Intent intent=new Intent(NotificationDetails.this,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    public void callservice(){

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
            postParam.put("notification_id", getIntent().getStringExtra("id"));

            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(NotificationDetails.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(NotificationDetails.this, NotificationDetails.this, postParam, Constant.REQ_NOTIFICATION_UPDATE, Constant.NOTIFICATION_UPDATE, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(NotificationDetails.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_NOTIFICATION_UPDATE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

        }
    }
}
