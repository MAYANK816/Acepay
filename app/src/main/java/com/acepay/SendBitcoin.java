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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Adapter.FeesAdapter;
import com.acepay.Model.AllUserModel;
import com.acepay.Model.CustomModel;
import com.acepay.Model.FeesChargeModet;
import com.acepay.Util.Constant;
import com.acepay.Util.DecimalDigitsInputFilter;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.zxing.integration.android.IntentIntegrator;


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

public class SendBitcoin extends ToolabarActivity implements View.OnClickListener,
        RetrofitResponse {

      ImageView ivINRToBct,ivBctToINR;
    TextView txName,tvSellAll,tvCharges, txNext,currentsymbol,tvwallet,tvnote,txmaxmin,tvphone,txBitcoin,tvBitcoin,tvSellrate,tvINR,txrs,txRs;
    EditText edIfsc,edBits;
    TextView txcalculate,txRupees;
    FeesAdapter feesAdapter;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    AutoCompleteTextView edNumber;
    private IntentIntegrator qrScan;
    public final static int QRcodeWidth = 500;
    ArrayList<CustomModel> customModels;
    double extra_charges = 0.00;
    Double price = 0.00;
    private LinearLayout LnBitcoin, lnRupees, lnIcons, lnBitlayer, lnrefresh;
    private NumberFormat formatter;
    private DecimalFormat  formatter1;
    private int flag = 1;
    private Double gst = 0.00, total = 0.00;
    private Double btc_charges = 0.00;
    private boolean doubleBackToExitPressedOnce = false;
    private String finacal, sellrate;
    String data2;
    Double newrate;
    Typeface tfArchitectsDaughter;
    String phn;
    Double finacals, bit, max, min, buy_rate = 0.00;
    ArrayList<AllUserModel> ArrayAlluserlist;
    ArrayList<String> AlluserListname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_bitcoin);
        tvSellAll= (TextView) findViewById(R.id.tvSellAll);

        txName = (TextView) findViewById(R.id.txName);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
        tvCharges = (TextView) findViewById(R.id.tvCharges);
        tvINR = (TextView) findViewById(R.id.tvINR);
        tvphone = (TextView) findViewById(R.id.tvphone);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
        txmaxmin = (TextView) findViewById(R.id.txmaxmin);
        tvnote = (TextView) findViewById(R.id.tvnote);
        txBitcoin = (TextView) findViewById(R.id.txBitcoin);
        txRupees= (TextView) findViewById(R.id.txRupees);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        ivINRToBct= (ImageView) findViewById(R.id.ivINRToBct);
        ivBctToINR= (ImageView) findViewById(R.id.ivBctToINR);


        ivINRToBct.setOnClickListener(this);
        ivBctToINR.setOnClickListener(this);
        tvSellAll.setOnClickListener(this);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);
//        edname = (EditText) findViewById(R.id.edname);
//        edAddress = (EditText) findViewById(R.id.edAddress);
        // imView = (ImageView) findViewById(R.id.imView);
        txNext = (TextView) findViewById(R.id.txNext);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(SendBitcoin.this,Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        formatter1 = new DecimalFormat("#0.00");

        //   txmaxmin.setText("(min: 1,000 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+", "+"max: 15,00,000 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+")");
        tvphone.setText("For higher amounts, call 1800-000-000");

        edBits = (EditText) findViewById(R.id.edBits);
        txcalculate = (TextView) findViewById(R.id.txcalculate);
        edIfsc = (EditText) findViewById(R.id.edIfsc);

        LnBitcoin= (LinearLayout) findViewById(R.id.LnBitcoin);
        lnRupees= (LinearLayout) findViewById(R.id.lnRupees);
        txRupees.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        edIfsc.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
//        txRs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        final Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

//        txrs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        ArrayAlluserlist=new ArrayList<>();
        AlluserListname=new ArrayList<>();
        //callService();
        callService();


        txNext.setOnClickListener(this);

        txName.setVisibility(View.VISIBLE);
        txName.setText("Send Acepay");
        customModels=new ArrayList<>();


        qrScan = new IntentIntegrator(this);

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        finacals =bit;

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }


        edBits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edBits.getText().toString().length() > 0) {

                    if ((!edBits.getText().toString().equals("."))) {

                        if ((!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this,
                                Constant.BTC_amount).equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(""))) {

                            Double charges = Double.parseDouble(edBits.getText().toString());
                            charges = Double.parseDouble(BigDecimal.valueOf(charges).toPlainString());

                            for (int x = 0; x < list.size(); x++) {

                                Double from = Double.parseDouble(list.get(x).getFrom());
                                Double newFrom = Double.parseDouble(BigDecimal.valueOf(from).toPlainString());

                                Double to = Double.parseDouble(list.get(x).getTo());
                                Double newto = Double.parseDouble(BigDecimal.valueOf(to).toPlainString());


                                if (Double.parseDouble(String.format("%.8f", charges)) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(String.format("%.8f", charges)) <= Double.parseDouble(list.get(x).getTo().trim())) {


                                    // tvCharges.setText("Fees "+list.get(x).getFees()+" Ace");
                                    extra_charges = Double.parseDouble(list.get(x).getFees());

                                    int extra_inr = (int) (extra_charges * price);

                                    extra_charges = extra_charges * price;

                                    gst = Double.parseDouble(list.get(x).getGst());
                                    total = ((extra_inr * gst) / 100) + extra_inr;
                                    gst = (extra_inr * gst) / 100;

                                    btc_charges = (double) total / price;
                                    tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

                                    break;
                                } else if (Double.parseDouble(String.format("%.8f", charges)) >= Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {

                                    extra_charges = Double.parseDouble(list.get(list.size() - 1).getFees());

                                    int extra_inr = (int) (extra_charges * price);

                                    extra_charges = extra_charges * price;

                                    gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                    total = ((extra_inr * gst) / 100) + extra_inr;
                                    gst = (extra_inr * gst) / 100;

                                    btc_charges = (double) total / price;
                                    tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

                                    break;
                                } else {
                                    extra_charges = 0.00;
                                    tvCharges.setText("");
                                    btc_charges = 0.00;
                                }
                            }
//                            double calcul = ((Double.parseDouble(edBits.getText().toString())+btc_charges) * price);//before minus
                            double calcul = ((Double.parseDouble(edBits.getText().toString())) * price);//before minus
                            finacal = BigDecimal.valueOf(calcul).toPlainString();
                            // txRupees.setText(finacal);
                            Double newfinal = Double.parseDouble(finacal);
                            txRupees.setText(String.format("%.2f", calcul));

                        } else {
                            Constant.alertDialog(SendBitcoin.this, "Your Account has no Amount ");
                        }
                    }

                } else {
                    extra_charges = 0.00;
                    tvCharges.setText("");
                    txcalculate.setText("0.00");
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

                    if ((!edIfsc.getText().toString().equals("."))) {

                        if (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(null) || (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(""))) {


                            try {

                                //todo by me

                                String data=edIfsc.getText().toString().replaceAll(",","");
                                String data1=data.trim();
//                                if(data1.startsWith("\\s")){
//                                    edIfsc.setText(data1.replaceFirst("\\s",""));
//                                }else{
//                                    edIfsc.setText(data1.replaceFirst("\\s",""));
////&amp;emsp;
//                                }



                                if(data1.startsWith("\\s")){
                                    data2=data1.replaceFirst("\\s","");
                                }else{
                                    data2=data1;
                                }
                                Log.e("DATA1", "onTextChanged:"+data1 +""+data2.trim().replaceAll("\\s+", ""));


                                double calcul = Double.parseDouble(data2.trim().replaceAll("\\s+", "")) / price;

                                Double finacal1 = Double.parseDouble(BigDecimal.valueOf(calcul).toPlainString());

                                for (int x = 0; x < list.size(); x++) {

                                    if (Double.parseDouble(String.format("%.8f", finacal1)) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(String.format("%.8f", finacal1)) <= Double.parseDouble(list.get(x).getTo().trim())) {

                                        Log.e("priya-->", list.get(x).getFrom() + " to-> " + list.get(x).getTo() + " charges-->" + Double.parseDouble(String.format("%.8f", finacal1)));
                                        //  tvCharges.setText("Fees "+list.get(x).getFees()+" Ace");
                                        extra_charges = Double.parseDouble(list.get(x).getFees());

                                        Double extra_inr = (extra_charges * price);

                                        extra_charges = extra_charges * price;

                                        gst = Double.parseDouble(list.get(x).getGst());
                                        total = ((extra_inr * gst) / 100) + extra_inr;
                                        gst = (extra_inr * gst) / 100;

                                        btc_charges = (double) total / price;
                                        tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

                                        Log.e("changes1-->", extra_charges + " total1 " + total + " price1 "
                                                + price + " gst1 " + gst + " btc_charges1 " + btc_charges + " extra_inr1 " + extra_inr);
                                        break;
                                    } else if (Double.parseDouble(String.format("%.8f", finacal1)) >=
                                            Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {

                                        extra_charges = Double.parseDouble(list.get(list.size() - 1).getFees());

                                        Double extra_inr = (extra_charges * price);

                                        extra_charges = extra_charges * price;

                                        gst = Double.valueOf((list.get(list.size() - 1).getGst()));
                                        total = ((extra_inr * gst) / 100) + extra_inr;
                                        gst = (extra_inr * gst) / 100;

                                        btc_charges = (double) total / price;
                                        tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

                                        Log.e("changes1-->", extra_charges + " total1 " + total + " price1 " + price + " gst1 " + gst + " btc_charges1 " + btc_charges + " extra_inr1 " + extra_inr);
                                        break;

                                    } else {
                                        extra_charges = 0.00;
                                        tvCharges.setText("");
                                        btc_charges = 0.00;
                                    }
                                }

                                BigDecimal d = new BigDecimal(calcul);
                                finacal = String.valueOf(finacal1 - btc_charges);
                                txcalculate.setText(String.format("%.8f", finacal1));

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                } else {

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
            new Retrofit2(this, SendBitcoin.this, Constant.REQ_BTCCHARGE, Constant.BTCCHARGE).callService(false);
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

            Double sellprice = Double.parseDouble(buy);

            sellrate = buy;

            txBitcoin.setText(formatter.format(price) + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(sellprice) + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));

            buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount)) * Double.parseDouble(buy);
            newrate = buy_rate;

            if (buy_rate > 0) {
                if (tvBitcoin.getText().toString().trim().equalsIgnoreCase("1.00000000")) {
                    tvINR.setText(formatter.format( price) + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));
                } else {
                    tvINR.setText(formatter.format( newrate) + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));
                }
            } else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));
            }


            //todo later on fluctuating buy/sell rate

            if (edBits.getText().toString().length() > 0) {
                if ((!edBits.getText().toString().equals("."))) {
                    if ((!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals("null")) || (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(""))) {
                        Double charges = Double.parseDouble(edBits.getText().toString());
                        charges = Double.parseDouble(BigDecimal.valueOf(charges).toPlainString());

                        for (int x = 0; x < list.size(); x++) {

                            Double from = Double.parseDouble(list.get(x).getFrom());
                            Double newFrom = Double.parseDouble(BigDecimal.valueOf(from).toPlainString());

                            Double to = Double.parseDouble(list.get(x).getTo());
                            Double newto = Double.parseDouble(BigDecimal.valueOf(to).toPlainString());


                            if (Double.parseDouble(String.format("%.8f", charges)) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(String.format("%.8f", charges)) <= Double.parseDouble(list.get(x).getTo().trim())) {


                                // tvCharges.setText("Fees "+list.get(x).getFees()+" Ace");
                                extra_charges = Double.parseDouble(list.get(x).getFees());

                                Double extra_inr = (extra_charges * price);

                                extra_charges = extra_charges * price;

                                gst = Double.parseDouble(list.get(x).getGst());
                                total = ((extra_inr * gst) / 100) + extra_inr;
                                gst = (extra_inr * gst) / 100;

                                btc_charges = (double) total / price;
                                tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

//                                Log.e("changes-->",extra_charges+" total "+total+" price "+price+" gst "+gst+" btc_charges "+btc_charges+" extra_inr "+extra_inr);
                                break;
                            } else if (Double.parseDouble(String.format("%.8f", charges)) >= Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {

                                extra_charges = Double.parseDouble(list.get(list.size() - 1).getFees());

                                Double extra_inr = (extra_charges * price);

                                extra_charges = extra_charges * price;

                                gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                total = ((extra_inr * gst) / 100) + extra_inr;
                                gst = (extra_inr * gst) / 100;

                                btc_charges = (double) total / price;
                                tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

//                                Log.e("changes-->",extra_charges+" total "+total+" price "+price+" gst "+gst+" btc_charges "+btc_charges+" extra_inr "+extra_inr);
                                break;
                            } else {
                                extra_charges = 0.00;
                                tvCharges.setText("");
                                btc_charges = 0.00;
                            }
                        }
                        double calcul = ((Double.parseDouble(edBits.getText().toString())) * price);//before minus                        finacal = BigDecimal.valueOf(calcul).toPlainString();
                        Double newfinal = Double.parseDouble(finacal);
                        txRupees.setText(String.format("%.2f", calcul));

                    } else {
                        Constant.alertDialog(SendBitcoin.this, "Your Account has no Amount ");
                    }
                }

            } else {
                extra_charges = 0.00;
                tvCharges.setText("");
                txcalculate.setText("0.00");
            }


            if (edIfsc.getText().toString().length() > 0) {

                if ((!edIfsc.getText().toString().equals("."))) {

                    if (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(null) || (!PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount).equals(""))) {

                        double calcul = Double.parseDouble(edIfsc.getText().toString()) / price;

                        Double finacal1 = Double.parseDouble(BigDecimal.valueOf(calcul).toPlainString());

                        for (int x = 0; x < list.size(); x++)
                        {
          if (Double.parseDouble(String.format("%.8f", finacal1)) >= Double.parseDouble(list.get(x).getFrom().trim())
                  && Double.parseDouble(String.format("%.8f", finacal1)) <= Double.parseDouble(list.get(x).getTo().trim())) {

//                                Log.e("priya-->",list.get(x).getFrom()+" to-> "+list.get(x).getTo()+" charges-->"+Double.parseDouble(String.format("%.8f", finacal1)));
                                //  tvCharges.setText("Fees "+list.get(x).getFees()+" Ace");
                                extra_charges = Double.parseDouble(list.get(x).getFees());

                                Double extra_inr = (extra_charges * price);

                                extra_charges = extra_charges * price;

                                gst = Double.parseDouble(list.get(x).getGst());
                                total = ((extra_inr * gst) / 100) + extra_inr;
                                gst = (extra_inr * gst) / 100;

                                btc_charges = (double) total / price;
                                tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

//                                Log.e("changes1-->",extra_charges+" total1 "+total+" price1 "+price+" gst1 "+gst+" btc_charges1 "+btc_charges+" extra_inr1 "+extra_inr);
                                break;
                            } else if (Double.parseDouble(String.format("%.8f", finacal1)) >= Double.parseDouble(list.get(list.size() - 1).getFrom().trim())) {

                                extra_charges = Double.parseDouble(list.get(list.size() - 1).getFees());

                                Double extra_inr = (extra_charges * price);

                                extra_charges = extra_charges * price;

                                gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                                total = ((extra_inr * gst) / 100) + extra_inr;
                                gst = (extra_inr * gst) / 100;

                                btc_charges = (double) total / price;
                                tvCharges.setText("Fees " + String.format("%.8f", btc_charges) + " Ace");

                                break;
                            } else {
                                extra_charges = 0.00;
                                tvCharges.setText("");
                                btc_charges = 0.00;
                            }
                        }

                        BigDecimal d = new BigDecimal(calcul);
                        finacal = String.valueOf(finacal1 - btc_charges);
                        txcalculate.setText(String.format("%.8f", finacal1));

                    }
                }

            } else {

                txcalculate.setText("0.00");
            }


        }
    };

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, SendBitcoin.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.lnrefresh:

                newRefereshing();

                break;

            case R.id.tvSellAll:
                Log.e("sellall", "onClick");

                if (LnBitcoin.isShown()) {
                    if (bit > 0) {

                        edBits.setText(String.format("%.8f", finacals));
                        Double sellprice = Double.parseDouble(sellrate);

                        Double newsell = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData
                                (SendBitcoin.this, Constant.BTC_amount));

                        Double aDouble=sellprice*newsell;
                        txRupees.setText(String.format("%.2f", aDouble)+"");


                    }
                    Log.e("insidefif", "onClick");
                    for (int x = 0; x < list.size(); x++) {

                        Log.e("tvSellAll", price + " " + Double.parseDouble(String.valueOf(finacals)));

                        Log.e("chargesBTC", price + " " + list.get(x).getFrom().trim() + "    " + list.get(x).getTo().trim());

                        if (finacals >= Double.parseDouble(list.get(x).getFrom().trim()) && finacals <=
                                Double.parseDouble(list.get(x).getTo().trim())) {

                            Log.e("chargesBTCIFFF", price + " " + list.get(x).getFrom().trim() + " " +
                                    "   " + list.get(x).getTo().trim());

                            extra_charges = Double.parseDouble(list.get(x).getFees());

                            Double extra_inr = (extra_charges * price);

                            extra_charges = extra_charges * price;

                            gst = Double.parseDouble(list.get(x).getGst());
                            total = ((extra_inr * gst) / 100) + extra_inr;
                            gst = (extra_inr * gst) / 100;

                            Double charges = Double.parseDouble(String.valueOf(total)) / Double.parseDouble(String.valueOf(price));

                            edBits.setText(String.format("%.8f", finacals));
//                            edBits.setText(String.format("%.8f", finacals-charges));
                            break;
                        } else if (finacals >= Double.parseDouble(list.get(list.size() - 1).getTo().trim())) {

                            extra_charges = Double.parseDouble(list.get(list.size() - 1).getFees());

                            Double extra_inr = (extra_charges * price);

                            extra_charges = extra_charges * price;

                            gst = Double.parseDouble(list.get(list.size() - 1).getGst());
                            total = ((extra_inr * gst) / 100) + extra_inr;
                            gst = (extra_inr * gst) / 100;

                            Double charges = Double.parseDouble(String.valueOf(total)) / Double.parseDouble(String.valueOf(price));

                            Log.e("extra_charges1", "" + extra_charges + " extra_inr " + extra_inr);
                            Log.e("gst1", "" + gst);
                            Log.e("total1", "" + total + " charges " + charges);
                            Log.e("iftotal1", total + " fincals " + Double.parseDouble(String.valueOf(finacals)));
                            edBits.setText(String.format("%.8f", finacals));
//                            edBits.setText(String.format("%.8f", finacals-charges));
                            break;
                        } else {

                            Log.e("elsetotal2", " " + Double.parseDouble(String.valueOf(finacals)));
                            edBits.setText(String.format("%.2f", finacals));
                        }
                    }


                } else {

                    //edIfsc.setText(String.valueOf(buy_rate));
                    Double sellprice = Double.parseDouble(sellrate);
//                    int pric = sellprice.intValue();
                    Double newsell = Double.parseDouble
                            (PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.BTC_amount));
                    Double newprice = (price * newsell);
                    Log.e("sellprice",sellprice.toString());
                    Log.e("newprice",newprice.toString());

                    String s[] = tvINR.getText().toString().split(" ");

                    String data=s[0].replaceAll(",","");
                    String data1=data.trim();

                    if(data1.startsWith("\\s")){
                        data2=data1.replaceFirst("\\s","");
                    }else{
                        data2=data1;
                    }


                    edIfsc.setText(data2.trim().replaceAll("\\s+", ""));

                    Log.e("DATA2", "onTextChanged11:"+data1 +""+data2.trim().replaceAll("\\s+", ""));

                    //                    edIfsc.setText(tvINR.getText().toString().trim().replaceAll("₹",""));
//                    edIfsc.setText(String.format("%.0f", newprice));


                }

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

            case R.id.txNext:

                Constant.hideKeyboard(this,v);

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) != null) {

                    if (lnRupees.isShown()) {

                        if (!txcalculate.getText().toString().equals("")) {

                            if ((!txcalculate.getText().toString().equals("."))) {

                                Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                                Log.e("finalValue", String.format("%.8f", finacal));
                                Log.e("extra_chargesAdd", " " + (Double.parseDouble(txcalculate.getText().toString()) + extra_charges));

                                if (((Double.parseDouble(txcalculate.getText().toString()) <= Double.parseDouble(String.format("%.8f", finacal))))) {

                                    if (Double.parseDouble(txcalculate.getText().toString()) > 0) {

                                        if (Double.parseDouble(edIfsc.getText().toString()) > min - 1 && Double.parseDouble(edIfsc.getText().toString()) < max + 1) {

                                            double totle_rate = extra_charges * price;

                                            Log.e("EXTRACHARGES", extra_charges + " " + extra_charges * price);
                                            Log.e("EXTRACHARGES", extra_charges + " " + extra_charges / price);
                                            Double cal = Double.parseDouble(txcalculate.getText().toString());
                                            Double newval = Double.parseDouble(BigDecimal.valueOf(cal).toPlainString());

                                            //double totle_amount=newval*price;
                                            double totle_amount = Double.parseDouble(edIfsc.getText().toString().trim());


                                            Intent intent1 = new Intent(SendBitcoin.this, BitcoinAddressAddedList.class);


                                            String data= tvINR.getText().toString().trim().
                                                    replaceAll(PreferenceFile.getInstance().getPreferenceData
                                                            (SendBitcoin.this, Constant.Currency_Symbol),"")
                                                    .replaceAll(",","");


                                            intent1.putExtra("data", data.trim().replaceAll("\\s+",
                                                    ""));

                                            Log.e("SEND", "onClick:22??"+data.trim().replaceAll("\\s+",
                                                    "")+ "DATA"+edIfsc.getText().toString().trim());


                                            Double TOTAMTBNI= Double.parseDouble(edIfsc.getText().toString().trim())+total;
                                            Double ACTUALAMT= Double.parseDouble(data.trim().replaceAll("\\s+", ""));

                                            if(TOTAMTBNI>=ACTUALAMT){
                                                intent1.putExtra("data_key","1");
                                            }else{
                                                intent1.putExtra("data_key","0");
                                            }


//                                            if(data.trim().replaceAll("\\s+", "").
//                                                    equalsIgnoreCase(edIfsc.getText().toString().trim())){
//                                                intent1.putExtra("data_key","1");
//                                            }else{
//                                                intent1.putExtra("data_key","0");
//                                            }



                                            intent1.putExtra("key", "send");
                                            intent1.putExtra("amount", String.format("%.8f", newval));
                                            intent1.putExtra("amount_inr", String.format("%.2f", totle_amount));
                                            intent1.putExtra("charge", String.format("%.2f", extra_charges));
                                            intent1.putExtra("charge_inr", String.format("%.2f", totle_rate));
                                            intent1.putExtra("gst", String.valueOf(gst));
                                            intent1.putExtra("final_charge", String.valueOf(total));
                                            intent1.putExtra("rate", String.valueOf(price).trim());



                                            intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());

                                            intent1.putExtra("previous_amount", PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));
                                            startActivity(intent1);
                                            Log.e("SENDBTC", tvBitcoin.getText().toString().trim());

                                        } else {
                                            Constant.alertDialog(SendBitcoin.this, "Please enter valid amount.");
                                        }
                                    } else {

                                        Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                                    }
                                } else {

                                    Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                                }
                            } else {
                                Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                            }
                        } else {
                            Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                        }
                    } else {
                        Log.e("lnruees-->", "No");
                        if (!edBits.getText().toString().equals("")) {

                            if ((!edBits.getText().toString().equals("."))) {

                                Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                                Log.e("finalValue", String.format("%.8f", finacal));

                                if (((Double.parseDouble(edBits.getText().toString()) <= Double.parseDouble(String.format("%.8f", finacal))))) {

                                    if (Double.parseDouble(edBits.getText().toString()) > 0) {

                                        double totle_rate = extra_charges * price;

                                        Double cal = Double.parseDouble(edBits.getText().toString());
                                        Double newval = Double.parseDouble(BigDecimal.valueOf(cal).toPlainString());

                                        //  double totle_amount=newval*price;
                                        double totle_amount = Double.parseDouble(txRupees.getText().toString().trim());



                                        if (Double.parseDouble(txRupees.getText().toString()) > min - 1
                                                && Double.parseDouble(txRupees.getText().toString()) < max + 1) {


                                            String data= tvINR.getText().toString().trim().
                                                    replaceAll(PreferenceFile.getInstance().getPreferenceData
                                                            (SendBitcoin.this, Constant.Currency_Symbol),"")
                                                    .replaceAll(",","");



                                            Intent intent1 = new Intent(SendBitcoin.this, BitcoinAddressAddedList.class);
                                            intent1.putExtra("key", "send");
                                            intent1.putExtra("data", data.trim().replaceAll("\\s+",
                                                    ""));


                                            if(data.trim().replaceAll("\\s+", "").equalsIgnoreCase(txRupees.getText().toString().trim())){
                                                intent1.putExtra("data_key","1");
                                            }else{
                                                intent1.putExtra("data_key","0");
                                            }

                                            Log.e("SEND", "onClick:261"+data.trim().replaceAll("\\s+",
                                                    "")+" "+txRupees.getText().toString().trim());




                                            intent1.putExtra("amount", String.format("%.8f", newval));
                                            intent1.putExtra("amount_inr", String.format("%.2f", totle_amount));
                                            intent1.putExtra("charge", String.format("%.2f", extra_charges));
                                            intent1.putExtra("charge_inr", String.format("%.2f", totle_rate));
                                            intent1.putExtra("gst", String.valueOf(gst));
                                            intent1.putExtra("final_charge", String.valueOf(total));
                                            intent1.putExtra("rate", String.valueOf(price).trim());
                                            intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());




                                            intent1.putExtra("previous_amount",
                                                    PreferenceFile.getInstance().getPreferenceData(this, Constant.Inr_Amount));
                                            startActivity(intent1);
                                        } else {

                                            Constant.alertDialog(SendBitcoin.this, "Please enter valid amount.");
                                        }
                                    } else {

                                        Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                                    }
                                } else {

                                    Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                                }
                            } else {
                                Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                            }
                        } else {
                            Constant.alertDialog(SendBitcoin.this, getString(R.string.amount_validation));
                        }
                    }

                } else {
                    Constant.alertDialog(SendBitcoin.this, "Your Account has not verified.");
                }


                break;
        }
    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, SendBitcoin.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void ManMinPrice(){

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, SendBitcoin.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(false);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        Log.e("REQ_Dashboard_Refresh", response.body().string());

                        JSONObject result1 = new JSONObject(response.body().string());

                        Log.e("REQ_Dashboard_Refresh", result1.toString());

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(SendBitcoin.this, Constant.Currency_Symbol));

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

            case Constant.REQ_MIN_MAX:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");
                    callnewservice();

                    Log.e("response-->",result1.toString());
                    Log.e("status-->",status+" message-->"+message);

                    if(status.equals("true")) {

                        JSONArray data = result1.getJSONArray("data");

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
                                        PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) + " max: " +
                                        formatter.format(Double.valueOf(max)) + " "
                                        + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                        }
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

                        ManMinPrice();

                        Log.e("REQ_BTC_RATE", result.toString());

                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            Double buyprice = Double.parseDouble(data.getString("buy"));
                            price = buyprice;

                            Double sellprices = Double.parseDouble(data.getString("sell"));

                            txBitcoin.setText(formatter.format(price) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(sellprices) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            sellrate = data.getString("buy");

                            buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            newrate = buy_rate;

                            if (buy_rate > 0) {
                                tvINR.setText(formatter.format( newrate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }


                        } else {
                            Constant.alertDialog(this, message);

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

            case Constant.REQ_BTCCHARGE:
                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("resultrate-->", result.toString());
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

                                String from = myobj.getString("btc_from");
                                String charge = myobj.getString("charge");
                                String to = myobj.getString("btc_to");

                                double d=Double.parseDouble(charge);
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(d).toPlainString());

                                double fromnew=Double.parseDouble(from);
                                Double doublefrom = Double.parseDouble(BigDecimal.valueOf(fromnew).toPlainString());

                                double todouble=Double.parseDouble(to);
                                Double doubleto = Double.parseDouble(BigDecimal.valueOf(todouble).toPlainString());

                                FeesChargeModet model = new FeesChargeModet();
                                model.setFees((String.format("%.6f", finacal)));
                                model.setFrom((String.format("%.8f", doublefrom)));
                                model.setTo((String.format("%.8f", doubleto)));
                                model.setGst(myobj.getString("gst"));

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

            case Constant.REQ_Left_balance:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data=result.getJSONObject("data");
                            JSONObject data1=data.getJSONObject("data");

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,data1.getString("available_balance"));
                            Constant.alertWithIntent(this, "BTC transfer successfully.",Dashboard.class);

                        } else
                        {
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

            case Constant.REQ_AddedAddresslist:

                try {

                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result--->",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {
                        AlluserListname.clear();
                        ArrayAlluserlist.clear();

                        JSONArray data=result.getJSONArray("data");

                        for(int x=0;x<data.length();x++){

                            JSONObject obj=data.getJSONObject(x);

                            AllUserModel allUserModel=new AllUserModel();

                            allUserModel.setId(obj.getString("id"));
                            allUserModel.setUsername(obj.getString("first_name")+" "+obj.getString("last_name"));
                            allUserModel.setPhone(obj.getString("phone"));
                            allUserModel.setAddress(obj.getString("address").replace("null","Not verified Account"));
                            AlluserListname.add(obj.getString("phone"));
                            ArrayAlluserlist.add(allUserModel);
                            setAdapter();

                        }

                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_TRANSFER_BITCOIN:

                try {

                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result--->",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {


                        JSONObject postParam = new JSONObject();

                        try {
                            postParam.put("address", PreferenceFile.getInstance().getPreferenceData(this,Constant.BITCOIN_ADDRESS));

                            if (Constant.isConnectingToInternet(SendBitcoin.this)) {
                                Log.e("connect--->",postParam.toString());
                                new Retrofit2(SendBitcoin.this, SendBitcoin.this, postParam,
                                        Constant.REQ_Left_balance, Constant.Left_balance, "3").callService(true);
                            }
                            else {
                                Log.e("connect--->", "no");
                                Constant.alertDialog(SendBitcoin.this, getResources().getString(R.string.check_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void setAdapter(){

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.adapter_list,AlluserListname);
        edNumber.setAdapter(adapter);
        edNumber.setThreshold(1);
        Log.e("searchlistsize", String.valueOf(AlluserListname.size()));
        edNumber.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                for(int x=0;x<AlluserListname.size();x++)
                {
                    if(ArrayAlluserlist.get(x).getPhone().equals(edNumber.getText().toString()))
                    {
//                        edname.setText(ArrayAlluserlist.get(x).getUsername());
//                        edAddress.setText(ArrayAlluserlist.get(x).getAddress());

                        Log.e("arrayusername ",ArrayAlluserlist.get(x).getUsername());

                    }
                }
            }
        });
    }


}

