package com.acepay.PaymentSection;

import android.content.Context;
import android.content.Intent;
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


import com.acepay.Model.FeesChargeModet;
import com.acepay.R;
import com.acepay.ToolabarActivity;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class NormalFinal extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

    TextView tv_rupees,bank_name,tv_ac_holder_name,tv_acc_number,tv_ifsc_code,tvNext;
//    ImageView ivarrow;
    String picturePath;
    boolean doubleBackToExitPressedOnce = false;
    File file;
    private HashMap<String, RequestBody> map;
    String operator = "", macAddress = "",androidSDK, IPaddress,androidVersion, androidBrand, androidManufacturer, androidModel;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    private DecimalFormat formatter1;
    private NumberFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_final);
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
//        txName = (TextView) findViewById(R.id.txName);
        bank_name = (TextView) findViewById(R.id.bank_name);
        tv_ifsc_code = (TextView) findViewById(R.id.tv_ifsc_code);
        tv_ac_holder_name = (TextView) findViewById(R.id.tv_ac_holder_name);
        tv_acc_number = (TextView) findViewById(R.id.tv_acc_number);
        tvNext = (TextView) findViewById(R.id.tvNext);
        tv_rupees = (TextView) findViewById(R.id.tv_rupees);
        formatter1 = new DecimalFormat("#0.00");

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(NormalFinal.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Normal Transfer");
        androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        androidVersion = android.os.Build.VERSION.RELEASE;
        androidBrand = android.os.Build.BRAND;
        androidManufacturer = android.os.Build.MANUFACTURER;
        androidModel = android.os.Build.MODEL;
        Log.e("DEviceDetails", androidSDK + " " + androidVersion + " " + androidBrand + " " + androidManufacturer + " " + androidModel);

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)!=null){

            bank_name.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
            tv_ac_holder_name.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));

            String str= PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).
                    substring(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).length() - 4);
            Log.e("str-->",str);

            int count =PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).length();
            count= count-str.length();

            String nb="";
            for(int x=0;x<count;x++){
                nb=nb+"*";
                Log.e("nb-->",nb);
            }
            tv_acc_number.setText(nb+str);
            tv_ifsc_code.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));

        }
        else {
            Constant.alertDialog(this, "You are not a verified customer, verify your account to deposit more amount.");



        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.alertDialogTwoButtons(NormalFinal.this,"Are you sure you want to cancel this deposit?");
            }
        });

        tv_rupees.setText("Please make payment of " + getIntent().getStringExtra("amount")+" "+
                PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)+"  \n from your below bank account only");

        tvNext.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);

        NetwordDetect();
        callnewservice();

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
                finish();
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.tvNext:

                PreferenceFile.getInstance().saveData(NormalFinal.this, Constant.NORMAL_DEP_AMT,
                        getIntent().getStringExtra("amount"));

                callReqService();

                break;

        }
    }


    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

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

        if(WIFI == true)
        {
            IPaddress = GetDeviceipWiFiData();

            TelephonyManager tMgr =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            operator = tMgr.getNetworkOperatorName();

        }

        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
            macAddress=getMacAddr();
            TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            operator=tManager.getNetworkOperatorName();
        }
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip=Formatter.formatIpAddress(inetAddress.hashCode());
                        return  ip;
//                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }


    private void callReqService(){

        Double total_deposit=0.00,total=0.00;
        Double amount=Double.parseDouble(getIntent().getStringExtra("amount").trim());
        total_deposit=amount;

        try
        {
            Log.e("total-->",total+" deposit "+total_deposit+" "+ getIntent().getStringExtra("bank_id"));

//            {"user_id":"3","deposit":"1000","bank_id":"8","ip":"192.168.3.100","network":"Vodafone"}
            JSONObject map = new JSONObject();
            map.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
            map.put("deposit",String.valueOf(total_deposit));
            map.put("bank_id", getIntent().getStringExtra("bank_id"));
            map.put("fee", String.valueOf(total));
            map.put("original_amount", getIntent().getStringExtra("amount"));

           map.put("ip", IPaddress);
           map.put("network", operator);

           map.put("mac_address", macAddress);
           map.put("device_sdk", androidSDK);
           map.put("device_version", androidVersion);
           map.put("device_brand", androidBrand);
           map.put("device_manufacturer", androidManufacturer);
           map.put("device_model", androidModel);


            Log.e("map", " " + IPaddress + " " + operator + " " +
                    PreferenceFile.getInstance().getPreferenceData(this, Constant.ID) + " transaction_id " +
                    " transaction_date " + getIntent().getStringExtra("amount") + " " + getIntent().getStringExtra("bank_id"));

            Log.e("NormalFinal", map.toString());


            if (Constant.isConnectingToInternet(NormalFinal.this)) {
                Log.e("NormalFinal", "2");
                new Retrofit2(NormalFinal.this, NormalFinal.this, map,
                        Constant.REQ_Wallet_deposit, Constant.Wallet_deposit, "3").
                        callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(NormalFinal.this, getResources().getString(R.string.check_connection));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callnewservice(){

        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once");
            new Retrofit2(this, NormalFinal.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"3").callService(true);
            //   new Retrofit2(this, MyTickets.this, Constant.REQ_MYTICKET,PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_NET_FEES:
                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        Log.e("statusfgsta", status);

                        if (status.equals("true")) {
                            list.clear();
                            Log.e("status", status);
                            JSONArray mydata = result.getJSONArray("data");
                            for (int i = 0; i < mydata.length(); i++)
                            {
                                Log.e("status", status);
                                JSONObject myobj = mydata.getJSONObject(i);

                                FeesChargeModet model = new FeesChargeModet();
                                model.setFees(myobj.getString("net_fee"));
                                model.setFrom(myobj.getString("from_amount"));
                                model.setTo((myobj.getString("to_amount")));
                                model.setGst((myobj.getString("gst")));

                                list.add(model);
                                Log.e("njsc", "" + list.size());
                            }

                            Log.e("listsize-->",list.size()+"");

                        } else {
//                            Constant.alertDialog(this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

            case Constant.REQ_Wallet_deposit:
                try {

                    JSONObject result1 = new JSONObject(response.body().string());
                    PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    Log.e("NormalFinal",result1.toString());

                    if(status.equals("true")){

                        JSONObject data=result1.getJSONObject("data");

                        String depositId=data.getString("id");
                        PreferenceFile.getInstance().saveData(this,Constant.NORMAL_DEP_ID_RECVD,depositId);

//                        notifyUser();

                        Intent intent=new Intent(NormalFinal.this,FinalyDeposit.class);
                        intent.putExtra("depositId",depositId);
                        intent.putExtra("amount", String.valueOf(formatter1.format
                                (Double.valueOf(getIntent().getStringExtra("amount")))));
                        intent.putExtra("ifsc",getIntent().getStringExtra("ifsc"));
                        intent.putExtra("brach_name",getIntent().getStringExtra("brach_name"));
                        intent.putExtra("account_holder_name",getIntent().getStringExtra("account_holder_name"));
                        intent.putExtra("bank_name",getIntent().getStringExtra("bank_name"));
                        intent.putExtra("bank_id",getIntent().getStringExtra("bank_id"));
                        intent.putExtra("account_number",getIntent().getStringExtra("account_number"));
                        startActivity(intent);
                    }
                    else {
                        Constant.alertDialog(this,message);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        macAddress=  wm.getConnectionInfo().getMacAddress();
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }
}
