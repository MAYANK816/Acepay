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

public class BuyBitcoin extends ToolabarActivity implements View.OnClickListener,RetrofitResponse {

    ImageView ivarrow,ivINRToBct,ivBctToINR;
    EditText edIfsc,edBits;
    TextView txcalculate,txNext,txRs,txrs,txRupees;
    Double charges = 0.00;
    Double price = 0.00;
    Double total = 0.00;
    Double min = 0.00, max = 0.00;
    Double gst = 0.00;
    LinearLayout LnBitcoin,lnRupees,lnIcons,lnBitlayer,lnrefresh;
    String finacal,buyrate,buysendrate;
    boolean doubleBackToExitPressedOnce = false;
    NumberFormat formatter;
    DecimalFormat formatter1;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    Typeface tfArchitectsDaughter;
    TextView txName,txBitcoin,tvSellrate,currentsymbol,tvwallet,tvBitcoin,tvINR,tvnote,txmaxmin,tvphone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_bitcoin);

//        ivarrow= (ImageView) findViewById(R.id.ivarrow);

        ivINRToBct= (ImageView) findViewById(R.id.ivINRToBct);
        ivBctToINR= (ImageView) findViewById(R.id.ivBctToINR);

        txName= (TextView) findViewById(R.id.txName);
        txRupees= (TextView) findViewById(R.id.txRupees);
        LnBitcoin= (LinearLayout) findViewById(R.id.LnBitcoin);
        lnRupees= (LinearLayout) findViewById(R.id.lnRupees);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        txmaxmin= (TextView) findViewById(R.id.txmaxmin);
        tvnote= (TextView) findViewById(R.id.tvnote);
        tvSellrate= (TextView) findViewById(R.id.tvSellrate);
        tvphone= (TextView) findViewById(R.id.tvphone);
        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        tvINR = (TextView) findViewById(R.id.tvINR);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        txNext = (TextView) findViewById(R.id.txNext);
        txcalculate = (TextView) findViewById(R.id.txcalculate);
        edIfsc = (EditText) findViewById(R.id.edIfsc);
        edBits = (EditText) findViewById(R.id.edBits);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);


        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(BuyBitcoin.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        formatter1 = new DecimalFormat("#0.00");

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        tvphone.setText("For higher amounts, call 1800-000-000");
        //Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        // Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        ivBctToINR.setOnClickListener(this);
        ivINRToBct.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        txBitcoin.setOnClickListener(this);

        txNext.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Buy bitcoin");

        txRupees.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        edIfsc.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        callService();
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

//        txRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//        txrs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

        edBits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edBits.getText().toString().length() > 0) {

                    if ((!edBits.getText().toString().equals("."))) {

                        if ((!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.BTC_amount).equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.BTC_amount).equals(""))) { // todo later on Inr_Amount told by priya

                            for (int x = 0; x < list.size(); x++) {
                                buysendrate = String.valueOf(price); //Todo current price save
                                double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();

                                Double newfinal = Double.parseDouble(finacal);

                                if (newfinal >= Double.parseDouble(list.get(x).getFrom().trim()) &&
                                        newfinal <= Double.parseDouble(list.get(x).getTo().trim())) {

                                    charges = Double.parseDouble(list.get(x).getFees());
                                    gst = Double.parseDouble(list.get(x).getGst());
                                    total = ((charges * gst) / 100) + charges;
                                    gst = (charges * gst) / 100;
                                    Log.e("BUY16", gst + " changes " + charges + " total " + total);
                                    txRupees.setText(String.format("%.2f", newfinal));
//                                    txRupees.setText(String.format("%.0f", newfinal+total));
                                    break;
                                } else if (newfinal >= Double.parseDouble(list.get(list.size() - 1).getTo().trim())) {
                                    charges = Double.parseDouble(list.get(list.size() - 1).getFees());
                                    gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                    total = ((charges * gst) / 100) + charges;
                                    gst = (charges * gst) / 100;
                                    Log.e("BUY17", gst + " changes " + charges + " total " + total);
//                                    txRupees.setText(String.format("%.0f", newfinal+total));
                                    txRupees.setText(String.format("%.2f", newfinal));
                                    break;

                                } else {

                                    txRupees.setText(String.format("%.2f", newfinal));

                                }
                            }

                            if (list.size() < 0) {

                                buysendrate = String.valueOf(price);
                                double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                //  txRupees.setText(finacal);
                                Double newfinal = Double.parseDouble(finacal);
                                txRupees.setText(String.format("%.2f", newfinal));
                            }

                        } else {
                            Constant.alertDialog(BuyBitcoin.this, "Your account has no amount. ");
                        }
                    }

                } else {
                    txRupees.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edIfsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edIfsc.getText().toString().length() > 0) {

                    if ((!edIfsc.getText().toString().equals(".")) || (!edIfsc.getText().toString().equals(""))) {

                        if ((!PreferenceFile.getInstance().
                                getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount).

                                equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount).equals(""))) {

                            for (int x = 0; x < list.size(); x++)
                            {
                                Log.e("ListFrom ",list.get(x).getFrom().trim());
                                Log.e("Listto ",list.get(x).getTo().trim());
                                Log.e("gstt ",String.valueOf(Double.parseDouble(list.get(x).getGst())));
                                Log.e("chargesss ",String.valueOf(Double.parseDouble(list.get(x).getFees())));
                                Log.e("edIfscTextvalue ", String.valueOf(Double.parseDouble(edIfsc.getText().toString())));

                                if (Double.parseDouble(edIfsc.getText().toString()) >=
                                        Double.parseDouble(list.get(x).getFrom().trim()) &&
                                        Double.parseDouble(edIfsc.getText().toString()) <=
                                                Double.parseDouble(list.get(x).getTo().trim()))
                                {

                                    Log.e("insideIf ","ifffff ");
                                    buysendrate = String.valueOf(price);
                                    charges = Double.parseDouble(list.get(x).getFees());
                                    gst = Double.parseDouble(list.get(x).getGst());
                                    Log.e("gstBefore ", gst.toString());

                                    total = ((charges * gst) / 100) + charges;
                                    gst = (charges * gst) / 100;

                                    Log.e("gstAfter ",gst.toString());

                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
                                    finacal = BigDecimal.valueOf(calcul).toPlainString();
                                    txcalculate.setText(String.format("%.8f", calcul));

                                    break;

                                }
                                else if (Double.parseDouble(edIfsc.getText().toString()) >= Double.parseDouble(list.get(list.size() - 1).getTo().trim()))
                                {
                                    Log.e("elseIff "," ElseIff");
                                    buysendrate = String.valueOf(price);
                                    charges = Double.parseDouble(list.get(list.size() - 1).getFees());
                                    gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                    total = ((charges * gst) / 100) + charges;

                                    gst = (charges * gst) / 100;

                                    Log.e("BUY22", gst + " changes " + charges + " total " + total);
                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
//                                    double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                    finacal = BigDecimal.valueOf(calcul).toPlainString();
                                    Log.e("BUY23", calcul + " " + finacal + ' ' + price);
                                    txcalculate.setText(String.format("%.8f", calcul));

                                    break;
                                }

                                else {
                                    Log.e("elseeeee "," elseeee");

                                    buysendrate = String.valueOf(price);
                                    Log.e("BUY24", price + " " + Double.parseDouble(edIfsc.getText().toString()));
                                    double calcul = (Double.parseDouble(edIfsc.getText().toString()) / price);
                                    finacal = BigDecimal.valueOf(calcul).toPlainString();
                                    Log.e("BUY25", calcul + " " + finacal + ' ' + price);
                                    txcalculate.setText(String.format("%.8f", calcul));

                                    charges = Double.parseDouble(list.get(x).getFees());
                                    gst = Double.parseDouble(list.get(x).getGst());
                                    Log.e("gstBeforeElseee ", gst.toString());

                                    total = ((charges * gst) / 100) + charges;
                                    gst = (charges * gst) / 100;

                                    Log.e("gstAfterElseee ",gst.toString());

                                }
                            }
                            if (list.size() < 0) {

                                buysendrate = String.valueOf(price);
                                Log.e("BUY26", price + " " + Double.parseDouble(edIfsc.getText().toString()));
                                double calcul = (Double.parseDouble(edIfsc.getText().toString()) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("BUY27", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));
                            }

                        } else {
                            Constant.alertDialog(BuyBitcoin.this, "Your Account has no amount.");
                        }
                    }
                }
                else
                {
                    txcalculate.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Log.e("bitcoin--->",finacal+" bit  "+bit);
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", bit));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        // tvAvlRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+" ");

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private void callnewservice(){

        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once");
            new Retrofit2(this, BuyBitcoin.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"2").callService(false);
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

            Double buyprice = Double.parseDouble(buy);
            price = buyprice;

            Double sellprice = Double.parseDouble(sell);
            int sellprices = sellprice.intValue();

            price = Double.parseDouble(buy);

            txBitcoin.setText(formatter.format(price) + " " + PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(sellprice) + " " + PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));

            buyrate = buy;

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            Double newBuy_rate = buy_rate;

            if (buy_rate > 0) {
                tvINR.setText(formatter.format(newBuy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));
//                tvINR.setText(String.format("%.2f",newBuy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));
            } else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));
            }


            //TODO NEW WAY >>>>>>>>>>

            if (edBits.getText().toString().length() > 0) {

                if ((!edBits.getText().toString().equals("."))) {

                    if ((!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.BTC_amount).equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.BTC_amount).equals(""))) { // todo later on Inr_Amount told by priya

                        for (int x = 0; x < list.size(); x++) {
                            buysendrate = String.valueOf(price); //Todo current price save
                            double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            Log.e("BUY1", calcul + " " + finacal + ' ' + price);
                            //  txRupees.setText(finacal);
                            Double newfinal = Double.parseDouble(finacal);

                            if (newfinal >= Double.parseDouble(list.get(x).getFrom().trim()) &&
                                    newfinal <= Double.parseDouble(list.get(x).getTo().trim())) {

                                charges = Double.parseDouble(list.get(x).getFees());
                                gst = Double.parseDouble(list.get(x).getGst());
                                total = ((charges * gst) / 100) + charges;
                                gst = (charges * gst) / 100;
                                Log.e("BUY2", gst + " changes " + charges + " total " + total);
                                txRupees.setText(String.format("%.2f", newfinal));
//                                txRupees.setText(String.format("%.0f", newfinal+total));
                                break;
                            } else if (newfinal >= Double.parseDouble(list.get(list.size() - 1).getTo().trim())) {
                                charges = Double.parseDouble(list.get(list.size() - 1).getFees());
                                gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                total = ((charges * gst) / 100) + charges;
                                gst = (charges * gst) / 100;
                                Log.e("BUY3", gst + " changes " + charges + " total " + total);
                                txRupees.setText(String.format("%.2f", newfinal));
//                                txRupees.setText(String.format("%.0f", newfinal+total));
                                break;
                            } else {

                                txRupees.setText(String.format("%.2f", newfinal));

                            }
                        }

                        if (list.size() < 0) {

                            buysendrate = String.valueOf(price);
                            Log.e("BUY4", price + " " + Double.parseDouble(edBits.getText().toString()));
                            double calcul = (Double.parseDouble(edBits.getText().toString()) * price);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            Log.e("BUY5", calcul + " " + finacal + ' ' + price);
                            //  txRupees.setText(finacal);
                            Double newfinal = Double.parseDouble(finacal);
                            txRupees.setText(String.format("%.2f", newfinal));
                        }

                    } else {
                        Constant.alertDialog(BuyBitcoin.this, "Your account has no amount ");
                    }
                }

            } else {
                txRupees.setText("0.00");
            }


            if (edIfsc.getText().toString().length() > 0) {

                if ((!edIfsc.getText().toString().equals(".")) || (!edIfsc.getText().toString().equals(""))) {

                    if ((!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount).equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount).equals(""))) {

                        for (int x = 0; x < list.size(); x++) {
                            if (Double.parseDouble(edIfsc.getText().toString()) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(edIfsc.getText().toString()) <= Double.parseDouble(list.get(x).getTo().trim())) {

                                buysendrate = String.valueOf(price);
                                charges = Double.parseDouble(list.get(x).getFees());
                                gst = Double.parseDouble(list.get(x).getGst());
                                total = ((charges * gst) / 100) + charges;

                                gst = (charges * gst) / 100;

                                Log.e("BUYY6", gst + " changes " + charges + " total " + total);
//                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("calculaif--->", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));

                                break;
                            } else if (Double.parseDouble(edIfsc.getText().toString()) >= Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {

                                buysendrate = String.valueOf(price);
                                charges = Double.parseDouble(list.get(list.size() - 1).getFees());
                                gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                total = ((charges * gst) / 100) + charges;

                                gst = (charges * gst) / 100;

                                Log.e("BUYY7", gst + " changes " + charges + " total " + total);
                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())) / price);
//                                double calcul = ((Double.parseDouble(edIfsc.getText().toString())-total) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("BUYY8", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));
                                break;
                            } else {
                                buysendrate = String.valueOf(price);
                                Log.e("BUYY8", price + " " + Double.parseDouble(edIfsc.getText().toString()));
                                double calcul = (Double.parseDouble(edIfsc.getText().toString()) / price);
                                finacal = BigDecimal.valueOf(calcul).toPlainString();
                                Log.e("BUYY10", calcul + " " + finacal + ' ' + price);
                                txcalculate.setText(String.format("%.8f", calcul));

                            }
                        }
                        if (list.size() < 0) {

                            buysendrate = String.valueOf(price);
                            Log.e("BUYY11", price + " " + Double.parseDouble(edIfsc.getText().toString()));
                            double calcul = (Double.parseDouble(edIfsc.getText().toString()) / price);
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            Log.e("BUYY12", calcul + " " + finacal + ' ' + price);
                            txcalculate.setText(String.format("%.8f", calcul));
                        }

                    } else {
                        Constant.alertDialog(BuyBitcoin.this, "Your Account has no amount.");
                    }
                }
            } else {
                txcalculate.setText("0.00");
            }

        }
    };


    private void ManMinPrice() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BuyBitcoin.this, Constant.REQ_MIN_MAX,
                    Constant.MIN_MAX+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(false);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BuyBitcoin.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
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


    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BuyBitcoin.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
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

//            case R.id.ivarrow:
//
//                Constant.hideKeyboard(this,v);
//                finish();
//
//
//                break;
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




            case R.id.txNext:



                Log.e("gst ",gst+"");
                Log.e("charges ",charges+"");
                double value=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount))-
                        (gst+charges);
                Log.e("Valuee ",value+"");

                if(lnRupees.isShown())
                {
                    Log.e("lnRupees-->", "yes");

                    if ((!edIfsc.getText().toString().equals("")))
                    {
                        if ((!edIfsc.getText().toString().equals(".")))
                        {

                           /* if (!(Double.parseDouble(edIfsc.getText().toString()) >
                                    Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount)))) */

                            if (!(Double.parseDouble(edIfsc.getText().toString()) > value))
                            {
                                if (Double.parseDouble(edIfsc.getText().toString()) < max+1 && Double.parseDouble(edIfsc.getText().toString()) > min-1 ) {

                                    Log.e("GstValuee-->",gst.toString());


                                    Intent intent1 = new Intent(BuyBitcoin.this, CheckSecurePin.class);
                                    intent1.putExtra("key", "" +
                                            "");

                                    if (LnBitcoin.isShown())
                                    {
                                        Log.e("bitcoin-->", txRupees.getText().toString());
                                        intent1.putExtra("inr_amount", txRupees.getText().toString());
                                    }
                                    else
                                    {
                                        Log.e("rupees-->", edIfsc.getText().toString());
                                        intent1.putExtra("inr_amount", edIfsc.getText().toString());
                                    }


                                    intent1.putExtra("btc_amount", txcalculate.getText().toString());
                                    intent1.putExtra("buy_rate",buysendrate);
                                    intent1.putExtra("fee", String.valueOf(total));
                                    intent1.putExtra("gst", String.valueOf(gst));//gst
                                    intent1.putExtra("charge", String.valueOf(charges));//network
                                    startActivity(intent1);

                                    Log.e("value-->max ",max+" min-->"+min+" value "+edIfsc.getText().toString());
                                }
                                else {

                                    Constant.alertDialog(BuyBitcoin.this, "Please enter valid amount.");
                                }

                            } else {

                                Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                            }
                        } else {

                            Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                        }
                    }

                    else {

                        Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                    }
                }
                else
                {

                    Log.e("lnRupees-->", "No");

                    Log.e("testValeee  ",txRupees.getText().toString());

                    Log.e("dcdvfdvf ", String.valueOf(Double.parseDouble(txRupees.getText().toString())));

                    if ((!txRupees.getText().toString().equals(""))) {

                        if ((!txRupees.getText().toString().equals("."))){

                            Log.e("Valuee ", String.valueOf(Double.parseDouble(txRupees.getText().toString())));

                            Log.e("Nextt ",
                                    String.valueOf(Double.parseDouble(PreferenceFile.getInstance().
                                            getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount))));

                          /*  if (!(Double.parseDouble(txRupees.getText().toString()) >
                                    Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Inr_Amount))))*/


                                if (!(Double.parseDouble(txRupees.getText().toString()) > value))
                            {

                                if (Double.parseDouble(txRupees.getText().toString()) > min-1 &&
                                        Double.parseDouble(txRupees.getText().toString())< max+1){

                                    Log.e("GstValuee-->",gst.toString());

                                    Intent intent1 = new Intent(BuyBitcoin.this, CheckSecurePin.class);
                                    intent1.putExtra("key", "buy");

                                    if (LnBitcoin.isShown()) {

                                        Log.e("bitcoin-->", txRupees.getText().toString());
                                        intent1.putExtra("inr_amount", txRupees.getText().toString());
                                    }
                                    else {
                                        Log.e("rupees-->", edIfsc.getText().toString());
                                        intent1.putExtra("inr_amount", edIfsc.getText().toString());
                                    }

                                    intent1.putExtra("btc_amount", edBits.getText().toString());
                                    intent1.putExtra("buy_rate", buysendrate);
                                    intent1.putExtra("fee", String.valueOf(total));
                                    intent1.putExtra("gst", String.valueOf(gst));
                                    intent1.putExtra("charge", String.valueOf(charges));
                                    startActivity(intent1);
                                }
                                else {

                                    Constant.alertDialog(BuyBitcoin.this, "Please Enter valid amount.");
                                }

                            } else {

                                Log.e("amountvalid1 ","djjcd");
                                Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                            }
                        } else {
                            Log.e("insuffi1 ","djjcd");
                            Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                        }
                    } else {
                        Log.e("insuffi2 ","djjcd");
                        Constant.alertDialog(BuyBitcoin.this, getString(R.string.amount_validation));
                    }
                }
                break;
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


                break;
            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {

                        Log.e("BUY", "REQ_Dashboard_Refresh");

                        JSONObject result1 = new JSONObject(response.body().string());

                        Log.e("BUY", "REQ_Dashboard_Refresh" + result1.toString());

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
                                    PreferenceFile.getInstance().getPreferenceData(BuyBitcoin.this, Constant.Currency_Symbol));

                            if (bit > 0) {
                                tvBitcoin.setText(String.format("%.8f", bit));
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
                            Constant.alertWithIntent(this, "Account Blocked.", BlockScreen.class);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
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

                            if (CountryObj.getString("type").equals("3")) {

                                Double min1 = Double.parseDouble(CountryObj.getString("min"));
                                Double max1 = Double.parseDouble(CountryObj.getString("max"));

                                min = Double.valueOf(formatter1.format(Double.valueOf(min1)));
                                max = Double.valueOf(formatter1.format(Double.valueOf(max1)));

                                Log.e("REQ_MIN_MAX", "onServiceResponse:" + min + " " + max);

                                if (CountryObj.has("note")) {

                                    tvnote.setText(CountryObj.getString("note"));
                                }

                                txmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this,
                                                Constant.Currency_Symbol) + " max: " +
                                        formatter.format(Double.valueOf(max)) + " "
                                        + PreferenceFile.getInstance().
                                        getPreferenceData(this, Constant.Currency_Symbol));
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

            case Constant.REQ_BTC_RATE:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result", result.toString());
                        callnewservice();
                        String status = result.getString("response");
                        String message = result.getString("message");
                        ManMinPrice();
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            Double buyprice = Double.parseDouble(data.getString("buy"));
                            price = buyprice;
//                            price=buyprice;

                            buysendrate = String.valueOf(price);

                            Double sellprices = Double.parseDouble(data.getString("sell"));
//                            int newsellprice=sellprices;
//                            Double newsellprice=sellprices;

                            txBitcoin.setText(formatter.format(price) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(sellprices) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            buyrate = data.getString("buy");

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));
                            Double newBuy_rate = buy_rate;

                            if (buy_rate > 0) {
                                tvINR.setText(formatter.format(newBuy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                        }

                        newRefereshing();
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