package com.acepay;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.acepay.Util.Constant;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class AboutUS extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

//    ImageView ivarrow;ivarrow
    TextView txName,txTxt;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);
        txTxt= (TextView) findViewById(R.id.txTxt);

//        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("About Us");
        callService();
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

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, AboutUS.this, Constant.REQ_ABOUT_US, Constant.ABOUT_US).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

//            case R.id.ivarrow:
//
//                finish();
//
//                break;

        }

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_ABOUT_US:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("false")){

                        JSONObject data=result1.getJSONObject("data");
                        txTxt.setText(data.getString("text"));

                    }else {

                        Constant.alertDialog(this,message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

        }


    }
}
