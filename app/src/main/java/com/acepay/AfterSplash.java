package com.acepay;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Model.CustomModel;
import com.acepay.SmsGateway.Sender;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.hbb20.CountryCodePicker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class AfterSplash extends AppCompatActivity implements View.OnClickListener,
        RetrofitResponse {
    TextView txSubmit;
    EditText edNumber,ed_refral_code;
    CountryCodePicker cpp;
    String country_code,country_id;
    Sender sender;
    Context context;
    ArrayList<CustomModel> customModels;
    int verifycode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_after_splash);
        context=this;
        txSubmit= (TextView) findViewById(R.id.txSubmit);
        ed_refral_code= (EditText) findViewById(R.id.ed_refral_code);
        edNumber= (EditText) findViewById(R.id.edNumber);
        edNumber.setText("");
        customModels=new ArrayList<>();

        cpp = (CountryCodePicker) findViewById(R.id.ccpPicker);

        txSubmit.setOnClickListener(this);

        edNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length()==10){

                    Constant.hideKeyboard(AfterSplash.this,edNumber);

                    JSONObject postParam = new JSONObject();

                    try {

                        postParam.put("phone", edNumber.getText().toString());
                        Log.e("PostParams ",postParam.toString());

                        if (Constant.isConnectingToInternet(AfterSplash.this)) {
                            new Retrofit2(AfterSplash.this, AfterSplash.this, postParam, Constant.REQ_CHECK_NO,
                                    Constant.CHECK_NO, "3").callService(true);
                        } else {
                            Constant.alertDialog(AfterSplash.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        cpp.setCustomMasterCountries("IN");
//        cpp.setDefaultCountryUsingNameCode("IN");
        cpp.setKeyboardAutoPopOnSearch(false);
        cpp.setClickable(false);
        cpp.setOnTouchListener(null);
        cpp.setOnCountryChangeListener(null);
        edNumber.setText("");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.txSubmit:


                if(verification()) {


                    if(ed_refral_code.isShown() && !ed_refral_code.getText().toString().trim().isEmpty()){
                        checkReferCode();
                    }else {

                        performSubmitAction();

                    }
                }
                break;
        }

    }


    private void checkReferCode()
    {

        JSONObject postParam = new JSONObject();

        try {

            postParam.put("refer_code", ed_refral_code.getText().toString().trim());

            Log.e("RefereParams ",postParam.toString());

            if (Constant.isConnectingToInternet(AfterSplash.this)) {
                new Retrofit2(AfterSplash.this, AfterSplash.this, postParam,
                        Constant.REQ_CHECK_REFER_CODE, Constant.CHECK_REFER_CODE, "3").callService(true);
            } else {
                Constant.alertDialog(AfterSplash.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void performSubmitAction(){

        Random r = new Random();
        // int i1 = r.nextInt(80 - 65) + 65;
        verifycode = (1000 + r.nextInt(9000));

//        Toast.makeText(this, "Code:- "+verifycode, Toast.LENGTH_SHORT).show();



        /*        new Sender.UpdateLoc().
        ("http://sms.nextgenbpo.in/sendsms/bulksms?username=nex-bokwallet&password=shaad
        &type=0&dlr=1&destination="+destination+"&source=Bokpay&message="+message.replaceAll(" ", "%20"));
         */


        /*http://sms.nextgenbpo.in/sendsms/bulksms?username=nex-tigerpay&password=tiger&type=0&dlr=1&" +
                "destination="+destination+"&source=TigrPy&message="+message.replaceAll(" ", "%20")*/


        /*sender=new Sender("sms.digimiles.in",8000,"di78-bitpay",
                                "didimile","Dear Customer, your OTP is " +
                                String.valueOf(verifycode)+" valid for 60 seconds. " +
                                "MetaPay will never call you asking for OTP. " +
                                "Sharing your OTP with anyone means you are giving" +
                                " your MetaPay access to them.","1","0",
                                cpp.getSelectedCountryCode()+
                                        edNumber.getText().toString(),"MetaPay",
                                cpp.getSelectedCountryCode(),AfterSplash.this, String.valueOf(verifycode));*/


        sender = new Sender("sms.digimiles.in", 8000, "nex-tigerpay", "tiger",
                "Dear Customer, your OTP is " + verifycode + " valid for 10 minutes. dont share your OTP with anyone for your security. AcePay&entityid=1201159109110077633&tempid=1207165719304038230",
                "1", "0", edNumber.getText().toString(), "ACECIO");


/*
 sender=new Sender("sms.digimiles.in",2346,"nex-bokwallet","shaad",
                "Dear Customer, your OTP is " +String.valueOf(verifycode)+" valid for 4 minutes." +
                        getResources().getString(R.string.app_name)+" will never call you asking for OTP. " +
                        "Sharing your OTP with anyone means you are giving your "+getResources().getString(R.string.app_name)+"  access to them.",
                "1","0",edNumber.getText().toString(),"Bokpay");*/



        //  String[] arguments = new String[] {"123"};
        sender.call();

        Locale swedishLocale = new Locale(cpp.getSelectedCountryNameCode(),cpp.getSelectedCountryNameCode());
        displayCurrencyInfoForLocale(swedishLocale,cpp.getSelectedCountryNameCode());
        finish();

        Intent intent1 = new Intent(AfterSplash.this, OtpVerification.class);
        intent1.putExtra("otp", String.valueOf(verifycode));
        intent1.putExtra("number", edNumber.getText().toString());

        if (!ed_refral_code.getText().toString().equals(" "))
        {
            intent1.putExtra("refer", ed_refral_code.getText().toString());
        } else {
            intent1.putExtra("refer", "");
        }
        intent1.putExtra("number", edNumber.getText().toString());
        intent1.putExtra("cpp", cpp.getSelectedCountryCode());
        intent1.putExtra("id", cpp.getSelectedCountryCode());
        intent1.putExtra("code", cpp.getSelectedCountryCode());
        startActivity(intent1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void displayCurrencyInfoForLocale(Locale locale,String selectedCountryNameCode) {

        Currency currency = Currency.getInstance(locale);
        PreferenceFile.getInstance().saveData(this,Constant.Currency_Code,currency.getCurrencyCode());
        PreferenceFile.getInstance().saveData(this,Constant.COUNTRY_CODE,cpp.getSelectedCountryCode());
        PreferenceFile.getInstance().saveData(this,Constant.selectedCountryNameCode,selectedCountryNameCode);
        Log.e("Locale:" ,"selectedCountryNameCode "+ selectedCountryNameCode);
        Log.e("Locale:" ,"locale "+ locale);
        Log.e("Locale:" ,""+ locale.getDisplayName());
        Log.e("CurrencyCode():" ,""+ currency.getCurrencyCode());
        Log.e("currency.getDisplayName" ,""+ currency.getDisplayName());
        Log.e("currency.getSymbol(): " ,""+ currency.getSymbol());
    }


    public boolean verification() {

        if(cpp.getDefaultCountryCode().toString().equals("")){
            Constant.alertDialog(this,"Please Select Country");
            return false;
        }
        if(edNumber.getText().toString().equals("")){

            Constant.alertDialog(this,getResources().getString(R.string.please_enter_phone_number));
            return false;
        }

        if(edNumber.getText().toString().length()<10){

            Constant.alertDialog(this,getResources().getString(R.string.please_enter_correct_number));
            return false;
        }
        return true;
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_CHECK_NO:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    Log.e("REQ_CHECK_NO", "onServiceResponse: "+result1.toString() );

                    if (status.equals("true")) {

                        Random r = new Random();
                        verifycode= (1000 + r.nextInt(9000));
                        Log.e("VerifyCode ",verifycode+"");
//                        Toast.makeText(this, verifycode, Toast.LENGTH_SHORT).show();


//                        Toast.makeText(this, "Code:- "+verifycode, Toast.LENGTH_SHORT).show();

//                        sender=new Sender("sms.digimiles.in",8000,"di78-bitpay","didimile","Dear Customer, your OTP is " +String.valueOf(verifycode)+
// " valid for 4 minutes. FIN-CEX will never call you asking for OTP. Sharing your OTP with anyone means you are giving your FIN-CEX  access to them.",
// "1","0",edNumber.getText().toString(),"MetaPay");

                     /*   sender=new Sender("sms.digimiles.in",2346,"nex-bokwallet","shaad",
                                "Dear Customer, your OTP is " +String.valueOf(verifycode)+" valid for 4 minutes." +
                                        getResources().getString(R.string.app_name)+" will never call you asking for OTP. " +
                                        "Sharing your OTP with anyone means you are giving your "+
                                        getResources().getString(R.string.app_name)+" access to them.",
                                "1","0",edNumber.getText().toString(),"Bokpay");*/

                        sender = new Sender("sms.digimiles.in", 8000, "nex-tigerpay", "tiger",
                                "Dear Customer, your OTP is " + verifycode + " valid for 10 minutes. dont share your OTP with anyone for your security. AcePay&entityid=1201159109110077633&tempid=1207165719304038230",
                                "1", "0", edNumber.getText().toString(), "ACECIO");



                        sender.call();


                        Log.e("CODE ", "onServiceResponse: "
                                +cpp.getSelectedCountryCode()+edNumber.getText().toString());


                        Locale swedishLocale = new Locale(cpp.getSelectedCountryNameCode(), cpp.getSelectedCountryNameCode());
                        displayCurrencyInfoForLocale(swedishLocale,cpp.getSelectedCountryNameCode());
                        finish();
                        Intent intent1=new Intent(AfterSplash.this,OtpVerification.class);
                        intent1.putExtra("otp",String.valueOf(verifycode));
                        intent1.putExtra("number",edNumber.getText().toString());

                        if(!ed_refral_code.getText().toString().equals(" "))
                        {
                            intent1.putExtra("refer",ed_refral_code.getText().toString());
                        }
                        else {
                            intent1.putExtra("refer","");
                        }
                        intent1.putExtra("number",edNumber.getText().toString());
                        intent1.putExtra("cpp",cpp.getSelectedCountryCode());
                        intent1.putExtra("id",cpp.getSelectedCountryCode());
                        intent1.putExtra("code",cpp.getSelectedCountryCode());
                        startActivity(intent1);
                    }
//                    else if (status.equals("false")){
//                        Constant.alertDialog(this, message);
//                        txSubmit.setVisibility(View.GONE);
//                    }
                    else
                    {
                        ed_refral_code.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;


            case Constant.REQ_NEW_COURTRY_CODE:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    if (status.equals("true")) {

                        customModels.clear();

                        CustomModel country1 = new CustomModel();
                        country1.setCountry_id("");
                        country1.setCountry_name("Select Country");
                        country1.setCode("");
                        customModels.add(country1);

                        JSONArray data = result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {

                            JSONObject CountryObj = data.getJSONObject(i);
                            CustomModel country = new CustomModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("sortname"));
                            country.setCode(CountryObj.getString("phonecode"));
                            customModels.add(country);
                        }
                    } else {
                        Constant.alertDialog(this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constant.REQ_CHECK_REFER_CODE:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    if (status.equals("true")) {
                        Log.e("ReferCode","success");
                        performSubmitAction();


                    } else {

                        Constant.alertDialog(this, message);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
