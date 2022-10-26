package com.acepay;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.acepay.SmsGateway.Sender;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class OtpVerification extends AppCompatActivity implements View.OnClickListener, RetrofitResponse
{
    TextView txNext,txback,txResend;
    ImageView ivarrow;
    Sender sender;
    String enterotp="";
    View view;
    double counter=2.00;
    TextView chronometer,txno;
    String otp,number,cpp,courtry_id,refer;
    CountDownTimer countDownTimer;
    EditText edfirst,edSecond,edthird,edForth;
    String otpReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_otp_verification);

        edfirst= (EditText) findViewById(R.id.edfirst);
        edSecond= (EditText) findViewById(R.id.edSecond);
        edthird= (EditText) findViewById(R.id.edthird);
        edForth= (EditText) findViewById(R.id.edForth);

        txNext= (TextView) findViewById(R.id.txNext);
        txback= (TextView) findViewById(R.id.txback);
        txResend= (TextView) findViewById(R.id.txResend);
        chronometer= (TextView) findViewById(R.id.chronometer);
        txno= (TextView) findViewById(R.id.txno);

        otp=getIntent().getStringExtra("otp");
        number=getIntent().getStringExtra("number");
        refer=getIntent().getStringExtra("refer");
        cpp=getIntent().getStringExtra("cpp");
        courtry_id=getIntent().getStringExtra("id");


        if (checkAndRequestPermissions())
        {
            //everythine will be fine and normal go
        }


        txno.setText("+"+cpp+number);



        ivarrow= (ImageView) findViewById(R.id.ivarrow);

        txback.setOnClickListener(this);
        txNext.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        txResend.setOnClickListener(this);
        edfirst.setOnClickListener(this);
        edSecond.setOnClickListener(this);
        edthird.setOnClickListener(this);
        edForth.setOnClickListener(this);



     /*  SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                try {

                    Log.e("OTPVERIFICATION ", messageText.toString().trim());

                    if (messageText.toString().contains("valid"))
                    {
                        Log.e("ifffffffffff ", messageText.toString().trim());
                        String a[] = messageText.toString().split("valid");
                        String b[] = a[0].toString().split(" is");
                        otpReceived = b[1].toString().toString().trim();
//                        Log.e("abbbb", a[0].toString() + " GAGANNN");
//                        Log.e("bbbbb", b[0].toString().toString().trim() + "   VAAAAA");
//                        Log.e("bbbbb", b[1].toString().toString().trim() + "   VAAAAA");
                    } else
                    {
                        Log.e("OTPVERIFICATIONELSEEEE ", messageText.toString().trim());
                    }

                    edfirst.setText(otpReceived.charAt(0) + "");
                    edSecond.setText(otpReceived.charAt(1) + "");
                    edthird.setText(otpReceived.charAt(2) + "");
                    edForth.setText(otpReceived.charAt(3) + "");

                *//* edfirst.setText(messageText.charAt(0) + "");
                edSecond.setText(messageText.charAt(1) + "");
                edthird.setText(messageText.charAt(2) + "");
                edForth.setText(messageText.charAt(3) + "");*//*

                    enterotp = edfirst.getText().toString() + edSecond.getText().toString() +
                            edthird.getText().toString() + edForth.getText().toString();


                    if (enterotp.equals(otp))
                    {
                        countDownTimer.cancel();
                        JSONObject postParam = new JSONObject();
                        try {

                            postParam.put("phone", number);

                            if (Constant.isConnectingToInternet(OtpVerification.this))
                            {
                                new Retrofit2(OtpVerification.this, OtpVerification.this, postParam,
                                        Constant.REQ_CHECK_NO, Constant.CHECK_NO, "3").callService(true);
                            } else {
                                Constant.alertDialog(OtpVerification.this, getResources().getString(R.string.check_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                   *//* intent = new Intent(OtpVerification.this, GenrateSecurePin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("class", "otp");
                    intent.putExtra("no", number);
                    intent.putExtra("cpp", cpp);
                    startActivity(intent);*//*
                    } else {

                        Constant.alertDialog(OtpVerification.this, getResources().getString(R.string.otp_not_match));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });*/

//        calltimer();
        callTimer();

        edfirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edfirst.getText().toString().length() == 1)     //size as per your requirement
                {
                    edSecond.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        edSecond.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edSecond.getText().toString().length() == 1)     //size as per your requirement
                {
                    edthird.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        edthird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edthird.getText().toString().length() == 1)     //size as per your requirement
                {
                    edForth.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        edfirst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(OtpVerification.this,v);
                    callmethod();
                }
                return false;
            }
        });

        edSecond.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(OtpVerification.this,v);
                    callmethod();
                }
                return false;
            }
        });

        edthird.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(OtpVerification.this,v);
                    callmethod();
                }
                return false;
            }
        });

        edForth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(OtpVerification.this,v);

                    callmethod();
                }
                return false;
            }
        });
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase("otp"))
            {
                final String message = intent.getStringExtra("message");

                Log.e("Messsage ",message);
//                your_edittext.setText(message);
                //Do whatever you want with the code here
            }
        }
    };


    void callmethod() {

        enterotp=edfirst.getText().toString()+edSecond.getText().toString()+edthird.getText().toString()+edForth.getText().toString();

        if(enterotp.equals(otp)){

            countDownTimer.cancel();
            // finish();

            JSONObject postParam = new JSONObject();

            try {

                postParam.put("phone", number);
                if (Constant.isConnectingToInternet(OtpVerification.this)) {
                    new Retrofit2(OtpVerification.this, OtpVerification.this, postParam, Constant.REQ_CHECK_NO, Constant.CHECK_NO, "3").callService(true);
                }
                else {

                    Constant.alertDialog(OtpVerification.this, getResources().getString(R.string.check_connection));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
/*  int time = 4 * 60 * 1000; //4 minutes
    public void calltimer() {

        txNext.setVisibility(View.VISIBLE);

//        countDownTimer=   new CountDownTimer(40000, 1000)
        countDownTimer=   new CountDownTimer(time, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                chronometer.setText( millisUntilFinished / 1000 + " secs left");
            }

            public void onFinish()
            {
                txResend.setVisibility(View.VISIBLE);
                enterotp="";
                edfirst.setText("");
                edSecond.setText("");
                edthird.setText("");
                edForth.setText("");

                chronometer.setText("No seconds left!");
                otp="";
                txNext.setVisibility(View.GONE);
            }
        }.start();
    }*/



    int time = 4 * 60 * 1000; //20 seconds
    int interval = 1000; // 1 second

    public static String getDateFromMillis(long d)
    {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        df.setTimeZone(TimeZone.getDefault().getTimeZone("GMT"));
        return df.format(d);
    }

    public void callTimer()
    {
        txNext.setVisibility(View.VISIBLE);

        countDownTimer=   new CountDownTimer(time, interval)
        {
            public void onTick(long millisUntilFinished)
            {
                chronometer.setText(getDateFromMillis(millisUntilFinished));
            }

            public void onFinish()
            {
                txResend.setVisibility(View.VISIBLE);
                enterotp="";
                edfirst.setText("");
                edSecond.setText("");
                edthird.setText("");
                edForth.setText("");

                chronometer.setText("No seconds left!");
                otp="";
                txNext.setVisibility(View.GONE);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent;


        switch (v.getId()){

            case R.id.txback:

                finish();
                intent=new Intent(OtpVerification.this,AfterSplash.class);
                startActivity(intent);
                break;

            case R.id.txResend:
                countDownTimer.cancel();
                txNext.setVisibility(View.VISIBLE);
                Random r = new Random();
                int verification = (1000 + r.nextInt(9000));
                otp=String.valueOf(verification);
                Log.e("otp-->",otp);

//                sender=new Sender("sms.digimiles.in",8000,"di78-bitpay",
//                        "didimile","Dear Customer, your OTP is " +otp+" valid for 4 minutes." +
//                        " FIN-CEX will never call you asking for OTP. Sharing your OTP with anyone means" +
//                        " you are giving your FIN-CEX access to them.","1","0",number,"MetaPay");

               /* sender=new Sender("sms.digimiles.in",2346,"nex-bokwallet","shaad",
                        "Dear Customer, your OTP is " +otp+" valid for 4 minutes." +
                                getResources().getString(R.string.app_name)+" will never call you asking for OTP. " +
                                "Sharing your OTP with anyone means you are giving your "+getResources().getString(R.string.app_name)+" access to them.",
                        "1","0",number,"Bokpay");*/




                sender = new Sender("sms.digimiles.in", 8000, "nex-tigerpay", "tiger",
                        "Dear Customer, your OTP is " + otp + " valid for 10 minutes. don't share your OTP with anyone for your security. AcePay&entityid=1201159109110077633&tempid=1207165719304038230",
                        "1", "0", number, "ACECIO");


                //  String[] arguments = new String[] {"123"};
                sender.call();
                callTimer();
                txResend.setVisibility(View.GONE);

                break;

            case R.id.ivarrow:

                finish();
                intent=new Intent(OtpVerification.this,AfterSplash.class);
                startActivity(intent);

                break;

            case R.id.txNext:

                 enterotp=edfirst.getText().toString()+edSecond.getText().toString()+edthird.getText().toString()+edForth.getText().toString();

                Log.e("otp-->",enterotp+" otp-->"+otp);

                if(enterotp.equals(otp))
                {

                    countDownTimer.cancel();

                    JSONObject postParam = new JSONObject();

                     try {

                        postParam.put("phone", number);

                        if (Constant.isConnectingToInternet(OtpVerification.this)) {
                            new Retrofit2(OtpVerification.this, OtpVerification.this, postParam, Constant.REQ_CHECK_NO, Constant.CHECK_NO, "3").callService(true);
                        }
                        else {
                            Constant.alertDialog(OtpVerification.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                   /* intent = new Intent(OtpVerification.this, GenrateSecurePin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("class", "otp");
                    intent.putExtra("no", number);
                    intent.putExtra("cpp", cpp);
                    startActivity(intent);*/
                }
                else {
                    Constant.alertDialog(this,getResources().getString(R.string.otp_not_match));
                }

                break;
        }

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_CHECK_NO:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String status = result1.getString("response");
                        String message = result1.getString("message");
                        Log.e("status--->", status + "");

                        if (status.equals("true")) {

                            JSONObject result = result1.getJSONObject("data");
                            Log.e("result--->", result.toString() + "");
                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.Accunt_status, result.getString("status"));

                            if(result.getString("status").equals("Inactive"))
                            {
                                Log.e("INSIDR--->", result.toString() + "");
                                PreferenceFile.getInstance().saveData(this, Constant.Accunt_status, result.getString("status"));
                                Constant.alertWithIntent(this,"Your Account has been Blocked.",BlockScreen.class);
                            }
                            else
                             {
                                Log.e("ELSE--->", result.toString() + "");
                                finish();
                                Intent intent1 = new Intent(OtpVerification.this, LoginNew.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                            }

                        } else {
                            finish();
                            Intent intent = new Intent(OtpVerification.this, GenrateSecurePin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("class", "otp");
                            intent.putExtra("no", number);
                            intent.putExtra("refer", refer);
                            intent.putExtra("cpp", cpp);
                            intent.putExtra("id", courtry_id);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e)
                    {
                        Log.e("exception-->", e.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

        }
    }
}
