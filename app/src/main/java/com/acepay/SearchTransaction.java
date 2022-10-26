package com.acepay;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;


import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class SearchTransaction extends ToolabarActivity implements RetrofitResponse {
    TextView tvBitcoin,tvwallet,tvINR,txName,txNext,tvstatedate,tvenddate;
    Typeface tfArchitectsDaughter;
    Double finacals,bit;
    DecimalFormat formatter;
    private DatePickerDialog datePickerDialog;
//    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;
    private SimpleDateFormat dateFormatter;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transaction);

//        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        txName = (TextView) findViewById(R.id.txName);
//        currentsymbol= (TextView) findViewById(R.id.currentsymbol);
        txNext= (TextView) findViewById(R.id.txNext);
        tvwallet= (TextView) findViewById(R.id.tvwallet);
        tvenddate= (TextView) findViewById(R.id.tvenddate);
        tvINR = (TextView) findViewById(R.id.tvINR);
        tvstatedate = (TextView) findViewById(R.id.tvstatedate);

        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);
        formatter = new DecimalFormat("#,##,###.00");
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        txName.setVisibility(View.VISIBLE);
        txName.setText("Statements");

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        Log.e("bitcoin--->",finacals+"");
        Log.e("bitcoin--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
        Log.e("INR--->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");
        callService();

        Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
        tvwallet.setText(formatter.format(inr) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

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
//                finish();
//
//            }
//        });

        txNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(tvstatedate.getText().toString().equals("")){

                    Constant.alertDialog(SearchTransaction.this,"Please Select Start date");
                }

                else if(tvenddate.getText().toString().equals("")){

                    Constant.alertDialog(SearchTransaction.this,"Please Select End date");
                }

                else {

                    Intent intent=new Intent(SearchTransaction.this,Statements.class);
                    intent.putExtra("key","search");
                    intent.putExtra("start",tvstatedate.getText().toString());
                    intent.putExtra("end",tvenddate.getText().toString());
                    startActivity(intent);
                }

            }
        });

        tvstatedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                date();

            }
        });

        tvenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=2;
                date();

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

    private void date() {

        final Calendar newCalendar = Calendar.getInstance();
        final Date curDate = new Date();

        dateFormatter.format(curDate);

        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date date=newDate.getTime();
                Date date1=newCalendar.getTime();

                if(flag==1){
                    tvstatedate.setText(dateFormatter.format(newDate.getTime()));
                }else {
                    tvenddate.setText(dateFormatter.format(newDate.getTime()));
                }



            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, SearchTransaction.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }



    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {
            case Constant.REQ_BTC_RATE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());
                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data = result.getJSONObject("data");

                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount)) * Double.parseDouble(data.getString("sell"));

                            if (buy_rate > 0) {

                                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            } else {
                                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }

                            Log.e("data--->", data.toString());

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
