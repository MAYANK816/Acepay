package com.acepay;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.TransactionTooLargeException;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acepay.Adapter.TransactionAdapter;
import com.acepay.DownloadPackage.DownloadService;
import com.acepay.Model.Transaction_model;
import com.acepay.Util.App;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Statements extends ToolabarActivity implements RetrofitResponse, View.OnClickListener{
    TextView tvBitcoin,tvwallet,tvINR,txName,tvInr,tvBtc;
    Typeface tfArchitectsDaughter;
    Double finacals,bit;
    Toolbar toolbar;
    RelativeLayout rllayer,rlmain;
    int flag=0;
    LinearLayout lnrefresh;
    EditText edSearch;
    TextView tvstatedate,tvenddate;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    String start="",end="";
    ArrayList<Transaction_model> transaction_models;
    TransactionAdapter transactionAdapter;
    public static RecyclerView recyclerView;
    NumberFormat formatter;
    ImageView pdf,search,calender;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        activity_transcation
        setContentView(R.layout.activity_transcation);
        transaction_models=new ArrayList<>();
        search = (ImageView) findViewById(R.id.search);
        pdf = (ImageView) findViewById(R.id.pdf);
        calender = (ImageView) findViewById(R.id.calender);
        tvBitcoin = (TextView) findViewById(R.id.tvBitcoin);
        tvInr = (TextView) findViewById(R.id.tvInr);
        rllayer = (RelativeLayout) findViewById(R.id.rllayer);
        edSearch = (EditText) findViewById(R.id.edSearch);
        tvBtc = (TextView) findViewById(R.id.tvBtc);
        txName = (TextView) findViewById(R.id.txName);
        tvwallet= (TextView) findViewById(R.id.tvwallet);
        tvINR = (TextView) findViewById(R.id.tvINR);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        lnrefresh= (LinearLayout) findViewById(R.id.lnrefresh);
        lnrefresh.setOnClickListener(this);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                transactionAdapter.filter(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        setSupportActionBar(toolbar);

        if(getIntent().getStringExtra("key").equals("statement")){
            flag=1;
        }

        if(getIntent().getStringExtra("key").equals("btc")){
            flag=1;
            tvInr.setVisibility(View.GONE);
        }

        if(getIntent().getStringExtra("key").equals("Inr")){
            flag=3;
            tvBtc.setVisibility(View.GONE);
        }

        if(getIntent().getStringExtra("key").equals("search")){

            Log.e("inside-->","yes");
            start=getIntent().getStringExtra("start");
            end=getIntent().getStringExtra("end");
        }

        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);
        formatter = NumberFormat.
                getCurrencyInstance(new Locale("en", PreferenceFile.getInstance().
                        getPreferenceData(Statements.this, Constant.selectedCountryNameCode).toString()));


        txName.setVisibility(View.VISIBLE);
        txName.setText("Statements");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        finacals = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

//        currentsymbol.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol)+" ");
        callService();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rllayer.isShown()){
                    Constant.hideKeyboard(Statements.this,search);
                    rllayer.setVisibility(View.GONE);
                }
                else {

                    rllayer.setVisibility(View.VISIBLE);
                    edSearch.requestFocus();
                }
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

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacals));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(Statements.this,Dashboard.class);
                startActivity(intent);
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Statements.this,SearchTransaction.class);
                startActivity(intent);
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(!start.equals("")&& (!end.equals("")))
                {
                   /* startDownload("https://bitok.co.in/homes/myPdfView/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID)+"/"+start+"/"+end);*/

                    startDownload(Constant.STATEMENT_PDF_URL+"homes/myPdfView/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID)+"/"+start+"/"+end);
//                    startDownload("http://metapay.io/homes/myPdfView/"+PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID)+"/"+start+"/"+end);
                    Log.e("downloading",Constant.STATEMENT_PDF_URL+"homes/myPdfView/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID)+"/"+start+"/"+end);
                }
                else
                {
                   /* startDownload("https://bitok.co.in/homes/AllTransactionPdf/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID)); */

                    startDownload(Constant.STATEMENT_PDF_URL+"homes/AllTransactionPdf/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID));


                    Log.e("downloading Else ",Constant.STATEMENT_PDF_URL+"homes/AllTransactionPdf/"+
                            PreferenceFile.getInstance().getPreferenceData(Statements.this,Constant.ID));

                }
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("bit_rate"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String buy = intent.getStringExtra("buy");
            String sell = intent.getStringExtra("sell");

            Log.e("dashboard>>>",buy+"sender id-->"+sell);

            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(Statements.this, Constant.BTC_amount)) * Double.parseDouble(buy);

            if (buy_rate > 0) {

                tvINR.setText(formatter.format(buy_rate) + " " + PreferenceFile.getInstance().getPreferenceData(Statements.this, Constant.Currency_Symbol));
            } else {
                tvINR.setText("0.00" + " " + PreferenceFile.getInstance().getPreferenceData(Statements.this, Constant.Currency_Symbol));
            }

        }
    };


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


    private void startDownload(String id)
    {
        Log.e("downloading-->","yes");
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("D_id",id);
        //intent.putExtra("DOWNLOAD_URL",Constants.QUOTE_DOWNLOAD);
        intent.putExtra("STORAGE", App.getQUOTATIONS());
        startService(intent);

        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:

                Log.e("click-->","yes");

                //if(transaction_models.size()>0) {

                    if (rllayer.isShown()) {

                        Constant.hideKeyboard(this, search);
                        rllayer.setVisibility(View.GONE);
                    }
                    else {

                        rllayer.setVisibility(View.VISIBLE);
                        edSearch.requestFocus();
                    }
               // }
               /* Intent intent=new Intent(Statements.this,SearchTransaction.class);
                startActivity(intent);*/

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
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

    private void callService() {

        if (Constant.isConnectingToInternet(this)) {

            new Retrofit2(this, Statements.this, Constant.REQ_BTC_RATE, Constant.BTC_RATE).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void transactiondate() {

        if (Constant.isConnectingToInternet(this)) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(Statements.this, Constant.ID));
                postParam.put("from_date", start);
                postParam.put("end_date", end);

                Log.e("postparam--->", postParam.toString());

                if (Constant.isConnectingToInternet(Statements.this)) {
                    Log.e("connect--->", "yes");
                    new Retrofit2(Statements.this, Statements.this, postParam, Constant.REQ_Transaction_date, Constant.Transaction_date, "3").callService(true);
                } else {

                    Log.e("connect--->", "no");
                    Constant.alertDialog(Statements.this, getResources().getString(R.string.check_connection));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void transaction() {

        if (Constant.isConnectingToInternet(this)) {

           new Retrofit2(this, Statements.this, Constant.REQ_Statements, Constant.Statements+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
           // new Retrofit2(this, Statements.this, Constant.REQ_Statements, Constant.Statements+"27").callService(true);
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

                    try
                    {
                        JSONObject result = new JSONObject(response.body().string());

                        if(getIntent().getStringExtra("key").equals("statement"))
                        {
                            flag=1;
                            transaction();
                        }

                        if(getIntent().getStringExtra("key").equals("search"))
                        {
                            flag=2;
                            transactiondate();
                        }

                        if(getIntent().getStringExtra("key").equals("Inr"))
                        {
                            flag=3;
                            transaction();
                        }

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true"))
                        {
                            JSONObject data = result.getJSONObject("data");
                            Double buy_rate = Double.parseDouble(PreferenceFile.getInstance().getPreferenceData
                                    (this, Constant.BTC_amount)) * Double.parseDouble(data.getString("buy"));

                            if (buy_rate > 0)
                            {
                                tvINR.setText(formatter.format(buy_rate) + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            }
                            else
                            {
                                tvINR.setText("0.00" + " " +
                                        PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
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
//8837880195
            case Constant.REQ_Date_Transaction_PDF:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {


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

         case Constant.REQ_All_transaction_pdf:

                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());
                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {


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

            case Constant.REQ_Statements:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    if (status.equals("true"))
                    {
                        transaction_models.clear();
                        JSONArray data=result.getJSONArray("data");

                        for (int x=0;x<data.length();x++) {
                            JSONObject obj = data.getJSONObject(x);
                            Transaction_model transaction_model=new Transaction_model();
                            transaction_model.setId(obj.getString("id"));
                            transaction_model.setDescription(obj.getString("message"));
                            transaction_model.setBtc(obj.getString("BTC"));
                            transaction_model.setStatus(obj.getString("status"));

                            Log.e("statuspriya-->",obj.getString("status"));

                            if(!obj.isNull("amount") && !obj.isNull("inr")) {

                                double calcul = Double.parseDouble(obj.getString("amount"));
                                double inr = Double.parseDouble(obj.getString("inr"));

                                BigDecimal d = new BigDecimal(calcul);
                                BigDecimal rff = new BigDecimal(inr);
                                Log.e("newcal-->","d -->"+ d);

                                String  finacal = String.valueOf(d);
                                String  sdfd = String.valueOf(rff);
                                transaction_model.setAmount(finacal);
                                transaction_model.setInrAmount(sdfd);
                            }
                            else
                                {
                                transaction_model.setAmount("0");
                                transaction_model.setInrAmount("0");
                                Log.e("null-->",""+ "null");
                            }

                            transaction_model.setDate(obj.getString("created"));
                            transaction_model.setTransaction_id(obj.getString("txid"));
                            transaction_models.add(transaction_model);

                            Log.e("amount-->",obj.getString("amount"));

                        }
                        Log.e("transactionsize-->",transaction_models.size()+"");

                        if(transaction_models.size()>0){

                            transactionAdapter = new
                                    TransactionAdapter(this, transaction_models, flag);
                            recyclerView.setAdapter(transactionAdapter);

                            pdf.setVisibility(View.VISIBLE);
                            calender.setVisibility(View.VISIBLE);
                        }
                        else {

                            Constant.alertWithIntent(this,"No Data Found!",Dashboard.class);
                        }

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

            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());

                        String status = result1.getString("response");
                        String message = result1.getString("message");
                        callService();

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

                            tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(Statements.this, Constant.Currency_Symbol));
                            // tvwallet.setText(String.format(formatter, ff)+" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            // tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));

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
                            Constant.alertWithIntent(this, "Account Blocked.", BlockScreen.class);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;


            case Constant.REQ_Transaction_date:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    if (status.equals("true"))
                    {
                        transaction_models.clear();
                        JSONArray data=result.getJSONArray("data");

                        for (int x=0;x<data.length();x++) {
                            JSONObject obj = data.getJSONObject(x);
                            Transaction_model transaction_model=new Transaction_model();
                            transaction_model.setId(obj.getString("id"));
                            transaction_model.setDescription(obj.getString("message"));
                            transaction_model.setBtc(obj.getString("BTC"));
                            transaction_model.setStatus(obj.getString("status"));

                            Log.e("statuspriya-->",obj.getString("status"));

                            if(!obj.isNull("amount") && !obj.isNull("inr")) {

                                double calcul = Double.parseDouble(obj.getString("amount"));
                                double inr = Double.parseDouble(obj.getString("inr"));

                                BigDecimal d = new BigDecimal(calcul);
                                BigDecimal rff = new BigDecimal(inr);
                                Log.e("newcal-->","d -->"+ d);

                                String  finacal = String.valueOf(d);
                                String  sdfd = String.valueOf(rff);
                                transaction_model.setAmount(finacal);
                                transaction_model.setInrAmount(sdfd);
                            }
                            else {
                                transaction_model.setAmount("0");
                                transaction_model.setInrAmount("0");
                                Log.e("null-->",""+ "null");
                            }

                            transaction_model.setDate(obj.getString("created"));
                            transaction_model.setTransaction_id(obj.getString("txid"));
                            transaction_models.add(transaction_model);

                            Log.e("amount-->",obj.getString("amount"));

                        }
                        Log.e("transactionsize-->",transaction_models.size()+"");

                        if(transaction_models.size()>0){

                            transactionAdapter = new TransactionAdapter(this, transaction_models, flag);
                            recyclerView.setAdapter(transactionAdapter);

                            pdf.setVisibility(View.VISIBLE);
                            calender.setVisibility(View.VISIBLE);
                        }
                        else {

                            Constant.alertWithIntent(this,"No Data Found!",Dashboard.class);
                        }

                    }
                    else {

                        Constant.alertDialog(this,message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

              //  break;

                /*try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");
                    if (status.equals("true"))
                    {
                        transaction_models.clear();
                        JSONArray data=result.getJSONArray("data");

                        for (int x=0;x<data.length();x++) {
                            JSONObject obj = data.getJSONObject(x);
                            Transaction_model transaction_model=new Transaction_model();
                            transaction_model.setId(obj.getString("id"));
                            transaction_model.setDescription(obj.getString("message"));
                            transaction_model.setBtc(obj.getString("BTC"));
                            transaction_model.setStatus(obj.getString("status"));

                            Log.e("statuspriya-->",obj.getString("status"));

                            if(!obj.isNull("amount") && !obj.isNull("inr")) {

                                double calcul = Double.parseDouble(obj.getString("amount"));
                                double inr = Double.parseDouble(obj.getString("inr"));

                                BigDecimal d = new BigDecimal(calcul);
                                BigDecimal rff = new BigDecimal(inr);
                                Log.e("newcal-->","d -->"+ d);

                                String  finacal = String.valueOf(d);
                                String  sdfd = String.valueOf(rff);
                                transaction_model.setAmount(finacal);
                                transaction_model.setInrAmount(sdfd);
                            }
                            else {
                                transaction_model.setAmount("0");
                                transaction_model.setInrAmount("0");
                                Log.e("null-->",""+ "null");
                            }

                            transaction_model.setDate(obj.getString("created"));
                            transaction_model.setTransaction_id(obj.getString("txid"));
                            transaction_models.add(transaction_model);

                            Log.e("amount-->",obj.getString("amount"));

                        }
                        Log.e("transactionsize-->",transaction_models.size()+"");

                        if(transaction_models.size()>0){

                            transactionAdapter = new TransactionAdapter(this, transaction_models, flag);
                            recyclerView.setAdapter(transactionAdapter);

                            pdf.setVisibility(View.VISIBLE);
                            calender.setVisibility(View.VISIBLE);
                        }
                        else {

                            Constant.alertWithIntent(this,"No Data Found!",Dashboard.class);
                        }

                    }
                    else {

                        Constant.alertDialog(this,message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                break;
        }

        }

    private void newRefereshing() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, Statements.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.lnrefresh:

                newRefereshing();

                break;


        }
    }
}
