package com.acepay;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;


import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by pro22 on 8/1/18.
 */

public class BackGroundService extends Service implements RetrofitResponse {
    Handler handler;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler=new Handler();
        timer = new Timer();    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

//        Log.e("onTaskRemoved","yes");

        PreferenceFile.getInstance().saveData(this, Constant.CheckApp,"1");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.CHECKZENDECK)==null){

//            ZendeskConfig.INSTANCE.init(this, "https://metapay.zendesk.com", "7b5b2197c0b75ab55e87b61e60b8fbf8e1ad2f48302c8bab", "mobile_sdk_client_3f693ea4959e1206d347");
//            // Identity identity = new AnonymousIdentity.Builder().build();
//
//            Identity identity;
//            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)!=null) {
//                identity = new AnonymousIdentity.Builder()
//                        .withNameIdentifier(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username))
//                        .withEmailIdentifier(PreferenceFile.getInstance().getPreferenceData(this,Constant.Email))
//                        .build();
//            }else
//            {
//                identity = new AnonymousIdentity.Builder().build();
//            }
//            ZendeskConfig.INSTANCE.setIdentity(identity);

            PreferenceFile.getInstance().saveData(this,Constant.CHECKZENDECK,"1");
        }


//        Log.e("onStartCommand-->","yes");
        TimerTask doAsynchronousTask = new TimerTask()
        {
            @Override
            public void run()
            {

                handler.post(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            JSONObject postParam = new JSONObject();

                            try {
                                postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(getApplication(),Constant.phone));

                                if (Constant.isConnectingToInternet(BackGroundService.this)) {

                                    try {
                                        new Retrofit2(BackGroundService.this, BackGroundService.this, postParam,
                                                Constant.REQ_CHECK_NO, Constant.CHECK_NO, "3").callService(false);
                                        new Retrofit2(BackGroundService.this, BackGroundService.this, postParam,
                                                Constant.REQ_BTC_RATE, Constant.BTC_RATE, "3").callService(false);
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Constant.alertDialog(BackGroundService.this, getResources().getString(R.string.check_connection));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 20000);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_CHECK_NO:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");
                    Log.e("message",message);
                    if (status.equals("true")) {

                        JSONObject data=result1.getJSONObject("data");
//                        Log.e("status-->",data.getString("status"));
                        PreferenceFile.getInstance().saveData(this,Constant.Accunt_status,data.getString("status"));

                        if(!data.getString("status").equalsIgnoreCase("Inactive")) {

                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY).equals("4")) {
//                                Log.e("value-->", "count4");
                                PreferenceFile.getInstance().saveData(this, Constant.COUNT_SECURITY, "1");
                            }
                        }

                    }else{
                        Log.e("message","ELSE"+message);
                        PreferenceFile.getInstance().logout();
                        Intent i=new Intent(BackGroundService.this,DeleteScreen.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;


            case Constant.REQ_BTC_RATE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

//                        Log.e("result-->",result.toString());

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data=result.getJSONObject("data");

                            double calcul = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));

                            BigDecimal d = new BigDecimal(calcul);
//                            Log.e("newcal-->","d -->"+ d);

                            PreferenceFile.getInstance().saveData(this,Constant.BUY,data.getString("buy"));
                            PreferenceFile.getInstance().saveData(this,Constant.SELL,data.getString("sell"));

//                            Log.e("current_ratebtc-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY));
//                            Log.e("current_ratesell-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.SELL));

                            Intent intent = new Intent("bit_rate");
                            intent.putExtra("buy",data.get("buy").toString());
                            intent.putExtra("sell", data.get("sell").toString());
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        }
                            // Constant.alertDialog(this, message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                else
                {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

        }

        }
}
