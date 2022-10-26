package com.acepay;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


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

public class ReceiveAmount extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {
    TextView txPhone;
    ImageView ivarrow,imView;
    TextView txName,txNext,tvwallet,tvBitcoin,txBitcoin,tvSellrate,tvINR;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap;
    LinearLayout lnrefresh;
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog progressDialog;
    NumberFormat formatter;
    DecimalFormat formatter1;
    Typeface tfArchitectsDaughter;
//    currentsymbol   ,ivarrow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_amount);

        txPhone= (TextView) findViewById(R.id.txPhone);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txBitcoin= (TextView) findViewById(R.id.txBitcoin);
        txName= (TextView) findViewById(R.id.txName);
        tvSellrate = (TextView) findViewById(R.id.tvSellrate);
        tvBitcoin= (TextView) findViewById(R.id.tvBitcoin);
        tvwallet= (TextView) findViewById(R.id.tvwallet);
        tvINR= (TextView) findViewById(R.id.tvINR);
//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        imView= (ImageView) findViewById(R.id.imView);


        formatter = NumberFormat.getCurrencyInstance(new Locale
                ("en", PreferenceFile.getInstance().
                        getPreferenceData(ReceiveAmount.this,
                                Constant.selectedCountryNameCode).toString()));

        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

        txPhone.setText("Your Registered Mobile Number : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.phone));

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

//        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Receive Money");

        callService();

        Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));

        String finacal = BigDecimal.valueOf(bit).toPlainString();


//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

        Double finacal1 = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());



        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal1));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            txBitcoin.setText(formatter.format(Double.valueOf(buy))+" "+
                    PreferenceFile.getInstance().getPreferenceData(ReceiveAmount.this,Constant.Currency_Symbol));
            tvSellrate.setText
                    (formatter.format(Double.valueOf(sell))+" "+PreferenceFile.getInstance().getPreferenceData
                            (ReceiveAmount.this,Constant.Currency_Symbol));

            Double buy_rate=Double.parseDouble
                    (PreferenceFile.getInstance().getPreferenceData(ReceiveAmount.this,Constant.BTC_amount))*Double.parseDouble(buy);

            if(buy_rate>0) {

                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().
                        getPreferenceData(ReceiveAmount.this, Constant.Currency_Symbol));

            }else
            {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(ReceiveAmount.this, Constant.Currency_Symbol));
            }


        }
    };


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);

                if(x==bitMatrixWidth-1){
                    progressDialog.dismiss();

                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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


    void request() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            progressDialog = new ProgressDialog(new ContextThemeWrapper(ReceiveAmount.this, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(ReceiveAmount.this);
        }
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, ReceiveAmount.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

            case R.id.ivarrow:

                finish();

                break;

            case R.id.lnrefresh:

                newRefereshing();

                break;

        }
    }

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, ReceiveAmount.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
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

                        if (status.equals("true")){

                            // PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"0");
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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(ReceiveAmount.this, Constant.Currency_Symbol));

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

                        request();

                        try {

                            bitmap = TextToImageEncode("Number"+PreferenceFile.getInstance().getPreferenceData(this,Constant.phone)+"Username"+PreferenceFile.getInstance().getPreferenceData(this,Constant.Username));
                            imView.setImageBitmap(bitmap);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            txBitcoin.setText(formatter.format(Double.valueOf(data.getString("buy")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvSellrate.setText(formatter.format(Double.valueOf(data.getString("sell")))+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            Double buy_rate=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount))*Double.parseDouble(data.getString("buy"));

                            if(buy_rate>0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

                            }else
                            {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                            Log.e("data--->", data.toString());

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


        }
    }

}
