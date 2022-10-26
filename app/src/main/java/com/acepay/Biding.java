package com.acepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.BidAskAdapter;
import com.acepay.Model.BidAskModel;
import com.acepay.Model.FeesChargeModet;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Biding extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {
    ImageView ivarrow;
    TextView txName,tvBuyrate,tvSellrate,tvBiding,currentsymbol,tvwallet,tvBitcoin,tvINR,txBitcoin,tvbit,tvtxt;
    EditText tvRate,tvAmount,edQuantity;
    DecimalFormat formatter;
    Double buyrate;
    LinearLayout lnrefresh;
    int total=0;
    int gst=0,charges=0;
    boolean doubleBackToExitPressedOnce = false;
    double avaluebit=0;
    ArrayList<FeesChargeModet> list = new ArrayList<>();
    int flag=0;
    BidAskAdapter adapter;
    ArrayList<BidAskModel> bidAskModels;
    Typeface tfArchitectsDaughter;
    long back_pressed=2000;
    RecyclerView recyclerView;
    ArrayList<BidAskModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biding);

        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);
        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        tvtxt= (TextView) findViewById(R.id.tvtxt);
        tvBuyrate= (TextView) findViewById(R.id.tvBuyrate);
        tvwallet= (TextView) findViewById(R.id.tvwallet);
        tvbit= (TextView) findViewById(R.id.tvbit);
        tvSellrate= (TextView) findViewById(R.id.tvSellrate);
        tvBitcoin= (TextView) findViewById(R.id.tvBitcoin);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        tvINR = (TextView) findViewById(R.id.tvINR);
        tvBiding= (TextView) findViewById(R.id.tvBiding);
        tvRate= (EditText) findViewById(R.id.tvRate);
        tvAmount= (EditText) findViewById(R.id.tvAmount);
        edQuantity= (EditText) findViewById(R.id.edQuantity);
        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        lnrefresh.setOnClickListener(this);
        txName.setText("Bidding");

        bidAskModels=new ArrayList<>();

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        formatter = new DecimalFormat("#,##,###");
        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol)+" ");

        //callStateService();
       callService();

        Double bit = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Log.e("bitcoin--->",finacal+"");
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        tvRate.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("click-->",charSequence.toString());

                if(!tvRate.getText().toString().equals(".") && (!tvRate.getText().toString().equals(""))){

                    tvbit.setVisibility(View.VISIBLE);
                   // Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Biding.this,Constant.BTC_amount));

                    Double bits= Double.parseDouble(tvRate.getText().toString())/buyrate;

                    Double finacal = Double.parseDouble(BigDecimal.valueOf(bits).toPlainString());
                    Double rate = Double.parseDouble(BigDecimal.valueOf(buyrate).toPlainString());
                    Log.e("insiderate-->",bits+"");
                    Log.e("buyrate-->",buyrate+"");
                    Log.e("rate-->",rate+"");
                    Log.e("finalrate-->",finacal+"");

                    if(buyrate>0)
                    {
                        tvbit.setText(String.format("%.8f", finacal)+" Ace");
                        avaluebit=finacal;
                    }
                    else
                    {
                        tvBitcoin.setText("0.00000000"+" Ace");
                    }

                    Log.e("bits-->",bits+"");

                }
                else
                {
                    tvbit.setVisibility(View.GONE);
                }

                if((tvRate.getText().toString().length()>0) && (!tvAmount.getText().toString().equals(""))) {

                    if((!tvRate.getText().toString().equals(".")) && (!tvAmount.getText().toString().equals("."))) {
                        Log.e("amount-->", tvAmount.getText().toString() + " rate-->" + tvRate.getText().toString());

                        Double amount = Double.parseDouble(tvAmount.getText().toString());
                        Double rate = Double.parseDouble(tvRate.getText().toString());

                        for (int x=0; x < list.size() ;x++) {

                            if(Double.parseDouble(tvAmount.getText().toString()) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(tvAmount.getText().toString()) <= Double.parseDouble(list.get(x).getTo().trim())){

                                 charges=Integer.parseInt(list.get(x).getFees());
                                 gst=Integer.parseInt(list.get(x).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("iftvRate",total+"");
                                break;
                            }

                            else if( Double.parseDouble(tvAmount.getText().toString()) >= Double.parseDouble(list.get(list.size()-1).getTo().trim())){
                                charges=Integer.parseInt(list.get(list.size()-1).getFees());
                                gst=Integer.parseInt(list.get(list.size()-1).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("iftvRate",total+"");
                                break;
                            }
                            else {
                                Log.e("elsetvRate",total+"");
                                total=0;
                            }
                        }

                       Log.e("total",total+"");
                        Double totle = (amount-total) / rate;
                        Double newbits = Double.parseDouble(BigDecimal.valueOf(totle).toPlainString());
                        Log.e("insiderate-->",totle+"");
                        Log.e("guantityBits-->",newbits+"");
                        Log.e("newbits-->",newbits+"");

                        Log.e("amount-->", amount + " rate-->" + rate + " totle-->" + totle);

                        edQuantity.setText(String.format("%.8f", newbits));
                        //edQuantity.setText(String.valueOf(finacal));
                    }
                }else {

                    edQuantity.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }

        tvAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("amount-->",charSequence.toString());

                if((tvAmount.getText().toString().length()>0) && (!tvRate.getText().toString().equals(""))) {

                    if((!tvAmount.getText().toString().equals(".")) && (!tvRate.getText().toString().equals("."))) {

                        Double amount = Double.parseDouble(tvAmount.getText().toString());
                        Double rate = Double.parseDouble(tvRate.getText().toString());

                        Log.e("amount-->", tvAmount.getText().toString() + " rate-->" + tvRate.getText().toString());

                        for (int x=0; x < list.size() ;x++) {

                            Log.e("from-->",Double.parseDouble(list.get(x).getFrom().trim()) + " to-->" + Double.parseDouble(list.get(x).getTo().trim())+ " charge-->" + Double.parseDouble(list.get(x).getFees().trim()));

                            if((Double.parseDouble(tvAmount.getText().toString()) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble(tvAmount.getText().toString()) <= Double.parseDouble(list.get(x).getTo().trim()))){

                                charges=Integer.parseInt(list.get(x).getFees());
                                gst=Integer.parseInt(list.get(x).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("iftvAmount",total+" "+gst);
                                break;
                            }
                            else if(Double.parseDouble(tvAmount.getText().toString()) >= Double.parseDouble(list.get(list.size()-1).getTo().trim())){
                                charges=Integer.parseInt(list.get(list.size()-1).getFees());
                                gst=Integer.parseInt(list.get(list.size()-1).getGst());
                                total=((charges*gst)/100)+charges;
                                gst=(charges*gst)/100;
                                Log.e("elseiftvAmount",total+" "+gst);
                                break;
                            }
                            else {
                                Log.e("esletvAmount",total+" "+gst);
                                total=0;
                            }
                        }

                        Log.e("amonut",amount+" total "+total+" rate "+rate);

                        Double totle = (amount-total) / rate;

                        Double newbits = Double.parseDouble(BigDecimal.valueOf(totle).toPlainString());
                        Log.e("insiderate-->",totle+"");
                        Log.e("guantityBits-->",newbits+"");
                        Log.e("newbits-->",newbits+"");

                        Log.e("amount-->", amount + " rate-->" + rate + " totle-->" + totle);

                        edQuantity.setText(String.format("%.8f", newbits));
                    }
                }else {

                    edQuantity.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvBiding.setOnClickListener(this);
        tvtxt.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));

    }

    private void onInit() {
        models=new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        models=(ArrayList<BidAskModel>)bidAskModels;
        Log.e("size--->",models.size()+"");

        adapter = new BidAskAdapter(this,models,"bid");
        recyclerView.setAdapter(adapter);

        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
            txName.setText("Bidding List");

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            buyrate=Double.valueOf(buy);

            txBitcoin.setText(formatter.format(Double.valueOf(buy))+" "+PreferenceFile.getInstance().getPreferenceData(Biding.this,Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell))+" "+PreferenceFile.getInstance().getPreferenceData(Biding.this,Constant.Currency_Symbol));

            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Biding.this,Constant.BTC_amount))*Double.parseDouble(buy);

            if(buy_rate>0) {

                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(Biding.this, Constant.Currency_Symbol));
            }
            else
            {
                tvINR.setText("0" + " " + PreferenceFile.getInstance().getPreferenceData(Biding.this, Constant.Currency_Symbol));
            }


            // to change Acepay dynamically after entering the rate
            if(!tvRate.getText().toString().equals(".") && (!tvRate.getText().toString().equals(""))){

                tvbit.setVisibility(View.VISIBLE);
                // Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Biding.this,Constant.BTC_amount));

                Double bits= Double.parseDouble(tvRate.getText().toString())/buyrate;

                Double finacal = Double.parseDouble(BigDecimal.valueOf(bits).toPlainString());
                Double rate = Double.parseDouble(BigDecimal.valueOf(buyrate).toPlainString());
                Log.e("insiderate-->",bits+"");
                Log.e("buyrate-->",buyrate+"");
                Log.e("rate-->",rate+"");
                Log.e("finalrate-->",finacal+"");

                if(buyrate>0)
                {
                    tvbit.setText(String.format("%.8f", finacal)+" Ace");
                    avaluebit=finacal;
                }
                else
                {
                    tvBitcoin.setText("0.00000000"+" Ace");
                }
                Log.e("bits-->",bits+"");
            }
            else
            {
                tvbit.setVisibility(View.GONE);
            }

        }
    };


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Biding.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.ivarrow:

                Constant.hideKeyboard(this,v);

                finish();

                break;

            case R.id.lnrefresh:

               // Log
                callService();

                break;

            case R.id.tvtxt:

                if(bidAskModels.size()>0) {
                    intent = new Intent(Biding.this, BidAsk.class);
                    intent.putExtra("mylist", bidAskModels);
                    intent.putExtra("key", "bid");
                    startActivity(intent);
                  //  onInit();
                }

                break;

            case R.id.tvBiding:

                Constant.hideKeyboard(this,v);

                    if (verification()) {

                        Log.e("buy_rate-->",PreferenceFile.getInstance().getPreferenceData(Biding.this, Constant.Inr_Amount)+" d-->"+tvAmount.getText().toString());

                        if (Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Biding.this, Constant.Inr_Amount)) >= Double.parseDouble(tvAmount.getText().toString())) {

                            Log.e("size-->",list.size()+"");

                         int amount=Integer.parseInt(tvAmount.getText().toString());
                           /*   for (int x=0;x<list.size();x++)
                            {
                                if(Double.parseDouble( tvAmount.getText().toString()) >= Double.parseDouble(list.get(x).getFrom().trim()) && Double.parseDouble( tvAmount.getText().toString()) <= Double.parseDouble(list.get(x).getTo().trim()) || Double.parseDouble( tvAmount.getText().toString()) <= Double.parseDouble(list.get(list.size()-1).getTo().trim()) ) {

                                     charges=Integer.parseInt(list.get(x).getFees());
                                     gst=Integer.parseInt(list.get(x).getGst());
                                    total=((charges*gst)/100)+charges;
                                    gst=(charges*gst)/100;

                                    Log.e("total-",total+" ");
                                    Log.e("totalinr-",String.valueOf(Integer.parseInt(tvAmount.getText().toString().trim())-total) );
                                    amount=(Integer.parseInt(tvAmount.getText().toString().trim())) ;

                                    break;
                                }
                                (Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Biding.this, "biddingamount"))+Double.parseDouble(tvAmount.getText().toString())
                            }
*/
                            Log.e("VALUES",tvAmount.getText().toString());
                            Log.e("VALUES",tvRate.getText().toString());
                            Log.e("VALUES",String.valueOf(total));
                            Log.e("VALUES",String.valueOf(gst));
                            Log.e("VALUES",String.valueOf(charges));
                            Log.e("VALUES", edQuantity.getText().toString());

                            if(list.size()<0){

                                Log.e("list.size()-->",list.size()+" "+total);
                                Intent intent1 = new Intent(Biding.this, CheckSecurePin.class);
                                intent1.putExtra("key", "bid");
                                intent1.putExtra("amount", tvAmount.getText().toString());
                                intent1.putExtra("rate", tvRate.getText().toString());
                                intent1.putExtra("fee",String.valueOf(total));
                                intent1.putExtra("gst",String.valueOf(gst));
                                intent1.putExtra("charge",String.valueOf(charges));
                                intent1.putExtra("total_btc", edQuantity.getText().toString());
                                startActivity(intent1);
                            }
                            else {

                                Log.e("list.size()-->",list.size()+" "+total);
                                Log.e("list.size()-->",amount+" "+total);
                                Intent intent1 = new Intent(Biding.this, CheckSecurePin.class);
                                intent1.putExtra("key", "bid");
                                intent1.putExtra("amount", String.valueOf(amount));
                                intent1.putExtra("rate", tvRate.getText().toString());
                                intent1.putExtra("fee",String.valueOf(total));
                                intent1.putExtra("gst",String.valueOf(gst));
                                intent1.putExtra("charge",String.valueOf(charges));
                                intent1.putExtra("total_btc", edQuantity.getText().toString());
                                startActivity(intent1);
                            }


                    }
                        else {

                            Constant.alertDialog(Biding.this, getString(R.string.amount_validation));
                        }
                    }

                break;
        }
    }


    public boolean verification(){

        if(tvRate.getText().toString().equals("")) {

            Constant.alertDialog(this,"Please enter rate.");
            return false;
        }

        if(tvRate.getText().toString().equals(".")){

            Constant.alertDialog(this,"Please enter valid rate");
            return false;
        }

        if(Double.parseDouble(tvRate.getText().toString())<=0){
            Constant.alertDialog(this,"Please enter valid rate.");
            return false;
        }

        if(tvAmount.getText().toString().equals("")){

            Constant.alertDialog(this,"Please enter amount.");
            return false;
        }

        if(tvAmount.getText().toString().equals(".")){

            Constant.alertDialog(this,"Please enter valid rate.");
            return false;
        }

        if(Double.parseDouble(tvAmount.getText().toString())<=0){
            Constant.alertDialog(this,"Please enter valid rate.");
            return false;
        }

        if(edQuantity.getText().toString().equals("")){

            Constant.alertDialog(this,"Please enter quantity.");
            return false;
        }

        return true;
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



//    @Override
//    public void finish() {
//
//        if(flag==1){
//            Log.e("Biding","iffinish");
//            finishAffinity();
//        }
//        else {
//            Log.e("Biding","elsefinish");
//            super.finish();
//        }
//
//    }

    private void callnewservice(){

        if (Constant.isConnectingToInternet(this)) {
            Log.e("callservice", "once");
            new Retrofit2(this, Biding.this, Constant.REQ_NET_FEES, Constant.NET_FEES+"2").callService(true);
            //   new Retrofit2(this, MyTickets.this, Constant.REQ_MYTICKET,PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Biding.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Biding.this, Constant.REQ_bid_list, Constant.bid_list+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        } else {
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


            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String status = result1.getString("response");
                        String message = result1.getString("message");
                        callnewservice();

                        if (status.equals("true")){

                            // PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"0");
                            JSONObject result = result1.getJSONObject("data");

                            Log.e("status--->", status + "");
                            Log.e("id-->", result.getString("id"));

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
                            Log.e("againcall--->","yes");
                            BigDecimal ff = new BigDecimal(inr);
                            Log.e("newcal-->","d -->"+ ff);

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(Biding.this, Constant.Currency_Symbol));
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

                            Log.e("idddddd-->", result.getString("id"));

                            if(!result.getString("first_name").equals("null")){

                                PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name")+" "+result.getString("last_name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                PreferenceFile.getInstance().saveData(this, Constant.email_verification, result.getString("verification"));
                                PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));

                                Log.e("resultimage-->",result.getString("image"));
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

                                Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.City_name));
                                Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.phone));

                                JSONObject CountryName=result.getJSONObject("CountryName");
                                PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                if(!result.isNull("BankName")){

                                    Log.e("bank-->","yes");

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

                                    Log.e("AddName -->","yes");
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
                                    Log.e("PanName-->","yes");

                                    JSONObject PanName=result.getJSONObject("PanName");
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_name,PanName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_last,PanName.getString("last_name"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_image,PanName.getString("image"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_number,PanName.getString("pan_number"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_dob,PanName.getString("dob"));
                                    PreferenceFile.getInstance().saveData(this,Constant.Pan_gender,PanName.getString("gender"));
                                }
                            }

                            // PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                        }
                        else
                        {
                            Constant.alertWithIntent(this,"Account Blocked",BlockScreen.class);

                        }

                    } catch (JSONException e) {
                        Log.e("exception-->", e.toString());
                    }
                    catch (IOException e) {

                    }
                }
                break;

            case Constant.REQ_BTC_RATE:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        callStateService();

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            buyrate=Double.valueOf(data.getString("buy"));

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))*Double.parseDouble(data.getString("buy"));

                            if(buy_rate>0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                            else
                            {
                                tvINR.setText("0" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                            Log.e("data--->", data.toString());

                        } else {

                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY)!=null) {
                                buyrate=Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY));
                                // buyrate=Double.valueOf(data.getString("sell"));

                                txBitcoin.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY)))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvSellrate.setText(formatter.format(Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.SELL)))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))*Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.SELL));

                                if(buy_rate>0) {

                                    tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                }else
                                {
                                    tvINR.setText("0" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
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

            case Constant.REQ_bid_list:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    newRefereshing();

                    if (status.equals("true")){

                       String string = "<font color='#1d42d9'>See More</font>";
                        tvtxt.setText("you are also applied for some bidding..."+Html.fromHtml(string));

                        JSONArray data=result.getJSONArray("data");

                        for(int x=0;x<data.length();x++){

                            JSONObject object=data.getJSONObject(x);
                            BidAskModel bidAskModel=new BidAskModel();
                            bidAskModel.setAmount(object.getString("amount"));
                            bidAskModel.setRate(object.getString("rate"));
                            bidAskModel.setStatus(object.getString("status"));
                            bidAskModel.setDate(object.getString("created"));
                            bidAskModel.setQuantity(object.getString("total_btc"));

                            bidAskModels.add(bidAskModel);
                        }
                        Log.e("SIZE-->",bidAskModels.size()+"");

                        //  tvtxt.setText("you are also applied for some bidding..."+getString(R.string.read_more));
                        onInit();
                    }
                    else {

                        tvtxt.setText("There are no pending bid/ask order. Place a new bid/ask order and it will executes automatically when there is a matching offer.");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_BIDING:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        Constant.alertWithIntent(this,message,Dashboard.class);
                    }
                    else {

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
