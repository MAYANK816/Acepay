package com.acepay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Model.AllUserModel;
import com.acepay.Model.CustomModel;
import com.acepay.Util.Constant;
import com.acepay.Util.DecimalDigitsInputFilter;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.zxing.integration.android.IntentIntegrator;
import com.hbb20.CountryCodePicker;
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

public class MoneyTranfer extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow;
    RelativeLayout imView;
    TextView txName,tvmaxmin,tvnote,currentsymbol,tvwallet,tvBitcoin,tvINR,txBitcoin,tvSellrate;
    TextView txNext,tvQrScanner;
    EditText edAmount,edusername;
    AutoCompleteTextView edPhn;
    boolean doubleBackToExitPressedOnce = false;
    private IntentIntegrator qrScan;
    public final static int QRcodeWidth = 500;
    ArrayList<CustomModel> customModels;
    CountryCodePicker ccpPicker;
    Bitmap bitmap;
    LinearLayout lnrefresh;
    ArrayList<AllUserModel> ArrayAlluserlist;
    ArrayList<String> AlluserListname;
    private NumberFormat formatter;
    private DecimalFormat formatter1;
    Typeface tfArchitectsDaughter;
    int flag=1;
    ArrayList<String> nonUserList;
    String phn;
    String positionn="", receiveraddress="",name="",phone="";

    String max, min;
    private static final int BARCODE = 10;
//    currentsymbol,ivarrow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_tranfer);
//            ivarrow = (ImageView) findViewById(R.id.ivarrow);
        nonUserList=new ArrayList<>();
     //   txName = (TextView) findViewById(R.id.txName);
        edAmount = (EditText) findViewById(R.id.edAmount);
        edPhn = (AutoCompleteTextView) findViewById(R.id.edPhn);
        edusername = (EditText) findViewById(R.id.edusername);

        tvwallet = (TextView) findViewById(R.id.tvwallet);
        tvmaxmin = (TextView) findViewById(R.id.tvmaxmin);
        tvQrScanner = (TextView) findViewById(R.id.tvQrScanner);
        tvnote = (TextView) findViewById(R.id.tvnote);
        txNext = (TextView) findViewById(R.id.txNext);
        txBitcoin = (TextView) findViewById(R.id.txBitcoin);
        ccpPicker = (CountryCodePicker) findViewById(R.id.ccpPicker);
//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        tvSellrate= (TextView) findViewById(R.id.tvSellrate);

        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(MoneyTranfer.this, Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);

        formatter1 = new DecimalFormat("#0.00");


        tvINR = (TextView) findViewById(R.id.tvINR);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");
        ArrayAlluserlist=new ArrayList<>();
        AlluserListname=new ArrayList<>();

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);
        edAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
//        ivarrow.setOnClickListener(this);
//        imView.setOnClickListener(this);
        tvQrScanner.setOnClickListener(this);
        txNext.setOnClickListener(this);
//        txName.setVisibility(View.VISIBLE);
//        txName.setText("Money Transfer");
        customModels=new ArrayList<>();
        callService();

        qrScan = new IntentIntegrator(this);

        Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));

        String finacal = BigDecimal.valueOf(bit).toPlainString();


        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));

        if(inr>0) {
            tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        else
        {
            tvwallet.setText("0.00" + " " +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
        }
        Double finacal1 = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());


        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal1));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        edPhn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                if (edPhn.getText().toString().length() == 10)     //size as per your requirement
                    if (edPhn.getText().toString().length() == 10)     //size as per your requirement
                    {
                        if(!edPhn.getText().toString().trim().equals(PreferenceFile.getInstance().getPreferenceData
                                (MoneyTranfer.this,Constant.phone))) {
                            setAdapter();
                        }else{
                            Constant.alertDialog(MoneyTranfer.this,"You can't add your own account.");

                        }
                    } else {
                        edusername.setText("");
                        ArrayAdapter adapter = new ArrayAdapter(MoneyTranfer.this, R.layout.adapter_list, nonUserList);
                        edPhn.setAdapter(adapter);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edPhn.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    Log.e("button-->","yes");
                    edusername.requestFocus();
                }
                return false;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            txBitcoin.setText(formatter.format(Double.valueOf(buy)) + " "
                    + PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.Currency_Symbol));
            tvSellrate.setText(formatter.format(Double.valueOf(sell)) + " "
                    + PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.Currency_Symbol));

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            if (buy_rate > 0) {

                tvINR.setText(formatter.format(buy_rate) + " "
                        + PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.Currency_Symbol));
            } else {
                tvINR.setText("0.00" + " "
                        + PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.Currency_Symbol));
            }
        }
    };

    public void setAdapter(){

        for(int x=0;x<AlluserListname.size();x++)
        {
            if(ArrayAlluserlist.get(x).getPhone().equals(edPhn.getText().toString()))
            {
                edusername.setText(ArrayAlluserlist.get(x).getUsername());

                Log.e("arrayusername ",ArrayAlluserlist.get(x).getUsername());
            }
        }

        Log.e("size-->",AlluserListname+"");
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.adapter_list,AlluserListname);
        edPhn.setAdapter(adapter);
        edPhn.setThreshold(1);
        Log.e("searchlistsize", String.valueOf(AlluserListname.size()));
        edPhn.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                for(int x=0;x<AlluserListname.size();x++)
                {
                    if(ArrayAlluserlist.get(x).getPhone().equals(edPhn.getText().toString()))
                    {
                        edusername.setText(ArrayAlluserlist.get(x).getUsername());

                        Log.e("arrayusername ",ArrayAlluserlist.get(x).getUsername());
                    }
                }
            }
        });
    }


    private void minmaxService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, MoneyTranfer.this, Constant.REQ_MIN_MAX, Constant.MIN_MAX + PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)).callService(true);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
//                    Log.e("codeDisplayValue", "onActivityResult: "+data.getStringExtra("resultDisplayValue") );
            //if qrcode has nothing in it
            if (data == null) {
                Toast.makeText(this, "Number Not Found", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
//                            Log.e("getContents()",result.getContents());
                        if(data.getStringExtra("resultDisplayValue")!=null) {
                            receiveraddress=data.getStringExtra("resultDisplayValue");
                            Log.e("NumberQR", receiveraddress.substring(6,16));
                            Log.e("UsernameQR", receiveraddress.substring(24));
                            edPhn.setText(receiveraddress.substring(6,16));
                            edusername.setText(receiveraddress.substring(24));

                            findAddress(receiveraddress);
                        }
                        else {
                            Toast.makeText(this, "Wrong QR code", Toast.LENGTH_SHORT).show();
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
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Number Not Found", Toast.LENGTH_LONG).show();
//            }
//            else {
//                try {
//                    JSONObject obj = new JSONObject(result.getContents());
//
//                    Log.e("result--->",obj.toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                }
//
//              //  edPhn.setText(result.getContents());
//
//                if(result.getContents()!=null){
//
//                    if(result.getContents().contains("Number")) {
//
//                        flag = 2;
//                        String CurrentString = result.getContents();
//                        String[] separated = CurrentString.split("Number");
//                        String number0=separated[0];
//                        String number=separated[1];
//                        String[] username = number.split("Username");
//                        String finalnumber=username[0];
//                        String finalusername=username[1];
//                        edPhn.setText(finalnumber);
//                        edusername.setText(finalusername);
//                        phn = number;
//
//
//                    }
//                    else {
//                        Toast.makeText(this, "Wrong QR code", Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }


    private void findAddress(String receiveraddress){

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("address", receiveraddress.trim());

            Log.e("findAddress", "findAddress: "+receiveraddress.trim());

            if (Constant.isConnectingToInternet(MoneyTranfer.this)) {
                new Retrofit2(MoneyTranfer.this, MoneyTranfer.this, postParam,
                        Constant.REQ_FIND_ADDRESS, Constant.FIND_ADDRESS, "3").callService(true);
            } else {
                Constant.alertDialog(MoneyTranfer.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, MoneyTranfer.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
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

            case R.id.tvQrScanner:
                Intent i=new Intent(this,BarcodeCustomScanner.class);
                startActivityForResult(i,BARCODE);
//                qrScan.initiateScan();

                break;

            case R.id.txNext:

                if(verification()) {

                    if(!(Double.parseDouble(edAmount.getText().toString())>Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this,Constant.Inr_Amount)))) {

                        if (Double.parseDouble(edAmount.getText().toString()) >=
                                (Double.parseDouble(formatter1.format(Double.valueOf(min)))) &&
                                Double.parseDouble(edAmount.getText().toString())
                                        < (Double.parseDouble(formatter1.format(Double.valueOf(max))) )) {

                            Intent intent1 = new Intent(MoneyTranfer.this, CheckSecurePin.class);
                            intent1.putExtra("key", "moneytransfer");
                            intent1.putExtra("phone", edPhn.getText().toString());
                            intent1.putExtra("amount", edAmount.getText().toString());
                            startActivity(intent1);
                        }
                        else {

                            Constant.alertDialog(this,"Please enter valid amount.");
                        }

                    }else {

                        Constant.alertDialog(this,getString(R.string.do_not_have_enough_balance));
                    }
                }

                break;
        }

    }

    public  boolean verification() {

        Log.e("verification--->", ccpPicker.getSelectedCountryCode());

            if (edPhn.getText().toString().equals("")) {
                Constant.alertDialog(this, "Please enter register number.");
                return false;
            }

        if (edusername.getText().toString().equals("")) {
                Constant.alertDialog(this, "Please enter username.");
                return false;
            }

        if (edAmount.getText().toString().equals("")) {

            Constant.alertDialog(this, "Please enter amount.");
            return false;
        }
        if (edAmount.getText().toString().equals(".")) {

            Constant.alertDialog(this, "Please enter valid amount.");
            return false;
        }

        if (Double.parseDouble(edAmount.getText().toString())==0) {

            Constant.alertDialog(this, "Please enter valid amount.");
            return false;
        }

        return true;

    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, MoneyTranfer.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void callservice(){

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, MoneyTranfer.this, Constant.REQ_FIND_USER,Constant.FIND_USER).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_FIND_ADDRESS:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("REQ_FIND_ADDRESS", "onServiceResponse: "+result.toString() );

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
//                        alertDialognew(receiveraddress,name,phone.trim());

                        flag = 2;


                        edPhn.setText(phone.trim());
                        edusername.setText(name);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

                }

                break;


            case Constant.REQ_MIN_MAX:

                try {

                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);

                            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.BITCOIN_ADDRESS) != null) {

                                if (CountryObj.getString("type").equals("8")) {


                                    Double min1 = Double.parseDouble(CountryObj.getString("min"));
                                    Double max1 = Double.parseDouble(CountryObj.getString("max"));


                                    min = String.valueOf(formatter1.format(Double.valueOf(min1)));
                                    max = String.valueOf(formatter1.format(Double.valueOf(max1)));

                                    Log.e("REQ_MIN_MAX", "onServiceResponse:" + min + " " + max);

                                    tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) +
                                            " " + PreferenceFile.getInstance().getPreferenceData(this,
                                            Constant.Currency_Symbol) + " max: " +
                                            formatter.format(Double.valueOf(max)) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                    if (CountryObj.has("note")) {

                                        tvnote.setText(CountryObj.getString("note"));
                                    }
                                }

                            } else {

                                if (CountryObj.getString("type").equals("12")) {

                                    Double min1 = Double.parseDouble(CountryObj.getString("min"));
                                    Double max1 = Double.parseDouble(CountryObj.getString("max"));

                                    min = String.valueOf(formatter1.format(Double.valueOf(min1)));
                                    max = String.valueOf(formatter1.format(Double.valueOf(max1)));

                                    Log.e("REQ_MIN_MAX", "onServiceResponse:" + min + " " + max);

                                    tvmaxmin.setText("min: " + formatter.format(Double.valueOf(min)) + " " +
                                            "" + PreferenceFile.getInstance().getPreferenceData
                                            (this, Constant.Currency_Symbol) + " max: " +
                                            formatter.format(Double.valueOf(max)) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                                    if (CountryObj.has("note")) {

                                        tvnote.setText(CountryObj.getString("note"));
                                    }

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(MoneyTranfer.this, Constant.Currency_Symbol));

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

            case Constant.REQ_FIND_USER:

                try {

                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result--->",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    minmaxService();

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
                        }

                       // setAdapter();

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
                        callservice();

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell"))) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
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


            case Constant.REQ_MONEY_TRANSFER:

                try {

                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result--->",result.toString());

                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        JSONObject data=result.getJSONObject("data");

                        PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,data.getString("btc_amount"));
                        PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,data.getString("inr_amount"));

                        Constant.alertWithIntent(this,message,Dashboard.class);

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

}