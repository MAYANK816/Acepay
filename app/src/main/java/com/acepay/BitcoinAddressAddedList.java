package com.acepay;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.BitCoinAddressAdapter;
import com.acepay.Model.AllUserModel;
import com.acepay.Model.BitcoinAddressModel;
import com.acepay.Util.Constant;
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

public class BitcoinAddressAddedList extends ToolabarActivity implements OnClickListener, RetrofitResponse
{

    RecyclerView recyclerView;
//    ImageView ivarrow;
    TextView txName,tvwallet,txBitcoin,tvSellrate,tvINR,txAddContact,tvQrScanner,tvBitcoin,currentsymbol;
    androidx.appcompat.widget.Toolbar toolbar;
    String key,amount="",charge="",amount_inr="",charge_inr="",final_charge="",gst="",rate="",actual_btc="";
    ArrayList<String> AlluserListname;
    ArrayList<String> nonUserList;
    Double finacals,bit;
    LinearLayout lnrefresh;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<com.acepay.Model.AllUserModel> ArrayAlluserlist;
    TextView tvQrScanner1,edname,edAddress;
    AutoCompleteTextView edNumber;
    BitCoinAddressAdapter adapter;
    private IntentIntegrator qrScan;
    NumberFormat formatter;
    Typeface tfArchitectsDaughter;
    ArrayList<BitcoinAddressModel> AllUserModel;
    String positionn="", receiveraddress="",name="",phone="";
//    txRs,currentsymbol,ivarrow,txrs
private static final int BARCODE = 10;
    private boolean isPhoneInput=false,isNameInput=false, isAddrassInput=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitcoin_address_added_list);
        AllUserModel=new ArrayList<>();
        AlluserListname=new ArrayList<>();
        ArrayAlluserlist=new ArrayList<>();
        nonUserList=new ArrayList<>();

        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvwallet = (TextView) findViewById(R.id.tvwallet);
//        currentsymbol = (TextView) findViewById(R.id.currentsymbol);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        txAddContact = (TextView) findViewById(R.id.txAddContact);
        tvINR = (TextView) findViewById(R.id.tvINR);
        txBitcoin = (TextView) findViewById(R.id.txBitcoin);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
        tvQrScanner = (TextView) findViewById(R.id.tvQrScanner);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(BitcoinAddressAddedList.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        key=getIntent().getStringExtra("key");

        if(key.equals("send")){
            amount=getIntent().getStringExtra("amount");
            charge=getIntent().getStringExtra("charge");
            amount_inr=getIntent().getStringExtra("amount_inr");
            charge_inr=getIntent().getStringExtra("charge_inr");
            final_charge=getIntent().getStringExtra("final_charge");
            gst=getIntent().getStringExtra("gst");
            rate=getIntent().getStringExtra("rate");
            actual_btc=getIntent().getStringExtra("actual_btc");
            Log.e("actual_btc",actual_btc+"   "+ getIntent().getStringExtra("data")
                    +" "+getIntent().getStringExtra("data_key"));
        }

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Log.e("bitcoin--->",finacals+"");
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }


        txName= (TextView) findViewById(R.id.txName);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        qrScan = new IntentIntegrator(this);

        tvQrScanner.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        txAddContact.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Acepay Address Book");
        callService();

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            txBitcoin.setText(formatter.format(Double.valueOf(buy))+" "+PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell))+" "+PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.Currency_Symbol));

            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.BTC_amount))*Double.parseDouble(buy);

            if(buy_rate>0) {
                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this, Constant.Currency_Symbol));
            }
            else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this, Constant.Currency_Symbol));
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
                Intent intent=new Intent(BitcoinAddressAddedList.this,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(" requestAddddd",requestCode+"");
        Log.e(" resultA",resultCode+"");
        Log.e(" dataValueA",data.getData()+"");

        if (data != null) {

            if (data == null) {
                Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    if(data.getStringExtra("resultDisplayValue")!=null) {
                        if(data.getStringExtra("resultDisplayValue")!=null) {
                            receiveraddress=data.getStringExtra("resultDisplayValue");
                            findAddress(receiveraddress);
                        }
                        else {
                            Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
//                            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }

            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }



//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
//            }
//            else {
//                try {
//                    if(result.getContents()!=null) {
//                        if(result.getContents()!=null) {
//                            receiveraddress=result.getContents();
//                            findAddress(receiveraddress);
//                        }
//                        else {
//                            Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void callservice() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_AddedAddresslist,Constant.AddedAddresslist+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void callfinduser(){

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_FIND_USER,Constant.FIND_USER).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }

    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BitcoinAddressAddedList.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void findAddress(String receiveraddress){

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("address", receiveraddress.trim());

            if (Constant.isConnectingToInternet(BitcoinAddressAddedList.this)) {
                new Retrofit2(BitcoinAddressAddedList.this, BitcoinAddressAddedList.this, postParam, Constant.REQ_FIND_ADDRESS, Constant.FIND_ADDRESS, "3").callService(true);
            } else {
                Constant.alertDialog(BitcoinAddressAddedList.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.lnrefresh:

                newRefereshing();

                break;

//            case R.id.ivarrow:
//
//                Intent intent=new Intent(BitcoinAddressAddedList.this,Dashboard.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//
//                break;

            case R.id.tvQrScanner:
                Intent i=new Intent(this,BarcodeCustomScanner.class);
                startActivityForResult(i,BARCODE);
//              qrScan.initiateScan();
                break;

            case R.id.txAddContact:

                Log.e("add-->","yes");
                alertDialog();

                break;
        }
    }

    public void alertDialog() {

        callfinduser();

        final Dialog dialog = new Dialog(BitcoinAddressAddedList.this, R.style.StatisticsDialog);

        dialog.setCancelable(false);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView2 = li.inflate(R.layout.add_bitcoin_address, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        tvQrScanner1 = (TextView) promptsView2.findViewById(R.id.tvQrScanner);
        edNumber = (AutoCompleteTextView) promptsView2.findViewById(R.id.edNumber);
        edname = (TextView) promptsView2.findViewById(R.id.edname);
        edAddress = (TextView) promptsView2.findViewById(R.id.edAddress);

        edNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equalsIgnoreCase("")) {
                    return;
                }
                if (isPhoneInput) {
                    edname.setText("");
                    edAddress.setText("");
                }

                isPhoneInput = false;

                if (edname.getText().toString().length() < 1 && edAddress.getText().toString().length() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (AllUserModel data : ArrayAlluserlist) {

                                Log.e("ArrayAlluserlist", ArrayAlluserlist.size() + " " + data.getPhone());


                                if (data.getPhone().equalsIgnoreCase(charSequence.toString())) {
                                    isPhoneInput = true;
                                    /*edname.setText(data.getUsername());
                                    edAddress.setText(data.getAddress());*/

                                    setAdapter();

                                    break;
                                } else {
                                    ArrayAdapter adapter = new ArrayAdapter(BitcoinAddressAddedList.this, R.layout.adapter_list, nonUserList);
                                    edNumber.setAdapter(adapter);
                                    edname.setText("");
                                    edAddress.setText("");
                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equalsIgnoreCase("")) {
                    return;
                }

                if (isAddrassInput) {
                    edNumber.setText("");
                    edname.setText("");
                }

                isAddrassInput = false;

                if (edname.getText().toString().length() < 1 && edNumber.getText().toString().length() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (AllUserModel data : ArrayAlluserlist) {
                                if (data.getAddress().equalsIgnoreCase(charSequence.toString())) {
                                    isAddrassInput = true;
                                    edNumber.setText(data.getPhone());
                                    edname.setText(data.getUsername());
                                    break;
                                } else {
                                    edNumber.setText("");
                                    edname.setText("");
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvQrScanner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verification()){

                    dialog.dismiss();

                    Log.e("addphone-->",edNumber.getText().toString());
                    Log.e("your-->",PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.phone));

                    if(!edNumber.getText().toString().equals(PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.phone))) {


                        Intent intent1 = new Intent(BitcoinAddressAddedList.this, CheckSecurePin.class);
                        intent1.putExtra("key", "addbitAddress");
                        intent1.putExtra("address", edAddress.getText().toString());
                        intent1.putExtra("phone", edNumber.getText().toString());
                        intent1.putExtra("name", edname.getText().toString());
                        if(key.equals("send")) {
                            intent1.putExtra("data_key", getIntent().getStringExtra("data_key"));
                            intent1.putExtra("data", getIntent().getStringExtra("data"));
                        }else{
                            intent1.putExtra("data_key", getIntent().getStringExtra("data_key"));
                            intent1.putExtra("data", getIntent().getStringExtra("data"));
                        }
                        intent1.putExtra("amount", getIntent().getStringExtra("amount"));
                        intent1.putExtra("charge", getIntent().getStringExtra("charge"));
                        intent1.putExtra("amount_inr", getIntent().getStringExtra("amount_inr"));
                        intent1.putExtra("charge_inr", getIntent().getStringExtra("charge_inr"));
                        intent1.putExtra("final_charge", getIntent().getStringExtra("final_charge"));
                        intent1.putExtra("gst", getIntent().getStringExtra("gst"));
                        intent1.putExtra("rate", getIntent().getStringExtra("rate"));
                        intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());


                        Log.e("BTCCCC", "onClick: "+getIntent().getStringExtra("data_key")+" "+
                                getIntent().getStringExtra("data"));

                        startActivity(intent1);
                    }
                    else {

                        Constant.alertDialog(BitcoinAddressAddedList.this,"You can't Add Your Account");
                    }

                }

            }
        });
    }

    public void alertDialognew(String addr,String name,String number) {

        callfinduser();

        final Dialog dialog = new Dialog(BitcoinAddressAddedList.this, R.style.StatisticsDialog);

        dialog.setCancelable(false);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView2 = li.inflate(R.layout.add_bitcoin_address, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        tvQrScanner1 = (TextView) promptsView2.findViewById(R.id.tvQrScanner);
        edNumber = (AutoCompleteTextView) promptsView2.findViewById(R.id.edNumber);
        edname = (TextView) promptsView2.findViewById(R.id.edname);
        edAddress = (TextView) promptsView2.findViewById(R.id.edAddress);

        edAddress.setText(addr);
        edname.setText(name);
        edNumber.setText(number);

        edNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equalsIgnoreCase("")) {
                    return;
                }
                if (isPhoneInput) {
                    edname.setText("");
                    edAddress.setText("");
                }

                isPhoneInput = false;

                if (edname.getText().toString().length() < 1 && edAddress.getText().toString().length() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (AllUserModel data : ArrayAlluserlist) {

                                Log.e("ArrayAlluserlist", ArrayAlluserlist.size() + " " + data.getPhone());


                                if (data.getPhone().equalsIgnoreCase(charSequence.toString())) {
                                    isPhoneInput = true;
                                    /*edname.setText(data.getUsername());
                                    edAddress.setText(data.getAddress());*/

                                    setAdapter();

                                    break;
                                } else {
                                    ArrayAdapter adapter = new ArrayAdapter(BitcoinAddressAddedList.this, R.layout.adapter_list, nonUserList);
                                    edNumber.setAdapter(adapter);
                                    edname.setText("");
                                    edAddress.setText("");
                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equalsIgnoreCase("")) {
                    return;
                }

                if (isAddrassInput) {
                    edNumber.setText("");
                    edname.setText("");
                }

                isAddrassInput = false;

                if (edname.getText().toString().length() < 1 && edNumber.getText().toString().length() < 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (AllUserModel data : ArrayAlluserlist) {
                                if (data.getAddress().equalsIgnoreCase(charSequence.toString())) {
                                    isAddrassInput = true;
                                    edNumber.setText(data.getPhone());
                                    edname.setText(data.getUsername());
                                    break;
                                } else {
                                    edNumber.setText("");
                                    edname.setText("");
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvQrScanner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(verification()){

                    dialog.dismiss();

                    Log.e("addphone-->",edNumber.getText().toString());
                    Log.e("your-->",PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.phone));

                    if(!edNumber.getText().toString().equals(PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.phone))) {


                        Intent intent1 = new Intent(BitcoinAddressAddedList.this, CheckSecurePin.class);
                        intent1.putExtra("key", "addbitAddress");
                        intent1.putExtra("address", edAddress.getText().toString());
                        intent1.putExtra("phone", edNumber.getText().toString());
                        intent1.putExtra("name", edname.getText().toString());

                        if(key.equals("send")) {
                            intent1.putExtra("data_key", getIntent().getStringExtra("data_key"));
                            intent1.putExtra("data", getIntent().getStringExtra("data"));
                        }else{
                            if(getIntent().hasExtra("data_key")) {
                                intent1.putExtra("data_key", getIntent().getStringExtra("data_key"));
                                intent1.putExtra("data", getIntent().getStringExtra("data"));
                            }else{
                                intent1.putExtra("data_key", "0");
                                intent1.putExtra("data", "");
                            }
                        }

                        intent1.putExtra("amount", getIntent().getStringExtra("amount"));
                        intent1.putExtra("charge", getIntent().getStringExtra("charge"));
                        intent1.putExtra("amount_inr", getIntent().getStringExtra("amount_inr"));
                        intent1.putExtra("charge_inr", getIntent().getStringExtra("charge_inr"));
                        intent1.putExtra("final_charge", getIntent().getStringExtra("final_charge"));
                        intent1.putExtra("gst", getIntent().getStringExtra("gst"));
                        intent1.putExtra("rate", getIntent().getStringExtra("rate"));
                        intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());

                        Log.e("BTCCCC", "onClick:1 "+getIntent().getStringExtra("data_key")+" "+
                                getIntent().getStringExtra("data"));

                        startActivity(intent1);



                        /*JSONObject postParam = new JSONObject();
                        try {
                            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this, Constant.ID));
                            postParam.put("address", edAddress.getText().toString());
                            postParam.put("phone", edNumber.getText().toString());
                            postParam.put("name", edname.getText().toString());

                            Log.e("postparam--->", postParam.toString());

                            if (Constant.isConnectingToInternet(BitcoinAddressAddedList.this)) {
                                Log.e("connect--->", "yes");
                                new Retrofit2(BitcoinAddressAddedList.this, BitcoinAddressAddedList.this, postParam, Constant.REQ_AddAddress, Constant.AddAddress, "3").callService(true);
                            } else {

                                Log.e("connect--->", "no");
                                Constant.alertDialog(BitcoinAddressAddedList.this, getResources().getString(R.string.check_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                    else {

                        Constant.alertDialog(BitcoinAddressAddedList.this,"You can't Add Your Account");
                    }

                }

            }
        });
    }



    public boolean verification() {

        if(edNumber.getText().toString().equals("")){

            Constant.alertDialog(this,"Please enter Receiver Phone number");
            return false;
        }

        if(edname.getText().toString().equals("")){

            Constant.alertDialog(this,"Please enter Receiver name");
            return false;
        }

        if(edAddress.getText().toString().equals("")){

            Constant.alertDialog(this,"Please enter Receiver Acepay Address");
            return false;
        }

        if(edAddress.getText().toString().equalsIgnoreCase("Not Verified Account")){

            Constant.alertDialog(this,"This account is not verified.");
            return false;
        }

        return true;

    }

    public void removeaddrss()
    {
        JSONObject postParam = new JSONObject();
        try {
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this,Constant.ID));
            // postParam.put("id", position);
            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(BitcoinAddressAddedList.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(this, BitcoinAddressAddedList.this, postParam, Constant.REQ_REMOVEBITCOINADDRESS, Constant.REMOVEBITCOINADDRESS, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(BitcoinAddressAddedList.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_FIND_ADDRESS:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());
                        if(result.has("data")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.length() > 0) {
                                name = data.getString("name");
                                phone = data.getString("phone");
                            } else {
                                name = "";
                                phone = "";
                            }
                        }else{
                            name = "";
                            phone = "";
                        }


                        alertDialognew(receiveraddress,name,phone.trim());

                        Log.e("ReceiverAddress",receiveraddress);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{

                }

                break;


            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String status = result1.getString("response");
                        String message = result1.getString("message");
                        callService();

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this, Constant.Currency_Symbol));
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

            case Constant.REQ_FIND_USER:

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
                            allUserModel.setAddress(obj.getString("block_address").replace("null","Not verified Account"));
                            AlluserListname.add(obj.getString("phone"));
                            ArrayAlluserlist.add(allUserModel);
                        }

                        //setAdapter();

                        Log.e("ArrayAlluserlist-->",ArrayAlluserlist.size()+"");
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_BTC_RATE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());


                        String status = result.getString("response");
                        String message = result.getString("message");
                        callservice();
//                        llTwo.setVisibility(View.VISIBLE);
//                        llone.setVisibility(View.VISIBLE);
//                        tvPleaseWait.setVisibility(View.GONE);

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy")))+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
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

                        } else
                        {
                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY)!=null) {
                                // buyrate=Double.valueOf(PreferenceFile.getInstance().getPreferenceData(this,Constant.BUY));
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

                        callservice();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                } else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;


            case Constant.REQ_AddAddress:

                if (response.isSuccessful()) {
                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true"))
                        {
                            callservice();
                        }
                        else
                        {
                            Constant.alertDialog(this,message);
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
                    Log.e("sssresult--->", result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        AllUserModel.clear();
                        JSONArray data = result.getJSONArray("data");

                        for (int x = 0; x < data.length(); x++)
                        {

                            JSONObject obj = data.getJSONObject(x);
                            BitcoinAddressModel allUserModel = new BitcoinAddressModel();

                            allUserModel.setId(obj.getString("id"));
                            positionn=obj.getString("id");

                            if(!obj.isNull("AddUser"))
                            {
                                JSONObject User = obj.getJSONObject("AddUser");

                                allUserModel.setUsername(User.getString("first_name") + " " + User.getString("last_name"));
                                allUserModel.setRegister_no(obj.getString("phone"));
                                allUserModel.setProfile(User.getString("image"));
                                allUserModel.setBitcoin_address(obj.getString("address").replace("null", "Not verified Account"));
                                AllUserModel.add(allUserModel);
                            }
                            else
                            {
                                allUserModel.setUsername(obj.getString("username"));
                                allUserModel.setRegister_no(obj.getString("phone"));
                                allUserModel.setProfile(" ");
                                allUserModel.setBitcoin_address(obj.getString("address").replace("null", "Not verified Account"));
                                AllUserModel.add(allUserModel);
                            }

                            Log.e("AllUserModel-->",AllUserModel.size()+"");
                        }


                        //  Log.e("amount-->",amount);9
                        adapter = new BitCoinAddressAdapter(BitcoinAddressAddedList.this,
                                tvBitcoin.getText().toString().trim(), AllUserModel,key,amount,charge,amount_inr,charge_inr,final_charge,gst,rate);
                        recyclerView.setAdapter(adapter);


                        adapter.setOnItemClickListener(new BitCoinAddressAdapter.MyClickListener() {
                            @Override
                            public void OnItemClickView(View v, int position) {
                                Log.e("your-->",PreferenceFile.getInstance().getPreferenceData(BitcoinAddressAddedList.this,Constant.phone));


                                Intent intent1 = new Intent(BitcoinAddressAddedList.this, CheckSecurePin.class);
                                intent1.putExtra("key", "removeaddress");
                                intent1.putExtra("actual_btc", tvBitcoin.getText().toString().trim());
                                Log.e("positionnnn",positionn);
                                Log.e("actual_btc",tvBitcoin.getText().toString().trim()+"");
                                intent1.putExtra("id",positionn);
                                startActivity(intent1);
                                //  removeaddrss();
                                //AllUserModel.remove(position);
                                //adapter.notifyItemRemoved(position);
                                //adapter.notifyDataSetChanged();
                                Log.e("bfdsjf","ghdhjfbdukjf");
                            }


                        });


                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_REMOVEBITCOINADDRESS:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        Log.e("delete","yes");
                        String status = result.getString("response");
                        String message = result.getString("message");

                        if (status.equals("true")) {
                            Constant.alertDialog(this,message);

                        }
                        else
                        {
                            Constant.alertDialog(this,message);

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

    public void setAdapter() {


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
                        edname.setText(ArrayAlluserlist.get(x).getUsername());
                        edAddress.setText(ArrayAlluserlist.get(x).getAddress());
                        Log.e("arrayusername ",ArrayAlluserlist.get(x).getUsername());

                    }
                }
            }
        });
    }
}
