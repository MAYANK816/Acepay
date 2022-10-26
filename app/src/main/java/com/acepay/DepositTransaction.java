package com.acepay;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.PaymentSection.FinalyDeposit;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.competition.BuyEntryPass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DepositTransaction extends ToolabarActivity implements RetrofitResponse, View.OnClickListener{

    TextView tvBitcoin,tvpaypalmin,tvpaypalmax,txExpress, txName,tvpayumax,tvpayumin,
            tvmax,tvmin,tvStatus,txBitcoin,tvSellrate,tvINR,tvwallet;
//    ImageView ivarrow;
    LinearLayout lnNoraml,lnpaypal,lPayu,lnExpress,linaer_pass,linear_upi;
    Typeface tfArchitectsDaughter;

    NumberFormat formatter;
    DecimalFormat  formatter1;
    Double buyrate;
    String min,max;
    LinearLayout lnrefresh;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_deposit_transaction);
        setContentView(R.layout.deposit_transaction);

        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Deposit");
//        currentsymbol = (TextView) findViewById(R.id.currentsymbol);
        tvSellrate= (TextView) findViewById(R.id.tvSellrate);
//        txExpress= (TextView) findViewById(R.id.txExpress);
        tvpaypalmin= (TextView) findViewById(R.id.tvpaypalmin);
        tvpaypalmax= (TextView) findViewById(R.id.tvpaypalmax);
        tvpayumax= (TextView) findViewById(R.id.tvpayumax);
        tvpayumin= (TextView) findViewById(R.id.tvpayumin);
        tvmax= (TextView) findViewById(R.id.tvmax);
        tvmin= (TextView) findViewById(R.id.tvmin);
        tvwallet= (TextView) findViewById(R.id.tvwallet);


//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        lPayu= (LinearLayout) findViewById(R.id.lPayu);
        linear_upi= (LinearLayout) findViewById(R.id.linear_upi);
//        lnExpress= (LinearLayout) findViewById(R.id.lnExpress);
        lnNoraml= (LinearLayout) findViewById(R.id.lnNoraml);
        linaer_pass= (LinearLayout) findViewById(R.id.linaer_pass);
        lnpaypal= (LinearLayout) findViewById(R.id.lnpaypal);
        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);

        tvStatus= (TextView) findViewById(R.id.tvStatus);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        tvINR = (TextView) findViewById(R.id.tvINR);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(DepositTransaction.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        formatter1 = new DecimalFormat("#0.00");

//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " ");

        callService();
        callAdminControlCheck();//todo to  check the deposit admin visibility



        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());


        if (bit > 0) {
            tvBitcoin.setText(String.format("%.8f", finacal));
        } else {
            tvBitcoin.setText("0.00000000");
        }

        lnNoraml.setOnClickListener(this);
        linaer_pass.setOnClickListener(this);
        lnpaypal.setOnClickListener(this);
//        lnExpress.setOnClickListener(this);
        lnrefresh.setOnClickListener(this);

//        ivarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });

        lPayu.setOnClickListener(this);
        linear_upi.setOnClickListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice1, new IntentFilter("refresh_wallet_balance"));
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this,Constant.NORMAL_DEP_AMT)!=null){
            tvStatus.setVisibility(View.VISIBLE);
        }else{
            tvStatus.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            buyrate = Double.valueOf(buy);

            txBitcoin.setText(formatter.format(Double.valueOf(buy)) + " " + PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell)) + " " + PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.Currency_Symbol));

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            if (buy_rate > 0) {

                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.Currency_Symbol));
            }
            else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.Currency_Symbol));
            }
        }
    };


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositTransaction.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callAdminControlCheck() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositTransaction.this, Constant.REQ_ADMIN_CONTROL, Constant.ADMIN_CONTROL).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callServicemenman() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositTransaction.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_MIN_MAX:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");


                    Log.e("response-->",result1.toString());
                    Log.e("status-->",status+" message-->"+message);

                    if(status.equals("true")){

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);

                            if(CountryObj.getString("type").equals("1")){  //later on 1 or 8

                                Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                Double  max1= Double.parseDouble(CountryObj.getString("max"));


                                min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                max= String.valueOf(formatter1.format(Double.valueOf(max1)));

                                tvmin.setText("Deposit limit Min:"+formatter.format(Double.valueOf(min))+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                /*tvmin.setText("Deposit Limit Min: "+formatter.format(Double.valueOf(min))+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+
                                        "Max: "+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));*/

                                tvmax.setText("Deposit limit Max: "+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            if(CountryObj.getString("type").equals("13")){

                                Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                Double  max1= Double.parseDouble(CountryObj.getString("max"));


                                min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                max= String.valueOf(formatter1.format(Double.valueOf(max1)));
                               /* tvmin.setText("Deposit Limit Min: "+formatter.format(Double.valueOf(min))+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+
                                        "Max: "+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));*/
                               tvmin.setText("Deposit limit Min:"+formatter.format(Double.valueOf(min))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvmax.setText("Deposit limit Max:"+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                                txExpress.setText("min: "+CountryObj.getString("min")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" max: "+CountryObj.getString("max")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvpaypalmin.setText("Min: "+formatter.format(Double.valueOf(min))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvpaypalmax.setText("Max: "+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvpayumin.setText("Deposit limit Min: "+formatter.format(Double.valueOf(min))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                               /* tvpayumin.setText("Deposit limit MIn: "+formatter.format(Double.valueOf(min))+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)
                                        +"Max: "+formatter.format(Double.valueOf(max))+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));*/

                                tvpayumax.setText("Deposit limit Max: "+formatter.format(Double.valueOf(max))+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));


//                                tvpaypal.setText("min: "+CountryObj.getString("min")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" max: "+CountryObj.getString("max")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                                tvpayu.setText("min: "+CountryObj.getString("min")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" max: "+CountryObj.getString("max")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }

                            if(CountryObj.getString("type").equals("11")){

                                // tvmaxmin.setText("min: "+CountryObj.getString("min")+" max: "+CountryObj.getString("max"));
//                                txExpress.setText("min: "+CountryObj.getString("min")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" max: "+CountryObj.getString("max")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            if(CountryObj.getString("type").equals("10")){

                                Log.e("inside-->","paypal"+" "+CountryObj.getString("min")+" "+CountryObj.getString("max"));
                                Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                Double  max1= Double.parseDouble(CountryObj.getString("max"));


                                min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                max= String.valueOf(formatter1.format(Double.valueOf(max1)));
                                tvpaypalmin.setText("Min: "+formatter.format(Double.valueOf(min))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvpaypalmax.setText("Max: "+formatter.format(Double.valueOf(max))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            if(CountryObj.getString("type").equals("9")){

                                Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                min= String.valueOf(formatter1.format(Double.valueOf(min1)));
                                max= String.valueOf(formatter1.format(Double.valueOf(max1)));


                         /*       tvpayumin.setText("Deposit limit MIn: "+formatter.format(Double.valueOf(min))+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)
                                        +"Max: "+formatter.format(Double.valueOf(max))+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));*/

                                tvpayumin.setText("Deposit limit Min: "+formatter.format(Double.valueOf(min))+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvpayumax.setText("Deposit limit Max: "+formatter.format(Double.valueOf(max))+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
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

            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")) {

                            JSONObject result = result1.getJSONObject("data");


                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                            PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));

                            PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                            PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                            Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                            Double inr = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));
                            BigDecimal ff = new BigDecimal(inr);

                            tvwallet.setText(formatter.format(ff) + " " +
                                    PreferenceFile.getInstance().getPreferenceData
                                            (DepositTransaction.this, Constant.Currency_Symbol));

                            if (bit > 0) {
                                tvBitcoin.setText(String.format("%.8f", finacal));
                            } else {
                                tvBitcoin.setText("0.00000000");
                            }


                            if (!result.getString("first_name").equals("null")) {

                                PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name") + " " + result.getString("last_name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                PreferenceFile.getInstance().saveData(this, Constant.email_verification, result.getString("verification"));
                                PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));

                                PreferenceFile.getInstance().saveData(this, Constant.City_name, result.getString("city"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_PAN, result.getString("verify_pan"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_BANK, result.getString("verify_bank"));
                                PreferenceFile.getInstance().saveData(this, Constant.VERIFY_Adhaar, result.getString("verify_add"));

                                if (!result.isNull("block_address")) {

                                    PreferenceFile.getInstance().saveData(this, Constant.BITCOIN_ADDRESS, result.getString("block_address"));
                                }

                                JSONObject StateName = result.getJSONObject("StateName");
                                PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));

                                JSONObject CountryName = result.getJSONObject("CountryName");
                                PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                if (!result.isNull("BankName")) {

                                    JSONObject BankName = result.getJSONObject("BankName");
                                    PreferenceFile.getInstance().saveData(this, Constant.BANK_NAME, BankName.getString("bank_name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_TYPE, BankName.getString("account_type"));
                                    PreferenceFile.getInstance().saveData(this, Constant.BRANCH, BankName.getString("branch"));
                                    PreferenceFile.getInstance().saveData(this, Constant.PASSBOOK_IMAGE, BankName.getString("passbook_image"));
                                    PreferenceFile.getInstance().saveData(this, Constant.IFSC, BankName.getString("ifsc"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_HOLDER, BankName.getString("account_holder"));
                                    PreferenceFile.getInstance().saveData(this, Constant.ACCOUNT_NUMBER, BankName.getString("account_number"));
                                }

                                if (!result.isNull("AddName")) {

                                    JSONObject AddName = result.getJSONObject("AddName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_image, AddName.getString("aadhar"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_image_back, AddName.getString("aadhar_back"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_number, AddName.getString("aadhar_number"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_line1, AddName.getString("line1"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Adhaar_line2, AddName.getString("line2"));
                                    PreferenceFile.getInstance().saveData(this, Constant.LANDMARK, AddName.getString("landmark"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_city, AddName.getString("city"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_state, AddName.getString("state"));

                                    JSONObject state = AddName.getJSONObject("StateName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Aadhar_state, state.getString("name"));

                                }
                                if (!result.isNull("PanName")) {

                                    JSONObject PanName = result.getJSONObject("PanName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_name, PanName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_last, PanName.getString("last_name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_image, PanName.getString("image"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_number, PanName.getString("pan_number"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_dob, PanName.getString("dob"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Pan_gender, PanName.getString("gender"));
                                }
                            }
                        } else {
                            Constant.alertWithIntent(this, "Account Blocked", BlockScreen.class);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                break;

            case Constant.REQ_BTC_RATE:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        callServicemenman();
                        String status = result.getString("response");
                        String message = result.getString("message");
                        callServicemenman();

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            buyrate = Double.valueOf(data.getString("buy"));

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            if (buy_rate > 0) {
                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }


                        } else {

                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY) != null) {

                                buyrate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY));

                                txBitcoin.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.BUY))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                tvSellrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.SELL));

                                if (buy_rate > 0) {
                                    tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;


            case Constant.REQ_ADMIN_CONTROL:

                if (response.isSuccessful())
                {
                    try{
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("AdminControlResult ",result.toString());

                        if (result.getString("response").equalsIgnoreCase("true"))
                        {
                            JSONObject deposit=result.getJSONObject("deposit");

                            if (deposit.getString("control").equalsIgnoreCase("1"))
                            {
                                normalDeposit="1";//to show
                            }
                            else {
                                normalDeposit="2";//to hide
                            }

                            normalReason=deposit.getString("reason");
                            Log.e("NormalDeposit ",normalDeposit);
                            Log.e("normalReason ",normalReason);

                            JSONObject payment=result.getJSONObject("payment");

                            if (payment.getString("control").equalsIgnoreCase("1"))
                            {
                                paymentDeposit="1";//to show
                            }
                            else {
                                paymentDeposit="2";//to hide
                            }

                            paymentReason=payment.getString("reason");
                            Log.e("paymentDeposit ",paymentDeposit);
                            Log.e("paymentReason ",paymentReason);

                            JSONObject upi=result.getJSONObject("upi");

                            if (upi.getString("control").equalsIgnoreCase("1"))
                            {
                                upiDeposit="1";//to show
                            }
                            else {
                                upiDeposit="2";//to hide
                            }

                            upiReason=upi.getString("reason");
                            Log.e("upiDeposit ",upiDeposit);
                            Log.e("upiReason ",upiReason);


                        }

                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                break;
        }
    }

    public void showAdminAction(String reason)
    {

        final Dialog dialog=new Dialog(DepositTransaction.this, R.style.StatisticsDialog);

        dialog.setTitle("TigerPay");
        dialog.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(DepositTransaction.this);
        View promptsView2 = li.inflate(R.layout.deposit_transaction_admin_popup, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button btnok=(Button)promptsView2.findViewById(R.id.btnokk);
        TextView tvReason=(TextView) promptsView2.findViewById(R.id.tvReason);

        tvReason.setText(reason);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    String normalDeposit="0";
    String normalReason="";
    String paymentDeposit="0";
    String paymentReason="";

    String upiDeposit="0";
    String upiReason="";

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, DepositTransaction.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
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

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

            case R.id.lnrefresh:

                callService();

                break;

//            case R.id.lnExpress:
//
//               Constant.alertDialog(this,"Coming Soon");
//
//                break;

            case R.id.lnNoraml:

                Log.e("Deposit ",normalDeposit);
                Log.e("reason ",normalReason);
                if (normalDeposit.equalsIgnoreCase("1"))
                {
                    try {

                        if (PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_AMT) != null) {

                            if (PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_AMT) != null
                                    && !PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_IFSC)
                                    .equals("2")) {

                                intent = new Intent(DepositTransaction.this, FinalyDeposit.class);
                                intent.putExtra("amount", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_AMT));
                                intent.putExtra("ifsc", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_IFSC));
                                intent.putExtra("brach_name", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_BRANCH_NAME));
                                intent.putExtra("account_holder_name", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_ACC_HOLDER_NAME));
                                intent.putExtra("bank_name", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_BANK_NAME));
                                intent.putExtra("bank_id", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_BANK_ID));
                                intent.putExtra("account_number", PreferenceFile.getInstance().getPreferenceData(DepositTransaction.this, Constant.NORMAL_DEP_ACC_NO));
                                startActivity(intent);
                            } else {
                                Constant.alertDialog(DepositTransaction.this, "Please Wait for previous transaction approval.");
                            }
                        } else {
                            intent = new Intent(DepositTransaction.this, DepositMoney.class);
                            intent.putExtra("key", "normal");
                            startActivity(intent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    showAdminAction(normalReason);

                }


                break;
            
            case R.id.lnpaypal:

                intent=new Intent(DepositTransaction.this,DepositMoney.class);
                intent.putExtra("key","paypal");
                startActivity(intent);

                break;

            case R.id.lPayu:

                Log.e("Deposit ",paymentDeposit);
                Log.e("reason ",paymentReason);
                if (paymentDeposit.equalsIgnoreCase("1"))
                {
                    Toast.makeText(this, "It will redirect to payment gateway soon", Toast.LENGTH_SHORT).show();
                }
                else {
                    showAdminAction(paymentReason);
                }



                break;

                case R.id.linear_upi:

                Log.e("Deposit ",paymentDeposit);
                Log.e("reason ",paymentReason);

                Toast.makeText(this, "It will redirect to UPI Id deposit soon", Toast.LENGTH_SHORT).show();

                break;

                case R.id.linaer_pass:

                Log.e("Deposit ",upiDeposit);
                Log.e("reason ",upiReason);
                if (upiDeposit.equalsIgnoreCase("1"))
                {
                    Intent intent1=new Intent(DepositTransaction.this, BuyEntryPass.class);
                    startActivity(intent1);

//                    Toast.makeText(this, "It will redirect to UPI Page soon", Toast.LENGTH_SHORT).show();
                }
                else {
                    showAdminAction(upiReason);
                }



                break;
        }


    }


    private BroadcastReceiver onNotice1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("DASHBOARD","onNotice1");
            tvStatus.setVisibility(View.GONE);
            newRefereshing();
        }
    };
}
