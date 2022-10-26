package com.acepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class PaypalPayment extends ToolabarActivity implements RetrofitResponse {
    TextView txName,tvTotal,tvNetwork;
    int paidamount, charges;
    int networkfees;
    Double totalAmt=0.0;

    public static final String PAYPAL_CLIENT_ID = "ARt_Ra-dLv6qAIswk_cM1ek-Oofo9hQjbdvuDTNNvEqQH0FrG6npCaIILUnfBJstw5AQG-n9DMci08k4";//testing
//    public static final String PAYPAL_CLIENT_ID = "AQd5nMDEiIoUR39Viun2ONSJZofhWM0L-domlzi69uoGf9-dCOIVke5X4spgjTNyftm962gOtzDr9Bc0";//live

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .acceptCreditCards(false)
            .clientId(PAYPAL_CLIENT_ID);

    boolean doubleBackToExitPressedOnce = false;
    private TextView proceedButton,tv;
    String amount,paymentId;
    Double total;
    String operator = "", macAddress = "",androidSDK, IPaddress,androidVersion, androidBrand, androidManufacturer, androidModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_payment);

//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        tvTotal= (TextView) findViewById(R.id.tvTotal);
        tvNetwork= (TextView) findViewById(R.id.tvNetwork);
        androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);

        androidVersion = android.os.Build.VERSION.RELEASE;
        androidBrand = android.os.Build.BRAND;
        androidManufacturer = android.os.Build.MANUFACTURER;
        androidModel = android.os.Build.MODEL;
        Log.e("DEviceDetails", androidSDK + " " + androidVersion + " "
                + androidBrand + " " + androidManufacturer + " " + androidModel);

            NetwordDetect();
        saveDataFeaturedJobPayments();

//        ivarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });

        callService();
    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, PaypalPayment.this, Constant.REQ_PAYPAY_FEE, Constant.PAYPAY_FEE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void saveDataFeaturedJobPayments() {

    }

    private void initviews() {

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        proceedButton = (TextView) findViewById(R.id.proceedButton);
        txName = (TextView) findViewById(R.id.txName);
        txName.setVisibility(View.VISIBLE);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayment();
            }
        });

        txName.setVisibility(View.VISIBLE);
        txName.setText("PayPal Payment");

        amount = getIntent().getStringExtra("amount");
        Double newamount = Double.parseDouble(amount);

        Double networkfees1 = (newamount / 100) * totalAmt;
        networkfees = networkfees1.intValue();

        Double charges1 = (newamount / 100) * totalAmt;
        charges =charges1.intValue();



        tvNetwork.setText("Gateway Charges : " + networkfees + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

        Double paidamount1 = networkfees1 + newamount;
        paidamount = paidamount1.intValue();


        tvTotal.setText("Total Amount : " + paidamount + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

        Log.e("newclass-->",paidamount+" networ-->"+networkfees+" chnages "+charges);
        // paymentTitle = "titileeeeee";
        Log.e("amount", amount + "..." + amount + "..." );
    }

    private void getPayment() {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(paidamount), "USD", "Deposit Money",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);
                        //  Toast.makeText(this, "Payment successful !", Toast.LENGTH_LONG).show();
                        JSONObject object = new JSONObject(paymentDetails);

                        Log.e("object-->",object.toString());

                        String status="";

                        if(object.optJSONObject("response").optString("state").equalsIgnoreCase("created"))
                        {
                            status="approved";
                        }
                        else {
                            status=object.optJSONObject("response").optString("state");
                        }

                        paymentId=object.optJSONObject("response").optString("id");
                        saveResponseFeaturedJobPayments(object.optJSONObject("response").optString("id"),status);
                        Log.e("paymntId", paymentId);
                        //startActivity(new Intent(this, JobList.class));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void saveResponseFeaturedJobPayments(String transactionId,String state) {
     /*   plan_id,job_id,featured_id,paid_amount,txn_id  */

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("transaction_id", transactionId);
            postParam.put("amount", amount);
            postParam.put("charges", String.valueOf(charges));
            postParam.put("status", state);
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this,Constant.ID));

            postParam.put("ip", IPaddress);
            postParam.put("network", operator);

            postParam.put("mac_address", macAddress);
            postParam.put("device_sdk", androidSDK);
            postParam.put("device_version", androidVersion);
            postParam.put("device_brand", androidBrand);
            postParam.put("device_manufacturer", androidManufacturer);
            postParam.put("device_model", androidModel);


            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(PaypalPayment.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(PaypalPayment.this, PaypalPayment.this, postParam, Constant.REQ_Paypay, Constant.Paypay, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(PaypalPayment.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 1000);
    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_PAYPAY_FEE:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        JSONObject data=result.getJSONObject("data");
                        Double d= Double.parseDouble(data.getString("total").trim());
                        Double tot = Double.parseDouble(data.getString("total").trim());
                        total= tot;

                        initviews();

                    }
                    else {

                        Constant.alertDialog(this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constant.REQ_Paypay:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    PreferenceFile.getInstance().saveData(this, Constant.COUNT_SECURITY, "1");
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        Constant.alertWithIntent(this, message,Dashboard.class);
                    } else {

                        Constant.alertDialog(this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }


    //Check the internet connection.
    private void NetwordDetect() {

        boolean WIFI = false;
        boolean MOBILE = false;

        Log.e("network", "yes");

        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    MOBILE = true;
        }


        if (WIFI == true) {
            IPaddress = GetDeviceipWiFiData();
            Log.e("ipwifi-->", IPaddress);

            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            operator = tMgr.getNetworkOperatorName();
            // operator = IPaddress;
            Log.e("operator-->", operator);

        }

        if (MOBILE == true) {
            IPaddress = GetDeviceipMobileData();
            Log.e("inMobile-->", IPaddress);

            TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

            operator = tManager.getNetworkOperatorName();
            Log.e("tManager-->", tManager.getNetworkOperatorName());
        }
    }

    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        macAddress=  wm.getConnectionInfo().getMacAddress();
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

}