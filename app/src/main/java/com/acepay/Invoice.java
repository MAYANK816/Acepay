package com.acepay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class Invoice extends ToolabarActivity implements View.OnClickListener, RetrofitResponse
{

    TextView txName,tvtax,tvGst,tvcharges,tvbank,tvOrder,tvrate,tvCreditTo,tvCredit,tvaccount,tvlnrate,tvcountry,tvifsc,
            tvdate,tvtime,tvholdername,tvname,tvnumber,tvamount,tvtotle,tvstatus,tvtext,tvbankname,tvfees,tvifscname,
            tvholder,tvaccountname,    tvifscname2,tvifsc2,tvaccountname2,tvaccount2,tvholdername2,tvholder2,tvbankname2,tvbank2,
    tvbank1,tvbankname1,tvholder1,tvholdername1,tvaccount1,tvaccountname1,tvifsc1,tvifscname1;
    ImageView ivarrow;

    LinearLayout tvClose,lnNetwork,lnLayer,lnLayer1,lnShare,lnLayer2,lnCopy,lnLayer12,llINR,llBTC,llBTC1;
    String previous;
    boolean doubleBackToExitPressedOnce = false;
    String sellername,purchasename,previousblanance,amount,finalbalance,finalamount,rate;
    Double finacal;
    double network_fee=0.0;
    double original_amount;
    NumberFormat formatter;
    View vieww,vvv,vieww2,vvv2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        vvv= (View) findViewById(R.id.vvv);
        vieww= (View) findViewById(R.id.vieww);
        txName= (TextView) findViewById(R.id.txName);
        tvtext= (TextView) findViewById(R.id.tvtext);
        tvcharges= (TextView) findViewById(R.id.tvcharges);
        tvtax= (TextView) findViewById(R.id.tvtax);
        tvGst= (TextView) findViewById(R.id.tvGst);

        tvaccountname= (TextView) findViewById(R.id.tvaccountname);
        lnCopy= (LinearLayout) findViewById(R.id.lnCopy);
        lnNetwork= (LinearLayout) findViewById(R.id.lnNetwork);
        tvClose= (LinearLayout) findViewById(R.id.tvClose);
        lnLayer= (LinearLayout) findViewById(R.id.lnLayer);
        lnLayer1= (LinearLayout) findViewById(R.id.lnLayer1);
        lnShare= (LinearLayout) findViewById(R.id.lnShare);

        tvtotle= (TextView) findViewById(R.id.tvtotle);
        tvholdername= (TextView) findViewById(R.id.tvholdername);
        tvlnrate= (TextView) findViewById(R.id.tvlnrate);
        tvfees= (TextView) findViewById(R.id.tvfees);
        tvcountry= (TextView) findViewById(R.id.tvcountry);
        tvifsc= (TextView) findViewById(R.id.tvifsc);
        tvholder= (TextView) findViewById(R.id.tvholder);

        tvOrder= (TextView) findViewById(R.id.tvOrder);
        tvbankname= (TextView) findViewById(R.id.tvbankname);
        tvCreditTo= (TextView) findViewById(R.id.tvCreditTo);
        tvCredit= (TextView) findViewById(R.id.tvCredit);
        tvbank= (TextView) findViewById(R.id.tvbank);
        tvtime= (TextView) findViewById(R.id.tvtime);

        tvdate= (TextView) findViewById(R.id.tvdate);
        tvamount= (TextView) findViewById(R.id.tvamount);
        tvrate= (TextView) findViewById(R.id.tvrate);
        tvifscname= (TextView) findViewById(R.id.tvifscname);
        tvaccount= (TextView) findViewById(R.id.tvaccount);
        tvname= (TextView) findViewById(R.id.tvname);
        tvstatus= (TextView) findViewById(R.id.tvstatus);

        tvnumber= (TextView) findViewById(R.id.tvnumber);
        tvbank1= (TextView) findViewById(R.id.tvbank1);
        tvbankname1= (TextView) findViewById(R.id.tvbankname1);
        tvholder1= (TextView) findViewById(R.id.tvholder1);
        tvholdername1= (TextView) findViewById(R.id.tvholdername1);
        tvaccount1= (TextView) findViewById(R.id.tvaccount1);
        tvaccountname1= (TextView) findViewById(R.id.tvaccountname1);

        tvifsc1= (TextView) findViewById(R.id.tvifsc1);
        tvifscname1= (TextView) findViewById(R.id.tvifscname1);
        llBTC= (LinearLayout) findViewById(R.id.llBTC);
        llINR= (LinearLayout) findViewById(R.id.llINR);




        llBTC1= (LinearLayout) findViewById(R.id.llBTC1);
        lnLayer12= (LinearLayout) findViewById(R.id.lnLayer12);
        lnLayer2= (LinearLayout) findViewById(R.id.lnLayer2);
        vieww2= (View) findViewById(R.id.vieww2);
        vvv2= (View) findViewById(R.id.vvv2);
        tvifscname2= (TextView) findViewById(R.id.tvifscname2);


        tvifsc2= (TextView) findViewById(R.id.tvifsc2);
        tvaccountname2= (TextView) findViewById(R.id.tvaccountname2);
        tvaccount2= (TextView) findViewById(R.id.tvaccount2);
        tvholdername2= (TextView) findViewById(R.id.tvholdername2);
        tvholder2= (TextView) findViewById(R.id.tvholder2);
        tvbankname2= (TextView) findViewById(R.id.tvbankname2);
        tvbank2= (TextView) findViewById(R.id.tvbank2);



        formatter = NumberFormat.getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                getPreferenceData(Invoice.this, Constant.selectedCountryNameCode).toString()));
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) formatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(decimalFormatSymbols);


        txName.setText("Invoice");
        tvClose.setOnClickListener(this);
        lnShare.setOnClickListener(this);
        lnCopy.setOnClickListener(this);

        callService();
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                Intent intent=new Intent(Invoice.this,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);

    }

    public void callService() {

        try {

            if (getIntent().getStringExtra("key").equals("inrtransfer")) {

                JSONObject postParam = new JSONObject();
                try {
                    // postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "1");


                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr,
                                Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("InrReceive")) {

                JSONObject postParam = new JSONObject();
                try {
                    // postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "2");


                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("withdraw")) {

                JSONObject postParam = new JSONObject();
                try {
                    // postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "4");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("deposit")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "3");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("payu")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "5");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("paypal")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "6");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("buy")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "5");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice, Constant.Invoice, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("bidding")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "3");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice, Constant.Invoice, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (getIntent().getStringExtra("key").equals("asking")) {

                JSONObject postParam = new JSONObject();
                try {
                    //postParam.put("user_id","56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "2");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice, Constant.Invoice, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("transfer_bitcoin")) {

                JSONObject postParam = new JSONObject();
                try {
                    //  postParam.put("user_id", "56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "1");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice, Constant.Invoice, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("sell")) {

                JSONObject postParam = new JSONObject();
                try {
                    // postParam.put("user_id", "56");
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "4");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice, Constant.Invoice, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("key").equals("moneyTransfer")) {

                JSONObject postParam = new JSONObject();
                try {
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("order_id", getIntent().getStringExtra("order_id"));
                    postParam.put("type", "1");

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(Invoice.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(Invoice.this, Invoice.this, postParam, Constant.REQ_Invoice_inr, Constant.Invoice_inr, "3").callService(true);
                    } else {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(Invoice.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId()){

            case R.id.ivarrow:
                finish();
                Intent intent1=new Intent(Invoice.this,Dashboard.class);
                startActivity(intent1);

                break;

            case R.id.lnCopy:

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipData clip=ClipData.newPlainText("lable"," ");

                if(getIntent().getStringExtra("key").equals("buy")) {
                    clip = ClipData.newPlainText("label", "Seller name: "+getResources().getString(R.string.app_name)+" India"+"\n"+ "Purchased Using : "+getResources().getString(R.string.app_name)+" Wallet"+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("bidding")) {
                    clip = ClipData.newPlainText("label", "Seller name: "+getResources().getString(R.string.app_name)+" India"+"\n"+ "Purchased Using : "+getResources().getString(R.string.app_name)+" Wallet"+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("sell")) {
                    clip = ClipData.newPlainText("label","Seller name: "
                            +PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)
                            +" wallet "+"\n"+ "Purchased : "+getResources().getString(R.string.app_name)+" India"+"\n"+"Previous Balance : "
                            +previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "
                            +tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()
                            +"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("asking")) {
                    clip = ClipData.newPlainText("label","Seller name: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)+" wallet "+"\n"+ "Purchased : "+getResources().getString(R.string.app_name)+" India"+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("moneyTransfer")) {
                    clip = ClipData.newPlainText("label", "Sender name: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)+"\n"+ "Receiver name : "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("InrReceive")) {
                    clip = ClipData.newPlainText("label",  "Credit To : "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "+tvbankname.getText().toString()+"\n"+"Amount : "+tvholdername.getText().toString()+"\n"+"Total Amount : "+tvaccountname.getText().toString().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }

                if(getIntent().getStringExtra("key").equals("withdraw")) {
                    clip = ClipData.newPlainText("label",  "Bank name : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Account number: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER)+"\n"+"IFSC CODE: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }

                if(getIntent().getStringExtra("key").equals("inrtransfer")) {
                    clip = ClipData.newPlainText("label",  "Credit To : "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "
                            +tvbankname.getText().toString()+"\n"+"Amount : "+tvholdername.getText().toString()+"\n"+"Total Amount : "+tvaccountname.getText().toString().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equals("deposit")) {
                    clip = ClipData.newPlainText("label", "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"Bank Name: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"IFSC CODE: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equalsIgnoreCase("paypal")) {
                    clip = ClipData.newPlainText("label", "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }
                if(getIntent().getStringExtra("key").equalsIgnoreCase("payu")) {
                    clip = ClipData.newPlainText("label", "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                }

                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "Infomation Copied", Toast.LENGTH_SHORT).show();

                break;

            case R.id.lnShare:

                if(getIntent().getStringExtra("key").equals("buy")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+getResources().getString(R.string.app_name)+" India"+"\n"+ "Purchased Using : "+getResources().getString(R.string.app_name)+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using" ));
                }
                if(getIntent().getStringExtra("key").equals("bidding")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+getResources().getString(R.string.app_name)+" India"+"\n"+ "Purchased Using : "+getResources().getString(R.string.app_name)+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using" ));
                }

                if(getIntent().getStringExtra("key").equals("transfer_bitcoin")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Send Amount : "+"From Your Account"+"\n"+ "Credit to: "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "+previous+" "+" Ace"+"\n"+"Amount : "+tvamount.getText().toString()+" Ace"+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+" Ace"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using" ));
                }

                if(getIntent().getStringExtra("key").equals("sell")) {

                    Log.e("AmountShare----",tvamount.getText().toString());
                    Log.e("TotalAmount----",tvtotle.getText().toString());

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+
                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)
                            +" wallet "+"\n"+ "Purchased : "+getResources().getString(R.string.app_name)+" India"+"\n"+"Previous Balance : "
                            +previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "
                            +tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()
                            +"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using" ));
                }

                if(getIntent().getStringExtra("key").equals("moneyTransfer")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Sender name: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)+"\n"+ "Receiver name : "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "+previous+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Rate Amount : "+tvrate.getText().toString()+"\n"+"Fee : "+"0.0"+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                if(getIntent().getStringExtra("key").equals("withdraw")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Bank name : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Account number: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER)+"\n"+"IFSC CODE: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));

                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                if(getIntent().getStringExtra("key").equals("inrtransfer")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Credit To : "
                            +tvCreditTo.getText().toString()+"\n"+"Previous Balance : "
                            +tvbankname.getText().toString()+"\n"+"Amount : "+tvholdername.getText().toString()+"\n"+"Total Amount : "
                            +tvaccountname.getText().toString().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));

                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }
                if(getIntent().getStringExtra("key").equals("InrReceive")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Credit To : "+tvCreditTo.getText().toString()+"\n"+"Previous Balance : "+tvbankname.getText().toString()+"\n"+"Amount : "+tvholdername.getText().toString()+"\n"+"Total Amount : "+tvaccountname.getText().toString().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));

                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                if(getIntent().getStringExtra("key").equals("deposit")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"Bank Name: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"IFSC CODE: "+PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                if(getIntent().getStringExtra("key").equalsIgnoreCase("paypal")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                if(getIntent().getStringExtra("key").equalsIgnoreCase("payu")) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name)+" Invoice");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Seller name: "+getResources().getString(R.string.app_name)+"\n"+"\n"+"Account Holder : "+PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)+"\n"+"Amount : "+tvamount.getText().toString()+"\n"+"Final Amount : "+tvtotle.getText().toString()+"\n"+"Order No : "+getIntent().getStringExtra("order_id"));
                    startActivity(Intent.createChooser(sharingIntent,"Share using"));
                }

                //   takeScreenshot();

                break;

            case R.id.tvClose:

                intent=new Intent(Invoice.this,Dashboard.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        switch (requestCode)
        {
            case Constant.REQ_Invoice:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());

                    tvtax.setText("0.00 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    Log.e("result-->",result1.toString());

                    if(status.equals("true")) {

                        if(getIntent().getStringExtra("key").equals("payu")) { // not to chk

                            tvlnrate.setVisibility(View.GONE);
                            tvrate.setVisibility(View.GONE);
                            lnNetwork.setVisibility(View.GONE);

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder :");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");
                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                            tvholdername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                            tvaccountname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                            tvifsc.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
                        }

                        if(getIntent().getStringExtra("key").equals("paypal")) {  //old one not to check this

                            tvlnrate.setVisibility(View.GONE);
                            tvrate.setVisibility(View.GONE);
                            lnNetwork.setVisibility(View.GONE);

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder :");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");
                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                            tvholdername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                            tvaccountname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                            tvifsc.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
                        }

                        if(getIntent().getStringExtra("key").equals("deposit")) {  //old one not to check this

                            tvlnrate.setVisibility(View.GONE);
                            tvrate.setVisibility(View.GONE);
                            lnNetwork.setVisibility(View.GONE);
                            llBTC.setVisibility(View.GONE);
                            llINR.setVisibility(View.VISIBLE);

                            tvbank1.setText("Bank Name :");
                            tvholder1.setText("Account Holder :");
                            tvaccount1.setText("Account Number :");
                            tvifsc1.setText("IFSC code :");
                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                            tvholdername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                            tvaccountname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                            tvifsc.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
                        }

                        if(getIntent().getStringExtra("key").equals("withdraw")) { //old one not to check this

                            tvlnrate.setVisibility(View.GONE);
                            tvrate.setVisibility(View.GONE);
                            llBTC.setVisibility(View.GONE);
                            llINR.setVisibility(View.VISIBLE);
                            tvbank1.setText("Bank Name :");
                            tvholder1.setText("Account Holder :");
                            tvaccount1.setText("Account Number :");
                            tvifsc1.setText("IFSC code :");

                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                            tvholdername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                            tvaccountname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                            tvifsc.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
                        }

                        if(getIntent().getStringExtra("key").equals("buy")) {
                            Log.e("buy-->","yes");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Purchased Using ");
                            tvCreditTo.setText(PreferenceFile.getInstance().
                                    getPreferenceData(Invoice.this, Constant.Currency_Symbol)+" Account");
                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";
                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject User=data.getJSONObject("User");

                            JSONObject CountryName=User.getJSONObject("CountryName");
                            tvcountry.setText(CountryName.getString("name"));

                            Double bit=null;

                            if(!data.isNull("previous_amount")) {

                                bit = Double.parseDouble(data.getString("previous_amount"));
                                finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(formatter.format(finacal) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }


                            tvOrder.setText("Order no: "+data.getString("txid"));
                            double rate = Double.parseDouble(data.getString("rate"));
                            double fee=Double.parseDouble(data.getString("fee"));

                            if(!data.isNull("fee")){

                                tvfees.setText(formatter.format(Double.valueOf(data.getString("tax")))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtax.setText(formatter.format(Double.valueOf(data.getString("gst")))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }
                            tvrate.setText(formatter.format(rate)+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));
                            String timeCreated = data.getString("created");
                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvnumber.setText(User.getString("phone"));
                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");
                            double total_btc=0;
                            if(!data.getString("total_btc").equals("null")) {
                                total_btc = Double.parseDouble(data.getString("total_btc"));
                            }
                            BigDecimal bigtotal = new BigDecimal(total_btc);
                            tvtext.setText("You have Purchased Acepay ("+String.format("%.8f",bigtotal)+" Ace)"+" " +
                                    "from your "+PreferenceFile.getInstance().getPreferenceData
                                    (Invoice.this, Constant.Currency_Code)+
                                    " Account");
                            double calcul = Double.parseDouble(data.getString("amount"));
                            double inr_amount = Double.parseDouble(User.getString("inr_amount"));

                            BigDecimal d = new BigDecimal(calcul);
                            BigDecimal InrD = new BigDecimal(inr_amount);


                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });

                            if(bit<=calcul){

                                tvholdername.setText(""+formatter.format(d)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                Double remAmt= finacal-calcul;

                                tvaccountname.setText(" "+formatter.format(remAmt)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvamount.setText(" "+formatter.format(calcul-fee)+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText("  "+formatter.format(d)+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }else{
                                tvamount.setText(" "+formatter.format(calcul-fee)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvholdername.setText(" "+formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                Double remAmt= finacal-calcul;
                                tvtotle.setText(" "+formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvaccountname.setText(" "+formatter.format(remAmt)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }


                        }


                        if(getIntent().getStringExtra("key").equals("bidding")) {

                            Log.e("bidding-->","yes");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Purchased Using ");
                            tvCreditTo.setText("Rs Account");

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);

                            //  tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace");

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject User=data.getJSONObject("User");

                            if(!data.isNull("previous_amount")) {

                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(String.format("%.0f", finacal) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order no: "+data.getString("txid"));

                            double rate = Double.parseDouble(data.getString("rate"));

                            double fee=Double.parseDouble(data.getString("fee"));

                            if(!data.isNull("fee")){

                                Log.e("fees-->",data.getString("fee"));
                                tvfees.setText(data.getString("tax")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtax.setText(data.getString("gst")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            tvrate.setText(String.format("%.0f",rate)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));

                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvnumber.setText(User.getString("phone"));


                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            double total_btc=0;

                            if(!data.getString("total_btc").equals("null")) {
                                Log.e("not-->","yes");
                                total_btc = Double.parseDouble(data.getString("total_btc"));
                                Log.e("not-->",total_btc+"");
                            }

                            BigDecimal bigtotal = new BigDecimal(total_btc);

                            tvtext.setText("You have Purchased Acepay ("+String.format("%.8f",bigtotal)+" Ace)"+" from your INR Account");

                            double calcul = Double.parseDouble(data.getString("amount"));
                            double inr_amount = Double.parseDouble(User.getString("inr_amount"));

                            BigDecimal d = new BigDecimal(calcul);
                            BigDecimal InrD = new BigDecimal(inr_amount);
                            Log.e("newcal-->","d -->"+ d);

                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setText(String.format("%.0f",calcul-fee)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvholdername.setText(String.format("%.0f",d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(String.format("%.0f",d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvaccountname.setText(String.format("%.0f",InrD)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }

                        if(getIntent().getStringExtra("key").equals("transfer_bitcoin")) {  //send

                            double rate=0;

                            Log.e("transfer_bitcoin-->","yes");

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder Name:");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Send to : ");
                            tvCreditTo.setText("Acepay Wallet ");

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject User=data.getJSONObject("User");

                            if(!data.isNull("rate")) {
                                rate= Double.parseDouble(data.getString("rate"));
                            }

                            if(!data.isNull("previous_amount")) { //5
                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                                if (bit > 0) {
                                    tvbankname.setText(formatter.format(finacal*rate) + " "
                                            +PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) );
                                } else {
                                    tvbankname.setText("0.00" + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order No: "+data.getString("txid"));

                            tvCreditTo.setText(data.getString("to_address"));

                            tvrate.setText(formatter.format(rate)+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));

                            String timeCreated = data.getString("created");

                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvnumber.setText(User.getString("phone"));

                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");


                            if(User.has("CountryName")) {
                                JSONObject CountryName = User.getJSONObject("CountryName");
                                tvcountry.setText(CountryName.getString("name"));
                            }else{
                                tvcountry.setText("");
                            }
                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvfees.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });

                            double user_btc = Double.parseDouble(User.getString("btc_amount"));
                            double amount_withdrawn = Double.parseDouble(data.getString("amount_withdrawn"));

                            tvtext.setText("Send acepay from your Account ("+
                                    String.format("%.8f", amount_withdrawn)+ "Ace) to other "+getResources().getString(R.string.app_name)+" Account with "
                                    +formatter.format(rate)+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" rate");

                            if(!data.isNull("network_fee")) {
                                network_fee = Double.parseDouble(data.getString("network_fee"));
                                BigDecimal ntF = new BigDecimal(network_fee);
                                tvfees.setText(formatter.format(ntF)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvtax.setText(formatter.format(Double.valueOf(data.getString("gst")))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }


                            if(!data.isNull("amount_sent")) {

                                double calcul = Double.parseDouble(data.getString("amount_sent"));

                                calcul=calcul/rate;
                                BigDecimal d = new BigDecimal(calcul);

                                Double totAmtt=Double.parseDouble(data.getString("amount_sent_inr").trim())
                                        -(Double.parseDouble(data.getString("gst"))
                                        +Double.parseDouble(data.getString("network_fee")));

                                Double totAmttqw11=Double.parseDouble(data.getString("amount_sent_inr").trim())
                                        +(Double.parseDouble(data.getString("gst"))
                                        +Double.parseDouble(data.getString("network_fee")));

                                Double totAmttqw=Double.parseDouble(data.getString("amount_sent_inr").trim());

                                Log.e("NETFEE","totAmtt "+totAmtt);
                                Log.e("NETFEE","totAmttqw11 "+totAmttqw11);
                                Log.e("NETFEE","totAmttqw "+totAmttqw);
                                Log.e("finacal",finacal*rate+"");
                                //prevAmt ==Amt Withdrawn
                                Log.e("NETFEE","finacal "+finacal+"");
                                Log.e("NETFEE","calcul "+calcul+"");
                                Log.e("NETFEE","finacal2 "+Double.parseDouble(String.format("%.8f", finacal))+"");

                                Log.e("NETFEE","amount_withdrawn "+amount_withdrawn+"");

                                if(Double.parseDouble(String.format("%.8f", finacal))==amount_withdrawn){

//                                if(Double.parseDouble(String.format("%.8f", finacal))==calcul){

                                    tvholdername.setText(formatter.format(totAmttqw)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    Double remBal=Double.parseDouble(data.getString("amount_sent_inr").trim())-totAmttqw;

                                    tvaccountname.setText(""+formatter.format(remBal)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                    tvamount.setText(formatter.format(totAmtt)+" "+ PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    tvtotle.setText(formatter.format(totAmttqw)+ " "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                }else{

                                    Double prevAmt= Double.parseDouble(formatter.format(finacal*rate));

                                    tvholdername.setText(formatter.format(totAmttqw11)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    Double remBal=prevAmt-totAmttqw11;

                                    tvaccountname.setText(""+formatter.format(remBal)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                    tvamount.setText(formatter.format(totAmttqw)+" "+ PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    tvtotle.setText(formatter.format(totAmttqw11)+" "+ PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");
                                }
                            }

                        }


                        if(getIntent().getStringExtra("key").equals("transfer_bitcoin_third_party"))
                        {  //send

                            double rate=0;

                            Log.e("transfer_bi_third_party","yes");

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder Name:");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Send to : ");
                            tvCreditTo.setText("Other Wallet ");

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);


                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");



                            JSONObject data=result1.getJSONObject("data");

                            JSONObject User=data.getJSONObject("User");

                            if(!data.isNull("rate")) {
                                rate= Double.parseDouble(data.getString("rate"));
                            }


                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvnumber.setText(User.getString("phone"));


                            if(!data.isNull("previous_amount")) { //5
                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                                if (bit > 0) {
                                    tvbankname.setText(formatter.format(finacal*rate) +
                                            " "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol) );
                                } else {
                                    tvbankname.setText("0.00" +
                                            " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order No: "+data.getString("txid"));

                            tvCreditTo.setText(data.getString("to_address"));

                            tvrate.setText(formatter.format(rate)+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));




                            if(!data.isNull("network_charge")) {
                                network_fee = Double.parseDouble(data.getString("network_charge"));
                                BigDecimal ntF = new BigDecimal(network_fee);
                                tvfees.setText(formatter.format(ntF)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvtax.setText(formatter.format(Double.valueOf(data.getString("gst")))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

//                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));

//                            if(User.has("country")) {
//                                tvcountry.setText(User.getString("country"));
//                            }else{
                            tvcountry.setText("");
//                            }

                            String timeCreated = data.getString("created");

                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvfees.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });

//                            double user_btc = Double.parseDouble(User.getString("btc_amount"));
                            double amountt = Double.parseDouble(String.valueOf(data.opt("previous_btc_amount")));
//                            double amountt = Double.parseDouble("10000");

                            tvtext.setText("Send acepay from your Account " +
                                    "("+String.format("%.8f", amountt)+ "Ace) to other Account with "
                                    +formatter.format(rate)+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" rate");


                            if(!data.isNull("amount_inr")) {

                                double calcul = Double.parseDouble(data.getString("amount_inr"));

                                calcul=calcul/rate;
                                BigDecimal d = new BigDecimal(calcul);

                                Double totAmtt=Double.parseDouble(data.getString("amount_inr").trim())
                                        -(Double.parseDouble(data.getString("gst"))
                                        +Double.parseDouble(data.getString("network_charge")));

                                Double totAmttqw11=Double.parseDouble(data.getString("amount_inr").trim())
                                        +(Double.parseDouble(data.getString("gst"))
                                        +Double.parseDouble(data.getString("network_charge")));

                                Double totAmttqw=Double.parseDouble(data.getString("amount_inr").trim());

                                Log.e("NETFEE","totAmtt "+totAmtt);
                                Log.e("NETFEE","totAmttqw11 "+totAmttqw11);
                                Log.e("NETFEE","totAmttqw "+totAmttqw);
                                Log.e("finacal",finacal*rate+"");
                                //prevAmt ==Amt Withdrawn
                                Log.e("NETFEE","finacal "+finacal+"");
                                Log.e("NETFEE","calcul "+calcul+"");
                                Log.e("NETFEE","finacal2 "+Double.parseDouble(String.format("%.8f", finacal))+"");

                                Log.e("NETFEE","amount_withdrawn "+amountt+"");

                                if(Double.parseDouble(String.format("%.8f", finacal))==amountt){

//                                if(Double.parseDouble(String.format("%.8f", finacal))==calcul){

                                    tvholdername.setText(formatter.format( totAmttqw)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    Double remBal=Double.parseDouble(data.getString("amount_inr").trim())-totAmttqw;

                                    tvaccountname.setText(""+formatter.format(remBal)+" "
                                            +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                    formatter.format(formatter.format(totAmtt)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    tvtotle.setText(formatter.format(totAmttqw)+ " "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                }else{

                                    Double prevAmt= Double.parseDouble(formatter.format( finacal*rate));

                                    tvholdername.setText(formatter.format(totAmttqw11)+" "
                                            +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    Double remBal=prevAmt-totAmttqw11;

                                    tvaccountname.setText(""+formatter.format(remBal)+" "
                                            +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                    tvamount.setText(formatter.format(totAmttqw)+" "+ PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");

                                    tvtotle.setText(formatter.format(totAmttqw11)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+"");
                                }
                            }
                        }


                        // todo sell+ upi sell
                        if(getIntent().getStringExtra("key").equals("sell"))
                        {
                            Log.e("sell-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder Name:");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Sell credited to ");

                            tvCreditTo.setText(PreferenceFile.getInstance().getPreferenceData(Invoice.this, Constant.Currency_Symbol)+"Account");

                            previous = PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";

                            sellername="Acepay wallet";
                            purchasename=PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)
                                    +" Account";

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject User=data.getJSONObject("User");

                            if(!data.isNull("previous_amount")) {

                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(formatter.format(finacal) + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " +
                                            PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            if(User.has("CountryName")) {
                                JSONObject CountryName = User.getJSONObject("CountryName");
                                tvcountry.setText(CountryName.getString("name"));
                            }else{
                                tvcountry.setText("");
                            }

                            tvOrder.setText("Order No: "+data.getString("txid"));

                            double sell_rate = Double.parseDouble(data.getString("sell_rate"));

                            double fee=Double.parseDouble(data.getString("fee"));

                            if(!data.isNull("fee")){
                                tvfees.setText(""+formatter.format(Double.valueOf(data.getString("tax")))
                                        + " "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtax.setText(formatter.format(Double.valueOf(data.getString("gst")))
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));                            }


                            tvrate.setText(formatter.format(sell_rate)+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));

                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvnumber.setText(User.getString("phone"));

                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            double total_btc=0.00;

                            if(!data.getString("sell_btc").equals("null")) {
                                total_btc = Double.parseDouble(data.getString("sell_btc"));

                            }

                            BigDecimal bigtotal = new BigDecimal(total_btc);

                            tvtext.setText("You have Sold acepay ("+String.format("%.8f",bigtotal)+" Ace)"+
                                    " and "+PreferenceFile.getInstance().getPreferenceData(Invoice.this, Constant.Currency_Code)+" is credited to your "+getResources().getString(R.string.app_name)+" Wallet");

                            double calcul = Double.parseDouble(User.getString("btc_amount"));
                            double sell_btc = Double.parseDouble(data.getString("sell_amount"));
                            double inr_amount = Double.parseDouble(User.getString("inr_amount"));
                            Double previous_btc_amount = Double.parseDouble(data.getString("previous_btc_amount"));
//                            total_btc
                            BigDecimal d = new BigDecimal(calcul);
                            BigDecimal bSell = new BigDecimal(sell_btc);
                            BigDecimal inr_am = new BigDecimal(inr_amount);
                            Log.e("newcal-->","d -->"+ d);
                            Log.e("newcal","d -->"+ d);
                            Log.e("newcal","sell_btc-fee "+ sell_btc);
                            Log.e("newcal","fee-fee "+ fee);

                            Log.e("total_btc Sell","fee-fee "+ total_btc);
                            Log.e("total_previous_btc Sell","fee-fee "+ previous_btc_amount);


                            if(Double.parseDouble(String.format("%.8f", total_btc))==
                                    Double.parseDouble(String.format("%.8f", previous_btc_amount)))
                            {

                                Log.e("inside If","fee-fee "+ "total_btc");

                                tvtotle.setText(""+formatter.format(bSell)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                              //  tvamount.setText(""+formatter.format(bSell)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvamount.setText(""+formatter.format(sell_btc-fee)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }
                            else  if(Double.parseDouble(String.format("%.8f", total_btc))>
                                    Double.parseDouble(String.format("%.8f", previous_btc_amount)))
                            {

                                Log.e("inside else If","fee-fee "+ "total_btc");

                                tvtotle.setText(""+formatter.format(bSell)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                          //      tvamount.setText(""+formatter.format(bSell)+" "
                           //             +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvamount.setText(""+formatter.format(sell_btc-fee)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }
                            else {
                                Log.e("inside else","fee-fee "+ "total_btc");
                                Log.e("inside else sell price","fee-fee "+bSell);

                                tvtotle.setText(""+formatter.format(bSell)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                                tvamount.setText(""+formatter.format(sell_btc-fee)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                             //   tvamount.setText(""+formatter.format(bSell)+" "
                                //        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }

                            Double totAmtRcvd=finacal+(sell_btc-fee);

                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });

                            tvholdername.setText(""+formatter.format(bSell)+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvaccountname.setText(""+formatter.format(totAmtRcvd)+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }

                        if(getIntent().getStringExtra("key").equals("asking"))
                        {
                            Log.e("asking-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder :");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            tvCredit.setText("Sell credited to ");

                            tvCreditTo.setText("Rs Account");

                            previous = PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+"Ace";

                            sellername=getResources().getString(R.string.app_name)+" wallet";
                            purchasename=PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" Account";

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);


                            //  tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount)+" Ace");

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject User=data.getJSONObject("User");

                            if(!data.isNull("previous_amount")) {

                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(String.format("%.2f", finacal) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order No: "+data.getString("txid"));

                            double sell_rate = Double.parseDouble(data.getString("sell_rate"));
                            double fee=Double.parseDouble(data.getString("fee"));

                            if(!data.isNull("fee")){

                                Log.e("fees-->",data.getString("fee"));
                                tvfees.setText(data.getString("tax")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtax.setText(data.getString("gst")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }


                            tvrate.setText(String.format("%.0f",sell_rate)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            PreferenceFile.getInstance().saveData(this,Constant.BTC_amount,User.getString("btc_amount"));

                            // tvaccountname.setText(User.getString("btc_amount")+"Ace");


                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(User.getString("first_name") + " " + User.getString("last_name"));
                            tvstatus.setText(data.getString("status"));

                            Log.e("phone-->",User.getString("phone"));
                            tvnumber.setText(User.getString("phone"));

                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            double total_btc=0;

                            if(!data.getString("sell_btc").equals("null")) {
                                Log.e("not-->","yes");
                                total_btc = Double.parseDouble(data.getString("sell_btc"));
                                Log.e("not-->",total_btc+"");
                            }

                            BigDecimal bigtotal = new BigDecimal(total_btc);

                            tvtext.setText("You have Sold acepay ("+String.format("%.8f",bigtotal)+" Ace)"+" and INR is credited to your "+getResources().getString(R.string.app_name)+" Wallet");

                            double calcul = Double.parseDouble(User.getString("btc_amount"));
                            double sell_btc = Double.parseDouble(data.getString("sell_amount"));
                            double inr_amount = Double.parseDouble(User.getString("inr_amount"));

                            BigDecimal d = new BigDecimal(calcul);
                            BigDecimal bSell = new BigDecimal(sell_btc);
                            BigDecimal inr_am = new BigDecimal(inr_amount);
                            Log.e("newcal-->","d -->"+ d);


                            Log.e("newcal-->","d -->"+ d);

                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });

                            tvholdername.setText(String.format("%.0f",bSell)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvaccountname.setText(String.format("%.0f",inr_am)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvamount.setText(String.format("%.0f",sell_btc-fee)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(String.format("%.0f",bSell)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }

                        if(getIntent().getStringExtra("key").equals("moneyTransfer"))
                        {
                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder Name:");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            previous =PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername="Acepay wallet";

                            tvaccount.setText("Current Balance");

                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);

                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+
                                    " "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);
                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject Sender=data.getJSONObject("Sender");

                            JSONObject CountryName=Sender.getJSONObject("CountryName");

                            tvCreditTo.setText(data.getString("receiver_phone"));

                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(Sender.getString("first_name") + " " + Sender.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,Sender.getString("inr_amount"));

                            tvCredit.setText("Credit To");
                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            tvaccountname.setText(Sender.getString("inr_amount")+" "+
                                    PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvtext.setText("You have transfer your "+getResources().getString(R.string.app_name)+" wallet Money to the other "+getResources().getString(R.string.app_name)+" Wallet");
                            /*tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                            tvholdername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                            tvaccountname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                            tvifscname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));*/

                            double calcul = Double.parseDouble(data.getString("amount"));

                            BigDecimal d = new BigDecimal(calcul);

                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            tvholdername.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvamount.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

//                            tvholdername.setText(formatter.format(String.valueOf(d))+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                            tvamount.setText(String.valueOf(d)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                            tvtotle.setText(String.valueOf(d)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }

                    }
                    else {
                        Constant.alertDialog(this,message);
                    }

                }
                catch (JSONException e)
                {
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constant.REQ_Invoice_inr:

                JSONObject result1 = null;
                try
                {
                    result1 = new JSONObject(response.body().string());

                    tvtax.setText("0 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    Log.e("result-->",result1.toString());

                    if(status.equals("true")) {

                        if(getIntent().getStringExtra("key").equals("moneyTransfer")) {

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername=getResources().getString(R.string.app_name)+" wallet";
                            tvaccount.setText("Current Balance");
                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);
                            //  tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);
                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject Sender=data.getJSONObject("Sender");
                            JSONObject CountryName=Sender.getJSONObject("CountryName");
                            tvCreditTo.setText(data.getString("receiver_phone"));


                            if(!data.isNull("previous_amount")) {

                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(formatter.format(finacal) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order No. "+data.getString("txid"));


                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(Sender.getString("first_name") + " " + Sender.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,Sender.getString("inr_amount"));

                            tvCredit.setText("Credit To");
                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            tvaccountname.setText(Sender.getString("inr_amount")+
                                    " "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvtext.setText("You have transfer your "+getResources().getString(R.string.app_name)+" wallet Money to the other "+getResources().getString(R.string.app_name)+" Wallet");


                            double calcul = Double.parseDouble(data.getString("amount"));

                            BigDecimal d = new BigDecimal(calcul);


                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            tvholdername.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvamount.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }


                        if(getIntent().getStringExtra("key").equals("withdraw")) {

                            tvholder.setText("Previous Balance :");
                            tvaccount.setText("Amount :");
                            tvifsc.setText("Current Balance :");

                            previous =PreferenceFile.getInstance()
                                    .getPreferenceData(this,Constant.Inr_Amount)
                                    +" "+PreferenceFile.getInstance().getPreferenceData(this,
                                    Constant.Currency_Symbol);

                            sellername="Acepay wallet";

                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);

                            JSONObject data=result1.getJSONObject("data");

                            if(!data.isNull("User")) {

                                JSONObject USer = data.getJSONObject("User");

                                JSONObject CountryName = USer.getJSONObject("CountryName");

                                tvname.setText(USer.getString("first_name") + " " +
                                        USer.getString("last_name"));

                                tvstatus.setText(data.getString("verify_admin").
                                        replace("No","Pending").replace
                                        ("Yes","Complete"));

                                tvcountry.setText(CountryName.getString("name"));

                                PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,
                                        USer.getString("inr_amount"));
                            }

                            tvOrder.setText("Order No. "+data.getString("txid"));

                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(tvstatus.getText().toString().trim().equalsIgnoreCase("pending")){
                                tvtext.setText("Your request is processed. Please Expect funds in your account within one working day");

                            }else{
                                tvtext.setText("Your request is processed and funds credited to your account");
                            }


                            double fee = Double.parseDouble(data.getString("fee"));

                            tvfees.setText(formatter.format(Double.valueOf(data.getString("tax")))+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtax.setText(formatter.format(Double.valueOf(data.getString("gst")))+" "
                                    +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            if(!data.isNull("credit")) {

                                double calcul = Double.parseDouble(data.getString("credit"));
                                original_amount = Double.parseDouble(data.getString("original_amount"));

                                tvamount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

                                BigDecimal d = new BigDecimal(calcul);

                                tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                                tvtotle.setText(formatter.format( original_amount)
                                        +" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                //  tvtotle.setText(String.valueOf(original_amount)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));


                                double newamount=original_amount-fee;
                                tvamount.setText(formatter.format( newamount)+" "
                                        +PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }else {
                                tvamount.setText("0.00 "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText("0.00 "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            double previous_balance = Double.parseDouble(data.getString("previous_balance"));

//                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)!=null){
                            tvbank.setVisibility(View.VISIBLE);
                            tvbankname.setVisibility(View.VISIBLE);
                            tvbank.setText("Previous Balance : ");
                            tvbankname.setText(formatter.format(previous_balance)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));


//                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)!=null){
                            tvholder.setVisibility(View.VISIBLE);
                            tvholder.setText("Amount : ");
                            tvholdername.setText(tvtotle.getText().toString().trim()+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));//show Previous Bal

                            double remBal=previous_balance- original_amount;
                            tvaccount.setVisibility(View.VISIBLE); // show tot withdraw amt
                            tvaccountname.setVisibility(View.VISIBLE); // show tot withdraw amt
                            tvaccount.setText("Current Balance : ");
                            tvaccountname.setText(formatter.format(remBal)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));

//



                            tvifsc.setVisibility(View.GONE); //show remaining bal
                            tvifscname.setVisibility(View.GONE); //show remaining bal
                            tvifscname.setText(formatter.format(remBal)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));

//                            }else{
//                                tvifsc.setVisibility(View.GONE);
//                            }


                        }

                        if(getIntent().getStringExtra("key").equals("deposit")) {

                            tvbank.setText("Bank Name : ");
                            tvholder.setText("Account Holder Name:");
                            tvaccount.setText("Account Number :");
                            tvifsc.setText("IFSC code :");

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount).trim()/*+
                                    " "+PreferenceFile.getInstance().getPreferenceData(this,Constant.
                                    Currency_Symbol)*/;

                            sellername="Acepay wallet";


                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);


                            JSONObject data=result1.getJSONObject("data");
                            JSONObject USer=data.getJSONObject("User");


                            JSONObject MetapayBanks=data.getJSONObject("MetapayBanks");

                            JSONObject CountryName=USer.getJSONObject("CountryName");

                            tvOrder.setText("Order No. "+data.getString("txid"));
                            String timeCreated = data.getString("created");


                            tvbankname.setText(MetapayBanks.getString("bank_name")+"");
                            tvholdername.setText(MetapayBanks.getString("acc_holder_name")+"");
                            tvaccountname.setText(MetapayBanks.getString("account_number")+"");
                            tvifscname.setText(MetapayBanks.getString("ifsc_code")+"");

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed

                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            tvname.setText(USer.getString("first_name") + " " + USer.getString("last_name"));
                            tvstatus.setText(data.getString("verify_admin").replace("No","Pending")
                                    .replace("Yes","Complete"));

                            if(tvstatus.getText().toString().trim().equalsIgnoreCase("pending")){
                                tvtext.setText("You have to Waitfor admin approval for the funds credited to your account");

                            }else{
                                tvtext.setText("Your request is processed and funds Credited to your account");
                            }

                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,USer.getString("inr_amount"));

                            double calcul = Double.parseDouble(data.getString("total"));
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(50) });

                            double fee = Double.parseDouble(data.getString("fee"));

                            tvfees.setText(formatter.format(fee)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));
                            double deposite=0.00;
                            if(!data.isNull("deposite")) {

                                 deposite = Double.parseDouble(data.getString("deposite"));
                                double original_amount = Double.parseDouble(data.getString("original_amount"));


                                BigDecimal dep = new BigDecimal(deposite);
                                BigDecimal original = new BigDecimal(original_amount);

                                double newamount=original_amount-fee;

                                tvamount.setText(formatter.format( newamount)+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
//                                tvtotle.setText(String.valueOf(dep)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText(formatter.format(dep)+" "+
                                        PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }else {
                                tvamount.setText("0.00 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText("0.00 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            BigDecimal d = new BigDecimal(calcul);

                            llBTC1.setVisibility(View.VISIBLE);
                            double previous_balance=0.00;
                            if(previous.equalsIgnoreCase("")
                                    || previous.equalsIgnoreCase("null")
                                    || previous.equalsIgnoreCase(null)

                                    ){
                                 previous_balance = Double.parseDouble("0.00");

                            }else{
                                 previous_balance = Double.parseDouble(previous);

                            }/* if(data.getString("previous_balance").equalsIgnoreCase("")
                                    || data.getString("previous_balance").equalsIgnoreCase("null")
                                    || data.getString("previous_balance").equalsIgnoreCase(null)

                                    ){
                                 previous_balance = Double.parseDouble("0.00");

                            }else{
                                 previous_balance = Double.parseDouble(data.getString("previous_balance"));

                            }*/
                            tvbank2.setVisibility(View.VISIBLE);
                            tvbankname2.setVisibility(View.VISIBLE);
                            tvbank2.setText("Previous Balance : ");



//                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER)!=null){
                            tvholder2.setVisibility(View.VISIBLE);
                            tvholder2.setText("Amount : ");
                            tvholdername2.setText(tvtotle.getText().toString().trim()+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));//show Previous Bal

                            double remBal=previous_balance+ original_amount;
                            double prevremBal=remBal- deposite;
//                            Toast.makeText(this, "fchf"+prevremBal+" ", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(this, "previous_balance"+previous_balance+" ", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(this, "original_amount "+original_amount+" ", Toast.LENGTH_SHORT).show();
                            tvbankname2.setText(formatter.format(prevremBal)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));

                            tvaccount2.setVisibility(View.VISIBLE); // show tot withdraw amt
                            tvaccountname2.setVisibility(View.VISIBLE); // show tot withdraw amt
                            tvaccount2.setText("Current Balance : ");
                            tvaccountname2.setText(formatter.format(remBal)+" "+PreferenceFile.getInstance().
                                    getPreferenceData(this,Constant.Currency_Symbol));

//

                        }

                        if(getIntent().getStringExtra("key").equals("payu")) {

                            tvbank1.setText("Bank Name : ");
                            tvholder1.setText("Account Holder :");
                            tvaccount1.setText("Account Number :");
                            tvifsc1.setText("IFSC code :");

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername=getResources().getString(R.string.app_name)+" wallet";

                            tvifscname1.setVisibility(View.GONE);
                            tvifsc1.setVisibility(View.GONE);
                            tvaccountname1.setVisibility(View.GONE);
                            tvaccount1.setVisibility(View.GONE);
                            tvbankname1.setVisibility(View.GONE);
                            tvbank1.setVisibility(View.GONE);
                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);
                            llBTC.setVisibility(View.GONE);
                            llINR.setVisibility(View.VISIBLE);

                            tvholdername.setText("Payment Gateway- PayU");


                            JSONObject data=result1.getJSONObject("data");
                            JSONObject USer=data.getJSONObject("User");
                            JSONObject CountryName=USer.getJSONObject("CountryName");

                            tvOrder.setText("Order No. "+data.getString("txid"));
//
                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            tvname.setText(USer.getString("first_name") + " " + USer.getString("last_name"));
                            tvstatus.setText(data.getString("status").replace("No","Pending").replace("Yes","Complete"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,USer.getString("inr_amount"));


                            tvtext.setText("Your request is processed and funds are credited to your account");


                            double calcul = Double.parseDouble(data.getString("amount"));
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            if(!data.isNull("amount")) {
                                double deposite = Double.parseDouble(data.getString("amount"));

                                BigDecimal dep = new BigDecimal(deposite);
                                tvamount.setText(String.valueOf(deposite)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText(String.valueOf(dep)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }else {
                                tvamount.setText("0 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText("0 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            BigDecimal d = new BigDecimal(calcul);
                            Log.e("newcal-->", "d -->" + d);

                        }

                        if(getIntent().getStringExtra("key").equals("paypal")) {

                            tvcharges.setText("Gateway Charges :");
                            tvbank1.setText("Bank Name : ");
                            tvholder1.setText("Account Holder :");
                            tvaccount1.setText("Account Number :");
                            tvifsc1.setText("IFSC code :");
                            tvtax.setVisibility(View.GONE);
                            tvGst.setVisibility(View.GONE);

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername=getResources().getString(R.string.app_name)+" wallet";

                            tvifscname1.setVisibility(View.GONE);
                            tvifsc1.setVisibility(View.GONE);
                            tvaccountname1.setVisibility(View.GONE);
                            tvaccount1.setVisibility(View.GONE);
                            tvbankname1.setVisibility(View.GONE);
                            tvbank1.setVisibility(View.GONE);

                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);
                            llBTC.setVisibility(View.GONE);
                            llINR.setVisibility(View.VISIBLE);

                            tvholdername1.setText("Payment Gateway- PayPal");

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject USer=data.getJSONObject("User");
                            JSONObject CountryName=USer.getJSONObject("CountryName");

                            tvfees.setText(data.getString("charges")+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvOrder.setText("Order No. "+data.getString("txid"));
                            String timeCreated = data.getString("created");

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            tvname.setText(USer.getString("first_name") + " " + USer.getString("last_name"));
                            tvstatus.setText(data.getString("status").replace("No","Pending").replace("Yes","Complete"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,Constant.Inr_Amount,USer.getString("inr_amount"));


                            tvtext.setText("Your request is processed and funds are credited to your account");


                            double calcul = Double.parseDouble(data.getString("amount"));
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            if(!data.isNull("amount")) {
                                int deposite = Integer.parseInt(data.getString("amount"));
                                int charges = Integer.parseInt(data.getString("charges"));

                                BigDecimal dep = new BigDecimal(deposite);
                                tvamount.setText(String.valueOf(dep)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText(String.valueOf(deposite+charges)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            }else {
                                tvamount.setText("0 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                                tvtotle.setText("0 "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            }

                            BigDecimal d = new BigDecimal(calcul);
                            Log.e("newcal-->", "d -->" + d);

                        }

                        if(getIntent().getStringExtra("key").equals("inrtransfer")) {

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername="Acepay wallet";

                            tvaccount.setText("Current Balance");

                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);

                            // tvbankname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);
                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject Sender=data.getJSONObject("Sender");
                            JSONObject CountryName=Sender.getJSONObject("CountryName");

                            if(!data.isNull("previous_amount")) {

                                Double bit = Double.parseDouble(data.getString("previous_amount"));
                                Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

                                if (bit > 0) {
                                    tvbankname.setText(formatter.format( finacal) + " "
                                            + PreferenceFile.getInstance().
                                            getPreferenceData(this, Constant.Currency_Symbol));
                                } else {
                                    tvbankname.setText("0.00" + " " +
                                            PreferenceFile.getInstance().
                                                    getPreferenceData(this, Constant.Currency_Symbol));
                                }
                            }

                            tvOrder.setText("Order No. "+data.getString("txid"));


                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(Sender.getString("first_name") + " "
                                    + Sender.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvCreditTo.setText(data.getString("receiver_phone"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,
                                    Constant.Inr_Amount,Sender.getString("inr_amount"));

                            tvCredit.setText("Credit To");
                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            tvaccountname.setText(Sender.getString("inr_amount")+
                                    " "+PreferenceFile.getInstance().getPreferenceData
                                    (this,Constant.Currency_Symbol));

                            tvtext.setText("You have transfer your "+getResources().getString(R.string.app_name)+" wallet Money to the other "+getResources().getString(R.string.app_name)+" Wallet");

                            double calcul = Double.parseDouble(data.getString("amount"));

                            BigDecimal d = new BigDecimal(calcul);


                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            // tvamount.setText(String.format("%.2f",d)+" Ace");

                            tvholdername.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvamount.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));


                        }

                        if(getIntent().getStringExtra("key").equals("InrReceive")) {

                            previous =PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount)+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol);

                            sellername="Acepay wallet";

                            tvaccount.setText("Current Balance");

                            tvrate.setVisibility(View.GONE);
                            tvlnrate.setVisibility(View.GONE);

                            tvbankname.setText(PreferenceFile.getInstance().getPreferenceData
                                    (this,Constant.Inr_Amount)
                                    + " "+PreferenceFile.getInstance().getPreferenceData
                                    (this,Constant.Currency_Symbol));

                            tvifsc.setVisibility(View.GONE);
                            tvifscname.setVisibility(View.GONE);
                            tvCredit.setVisibility(View.VISIBLE);
                            tvCreditTo.setVisibility(View.VISIBLE);

                            JSONObject data=result1.getJSONObject("data");
                            JSONObject Sender=data.getJSONObject("Sender");
                            JSONObject CountryName=Sender.getJSONObject("CountryName");

                            tvOrder.setText("Order No. "+data.getString("txid"));

                            String timeCreated = data.getString("created");


                            try {
                                String date1[] = timeCreated.split(" ");
                                tvdate.setText(date1[0]);
                                tvtime.setText(date1[1]);
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            tvname.setText(Sender.getString("first_name") + "" +
                                    " " + Sender.getString("last_name"));
                            tvstatus.setText(data.getString("status"));
                            tvCreditTo.setText(data.getString("receiver_phone"));
                            tvcountry.setText(CountryName.getString("name"));

                            PreferenceFile.getInstance().saveData(this,
                                    Constant.Inr_Amount,Sender.getString("inr_amount"));

                            tvCredit.setText("Credit To");
                            tvbank.setText("Previous Balance");
                            tvholder.setText("Amount");
                            tvaccount.setText("Current Balance");

                            tvaccountname.setText(Sender.getString("inr_amount")
                                    +" "+PreferenceFile.getInstance().getPreferenceData
                                    (this,Constant.Currency_Symbol));

                            tvtext.setText("You have transfer youe "+getResources().getString(R.string.app_name)+" wallet Money to the other "+getResources().getString(R.string.app_name)+" Wallet");


                            double calcul = Double.parseDouble(data.getString("amount"));

                            BigDecimal d = new BigDecimal(calcul);
                            Log.e("newcal-->","d -->"+ d);

                            tvholdername.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvamount.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                            tvtotle.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

                            tvholdername.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvamount.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
                            tvtotle.setText(formatter.format(d)+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));

                        }

                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

}
