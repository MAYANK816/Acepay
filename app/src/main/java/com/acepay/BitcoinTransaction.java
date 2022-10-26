package com.acepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.BitcoinTransactionAdapter;
import com.acepay.Adapter.INRAdapter;
import com.acepay.Model.Transaction_model;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class BitcoinTransaction  extends ToolabarActivity implements RetrofitResponse,View.OnClickListener {
    TextView tvBitcoin,tvwallet,tvINR,txName,tvInr,tvBtc;
    Typeface tfArchitectsDaughter;
    Double finacals,bit;
    androidx.appcompat.widget.Toolbar toolbar;
    LinearLayout lnrefresh;
    int flag=0;
    boolean doubleBackToExitPressedOnce = false;
    String start="",end="";
    ArrayList<Transaction_model> transaction_models;
    BitcoinTransactionAdapter transactionAdapter;
    INRAdapter inrAdapter;
    RecyclerView recyclerView;
    private NumberFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_transaction);
        transaction_models=new ArrayList<>();
//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        tvInr = (TextView) findViewById(R.id.tvInr);
        tvBtc = (TextView) findViewById(R.id.tvBtc);
        txName = (TextView) findViewById(R.id.txName);
//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        tvwallet= (TextView) findViewById(R.id.tvwallet);
        tvINR = (TextView) findViewById(R.id.tvINR);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

        toolbar= (Toolbar) findViewById(R.id.toolbar);

        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);


        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(BitcoinTransaction.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        txName.setVisibility(View.VISIBLE);

        if(getIntent().getStringExtra("key").equals("btc")){
            txName.setText("Acepay Transaction");
        }
        else
        {
            txName.setText("INR Transaction");
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Log.e("bitcoin--->",finacals+"");
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        callService();

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }
//        ivarrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               finish();
//            }
//        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().
                    getPreferenceData(BitcoinTransaction.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            if (buy_rate > 0) {

                tvINR.setText(formatter.format(buy_rate) + " " +
                        PreferenceFile.getInstance().getPreferenceData(BitcoinTransaction.this,
                                Constant.Currency_Symbol));
            } else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().
                        getPreferenceData(BitcoinTransaction.this, Constant.Currency_Symbol));
            }
        }
    };

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

            new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void transaction() {

        if (Constant.isConnectingToInternet(this)) {

            if(getIntent().getStringExtra("key").equals("inr"))
            {
                //new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_INR_Transactions, Constant.INR_Transactions+"56").callService(true);
                new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_INR_Transactions,
                        Constant.INR_Transactions+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
            }
            else {
               // new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_Bitcoin_Transaction, Constant.Bitcoin_Transaction+"56").callService(true);
                new Retrofit2(this, BitcoinTransaction.this, Constant.REQ_Bitcoin_Transaction, Constant.Bitcoin_Transaction+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
            }


            // new Retrofit2(this, Statements.this, Constant.REQ_Statements, Constant.Statements+"27").callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
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

                        callService();

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(BitcoinTransaction.this, Constant.Currency_Symbol));
                            // tvwallet.setText(String.format(formatter, ff)+" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            // tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

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
                        else {
                            Constant.alertWithIntent(this,"Account Blocked",BlockScreen.class);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;



            case Constant.REQ_BTC_RATE:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        transaction();

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }


                        } else {

                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY)!=null) {
//                                buyrate=Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY));
//                                // buyrate=Double.valueOf(data.getString("sell"));
//
//                                txBitcoin.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY)))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                                tvSellrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.SELL)))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))*Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.SELL));

                                if(buy_rate>0) {

                                    tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                }else
                                {
                                    tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }
                break;

            case Constant.REQ_Bitcoin_Transaction:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    if (status.equals("true"))
                    {
                        transaction_models.clear();
                        JSONArray data=result.getJSONArray("data");

                        for (int x=0;x<data.length();x++) {
                            JSONObject obj = data.getJSONObject(x);

                            Log.e("message-->",obj.getString("message"));

                           // if((obj.getString("message").equals("Bidding for sell Bitcoin")||(obj.getString("message").equals("Bitcoin transfer")||obj.getString("message").equals("Sell Acepay")||obj.getString("message").equals("Buy Acepay"))) {

                                Transaction_model transaction_model = new Transaction_model();
                                transaction_model.setId(obj.getString("id"));
                                transaction_model.setDescription(obj.getString("message"));
                                transaction_model.setBtc(obj.getString("BTC"));
                                transaction_model.setStatus(obj.getString("status"));
                                transaction_model.setInrAmount(obj.getString("inr"));

                                if (!obj.isNull("amount")) {

                                    double calcul = Double.parseDouble(obj.getString("amount"));

                                    BigDecimal d = new BigDecimal(calcul);
                                    Log.e("newcal-->", "d -->" + d);
                                    String finacal = String.valueOf(d);
                                    transaction_model.setAmount(finacal);
                                }
                                else {
                                    transaction_model.setAmount("0");
                                    Log.e("null-->", "" + "null");
                                }

                                transaction_model.setRate(obj.getString("rate"));
                                transaction_model.setDate(obj.getString("created"));
                                transaction_model.setTransaction_id(obj.getString("txid"));
                            transaction_model.setTransaction_by(obj.getString("transaction_by"));

                            if(obj.getString("BTC").equals("6") || obj.getString("BTC").equals("1")){

                                transaction_model.setReceiver_name(obj.getString("receive_name"));
                                transaction_model.setReceiver_no(obj.getString("receive_phone"));
                            }

                              /*  if(){//BTC 1 for Transfer BTC means send Acepay need reciver name/number

                                }*/

                                Log.e("type-->",getIntent().getStringExtra("type"));
                                Log.e("BTC-->",obj.getString("BTC"));

                                if(obj.getString("BTC").equals(getIntent().getStringExtra("type"))) {
                                    transaction_models.add(transaction_model);
                                }
                           // }
                            Log.e("amount-->",obj.getString("amount"));
                        }

                        if(transaction_models.size()>0) {
                            transactionAdapter = new BitcoinTransactionAdapter(this, transaction_models);
                            recyclerView.setAdapter(transactionAdapter);
                        }
                        else {
                            Constant.alertWithIntent(this,"No Data Found!",Dashboard.class);
                        }

                        Log.e("transactionsize-->",transaction_models.size()+"");

                    }
                    else {

                        Constant.alertDialog(this,message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_INR_Transactions:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    if (status.equals("true"))
                    {
                        transaction_models.clear();
                        JSONArray data=result.getJSONArray("data");

                        for (int x=0;x<data.length();x++) {
                            JSONObject obj = data.getJSONObject(x);

                                Transaction_model transaction_model = new Transaction_model();
                                transaction_model.setId(obj.getString("id"));
                                transaction_model.setDescription(obj.getString("message"));
                                transaction_model.setBtc(obj.getString("INR"));

                                if (!obj.isNull("amount")) {

                                    double calcul = Double.parseDouble(obj.getString("amount"));

                                    BigDecimal d = new BigDecimal(calcul);
                                    Log.e("newcal-->", "d -->" + d);
                                    String finacal = String.valueOf(d);
                                    transaction_model.setAmount(finacal);
                                }
                                else {
                                    transaction_model.setAmount("0");
                                    Log.e("null-->", "" + "null");
                                }

                            if(obj.getString("INR").equals("1") || obj.getString("INR").equals("2")){

                                transaction_model.setReceiver_name(obj.getString("receive_name"));
                                transaction_model.setReceiver_no(obj.getString("receive_phone"));
                            }

                                transaction_model.setDate(obj.getString("created"));
                                transaction_model.setStatus(obj.getString("status"));
                                transaction_model.setTransaction_id(obj.getString("txid"));
                            transaction_model.setTransaction_by(obj.getString("transaction_by"));


                            if(obj.getString("INR").equals(getIntent().getStringExtra("type"))) {
                                transaction_models.add(transaction_model);
                            }
                            Log.e("type-->",getIntent().getStringExtra("type"));
                            Log.e("INR-->",obj.getString("INR"));
                            Log.e("amount-->",obj.getString("amount"));
                        }

                        if(transaction_models.size()>0) {
                            inrAdapter = new INRAdapter(this, transaction_models);
                            recyclerView.setAdapter(inrAdapter);
                        }
                        else {
                            Constant.alertWithIntent(this,"No Data Found!",Dashboard.class);
                        }

                        Log.e("transactionsize-->",transaction_models.size()+"");

                    }
                    else {

                        Constant.alertDialog(this,message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lnrefresh:

                newRefereshing();

                break;
        }

    }
}
