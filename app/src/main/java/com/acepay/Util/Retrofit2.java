package com.acepay.Util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pro22 on 3/5/17.
 */

public class Retrofit2 {

    private ProgressDialog pd;
    private String url;
    private int requestCode;
    private RetrofitResponse result;
    private JSONObject postParam;
    private JsonObject jsonObject;
    MultipartBody.Part part, part2, part3;
    String value;
    public static int x = 0;
    HashMap<String, RequestBody> map;
    private Call<ResponseBody> call;
    private Context mContext;
    int ifsc=0;
    String ApiCall="0";

    public Retrofit2(Context mContext, RetrofitResponse result, int requestCode, String url)//for get request
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
    }

    public Retrofit2(Context mContext, RetrofitResponse result, int requestCode, String url,int ifsc)//for get request
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
        this.ifsc = ifsc;

    }

    //value 2
    public Retrofit2(Context mContext, RetrofitResponse result, HashMap<String, RequestBody> map, MultipartBody.Part part, int requestCode, String url, String value)//for POST URL
    {
        Log.e("value", value + "");
        this.mContext = mContext;
        this.result = result;
        this.map = map;
        this.part = part;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;

    }

    //value 7
    public Retrofit2(Context mContext, RetrofitResponse result,
                     JsonObject postParam, int requestCode, String url, String value)//for POST URL
    {

        this.mContext = mContext;
        this.result = result;
        this.jsonObject = postParam;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;
    }

    //value 3
    public Retrofit2(Context mContext, RetrofitResponse result,
                     JSONObject postParam, int requestCode, String url, String value)//for POST URL
    {

        this.mContext = mContext;
        this.result = result;
        this.postParam = postParam;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;
    }

    public Retrofit2(Context mContext, RetrofitResponse result, JSONObject postParam, int requestCode, String url, String value,String apicall)//for POST URL
    {

        this.mContext = mContext;
        this.result = result;
        this.postParam = postParam;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;
        this.ApiCall=apicall;
    }

    //value 4
    // with map simple post
    public Retrofit2(Context mContext, RetrofitResponse result, HashMap<String, RequestBody> map, int requestCode, String url, String value)//for POST URL
    {
        Log.e("value", value + "");
        this.mContext = mContext;
        this.result = result;
        this.map = map;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;

    }

    //value 5
    public Retrofit2(Context mContext, RetrofitResponse result, HashMap<String, RequestBody> map,
                     MultipartBody.Part part, MultipartBody.Part part2, int requestCode, String url, String value)//for POST URL
    {
        Log.e("value", value + "");
        this.mContext = mContext;
        this.result = result;
        this.map = map;
        this.part = part;
        this.part2 = part2;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;
    }


//    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build();

            /*OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });*/
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void callService(boolean dialog) {
        try
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            {
                pd = new ProgressDialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog));
            }
            else {
                pd = new ProgressDialog(mContext);
            }

            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);


            if (dialog)
            {
                pd.show();
            }

            String Base;

            if(ifsc==1){
                Base  = "https://ifsc.razorpay.com/";
            }
            else if (value=="7") {
//                Base = Constant.BASE_URL;

                Base = Constant.BASE_URL_upi;


                Log.e("Base ",Base);
            }
            else {
                Base=Constant.BASE_URL;
            }

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()//Use For Time Out
                    .readTimeout(4, TimeUnit.MINUTES)
                    .connectTimeout(4, TimeUnit.MINUTES)
                    .build();

            System.setProperty("http.keepAlive", "false");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Base)
                    .client(okHttpClient)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RetrofitService retrofitService = retrofit.create(RetrofitService.class);

            Log.e("url ", url);

            if (url.contains(Constant.BEGINNER_CONTENT) || url.contains(Constant.NET_FEES) ||
                    url.contains(Constant.PAYPAY_FEE) || url.contains(Constant.TRANSACTION_LOG)||
                    url.contains(Constant.Dashboard_Refresh)||url.contains(Constant.ADVERTISEMAENT)||
                    url.contains(Constant.BTCCHARGE)||url.contains(Constant.Latest_btc_transaction)||
                    url.contains(Constant.Latest_INR_Transaction) ||url.contains(Constant.Ask_list) ||
                    url.contains(Constant.SINK_INTRUCTION) ||url.contains(Constant.USER_NOTIFICATION) ||
                    url.contains(Constant.Bitcoin_Transaction)||url.contains(Constant.INR_Transactions) ||
                    url.contains(Constant.MIN_MAX) ||url.contains(Constant.Terms_conditiona) ||
                    url.contains(Constant.AdminBank) ||url.contains(Constant.Statements) ||
                    url.contains(Constant.AddedAddresslist) ||url.contains(Constant.bid_list) ||url.contains(Constant.BTC_ADDRESS)
                    ||url.contains(Constant.AllRecord) ||url.contains(Constant.City) ||url.contains(Constant.NEW_COURTRY_CODE)
                    ||url.equals(Constant.BTC_RATE) ||url.equals(Constant.Country) || url.contains(Constant.State)||
                    url.contains(Constant.ABOUT_US) || url.contains(Constant.CHECK_DEPOSIT_STATUS)||url.contains(Constant.ADMIN_CONTROL)||
                    url.contains(Constant.COMPETITION_PLANS)||url.contains(Constant.UPI_DETAILS)||url.contains(Constant.CHECK_PREMIUM_USERS)
                    ||url.contains(Constant.GIFTS_REWARDS)||url.contains(Constant.ACTIVITIES)||url.contains(Constant.TOP_10_LIST)
                    ||url.contains(Constant.WINNERS_ANNOUNCEMENT)
                    || url.contains("https://ifsc.razorpay.com/")){
                Log.e("get service ", "service ");
                call = retrofitService.callGetService(url);
            }
            // for single image
            else if (value.equals("2")) {
                call = retrofitService.callMultipartService(url, map, part);
            }
            else if (value.equals("4")) {
                call = retrofitService.callPostWithMap(url, map);
            }
            // for no image
            else if (value.equals("3")) {
                Log.e("simple-->", "yes");
                call = retrofitService.callPostService(url, RequestBody.create(MediaType.parse("application/json"),
                        postParam.toString()));
            }
            else if (value.equals("7")) {
                Log.e("simple-->", "yes");
                call = retrofitService.postRawJSON(url,jsonObject);
            }

            else if (value.equals("5")) {
                Log.e("map-->", "yes");
                call = retrofitService.callPostTwoImage(url, map, part, part2);
            }


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {

                    try{
                        Log.e("VALUE>>",response.toString()+"value:- "+value);
                    if (response.isSuccessful())
                    {
                        Log.e("InsideRequestCode",requestCode+"");
                            result.onServiceResponse(requestCode, response);
                            x=1;
                        pd.cancel();
                    }
                    else
                    {
                        if(ifsc==1)
                        {
                            x=2;
                            Constant.alertDialog(mContext, "Incorrect IFSC code");

                        }else
                            {
                            Constant.alertDialog(mContext, "Error");
                        }
                    }
                        if (pd.isShowing()) {
                            pd.cancel();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        if (pd.isShowing()) {
                            pd.cancel();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (pd.isShowing()) {
                        pd.cancel();
                    }

                    call.cancel();
                    t.printStackTrace();
                    showAlertOnTimeOut("Connection Time Out", call, this);
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void showAlertOnTimeOut(String message, final Call<ResponseBody> call, final Callback<ResponseBody> callbackk) {


        try {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new androidx.appcompat.view.ContextThemeWrapper(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog));
            alertDialogBuilder.setMessage(message);

            alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
//                if (!url.contains(Constant.MAKE_PAYMENT)) {
                    pd.show();
//                }
                    call.clone().enqueue(callbackk);
                }
            });


            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        }catch (Exception e){

        }
    }


}