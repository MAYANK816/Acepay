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

import javax.net.ssl.SSLContext;
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

public class RetrofitUPI {


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


    public RetrofitUPI(Context mContext, RetrofitResponse result,
                     JsonObject postParam, int requestCode, String url, String value)//for POST URL
    {

        this.mContext = mContext;
        this.result = result;
        this.jsonObject = postParam;
        this.requestCode = requestCode;
        this.url = url;
        this.value = value;
    }

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
            Log.e("OutsideMain Try",requestCode+"");

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

            Base = Constant.BASE_URL_upi;
            Log.e("Base ",Base);

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

            call = retrofitService.postRawJSON(url,jsonObject);




            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {

                    Log.e("InsideOn Response",requestCode+"");

                    try{
                        Log.e("VALUE>>",response.toString()+"value:- "+value);
                        if (response.isSuccessful())
                        {
                            Log.e("InsideRequestCode",requestCode+"");


                            result.onServiceResponse(requestCode, response);
                            x=1;
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
        }
        catch (Exception e)
        {
            Log.e("InsideOn outsideCatxh",requestCode+"");

            e.printStackTrace();
        }
    }


    public void showAlertOnTimeOut(String message, final Call<ResponseBody> call, final Callback<ResponseBody> callbackk) {

        Log.e("showAlertOnTimeOut upi",requestCode+"");
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
