package com.acepay;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Model.FeesChargeModet;
import com.acepay.Util.Constant;
import com.acepay.Util.DecimalDigitsInputFilter;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Withdrow extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow;
    EditText edAmount,edDecription;
    TextView txcalculate,txNext,tvBitcoin,tvmaxmin,tvnote;

    private Double price=0.00;
    private Double charges=0.00,gst=0.00;
    String finacal;
    LinearLayout lnrefresh;
    Typeface tfArchitectsDaughter;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    private NumberFormat formatter;
    private DecimalFormat formatter1;
    String max,min;
    TextView txName,txBitcoin,txAvlBitcoin,currentsymbol,tvwallet,tvSellrate,tvINR;
    String operator = "", macAddress = "",androidSDK, IPaddress,androidVersion,
            androidBrand, androidManufacturer, androidModel;

//    ivarrow,


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrow);
        txName= (TextView) findViewById(R.id.txName);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        tvBitcoin= (TextView) findViewById(R.id.tvBitcoin);
        txNext = (TextView) findViewById(R.id.txNext);
        tvmaxmin= (TextView) findViewById(R.id.tvmaxmin);
        tvnote= (TextView) findViewById(R.id.tvnote);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        txcalculate = (TextView) findViewById(R.id.txcalculate);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
        edAmount = (EditText) findViewById(R.id.edAmount);
        edDecription = (EditText) findViewById(R.id.edDecription);
        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(Withdrow.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        edAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        formatter1 = new DecimalFormat("#0.00");
        tvINR = (TextView) findViewById(R.id.tvINR);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

//        ivarrow.setOnClickListener(this);
        txBitcoin.setOnClickListener(this);
        txNext.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Withdraw");

        androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        androidVersion = android.os.Build.VERSION.RELEASE;
        androidBrand = android.os.Build.BRAND;
        androidManufacturer = android.os.Build.MANUFACTURER;
        androidModel = android.os.Build.MODEL;
        Log.e("DEviceDetails", androidSDK + " " + androidVersion + " " + androidBrand + " " + androidManufacturer + " " + androidModel);

        NetwordDetect();
        callService();


        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());


        if (bit > 0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }


        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(inr>0)
        {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));

        /*txAvlBitcoin.setText(bit+"");

        //    txAvlBitcoin.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+" ");
        tvAvlRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+" ");*/

        /*if(!PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount).equals("")){

            txAvlBitcoin.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+" ");
        }
        else
        {
            txAvlBitcoin.setText("No Inr Available");
        }

        if(!PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount).equals("")){

            tvAvlRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+" ");
        }
        else
        {
            tvAvlRs.setText("No Bitcoin Available");
        }*/
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
            Log.e("ipwifi-->",IPaddress);

            TelephonyManager tMgr =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            operator = tMgr.getNetworkOperatorName();
            Log.e("operator-->",operator);

        }

        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
            Log.e("inMobile-->",IPaddress);

            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            operator=tManager.getNetworkOperatorName();
            Log.e("tManager-->",tManager.getNetworkOperatorName());

        }
    }

    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
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

    public String GetDeviceipWiFiData()
    {

        WifiManager wm = (WifiManager)getApplicationContext(). getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")

        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }

    private void callnewservice(){

        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once");
            new Retrofit2(this, Withdrow.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"4")
                    .callService(false);
            //   new Retrofit2(this, MyTickets.this, Constant.REQ_MYTICKET,PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            price=Double.parseDouble(buy);

            txBitcoin.setText(buy + " ");

            txBitcoin.setText(formatter.format(Double.valueOf(buy))+" "+
                    PreferenceFile.getInstance().getPreferenceData(Withdrow.this,Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell))+" "+
                    PreferenceFile.getInstance().getPreferenceData(Withdrow.this,Constant.Currency_Symbol));

            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Withdrow.this,Constant.BTC_amount))*Double.parseDouble(buy);

            if(buy_rate>0) {

                tvINR.setText(formatter.format(buy_rate) + " " +
                        PreferenceFile.getInstance().getPreferenceData(Withdrow.this, Constant.Currency_Symbol));
            }
            else
            {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(Withdrow.this, Constant.Currency_Symbol));
            }
        }
    };


    private void minmax() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Withdrow.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
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


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Withdrow.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Withdrow.this,
                    Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().
                    getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onClick(View v) {
        final Intent intent;

        switch (v.getId()) {

            case R.id.lnrefresh:

                newRefereshing();

                break;

            case R.id.txNext:

                Constant.hideKeyboard(this,v);

                if (!edAmount.getText().toString().equals("")) {

                    if(edAmount.getText().toString().equals(".")){

                        Constant.alertDialog(this,getString(R.string.please_select_valid_amount));
                    }

                   else if(Double.parseDouble(edAmount.getText().toString()) <= 0){

                        Constant.alertDialog(this,getString(R.string.please_select_valid_amount));
                    }

                   else if (!(Double.parseDouble(edAmount.getText().toString())
                            > Double.parseDouble(PreferenceFile.getInstance().
                            getPreferenceData(Withdrow.this, Constant.Inr_Amount)))) {

                        if (Double.parseDouble(edAmount.getText().toString()) > 0) {

                            if (Double.parseDouble(edAmount.getText().toString()) >=
                                    (Double.parseDouble(formatter1.format(Double.valueOf(min)))) &&
                                    Double.parseDouble(edAmount.getText().toString())
                                            < (Double.parseDouble(formatter1.format(Double.valueOf(max))))) {

                             TextView tvBackname, tvacName, tvacNo, tvIfsc, tvOk, tvcancel;
                            final Dialog dialog = new Dialog(this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            LayoutInflater li = LayoutInflater.from(this);
                            View promptsView = li.inflate(R.layout.withdraw, null);
                            dialog.setContentView(promptsView);
                            tvBackname = (TextView) promptsView.findViewById(R.id.tvBackname);
                            tvacName = (TextView) promptsView.findViewById(R.id.tvacName);
                            tvacNo = (TextView) promptsView.findViewById(R.id.tvacNo);
                            tvIfsc = (TextView) promptsView.findViewById(R.id.tvIfsc);

                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BANK_NAME) != null) {
                                tvBackname.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.BANK_NAME));
                                tvacName.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.ACCOUNT_HOLDER));
                              //  tvacNo.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.ACCOUNT_NUMBER));
                                tvIfsc.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.IFSC));

                                String str= PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).substring(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).length() - 4);
                                Log.e("str-->",str);

                                int count =PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER).length();
                                count= count - str.length();

                                String nb="";
                                for(int x=0;x<count;x++){
                                    nb=nb+"*";
                                    Log.e("nb-->",nb);
                                }
                                tvacNo.setText(nb+str);

                                tvOk = (TextView) promptsView.findViewById(R.id.tvOk);
                                tvcancel = (TextView) promptsView.findViewById(R.id.tvcancel);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.show();

                                tvOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        Double total_deposit=0.00,total=0.00;
                                        Double amount=Double.parseDouble(edAmount.getText().toString().trim());
                                        total_deposit=amount;

                                        for (int x=0;x<list.size();x++)
                                        {
                                            if(amount >= Double.parseDouble(list.get(x).getFrom().trim()) && amount <=
                                                    Double.parseDouble(list.get(x).getTo().trim()) ||
                                                    amount >= Double.parseDouble(list.get(list.size()-1).getFrom().trim()) ){

                                                charges=Double.parseDouble(list.get(x).getFees());
                                                gst=Double.parseDouble(list.get(x).getGst());
                                                total=((charges*gst)/100)+charges;
                                                gst=(charges*gst)/100;
                                                total_deposit=amount-total;

                                            }
                                        }


                                        Intent intent1 = new Intent(Withdrow.this, CheckSecurePin.class);
                                        intent1.putExtra("key", "withdraw");
                                        intent1.putExtra("inr_amount",String.valueOf(formatter1.format(total_deposit)));
                                        intent1.putExtra("fee",String.valueOf(formatter1.format(total)));
                                        intent1.putExtra("charges",String.valueOf(formatter1.format(charges)));
                                        intent1.putExtra("gst",String.valueOf(formatter1.format(gst)));
                                        intent1.putExtra("description", edDecription.getText().toString());
//                                        intent1.putExtra("original_amount",edAmount.getText().toString());
                                        intent1.putExtra("original_amount",String.valueOf(formatter1.format(Double.valueOf(edAmount.getText().toString()))));
                                        startActivity(intent1);
                                    }
                                });

                                tvcancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                            } else {

                                Constant.alertWithIntent(this, "Please firstly verify your Account", PanCardVerification.class);
                            }

                        }else {
                                Constant.alertDialog(Withdrow.this, "Please enter valid amount.");
                            }

                            } else {

                                Constant.alertDialog(Withdrow.this, getString(R.string.amount_validation));
                            }


                    } else {

                        Constant.alertDialog(Withdrow.this, getString(R.string.amount_validation));
                    }

                    break;
                }
                else {
                    Constant.alertDialog(Withdrow.this, getString(R.string.please_select_amount));
                }
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result1 = new JSONObject(response.body().string());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){

                            JSONObject result = result1.getJSONObject("data");


                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                            PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));

                            PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                            PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                            Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                            Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
                            BigDecimal ff = new BigDecimal(inr);

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(Withdrow.this, Constant.Currency_Symbol));

                            if(bit>0)
                            {
                                tvBitcoin.setText(String.format("%.8f", finacal));
                            }
                            else
                            {
                                tvBitcoin.setText("0.00000000");
                            }


                            if(!result.getString("first_name").equals("null")){

                                PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name")+" "+result.getString("last_name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                PreferenceFile.getInstance().saveData(this, Constant.email_verification, result.getString("verification"));
                                PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));

                                PreferenceFile.getInstance().saveData(this, Constant.City_name, result.getString("city"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_PAN, result.getString("verify_pan"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_BANK, result.getString("verify_bank"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_Adhaar, result.getString("verify_add"));

                                if(!result.isNull("block_address")){

                                    PreferenceFile.getInstance().saveData(this, Constant.BITCOIN_ADDRESS, result.getString("block_address"));
                                }

                                JSONObject StateName=result.getJSONObject("StateName");
                                PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));


                                JSONObject CountryName=result.getJSONObject("CountryName");
                                PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                if(!result.isNull("BankName")){

                                    JSONObject BankName=result.getJSONObject("BankName");
                                    PreferenceFile.getInstance().saveData(this,Constant.BANK_NAME,BankName.getString("bank_name"));
                                    PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_TYPE,BankName.getString("account_type"));
                                    PreferenceFile.getInstance().saveData(this,Constant.BRANCH,BankName.getString("branch"));
                                    PreferenceFile.getInstance().saveData(this,Constant.PASSBOOK_IMAGE,BankName.getString("passbook_image"));
                                    PreferenceFile.getInstance().saveData(this,Constant.IFSC,BankName.getString("ifsc"));
                                    PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_HOLDER,BankName.getString("account_holder"));
                                    PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_NUMBER,BankName.getString("account_number"));
                                }

                                if(!result.isNull("AddName")) {

                                    JSONObject AddName=result.getJSONObject("AddName");
                                    PreferenceFile.getInstance().saveData(this,Constant.Adhaar_image,AddName.getString("aadhar"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Adhaar_image_back,AddName.getString("aadhar_back"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Adhaar_number,AddName.getString("aadhar_number"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Adhaar_line1,AddName.getString("line1"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Adhaar_line2,AddName.getString("line2"));
                                    PreferenceFile.getInstance().saveData(this,Constant.LANDMARK,AddName.getString("landmark"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Aadhar_city,AddName.getString("city"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Aadhar_state,AddName.getString("state"));

                                    JSONObject state=AddName.getJSONObject("StateName");
                                    PreferenceFile.getInstance().saveData(this,Constant.Aadhar_state,state.getString("name"));

                                }
                                if(!result.isNull("PanName")) {

                                    JSONObject PanName=result.getJSONObject("PanName");
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_name,PanName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_last,PanName.getString("last_name"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_image,PanName.getString("image"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_number,PanName.getString("pan_number"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_dob,PanName.getString("dob"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_gender,PanName.getString("gender"));
                                }
                            }

                        }
                        else
                        {
                            Constant.alertWithIntent(this,"Account Blocked",BlockScreen.class);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {

                    }
                }
                break;


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
                            Constant.alertDialog(this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

            case Constant.REQ_BTC_RATE:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        minmax();

                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            price=data.getDouble("buy");
                            txBitcoin.setText(data.getString("buy") + " ");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy")))+" "

                                    +PreferenceFile.getInstance().getPreferenceData(this,
                                    Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell")))+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,
                                    Constant.Currency_Symbol));

                            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.BTC_amount))
                                    *Double.parseDouble(data.getString("buy"));

                            if(buy_rate>0) {
                                tvINR.setText(formatter.format(buy_rate) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this,
                                                Constant.Currency_Symbol));
                            }
                            else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                        } else {
                            Constant.alertDialog(this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;

            case Constant.REQ_MIN_MAX:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");
                    callnewservice();

                    Log.e("response-->",result1.toString());
                    Log.e("status-->",status+" message-->"+message);

                    if(status.equals("true")){

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);

                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BITCOIN_ADDRESS)!=null) {

                                if (CountryObj.getString("type").equals("2")) { //


                                    Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                    Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                    min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                    max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                    Log.e("REQ_MIN_MAX", "onServiceResponse:"+min+ " "+max);

                                    tvnote.setText(CountryObj.getString("note"));

                                    tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min))
                                            + " " + PreferenceFile.getInstance().
                                            getPreferenceData(this, Constant.Currency_Symbol) + " max: " +
                                            formatter.format(Double.valueOf(max))
                                            + " " + PreferenceFile.getInstance().
                                            getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }
                            else {
                                if (CountryObj.getString("type").equals("13")) {

                                    Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                    Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                    min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                    max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                    Log.e("REQ_MIN_MAX", "onServiceResponse:"+min+ " "+max);

                                    tvnote.setText(CountryObj.getString("note"));
                                    tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) +
                                            " max: " +
                                            formatter.format(Double.valueOf(max))  + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                }

                            }
                        }

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