package com.acepay;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.DownloadPackage.DownloadService;
import com.acepay.Util.App;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class Transaction_details extends AppCompatActivity implements RetrofitResponse {

    private TextView tvtransactionType, tvTransfer, tvmodel, tvmacaddress, txrate, txGst, tvtotle, tvRate, tvtotal,
            tvIp, totle_amount, tvAmount, tvNetworkOper, tvsenderId, tvreceiver_id, tvInrNetwork, tvreceiverMobile,
            tvGstBTC, tvnetworlkfees, tvtransactiondatetime, tvstatur, tvTranIp, tvFrom, tvTo, txName, tvnetworkfeesBTC;
    private ImageView ivarrow, pdf;
    private LinearLayout lnLayer, lnlayer, lnLeaner, lnReceiver, lnNetwork, lnRate, lnamountlayeer, lnsiderreceiver, lnTotle, lnNew;
    private String id, btc;
    boolean doubleBackToExitPressedOnce = false;
    NumberFormat formatter;
    ScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        id = getIntent().getStringExtra("id");
        btc = getIntent().getStringExtra("btc");

        Log.e("btc", btc);

        sv = (ScrollView) findViewById(R.id.sv);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        pdf = (ImageView) findViewById(R.id.pdf);
        tvtransactionType = (TextView) findViewById(R.id.tvtransactionType);
        tvTransfer = (TextView) findViewById(R.id.tvTransfer);
        tvmodel = (TextView) findViewById(R.id.tvmodel);
        tvnetworkfeesBTC = (TextView) findViewById(R.id.tvnetworkfeesBTC);
        tvGstBTC = (TextView) findViewById(R.id.tvGstBTC);
        txrate = (TextView) findViewById(R.id.txrate);
        lnsiderreceiver = (LinearLayout) findViewById(R.id.lnsiderreceiver);
        lnRate = (LinearLayout) findViewById(R.id.lnRate);
        lnlayer = (LinearLayout) findViewById(R.id.lnlayer);
        lnNetwork = (LinearLayout) findViewById(R.id.lnNetwork);
        lnTotle = (LinearLayout) findViewById(R.id.lnTotle);
        lnReceiver = (LinearLayout) findViewById(R.id.lnReceiver);
        lnamountlayeer = (LinearLayout) findViewById(R.id.lnamountlayeer);
        lnLeaner = (LinearLayout) findViewById(R.id.lnLeaner);
        tvIp = (TextView) findViewById(R.id.tvIp);
        txGst = (TextView) findViewById(R.id.txGst);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvmacaddress = (TextView) findViewById(R.id.tvmacaddress);
        tvtotle = (TextView) findViewById(R.id.tvtotle);
        totle_amount = (TextView) findViewById(R.id.totle_amount);
        tvnetworlkfees = (TextView) findViewById(R.id.tvnetworlkfees);
        tvInrNetwork = (TextView) findViewById(R.id.tvInrNetwork);
        tvRate = (TextView) findViewById(R.id.tvRate);
        tvtotal = (TextView) findViewById(R.id.tvtotal);
        lnLayer = (LinearLayout) findViewById(R.id.lnLayer);
        lnNew = (LinearLayout) findViewById(R.id.lnNew);
        tvsenderId = (TextView) findViewById(R.id.tvsenderId);
        txName = (TextView) findViewById(R.id.txName);
        tvreceiver_id = (TextView) findViewById(R.id.tvreceiver_id);
        tvreceiverMobile = (TextView) findViewById(R.id.tvreceiverMobile);
        tvtransactiondatetime = (TextView) findViewById(R.id.tvtransactiondatetime);
        tvstatur = (TextView) findViewById(R.id.tvstatur);
        tvTranIp = (TextView) findViewById(R.id.tvTranIp);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvTo = (TextView) findViewById(R.id.tvTo);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        tvNetworkOper = (TextView) findViewById(R.id.tvNetworkOper);
        txName.setVisibility(View.VISIBLE);
        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(Transaction_details.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        if (btc.equals("1")) {
            txName.setText("Acecoins transfer");
        }
        if (btc.equals("2")) {
            txName.setText("Bidding for sell Acecoins");
        }
        if (btc.equals("3")) {
            txName.setText("Bidding for buy Acecoins");
        }
        if (btc.equals("4")) {
            txName.setText("Sell Acecoins");
        }
        if (btc.equals("5")) {
            txName.setText("Buy Acecoins");
        }
        if (btc.equals("6")) {
            txName.setText("INR Wallet Transfer");
        }

        if (btc.equals("7")) {
            txName.setText("INR Wallet Deposit");
        }
        if (btc.equals("8")) {
            txName.setText("INR Wallet Withdraw");
        }
        if (btc.equals("9")) {
            txName.setText("INR Wallet Recieve");
        }
        if (btc.equals("10")) {
            txName.setText("INR Wallet Deposit (PayU)");
        }
        if (btc.equals("11")) {
            txName.setText("INR Wallet Deposit (PayPal)");
        }
        if (btc.equals("12")) {
            txName.setText("Acecoins Receive");
        }

        if (btc.equals("6")) {
            lnLayer.setVisibility(View.GONE);
        }

        if (btc.equals("2") || btc.equals("3")) {

            lnLayer.setVisibility(View.GONE);
            lnNew.setVisibility(View.VISIBLE);
        }

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("transaction_id", id);
            postParam.put("btc", btc);

            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(Transaction_details.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(Transaction_details.this, Transaction_details.this, postParam, Constant.REQ_transaction_details, Constant.transaction_details, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(Transaction_details.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Log.e("url-->", "http://metapay.io/homes/TransactionOnePdf/" + PreferenceFile.getInstance().getPreferenceData(Transaction_details.this, Constant.ID) + "/" + id + "/" + btc);
//                startDownload("https://bitok.co.in/homes/TransactionOnePdf/" + PreferenceFile.getInstance().getPreferenceData(Transaction_details.this, Constant.ID) + "/" + id + "/" + btc);
                startDownload(Constant.STATEMENT_PDF_URL+"/homes/TransactionOnePdf/" +
                        PreferenceFile.getInstance().getPreferenceData(Transaction_details.this, Constant.ID) + "/" + id + "/" + btc);
                // http://18.216.88.154/metaPay/homes/TransactionOnePdf/user_id/transactions_id/type
            }
        });
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
            Log.e("Biding", "once");
//            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                finish();
            }
        }, 1000);
    }

    private void startDownload(String id) {
        Log.e("downloading-->", "yes");
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("D_id", id);
        //intent.putExtra("DOWNLOAD_URL",Constants.QUOTE_DOWNLOAD);
        intent.putExtra("STORAGE", App.getQUOTATIONS());
        startService(intent);

        Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_transaction_details:

                sv.setVisibility(View.VISIBLE);
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");

                    lnLeaner.setVisibility(View.VISIBLE);
                    if (status.equals("true")) {

                        JSONObject jsonObject = result.getJSONObject("data");

                        if (btc.equals("6") ||btc.equals("14")) {

                            tvmodel.setText(jsonObject.getString("device_model") + "/" +
                                    jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            if (!jsonObject.isNull("network") || jsonObject.getString
                                    ("network").equalsIgnoreCase("")) {
                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.GONE);

                            tvIp.setText(jsonObject.getString("ip"));

                            tvstatur.setText(jsonObject.getString("status"));

                            JSONObject User = jsonObject.getJSONObject("Receiver");


                            if (jsonObject.getString("transaction_by").equals("2"))
                            {
                                tvsenderId.setText("Admin");
                            }
                            else {
                                JSONObject Sender = jsonObject.getJSONObject("Sender");
                                tvsenderId.setText(Sender.getString("first_name")+
                                        " "+Sender.getString("last_name"));
                            }

                            tvreceiverMobile.setText(User.getString("phone"));

                            double total_btc = Double.parseDouble(
                                    jsonObject.getString("amount").trim());

                            BigDecimal b = new BigDecimal(total_btc);

//                            tvsenderId.setText(User.getString("sender_id"));

//                            tvreceiver_id.setText(jsonObject.getString("receiver_id"));
                            tvreceiver_id.setText(User.getString("first_name")+
                                    " "+User.getString("last_name"));


                            tvAmount.setText(PreferenceFile.getInstance().
                                    getPreferenceData(Transaction_details.this,
                                            Constant.Currency_Code)+" : " +
                                    formatter.format(b));


                        }
                        if (btc.equals("7")) {

                            tvmodel.setText(jsonObject.getString("device_model")
                                    + "/" + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            if (!jsonObject.isNull("network") || jsonObject.getString
                                    ("network").equalsIgnoreCase("")) {
                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            lnsiderreceiver.setVisibility(View.GONE);
                            lnReceiver.setVisibility(View.GONE);

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.GONE);

                            tvIp.setText(jsonObject.getString("ip"));

                            if (jsonObject.getString("verify_admin").equalsIgnoreCase("no")) {
                                tvstatur.setText("Pending");
                            } else {
                                tvstatur.setText("Success");
                            }


                            double total_btc = Double.parseDouble(jsonObject.getString("deposite").trim());
                            BigDecimal b = new BigDecimal(total_btc);

                            tvmodel.setText(jsonObject.getString("device_model") + "/" +
                                    jsonObject.getString("device_brand"));

                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            tvAmount.setText(PreferenceFile.getInstance().
                                    getPreferenceData(Transaction_details.this,Constant.Currency_Code)
                                    +" " +
                                    ": " + formatter.format(b) + " " +
                                    PreferenceFile.getInstance().getPreferenceData
                                            (this, Constant.Currency_Symbol));
                        }
                        if (btc.equals("10")) {

                            lnsiderreceiver.setVisibility(View.GONE);
                            lnReceiver.setVisibility(View.GONE);

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.GONE);

                            // tvIp.setText(jsonObject.getString("ip"));
                            tvstatur.setText(jsonObject.getString("status"));

                            double total_btc = Double.parseDouble(jsonObject.getString("amount").trim());
                            BigDecimal b = new BigDecimal(total_btc);

                            tvAmount.setText(PreferenceFile.getInstance().
                                    getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                    +  formatter.format(b));
                        }

                        if (btc.equals("11")) {

                            tvIp.setText(jsonObject.getString("ip"));
                            tvmodel.setText(jsonObject.getString("device_model") + "/" + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network").equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }
                            lnsiderreceiver.setVisibility(View.GONE);
                            lnReceiver.setVisibility(View.GONE);

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.GONE);


                            tvstatur.setText(jsonObject.getString("status"));
                            //  tvstatur.setText(jsonObject.getString("verify_admin"));


                            double total_btc = Double.parseDouble(jsonObject.getString("amount").trim());
                            BigDecimal b = new BigDecimal(total_btc);

                            tvAmount.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    +  formatter.format(b));

                        }
                        if (btc.equals("8")) {

                            tvmodel.setText(jsonObject.getString("device_model") + "/"
                                    + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network")
                                    .equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }
                            lnsiderreceiver.setVisibility(View.GONE);
                            lnReceiver.setVisibility(View.GONE);

                            lnlayer.setVisibility(View.VISIBLE);
                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.VISIBLE);

                            tvIp.setText(jsonObject.getString("ip"));
                            txrate.setText("Withdrawal ");

                            if (jsonObject.getString("verify_admin").equalsIgnoreCase("no")) {
                                tvstatur.setText("Pending");
                            } else {
                                tvstatur.setText("Success");
                            }

                            Double total_btc = Double.parseDouble(jsonObject.getString("credit").trim());
                            Double tax = Double.parseDouble(jsonObject.getString("tax").trim());
                            Double original_amount = Double.parseDouble(jsonObject.getString("original_amount").trim());
                            // int gst = Integer.parseInt(jsonObject.getString("gst").trim()) ;

                            BigDecimal b = new BigDecimal(total_btc);

                            tvRate.setText("Fees : " +  formatter.format(tax) + " "
                                    + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvAmount.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(total_btc) + " " +
                                    PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvtotal.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    +  formatter.format(original_amount)
                                    + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                        }
                        if (btc.equals("9")) {

                            tvmodel.setText(jsonObject.getString("device_model") + "/" + jsonObject.getString("device_brand"));
                            if (!jsonObject.isNull("mac_address") ) {
                                if(jsonObject.getString("mac_address").equalsIgnoreCase("")){
                                    tvmacaddress.setText("N/A");
                                }else{
                                    tvmacaddress.setText(jsonObject.getString("mac_address"));
                                }
                            }else{
                                tvmacaddress.setText("N/A");
                            }

                            if (!jsonObject.isNull("network")) {

                                if(jsonObject.getString("network").equalsIgnoreCase("")){
                                    tvNetworkOper.setText("N/A");
                                }else{
                                    tvNetworkOper.setText(jsonObject.getString("network"));
                                }
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnamountlayeer.setVisibility(View.GONE);


                            tvIp.setText(jsonObject.getString("ip"));
                            tvstatur.setText(jsonObject.getString("status"));

                            JSONObject User = jsonObject.getJSONObject("Sender");
                            JSONObject Receiver = jsonObject.getJSONObject("Receiver");
                            tvreceiverMobile.setText(Receiver.getString("phone"));

//                            tvsenderId.setText(User.getString("sender_id"));
                            tvsenderId.setText(User.getString("first_name")+" "+User.getString("last_name"));
//                            tvreceiver_id.setText(jsonObject.getString("receiver_id"));
                            tvreceiver_id.setText(Receiver.getString("first_name")+" "+
                                    Receiver.getString("last_name"));


                            double total_btc = Double.parseDouble(jsonObject.getString("convert_inr_receive").trim());
//                            double total_btc = Double.parseDouble(jsonObject.getString("amount").trim());
                            BigDecimal b = new BigDecimal(total_btc);

                            tvAmount.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    +  formatter.format(b)+" "+PreferenceFile.getInstance()
                                    .getPreferenceData(Transaction_details.this,Constant.Currency_Symbol));


                        }

                        if (btc.equals("1")) {

                            tvmodel.setText(jsonObject.getString("device_model") + "/" +
                                    jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network")
                                    .equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }
                            tvsenderId.setText(jsonObject.getString("from_address"));
                            tvreceiver_id.setText(jsonObject.getString("to_address"));

                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));

                            tvstatur.setText(jsonObject.getString("status"));

                            tvIp.setText(jsonObject.getString("ip"));

                            if (jsonObject.has("error_message")) {
                                tvFrom.setText("BTC : " + "0.000");
                                totle_amount.setText(PreferenceFile.getInstance().getPreferenceData
                                        (Transaction_details.this,Constant.Currency_Code)+" : " + "0.000");
                                tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData
                                        (Transaction_details.this,Constant.Currency_Code)+" : " + "0.000");

                                tvTo.setText(PreferenceFile.getInstance().getPreferenceData
                                        (Transaction_details.this,Constant.Currency_Code)+" : " + "0.00");
                                tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData
                                        (Transaction_details.this,Constant.Currency_Code)+" : " + "0.00");
//                                    tvtotle.setText("INR : " + "0.000");  TODO BY MEE
                            } else {

                                int sendmoney = jsonObject.getInt("amount_sent");
                                Double rate = Double.parseDouble(jsonObject.getString("rate"));

                                double calcul = Double.parseDouble(jsonObject.getString("amount_sent").trim());
                                BigDecimal d = new BigDecimal(calcul);

                                double previous_amount = Double.parseDouble(jsonObject.getString("previous_amount").trim());
                                double amount_withdrawn = Double.parseDouble(jsonObject.getString("amount_withdrawn").trim());
                                BigDecimal a = new BigDecimal(amount_withdrawn);

                                double network_fee = Double.parseDouble(jsonObject.getString("network_fee").trim());
                                BigDecimal b = new BigDecimal(network_fee);
                                tvnetworlkfees.setText(
                                        PreferenceFile.getInstance().getPreferenceData(
                                                Transaction_details.this,Constant.Currency_Code)+" : "
                                                +  formatter.format(network_fee) + " " +
                                                PreferenceFile.getInstance().getPreferenceData
                                                        (this, Constant.Currency_Symbol));

                                Double network_fee_inr = Double.parseDouble(jsonObject.getString("gst").trim());
                                BigDecimal newnetwork_fee_inr = new BigDecimal(network_fee_inr);
                                tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData
                                        (Transaction_details.this,Constant.Currency_Code)+" : "
                                        + formatter.format(network_fee_inr) + " "
                                        + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                Double amount_sent_inr = Double.parseDouble(jsonObject.getString("amount_sent_inr").trim());
                                Double final_charge = Double.parseDouble(jsonObject.getString("final_charge").trim());
                                BigDecimal newamount_sent_inr = new BigDecimal(amount_sent_inr);

                                Double networkFeeBTC = network_fee / Double.valueOf(rate);
                                Double gstFeeBTC = Double.valueOf(network_fee_inr) / Double.valueOf(rate);
                                tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                                tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");


                                if(Double.parseDouble(String.format("%.8f",previous_amount))==
                                        Double.parseDouble(String.format("%.8f",amount_withdrawn))){
                                    //send all
                                    Log.e("TRANSFERALL","TRUE");
                                    tvFrom.setText("BTC : " + String.format("%.8f", calcul) + "Ace ");
                                    tvTo.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : "
                                            +formatter.format(amount_sent_inr - final_charge) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this,
                                                    Constant.Currency_Symbol));

                                    tvtotle.setText(String.format("%.8f", amount_withdrawn) + " Ace");
                                    totle_amount.setText(PreferenceFile.getInstance().
                                            getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                            + formatter.format(amount_sent_inr) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                }else{
                                    Log.e("TRANSFERALL","FALSE");
                                    tvFrom.setText("BTC : " + String.format("%.8f", calcul) + "Ace ");
                                    totle_amount.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : "
                                            + formatter.format(amount_sent_inr + final_charge) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
//                                    tvTo.setText(" : " + String.valueOf(amount_sent_inr - final_charge) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    Double totAmtBTC = calcul + networkFeeBTC + gstFeeBTC;
                                    tvtotle.setText(String.format("%.8f", totAmtBTC) + " Ace");
                                    tvTo.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : " +
                                            formatter.format(amount_sent_inr) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
//                                    totle_amount.setText("INR : " + String.valueOf(amount_sent_inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }


                            }
                        }


                        if(btc.equals("15")){
                            tvmodel.setText(jsonObject.getString("device_model") + "/" + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            if (!jsonObject.isNull("network") ||
                                    jsonObject.getString("network").equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }
                            tvsenderId.setText(jsonObject.getString("from_address"));
                            tvreceiver_id.setText(jsonObject.getString("to_address"));

                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));

                            tvstatur.setText(jsonObject.getString("status"));

                            tvIp.setText(jsonObject.getString("ip"));

                            if (jsonObject.has("error_message")) {
                                tvFrom.setText("BTC : " + "0.000");
                                totle_amount.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " + "0.000");
                                tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " + "0.000");

                                tvTo.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " + "0.00");
                                tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " + "0.00");
//                                    tvtotle.setText("INR : " + "0.000");  TODO BY MEE
                            } else {

                                int sendmoney = jsonObject.getInt("amount");
                                Double rate = Double.parseDouble(jsonObject.getString("rate"));

                                double calcul = Double.parseDouble(jsonObject.getString("amount").trim());
                                BigDecimal d = new BigDecimal(calcul);

                                double previous_amount = Double.parseDouble(jsonObject.getString("previous_amount").trim());
                                double amount_withdrawn = Double.parseDouble(jsonObject.getString("amount").trim());
                                BigDecimal a = new BigDecimal(amount_withdrawn);

                                double network_fee = Double.parseDouble(jsonObject.getString("network_charge").trim());
                                BigDecimal b = new BigDecimal(network_fee);
                                tvnetworlkfees.setText(
                                        PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,
                                                Constant.Currency_Code)+" : "
                                                +formatter.format(network_fee) + " " +
                                                PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                Double network_fee_inr = Double.parseDouble(jsonObject.getString("gst").trim());
                                BigDecimal newnetwork_fee_inr = new BigDecimal(network_fee_inr);
                                tvInrNetwork.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                        + formatter.format(network_fee_inr) + " "
                                        + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                Double amount_sent_inr = Double.parseDouble(jsonObject.getString("amount_inr").trim());
                                Double final_charge = Double.parseDouble(jsonObject.getString("final_charge").trim());
                                BigDecimal newamount_sent_inr = new BigDecimal(amount_sent_inr);

                                Double networkFeeBTC = network_fee / Double.valueOf(rate);
                                Double gstFeeBTC = Double.valueOf(network_fee_inr) / Double.valueOf(rate);
                                tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                                tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");


                                if(Double.parseDouble(String.format("%.8f",previous_amount))==
                                        Double.parseDouble(String.format("%.8f",amount_withdrawn))){
                                    //send all
                                    Log.e("TRANSFERALL","TRUE");
                                    tvFrom.setText("BTC : " + String.format("%.8f", calcul) + "Ace ");
                                    tvTo.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : "
                                            +formatter.format(amount_sent_inr - final_charge) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                    tvtotle.setText(String.format("%.8f", amount_withdrawn) + " Ace");
                                    totle_amount.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : "
                                            +formatter.format(amount_sent_inr) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                }else{
                                    Log.e("TRANSFERALL","FALSE");
                                    tvFrom.setText("BTC : " + String.format("%.8f", calcul) + "Ace ");
                                    totle_amount.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : "
                                            + formatter.format(amount_sent_inr + final_charge) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
//                                    tvTo.setText(" : " + String.valueOf(amount_sent_inr - final_charge) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                    Double totAmtBTC = calcul + networkFeeBTC + gstFeeBTC;
                                    tvtotle.setText(String.format("%.8f", totAmtBTC) + " Ace");
                                    tvTo.setText(PreferenceFile.getInstance().getPreferenceData
                                            (Transaction_details.this,Constant.Currency_Code)+" : " +
                                            formatter.format(amount_sent_inr) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(
                                                    this, Constant.Currency_Symbol));
//                                    totle_amount.setText("INR : " + String.valueOf(amount_sent_inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }
                        }

                        if (btc.equals("12")) {
                            tvsenderId.setText(jsonObject.getString("from_address"));
                            tvreceiver_id.setText(jsonObject.getString("to_address"));
                            tvTransfer.setText("Received : ");

                            tvmodel.setText(jsonObject.getString("device_model") + "/"
                                    + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            if (!jsonObject.isNull("network") || jsonObject.getString("network")
                                    .equalsIgnoreCase(""))
                            {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            if (jsonObject.getString("transaction_by").equals("2"))
                            {
                                tvreceiverMobile.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.phone));

                            }
                            else {
                                if (!jsonObject.isNull("User")) {
                                    JSONObject User = jsonObject.getJSONObject("User");
                                    tvreceiverMobile.setText(User.getString("phone"));
                                }
                            }





                            lnNetwork.setVisibility(View.GONE);
//                            lnGst.setVisibility(View.GONE);
                            lnTotle.setVisibility(View.GONE);


                            tvstatur.setText(jsonObject.getString("status"));

                            tvIp.setText(jsonObject.getString("ip"));

                            if (jsonObject.has("error_message")) {
                                tvFrom.setText("BTC : " + "0.000");
                                totle_amount.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+
                                        " : " + "0.00");
                                tvnetworlkfees.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+
                                        " : " + "0.00");

                                tvTo.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                        + "0.00");
                                tvInrNetwork.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                        + "0.00");
                                tvtotle.setText(PreferenceFile.getInstance().
                                        getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                        + "0.00");  //TODO BY MEE
                            } else {

                                Double sendmoney;
                                //todo by admin
                                if (jsonObject.opt("transaction_by").equals("2"))
                                {

                                    int amnt=jsonObject.optInt("amount_withdrawn");
                                    sendmoney  = Double.parseDouble(String.valueOf(amnt));
                                }
                                else
                                {
                                    sendmoney = (Double) jsonObject.opt("amount_withdrawn");

                                }


                                double calcul = Double.parseDouble(jsonObject.getString("amount_sent").trim());
                                BigDecimal d = new BigDecimal(calcul);
                                Double rate = Double.parseDouble(jsonObject.getString("rate"));
                                Double amount_withdrawn = Double.parseDouble(jsonObject.getString("amount_sent_inr").trim());
                                BigDecimal a = new BigDecimal(amount_withdrawn);
                                double network_fee=0.0;

                                if (!jsonObject.isNull("network_fee")) {
                                    network_fee = Double.parseDouble(jsonObject.getString("network_fee").trim());
                                    BigDecimal b = new BigDecimal(network_fee);
                                    tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                            + formatter.format(b) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : 0.00");
                                }

                                tvFrom.setText("BTC : " + String.format("%.8f", calcul) + "Ace ");
                                totle_amount.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)
                                        +" : " +
                                        formatter.format(amount_withdrawn) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));


                                //  if(!jsonObject.getString("amount_withdrawn_inr").equals("null")) {

                                Double final_charge = Double.parseDouble(jsonObject.getString("final_charge").trim());
                                BigDecimal newamount_withdrawn_inr = new BigDecimal(final_charge);

                                Double amount_sent_inr = Double.parseDouble(jsonObject.getString("amount_sent_inr").trim());
                                BigDecimal newamount_sent_inr = new BigDecimal(amount_sent_inr);

                                Double network_fee_inr = Double.parseDouble(jsonObject.getString("gst").trim());
                                Double newnetwork_fee_inr = (amount_sent_inr - final_charge);

                                Double networkFeeBTC = Double.valueOf(network_fee) / Double.valueOf(rate);
                                Double gstFeeBTC = Double.valueOf(network_fee_inr) / Double.valueOf(rate);
                                tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                                tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");


                                Double totAmtBTC = calcul + networkFeeBTC + gstFeeBTC;
                                tvtotle.setText(String.format("%.8f", totAmtBTC) + " Ace");

                                tvTo.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                        formatter.format(newnetwork_fee_inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                        + formatter.format(network_fee_inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                // tvtotle.setText("INR : " + String.valueOf(newamount_withdrawn_inr));
                                //  }
                            }
                        }

                        if (btc.equals("4")) {

                            tvstatur.setText(jsonObject.getString("status"));
                            tvreceiver_id.setVisibility(View.GONE);

                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));
                            tvIp.setText(jsonObject.getString("ip"));

                            lnTotle.setVisibility(View.VISIBLE);
                            lnsiderreceiver.setVisibility(View.GONE);
                            lnNetwork.setVisibility(View.VISIBLE);

                            tvmodel.setText(jsonObject.getString("device_model") + "/" + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network").equalsIgnoreCase("")) {
                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            Double calcul = Double.parseDouble(jsonObject.getString("sell_btc").trim());

                            Double fee = Double.parseDouble(jsonObject.getString("fee").trim());
                            Double tax = Double.parseDouble(jsonObject.getString("tax").trim());
                            Double gst = Double.parseDouble(jsonObject.getString("gst").trim());

                            Double sell_amount =Double.parseDouble(jsonObject.getString("sell_amount").trim());
                            Double amount_withdrawn = Double.parseDouble(jsonObject.getString("sell_amount").trim());

                            BigDecimal d = new BigDecimal(calcul);
                            BigDecimal a = new BigDecimal(amount_withdrawn);

                            Double sell_rate = Double.parseDouble(jsonObject.getString("sell_rate").trim());
                            Double prevBTC = Double.parseDouble(jsonObject.getString("previous_btc_amount").trim());

                            Double rate = Double.parseDouble(jsonObject.getString("sell_rate").trim());

                            Double networkFeeBTC = Double.valueOf(fee) / Double.valueOf(rate);
                            Double gstFeeBTC = Double.valueOf(gst) / Double.valueOf(rate);

                            tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                            tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");

                            Double totAmtBTC = calcul + networkFeeBTC + gstFeeBTC;

                            if(Double.parseDouble(String.format("%.8f", calcul))==Double.parseDouble
                                    (String.format("%.8f", prevBTC))) {
                                Double remBTC=calcul-(networkFeeBTC+gstFeeBTC);
                                tvtotle.setText(String.format("%.8f", d) + " Ace");
                                tvFrom.setText("BTC : " + String.format("%.8f", remBTC) + " Ace");
                            }else  if(Double.parseDouble(String.format("%.8f", calcul))<Double.
                                    parseDouble(String.format("%.8f", prevBTC))) {
                                Double remBTC=(amount_withdrawn - (tax + gst))/sell_rate;
                                tvtotle.setText(String.format("%.8f", d) + " Ace");
                                tvFrom.setText("BTC : " + String.format("%.8f", remBTC) + " Ace");
                            }


                            totle_amount.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(amount_withdrawn) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvTo.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(amount_withdrawn - (tax + gst)) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(tax) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(gst) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                        }

                        if (btc.equals("3")) {

                            lnLayer.setVisibility(View.VISIBLE);
                            lnsiderreceiver.setVisibility(View.GONE);
                            tvreceiver_id.setVisibility(View.GONE);

                            lnNew.setVisibility(View.GONE);
                            lnTotle.setVisibility(View.VISIBLE);
                            lnNetwork.setVisibility(View.VISIBLE);

                            tvstatur.setText(jsonObject.getString("status"));
                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));
                            tvIp.setText(jsonObject.getString("ip"));

                            double calcul = Double.parseDouble(jsonObject.getString("amount").trim());
                            BigDecimal d = new BigDecimal(calcul);

                            double rate = Double.parseDouble(jsonObject.getString("rate").trim());
                            BigDecimal a = new BigDecimal(rate);

                            double total_btc = Double.parseDouble(jsonObject.getString("total_btc").trim());
                            BigDecimal b = new BigDecimal(total_btc);


                            tvmodel.setText(jsonObject.getString("device_model") + "/" + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));

                            if (!jsonObject.isNull("network") || jsonObject.getString("network").equalsIgnoreCase("")) {
                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }


                            Double tax =Double.parseDouble(jsonObject.getString("tax").trim());
                            Double gst =Double.parseDouble(jsonObject.getString("gst").trim());

                            Double networkFeeBTC = Double.valueOf(tax) / Double.valueOf(rate);
                            Double gstFeeBTC = Double.valueOf(gst) / Double.valueOf(rate);

                            Double remINR= calcul-(tax+gst);
                            int i = Integer.valueOf(remINR.intValue());

                            tvTo.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "+i+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(gst) + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(tax) + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                            tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");

                            tvTransfer.setText("Amount Acepay : ");
                            tvFrom.setText(String.format("%.8f",total_btc)+ " Ace");


                            tvAmount.setText("BTC : " + String.format("%.8f",total_btc)+ " Ace");
//                            tvAmount.setText("BTC : " + String.valueOf(b));
                            tvRate.setText(PreferenceFile.getInstance().getPreferenceData(
                                    Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(a));
                            tvtotal.setText(PreferenceFile.getInstance().getPreferenceData(
                                    Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(d));
//
                            Double totBTC=total_btc+networkFeeBTC+gstFeeBTC;

                            tvtotle.setText(String.format("%.8f",totBTC)+ " Ace");
                            totle_amount.setText(PreferenceFile.getInstance().getPreferenceData(
                                    Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(d));

                        }

                        if (btc.equals("2")) {

                            lnLayer.setVisibility(View.GONE);
                            lnNew.setVisibility(View.VISIBLE);
                            lnsiderreceiver.setVisibility(View.GONE);
                            tvreceiver_id.setVisibility(View.GONE);

                            tvstatur.setText(jsonObject.getString("status"));
                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));
                            tvIp.setText(jsonObject.getString("ip"));

                            double calcul = Double.parseDouble(jsonObject.getString("sell_btc").trim());
                            BigDecimal d = new BigDecimal(calcul);


                            double rate = Double.parseDouble(jsonObject.getString("sell_rate").trim());
                            BigDecimal a = new BigDecimal(rate);


                            double total_btc = Double.parseDouble(jsonObject.getString("sell_amount").trim());
                            BigDecimal b = new BigDecimal(total_btc);


                            tvmodel.setText(jsonObject.getString("device_model") + "/"
                                    + jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network")
                                    .equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }

                            tvAmount.setText("BTC : " + String.format("%.8f",d)+ " Ace");
                            tvRate.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(a)+" "+PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Symbol));
                            tvtotal.setText(PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(b)+" "+PreferenceFile.getInstance().getPreferenceData
                                    (Transaction_details.this,Constant.Currency_Symbol));

                        }

                        if (btc.equals("5")) {

                            tvTransfer.setText("Buy ");

                            tvstatur.setText(jsonObject.getString("status"));
                            JSONObject User = jsonObject.getJSONObject("User");
                            tvreceiverMobile.setText(User.getString("phone"));
                            tvIp.setText(jsonObject.getString("ip"));

                            tvreceiver_id.setVisibility(View.GONE);
                            lnTotle.setVisibility(View.VISIBLE);
                            lnNetwork.setVisibility(View.VISIBLE);
                            lnsiderreceiver.setVisibility(View.GONE);

                            Double calcul = Double.parseDouble(jsonObject.getString("amount").trim());
                            Double fee = Double.parseDouble(jsonObject.getString("fee").trim());
                            Double tax = Double.parseDouble(jsonObject.getString("tax").trim());
                            Double gst = Double.parseDouble(jsonObject.getString("gst").trim());
                            Double rate = Double.parseDouble(jsonObject.getString("rate").trim());
                            BigDecimal d = new BigDecimal(calcul);

                            Double networkFeeBTC = Double.valueOf(fee) / Double.valueOf(rate);
                            Double gstFeeBTC = Double.valueOf(gst) / Double.valueOf(rate);


                            double amount_withdrawn = Double.parseDouble(jsonObject.getString("total_btc").trim());
                            BigDecimal a = new BigDecimal(amount_withdrawn);

                            tvnetworkfeesBTC.setText(String.format("%.8f", networkFeeBTC) + " Ace");
                            tvGstBTC.setText(String.format("%.8f", gstFeeBTC) + " Ace");
                            Double totAmtBTC = amount_withdrawn + networkFeeBTC + gstFeeBTC;
                            tvtotle.setText(String.format("%.8f", totAmtBTC) + " Ace");
                            tvmodel.setText(jsonObject.getString("device_model") + "/" +
                                    jsonObject.getString("device_brand"));
                            tvmacaddress.setText(jsonObject.getString("mac_address"));
                            // tvIp.setText(jsonObject.getString("device_model")+"/"+jsonObject.getString("device_brand"));
                            if (!jsonObject.isNull("network") || jsonObject.getString("network").
                                    equalsIgnoreCase("")) {

                                tvNetworkOper.setText(jsonObject.getString("network"));
                            } else {
                                tvNetworkOper.setText("N/A");
                            }
                            totle_amount.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(calcul)
                                    +" " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvInrNetwork.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(gst)+" "
                                    + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvnetworlkfees.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : "
                                    + formatter.format(tax)+" "
                                    + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvTo.setText(PreferenceFile.getInstance().getPreferenceData(Transaction_details.this,Constant.Currency_Code)+" : " +
                                    formatter.format(calcul - fee)+" "
                                    + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvFrom.setText("BTC : " + String.format("%.8f", amount_withdrawn) + "Ace ");

                        }

                        if (btc.equals("1")) {
                            tvtransactionType.setText("Acecoins transfer");
                        }
                        if (btc.equals("2")) {
                            tvtransactionType.setText("Bidding for sell Acecoins");
                        }
                        if (btc.equals("3")) {
                            tvtransactionType.setText("Bidding for buy Acecoins");
                        }
                        if (btc.equals("4")) {
                            tvtransactionType.setText("Sell Acecoins");
                        }
                        if (btc.equals("5")) {
                            tvtransactionType.setText("Buy Acecoins");
                        }
                        if (btc.equals("6") || btc.equals("14")) {
                            tvtransactionType.setText("INR Wallet Transfer");
                        }
                        if (btc.equals("7")) {
                            tvtransactionType.setText("INR Wallet Deposit");
                        }
                        if (btc.equals("8")) {
                            tvtransactionType.setText("INR Wallet Withdraw");
                        }
                        if (btc.equals("9")) {
                            tvtransactionType.setText("INR Wallet Withdraw");
                        }
                        if (btc.equals("10")) {
                            tvtransactionType.setText("INR Wallet Deposit(PayU)");
                        }
                        if (btc.equals("11")) {
                            tvtransactionType.setText("INR Wallet Deposit(PayPay)");
                        }
                        if (btc.equals("12")) {
                            tvtransactionType.setText("Acecoins Receive");
                        }
                        if (btc.equals("15")) {
                            tvtransactionType.setText("Acecoins transfer Third Party");
                        }
                        String timeCreated = jsonObject.getString("created");

                        /*DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                        try {
                            date = dateFormat.parse(timeCreated);
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS"); //If you need time just put specific format for time like 'HH:mm:ss'
                            String dateStr = formatter.format(date);
                            Log.e("datetime", "" + dateStr);
                            // String timess = dateStr.substring(19, 22);
                          // tvtransactiondatetime.setText(dateStr);
                           tvtransactiondatetime.setText(timeCreated);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/

                        tvtransactiondatetime.setText(timeCreated);


                    } else {

                        Constant.alertDialog(this, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
        }


    }
}