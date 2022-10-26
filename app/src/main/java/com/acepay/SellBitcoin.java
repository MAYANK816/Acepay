package com.acepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class SellBitcoin extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow,ivINRToBct,ivBctToINR;
    EditText edIfsc,edBits;
    TextView txcalculate,txNext,txRupees,tvSellAll;
    Double price=0.00;
    Double charges=0.00;
    Double finacals,bit;
    Double gst=0.00;
    Double min=0.00,max=0.00;
    Double buy_rate;
    Double total=0.00;
    boolean doubleBackToExitPressedOnce = false;
    String finacal,sellrate;
    boolean sellallflag;
    NumberFormat formatter;
    DecimalFormat formatter1;


    ArrayList<FeesChargeModet> list = new ArrayList<>();
    LinearLayout LnBitcoin,lnRupees,lnIcons,lnBitlayer,lnrefresh;
    Typeface tfArchitectsDaughter;
    TextView txName,txBitcoin,tvwallet,tvBitcoin,tvINR,tvSellrate,tvphone,tvnote,txmaxmin,txRs,currentsymbol,txrs;

//    txRs,currentsymbol,ivarrow,txrs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_bitcoin);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
       // txName= (TextView) findViewById(R.id.txName);
        sellallflag=false;
        txRupees= (TextView) findViewById(R.id.txRupees);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        tvSellAll= (TextView) findViewById(R.id.tvSellAll);
        tvSellrate= (TextView) findViewById(R.id.tvSellrate);
//        txRs= (TextView) findViewById(R.id.txRs);

        ivINRToBct= (ImageView) findViewById(R.id.ivINRToBct);
        ivBctToINR= (ImageView) findViewById(R.id.ivBctToINR);


//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        tvphone= (TextView) findViewById(R.id.tvphone);
        txmaxmin= (TextView) findViewById(R.id.txmaxmin);
        tvnote= (TextView) findViewById(R.id.tvnote);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        tvINR = (TextView) findViewById(R.id.tvINR);
        txNext = (TextView) findViewById(R.id.txNext);
//        txrs = (TextView) findViewById(R.id.txrs);
        edBits = (EditText) findViewById(R.id.edBits);
        txcalculate = (TextView) findViewById(R.id.txcalculate);
        edIfsc = (EditText) findViewById(R.id.edIfsc);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);
        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(SellBitcoin.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        formatter1 = new DecimalFormat("#0.00");


        LnBitcoin= (LinearLayout) findViewById(R.id.LnBitcoin);
        lnRupees= (LinearLayout) findViewById(R.id.lnRupees);

//        txrs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
        ivBctToINR.setOnClickListener(this);
        ivINRToBct.setOnClickListener(this);
        tvSellAll.setOnClickListener(this);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

//        txRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

        // txmaxmin.setText("(min: 1,000 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+", "+"max: 15,00,000 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+")");
        tvphone.setText("For higher amounts, call 1800-000-000");

        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        finacals =bit;

        Log.e("bitcoin--->",finacals+"");
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");
        txRupees.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        edIfsc.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        if(bit>0) {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else {
            tvBitcoin.setText("0.00000000");
        }

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

//        ivarrow.setOnClickListener(this);
        txBitcoin.setOnClickListener(this);
        txNext.setOnClickListener(this);
      //  txName.setVisibility(View.VISIBLE);

      //  txName.setText("Sell bitcoin");
        callService();

        edBits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(edBits.getText().toString().length()>0) {

                    if((!edBits.getText().toString().equals("."))){

                        if ((!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals("null"))
                                || (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals(""))) {

                            for (int x=0; x < list.size(); x++)
                            {
                                Log.e("price-->", price + " " + Double.parseDouble(edBits.getText().toString()));
                                double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("calcula1--->", calcul + " " + finacal + ' ' + price);

                                Double newfinal = Double.parseDouble(finacal.trim());

                                if(newfinal >= Double.parseDouble(list.get(x).getFrom().trim()) &&
                                        newfinal <= Double.parseDouble(list.get(x).getTo().trim())){

                                    charges=Double.parseDouble(list.get(x).getFees());
                                    gst=Double.parseDouble(list.get(x).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;
                                    Log.e("gst",gst+" changes "+charges+" total "+total);
//                                    txRupees.setText(String.format("%.0f", newfinal-total));// previously this was added  newfinal-total
                                    txRupees.setText(String.format("%.2f", newfinal));
                                    Log.e("calcula2--->", calcul + " " + finacal + ' ' + price+" "+newfinal);

                                    break;

                                }
                                else if(newfinal >= Double.parseDouble(list.get(list.size()-1).getFrom().trim())){

                                    charges=Double.parseDouble(list.get(list.size()-1).getFees());
                                    gst=Double.parseDouble(list.get(list.size()-1).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;
                                    Log.e("gst",gst+" changes "+charges+" total "+total);
                                    txRupees.setText(String.format("%.2f", newfinal));// previously this was added  newfinal-total
//                                txRupees.setText(String.format("%.0f", newfinal-total));// previously this was added  newfinal-total
                                    Log.e("calcula3--->", calcul + " " + finacal + ' ' + price+" "+newfinal);

                                    break;

                                }
                                else {

                                    txRupees.setText(String.format("%.2f", newfinal));
                                }

                            }

                            if(list.size()<0) {
                                Log.e("price-->", price + " " + Double.parseDouble(edBits.getText().toString()));
                                double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();

                                //txRupees.setText(finacal);
                                Double newfinal = Double.parseDouble(finacal.trim());
                                Log.e("calcula4--->", calcul + " " + finacal + ' ' + price+" "+newfinal);
                                txRupees.setText(String.format("%.2f", newfinal));
                            }

                        } else
                        {
                            Constant.alertDialog(SellBitcoin.this, "Your Account has no Amount ");
                        }
                    }

                }else {
                    txRupees.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edIfsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

                if(edIfsc.getText().toString().length()>0) {

                    if((!edIfsc.getText().toString().equals(".")) || (!edIfsc.getText().toString().equals(""))) {

                        if (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals(null) ||
                                (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals("")))
                        {

                            for (int x=0;x<list.size();x++)
                            {
                                if(Double.parseDouble(edIfsc.getText().toString())
                                        >= Double.parseDouble(list.get(x).getFrom().trim())
                                        && Double.parseDouble(edIfsc.getText().toString())
                                        <= Double.parseDouble(list.get(x).getTo().trim())){

                                    charges=Double.parseDouble(list.get(x).getFees());
                                    gst=Double.parseDouble(list.get(x).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;

                                    Log.e("gst",gst+" changes "+charges+" total "+total);
//                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
                                    finacal = BigDecimal.valueOf(calcul).toPlainString();
                                    Log.e("calcula9--->", calcul + " " + finacal + ' ' + price);
                                    txcalculate.setText(String.format("%.8f", calcul));
                                    break;

                                }

                                else if(Double.parseDouble(edIfsc.getText().toString()) >=
                                        Double.parseDouble(list.get(list.size()-1).getFrom().trim())){
                                    charges=Double.parseDouble(list.get(list.size()-1).getFees());
                                    gst=Double.parseDouble(list.get(list.size()-1).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;

                                    Log.e("gst",gst+" changes "+charges+" total "+total);
                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
//                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                    finacal = BigDecimal.valueOf(calcul).toPlainString();
                                    Log.e("calcula5--->", calcul + " " + finacal + ' ' + price);
                                    txcalculate.setText(String.format("%.8f", calcul));
                                    break;

                                }
                                else {
                                    double calcul = Double.parseDouble(edIfsc.getText().toString()) / price;

                                    BigDecimal d = new BigDecimal(calcul);
                                    finacal = String.valueOf(d);

                                    // txcalculate.setText(String.format("%.8f", d));
                                    // txcalculate.setText(finacal);
                                    txcalculate.setText(String.format("%.8f", calcul));

                                    charges=Double.parseDouble(list.get(x).getFees());
                                    gst=Double.parseDouble(list.get(x).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;

                                }
                            }

                            if(list.size()<0) {


                                double calcul = Double.parseDouble(edIfsc.getText().toString()) / price;

                                BigDecimal d = new BigDecimal(calcul);
                                finacal = String.valueOf(d);

                                // txcalculate.setText(String.format("%.8f", d));
                                // txcalculate.setText(finacal);
                                txcalculate.setText(String.format("%.8f", calcul));
                            }
                        }
                    }

                }else {

                    txcalculate.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private void callnewservice(){

        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once");
            new Retrofit2(this, SellBitcoin.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"1").callService(false);
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

            Double buyprice=Double.parseDouble(sell);
            price=buyprice;

            Double sellprice=Double.parseDouble(buy);


            txBitcoin.setText(formatter.format(sellprice)+" "+PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this,Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(price)+" "+PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this,Constant.Currency_Symbol));

            sellrate=sell;

            buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this,Constant.BTC_amount))*Double.parseDouble(buy);

            Double newrate=buy_rate;

            if(buy_rate>0) {
                if(tvBitcoin.getText().toString().trim().equalsIgnoreCase("1.00000000")){
                    tvINR.setText(formatter.format(price) + " " +
                            PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.Currency_Symbol));
                }else{
                    tvINR.setText(formatter.format(newrate) + " " +
                            PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.Currency_Symbol));
                }
            }
            else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.Currency_Symbol));
            }


            //TODO BY CHANGING PRICE OF BUY N SELL
            if(edBits.getText().toString().length()>0) {

                if((!edBits.getText().toString().equals("."))){

                    if ((!PreferenceFile.getInstance().getPreferenceData
                            (SellBitcoin.this, Constant.BTC_amount).equals("null")) ||
                            (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals(""))) {

                        for (int x=0; x < list.size(); x++)
                        {
                            Log.e("price-->", price + " " + Double.parseDouble(edBits.getText().toString()));
                            double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            Log.e("calcula6--->", calcul + " " + finacal + ' ' + price);
                            //txRupees.setText(finacal);

                            Double newfinal = Double.parseDouble(finacal.trim());

                            if(newfinal >= Double.parseDouble(list.get(x).getFrom().trim()) &&
                                    newfinal <= Double.parseDouble(list.get(x).getTo().trim())){

                                charges=Double.parseDouble(list.get(x).getFees());
                                gst=Double.parseDouble(list.get(x).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("gst",gst+" changes "+charges+" total "+total);
                                txRupees.setText(String.format("%.2f", newfinal));
//                                txRupees.setText(String.format("%.0f", newfinal+total));
                                break;
                            }

                            else if(newfinal >= Double.parseDouble(list.get(list.size()-1).getFrom().trim())){
                                charges=Double.parseDouble(list.get(list.size()-1).getFees());
                                gst=Double.parseDouble(list.get(list.size()-1).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("gst",gst+" changes "+charges+" total "+total);
                                txRupees.setText(String.format("%.2f", newfinal));
//                                txRupees.setText(String.format("%.0f", newfinal+total));
                                break;

                            }
                            else {
                                charges=0.00;
                                gst=0.00;
                                total=0.00;
                                gst=0.00;
                                txRupees.setText(String.format("%.2f", newfinal));
                            }

                        }

                        if(list.size()<0) {

                            charges=0.00;
                            gst=0.00;
                            total=0.00;
                            gst=0.00;

                            Log.e("price-->", price + " " + Double.parseDouble(edBits.getText().toString()));
                            double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            Double newfinal = Double.parseDouble(finacal.trim());
                            txRupees.setText(String.format("%.2f", newfinal));
                        }

                    } else
                    {
                        Constant.alertDialog(SellBitcoin.this, "Your Account has no Amount ");
                    }
                }

            }else {
                txRupees.setText("0.00");
            }


            if(edIfsc.getText().toString().length()>0) {

                if((!edIfsc.getText().toString().equals(".")) || (!edIfsc.getText().toString().equals(""))) {

                    if (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals(null) || (!PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount).equals(""))) {

                        for (int x=0;x<list.size();x++)
                        {
                            if(Double.parseDouble(edIfsc.getText().toString()) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(edIfsc.getText().toString()) <= Double.parseDouble(list.get(x).getTo().trim())){

                                charges=Double.parseDouble(list.get(x).getFees());
                                gst=Double.parseDouble(list.get(x).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;

                                Log.e("gst",gst+" changes "+charges+" total "+total);
                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
//                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("calcula10--->", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));
                                break;
                            }

                            else if((Double.parseDouble(edIfsc.getText().toString()) >= Double.parseDouble(list.get(list.size()-1).getFrom().trim()))){

                                charges=Double.parseDouble(list.get(list.size()-1).getFees());
                                gst=Double.parseDouble(list.get(list.size()-1).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;

                                Log.e("gst",gst+" changes "+charges+" total "+total);
                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
//                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("calcula8--->", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));
                                break;

                            }
                            // dfjdgfjidgf
                            else {
                                double calcul = Double.parseDouble(edIfsc.getText().toString()) / price;

                                BigDecimal d = new BigDecimal(calcul);
                                finacal = String.valueOf(d);
                                charges=0.00;
                                gst=0.00;
                                total=0.00;
                                gst=0.00;
                                // txcalculate.setText(String.format("%.8f", d));
                                // txcalculate.setText(finacal);
                                txcalculate.setText(String.format("%.8f", calcul));

                            }


                        }

                        if(list.size()<0) {

                            Log.e("btc-->", finacals + "price " + price);
                            Log.e("rupee-->", edIfsc.getText().toString());
                            charges=0.00;
                            gst=0.00;
                            total=0.00;
                            gst=0.00;
                            double calcul = Double.parseDouble(edIfsc.getText().toString()) / price;

                            BigDecimal d = new BigDecimal(calcul);
                            finacal = String.valueOf(d);

                            // txcalculate.setText(String.format("%.8f", d));
                            // txcalculate.setText(finacal);
                            txcalculate.setText(String.format("%.8f", calcul));
                        }
                    }
                }

            }else {

                txcalculate.setText("0.00");
            }

        }
    };


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
    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, SellBitcoin.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, SellBitcoin.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

            case R.id.lnrefresh:

                newRefereshing();

                break;


            case R.id.ivINRToBct:
                Log.e("imr","ivINRToBct");

                edBits.setText("");
                edIfsc.setText("");
                txcalculate.setText("");
                txcalculate.setHint("0.0 Ace");
                txRupees.setText("");
                txRupees.setHint("0 "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));


                if(lnRupees.isShown()){
                    lnRupees.setVisibility(View.GONE);
                    LnBitcoin.setVisibility(View.VISIBLE);
                }
                else {
                    LnBitcoin.setVisibility(View.GONE);
                    lnRupees.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.ivBctToINR:

                Log.e("imr","ivBctToINR");
                edBits.setText("");
                edIfsc.setText("");
                txcalculate.setText("");
                txcalculate.setHint("0.0 Ace");
                txRupees.setText("0 ");
                txRupees.setHint("0 "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));


                if(LnBitcoin.isShown()){
                    LnBitcoin.setVisibility(View.GONE);
                    lnRupees.setVisibility(View.VISIBLE);
                }
                else {
                    LnBitcoin.setVisibility(View.VISIBLE);
                    lnRupees.setVisibility(View.GONE);
                }

                break;




            case R.id.tvSellAll:

                try {
                    if (LnBitcoin.isShown()) {

                        if (bit > 0) {
                         /*   edBits.setText(String.format("%.8f", finacals));
                            String data=tvINR.getText().toString().trim().replaceAll
                                    (PreferenceFile.getInstance().getPreferenceData
                                                    (SellBitcoin.this, Constant.Currency_Symbol),
                                            "").replaceAll(",", "");
                            String data1=data.trim();
                            if(data1.startsWith("\\s")){
                                txRupees.setText(data1.replaceFirst("\\s",""));
                            }else{
                                txRupees.setText(data1.replaceFirst("\\s",""));
//&amp;emsp;
                            }*/

                            edBits.setText(String.format("%.8f", finacals));
                            Double sellprice = Double.parseDouble(sellrate);

                            Double newsell = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData
                                    (SellBitcoin.this, Constant.BTC_amount));

                            Double aDouble=sellprice*newsell;
                            txRupees.setText(String.format("%.2f", aDouble)+"");
                            sellallflag=true;

                        } else {
                            edBits.setText("0.00000000");
                            charges = 0.00;
                            gst = 0.00;
                            total = 0.00;
                            gst = 0.00;
                            txRupees.setText("0.00");
                        }
                    } else {

                        Double sellprice = Double.parseDouble(sellrate);

                        Double newsell = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData
                                (SellBitcoin.this, Constant.BTC_amount));

                        for (int x = 0; x < list.size(); x++) {

//                            double calcul = (Double.parseDouble(String.valueOf(newsell)) * price);
                            double calcul = (Double.parseDouble(String.valueOf(newsell)) * sellprice);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();

                            Double newfinal = Double.parseDouble(finacal);

                            if (calcul >= Double.parseDouble(list.get(x).getFrom().trim())
                                    && calcul <= Double.parseDouble(list.get(x).getTo().trim())) {

                                charges = Double.parseDouble(list.get(x).getFees());
                                gst = Double.parseDouble(list.get(x).getGst());
                                total = ((charges * gst) / 100) + charges;
                                gst = (charges * gst) / 100;

                           /*     String rr = tvINR.getText().toString().trim().replaceAll(
                                        PreferenceFile.getInstance().getPreferenceData
                                                (SellBitcoin.this, Constant.Currency_Symbol), "").
                                        replaceAll(",", "").replaceAll(" ","");

                                Double dd=null;

                                if(rr.startsWith("\\s")){
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
                                }else{
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
//&amp;emsp;
                                }


                                Double dd = Double.parseDouble(rr.toString().trim());

                                edIfsc.setText(String.format("%.2f", dd) + "");
*/

                                edIfsc.setText(String.format("%.2f", calcul));
                                break;

                            } else if (calcul >= Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {
                                charges = Double.parseDouble(list.get(list.size() - 1).getFees());
                                gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                total = ((charges * gst) / 100) + charges;
                                gst = (charges * gst) / 100;

                              /*  String rr = tvINR.getText().toString().trim().replaceAll(
                                        PreferenceFile.getInstance().getPreferenceData
                                                (SellBitcoin.this, Constant.Currency_Symbol), "").
                                        replaceAll(",", "").replaceAll(" ","");

                                Double dd=null;

                                if(rr.startsWith("\\s")){
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
                                }else{
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
//&amp;emsp;
                                }
//                                Double dd = Double.parseDouble(rr.toString().trim());

                                edIfsc.setText(String.format("%.2f", dd) + "");
*/

                                edIfsc.setText(String.format("%.2f", calcul));
                                break;
                            } else {

                                charges = 0.00;
                                gst = 0.00;
                                total = 0.00;
                                gst = 0.00;

                             /*   String rr = tvINR.getText().toString().trim().replaceAll(
                                        PreferenceFile.getInstance().getPreferenceData
                                                (SellBitcoin.this, Constant.Currency_Symbol), "").
                                        replaceAll(",", "").replaceAll(" ","");

                                Double dd=null;

                                if(rr.startsWith("\\s")){
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
                                }else{
                                    dd = Double.parseDouble(rr.replaceFirst("\\s",""));
//&amp;emsp;
                                }

//                                Double dd = Double.parseDouble(rr.toString().trim());

                                edIfsc.setText(String.format("%.2f", dd) + "");*/


                                edIfsc.setText(String.format("%.2f", calcul));
                            }

                        }


                        if (list.size() < 0) {

                            charges = 0.00;
                            gst = 0.00;
                            total = 0.00;
                            gst = 0.00;
                            double calcul = (Double.parseDouble(String.valueOf(newsell)) * price);

                          /*  String rr = tvINR.getText().toString().trim().replaceAll(
                                    PreferenceFile.getInstance().getPreferenceData
                                            (SellBitcoin.this, Constant.Currency_Symbol), "").
                                    replaceAll(",", "").replaceAll(" ","");

                            Double dd=null;

                            if(rr.startsWith("\\s")){
                                dd = Double.parseDouble(rr.replaceFirst("\\s",""));
                            }else{
                                dd = Double.parseDouble(rr.replaceFirst("\\s",""));
//&amp;emsp;
                            }

//                            Double dd = Double.parseDouble(rr.toString().trim());

                            edIfsc.setText(String.format("%.2f", dd) + "");
*/

//                        double calcul = (Double.parseDouble(String.valueOf(newsell)) * price);
                            edIfsc.setText(String.format("%.2f", calcul));
                        }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case R.id.txNext:

                Constant.hideKeyboard(this,v);

                Log.e("gst ",gst+"");
                Log.e("charges ",charges+"");
                Log.e("Amountt ",PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount));
                double value=0.0;
                if(sellallflag==true)
                {
                    value=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount));
                }
                else{
                    value=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.BTC_amount))-
                    (gst+charges);
                }
                Log.e("Valuee ",value+"");

                if(lnRupees.isShown()) {

                    if (!txcalculate.getText().toString().equals("")) {

                        if ((!txcalculate.getText().toString().equals("."))) {

//                            Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
                            Double bit=value;
                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                            Log.e("finacal ",finacal+"");
                            Log.e("txcalculate ",Double.parseDouble(txcalculate.getText().toString())+"");
                            Log.e("Nexttt ",Double.parseDouble(String.format("%.8f", finacal))+"");
                            Log.e("edIfsc ",Double.parseDouble(edIfsc.getText().toString())+"");
                            Log.e("min ",min+"");
                            Log.e("max ",max+"");

                                if (((Double.parseDouble(edIfsc.getText().toString())
                                        <= buy_rate)))
                            {
                                if (Double.parseDouble(edIfsc.getText().toString()) > 0)
                                {

                                    if(Double.parseDouble(edIfsc.getText().toString()) >= min &&
                                            Double.parseDouble(edIfsc.getText().toString()) < max) {
                                        String data= tvINR.getText().toString().trim().
                                                replaceAll(PreferenceFile.getInstance().getPreferenceData
                                                        (SellBitcoin.this, Constant.Currency_Symbol),"")
                                                .replaceAll(",","");

                                        Intent intent1 = new Intent(SellBitcoin.this, CheckSecurePin.class);
                                        intent1.putExtra("key", "sell");
                                        intent1.putExtra("type", "sell");
                                        if (sellallflag == true) {
                                            Double actualdata=Double.parseDouble(data)-(gst+charges);
                                            intent1.putExtra("data", String.valueOf(actualdata).trim().replaceAll("\\s+",
                                                    ""));
                                        } else {
                                            intent1.putExtra("data", data.trim().replaceAll("\\s+",
                                                    ""));
                                        }

                                        Log.e("SELL", "onClick:22??"+data.trim().replaceAll("\\s+",
                                                "")+ "DATA"+edIfsc.getText().toString().trim());

                                        Double TOTAMTBNI= Double.parseDouble(edIfsc.getText().toString().trim())+total;
                                        Double ACTUALAMT= Double.parseDouble(data.trim().replaceAll("\\s+", ""));

                                        if(TOTAMTBNI>=ACTUALAMT){
                                            intent1.putExtra("data_key","1");
                                        }else{
                                            intent1.putExtra("data_key","0");
                                        }

                                        intent1.putExtra("inr_amount", edIfsc.getText().toString());
                                        intent1.putExtra("btc_amount", txcalculate.getText().toString());
                                        intent1.putExtra("sell_rate", sellrate);
                                        intent1.putExtra("fee", String.valueOf(total));
                                        intent1.putExtra("gst", String.valueOf(gst));
                                        intent1.putExtra("charge", String.valueOf(charges));
                                        intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());
                                        startActivity(intent1);
                                    }
                                    else
                                    {
                                        Constant.alertDialog(SellBitcoin.this, "Please enter valid amount");

                                    }
                                }
                                else {
                                    Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                                }
                            } else {

                                Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                            }
                        }
                        else {
                            Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                        }
                    }
                    else {
                        Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                    }
                }
                else
                {
                    if (!edBits.getText().toString().equals(""))
                    {

                        if ((!edBits.getText().toString().equals(".")))
                        {

                            Double bit=value;

                           /* Double bit=Double.parseDouble(PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.BTC_amount));*/

                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                            if (((Double.parseDouble(edBits.getText().toString().trim())
                                    <= Double.parseDouble(String.format("%.8f", finacal))))){

                                Log.e("CRASH1", "onClick: "+
                                        txRupees.getText().toString().trim().replaceAll(" ","")+" "+
                                        min+" "+max);

//

                                Log.e("CRASH", "onClick: "+
                                        Double.parseDouble(txRupees.getText().toString().trim().replaceFirst("^ *", ""))+" "+
                                        min+" "+max);



                                if(Double.parseDouble(txRupees.getText().toString().trim().
                                        replaceAll(" ","")) > min-1
                                        && Double.parseDouble(txRupees.getText().
                                        toString().trim().replaceAll(" ",""))
                                        < max+1) {

                                    if (Double.parseDouble(edBits.getText().toString().trim()) > 0)
                                    {
                                        String data= tvINR.getText().toString().trim().
                                                replaceAll(PreferenceFile.getInstance().getPreferenceData
                                                        (SellBitcoin.this, Constant.Currency_Symbol),"")
                                                .replaceAll(",","");


                                        Intent intent1 = new Intent(SellBitcoin.this, CheckSecurePin.class);
                                        intent1.putExtra("key", "sell");
                                        intent1.putExtra("type", "sell");

                                        intent1.putExtra("data", data.trim().replaceAll("\\s+",
                                                ""));


                                        if(data.trim().replaceAll("\\s+", "").equalsIgnoreCase(txRupees.getText().toString().trim())){
                                            intent1.putExtra("data_key","1");
                                        }else{
                                            intent1.putExtra("data_key","0");
                                        }

                                        Log.e("SELL", "onClick:261"+data.trim().replaceAll("\\s+",
                                                "")+" "+txRupees.getText().toString().trim());

                                        intent1.putExtra("inr_amount", txRupees.getText().toString());
                                        intent1.putExtra("btc_amount", edBits.getText().toString());
                                        intent1.putExtra("sell_rate", sellrate);
                                        intent1.putExtra("fee", String.valueOf(total));
                                        intent1.putExtra("gst", String.valueOf(gst));
                                        intent1.putExtra("charge", String.valueOf(charges));
                                        intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());
                                        startActivity(intent1);
                                    }
                                    else {

                                        Constant.alertDialog(SellBitcoin.this,"Please enter valid amount.");
                                    }
                                }
                                else {

                                    Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                                }
                            } else {

                                Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                            }
                        } else {
                            Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                        }
                    } else {
                        Constant.alertDialog(SellBitcoin.this, getString(R.string.amount_validation));
                    }
                }
                break;
        }

    }

    private void ManMinPrice(){

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, SellBitcoin.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(false);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {


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




            case Constant.REQ_MIN_MAX:
                try
                {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");
                    callnewservice();


                    Log.e("response-->",result1.toString());
                    Log.e("status-->",status+" message-->"+message);

                    if(status.equals("true")) {

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);

                            if(CountryObj.getString("type").equals("4")){

                                Double min1=  Double.parseDouble(CountryObj.getString("min"));
                                Double  max1= Double.parseDouble(CountryObj.getString("max"));

                                min= Double.valueOf(formatter1.format(Double.valueOf(min1)));
                                max= Double.valueOf(formatter1.format(Double.valueOf(max1)));


                                if(CountryObj.has("note")){
                                    tvnote.setText(CountryObj.getString("note"));
                                }
                                txmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this,
                                                Constant.Currency_Symbol) + " max: " +
                                        formatter.format(Double.valueOf(max))  + " "
                                        + PreferenceFile.getInstance().getPreferenceData(this,
                                        Constant.Currency_Symbol));
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
                        JSONObject result2 = new JSONObject(response.body().string());
                        JSONObject result1 = new JSONObject(result2.toString());

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(SellBitcoin.this, Constant.Currency_Symbol));

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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;


            case Constant.REQ_BTC_RATE:


                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        callnewservice();


                        String status = result.getString("response");
                        String message = result.getString("message");

                        ManMinPrice();
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            Double buyprice=Double.parseDouble(data.getString("sell"));
                            price=buyprice;

                            Double sellprices=Double.parseDouble(data.getString("buy"));
//                          Double newsellprice=sellprices;
                            int newsellprice=sellprices.intValue();

                            txBitcoin.setText(formatter.format(sellprices)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(price)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            sellrate= data.getString("sell");

                            buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))
                                    *Double.parseDouble(data.getString("buy"));

//                           int newrate=buy_rate.intValue();

                            if(buy_rate>0) {

                                tvINR.setText(formatter.format(buy_rate) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                            else
                            {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                            newRefereshing();
                        } else
                        {
                            Constant.alertDialog(this, message);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;


            case Constant.REQ_SELL_BTC:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");
                            Log.e("data json", String.valueOf(data));
                            double inr = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))-Double.parseDouble(edIfsc.getText().toString());
                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,String.valueOf(inr));

                            Constant.alertWithIntent(this, message,Dashboard.class);

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

        }
    }
}