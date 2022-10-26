package com.acepay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.App;
import com.acepay.Util.Constant;
import com.acepay.Util.GpsTracker;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, RetrofitResponse,KeyEvent.Callback {

    TextView txnext, txSubmit,tvforgot, txEnter,tvattempts, tvGenerate;
    ImageView ivarrow,imFis,imS,imth,imfo;
    EditText edfirst, edSecond, edthird, edFouth;
    String confirm_secure_pin;
    Double latitude,longitude;
    GpsTracker gpsTracker;
    LinearLayout lnLayerforgot;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edfirst = (EditText) findViewById(R.id.edfirst);
        edSecond = (EditText) findViewById(R.id.edSecond);
        edthird = (EditText) findViewById(R.id.edthird);
        edFouth = (EditText) findViewById(R.id.edFouth);
        txnext = (TextView) findViewById(R.id.txnext);
        tvforgot = (TextView) findViewById(R.id.tvforgot);
        lnLayerforgot = (LinearLayout) findViewById(R.id.lnLayerforgot);
        tvGenerate = (TextView) findViewById(R.id.tvGenerate);
        tvattempts = (TextView) findViewById(R.id.tvattempts);
       // txEnter = (TextView) findViewById(R.id.txEnter);
        txSubmit = (TextView) findViewById(R.id.txSubmit);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);

        imFis = (ImageView) findViewById(R.id.imFis);
        imS = (ImageView) findViewById(R.id.imS);
        imth = (ImageView) findViewById(R.id.imth);
        imfo = (ImageView) findViewById(R.id.imfo);

        txnext.setOnClickListener(this);
        txSubmit.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        tvforgot.setOnClickListener(this);

        edFouth.setOnClickListener(this);
        edthird.setOnClickListener(this);
        edSecond.setOnClickListener(this);
        edfirst.setOnClickListener(this);

        Log.e("login pin-->","yes");
      //Log.e("secure pin-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.secure_pin));

        edfirst.setText("");
        edSecond.setText("");
        edthird.setText("");
        edFouth.setText("");

      //edfirst.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
      //edfirst.setInputType(InputType.TYPE_CLASS_NUMBER);

        imFis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edfirst.setVisibility(View.VISIBLE);
                imFis.setVisibility(View.GONE);
                edfirst.requestFocus();
//
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }
            }
        });

        edfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edfirst.setVisibility(View.VISIBLE);
                imFis.setVisibility(View.GONE);
                edfirst.requestFocus();
//
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }
            }
        });

        edfirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edfirst, InputMethodManager.SHOW_IMPLICIT);

                    edfirst.setVisibility(View.VISIBLE);
                    imFis.setVisibility(View.GONE);
                    edfirst.requestFocus();
//
                    if(edSecond.getText().toString().length()==1){
                        edSecond.setVisibility(View.INVISIBLE);
                        imS.setVisibility(View.VISIBLE);
                    }
                    if(edthird.getText().toString().length()==1){
                        edthird.setVisibility(View.INVISIBLE);
                        imth.setVisibility(View.VISIBLE);
                    }
                    if(edFouth.getText().toString().length()==1){
                        edFouth.setVisibility(View.INVISIBLE);
                        imfo.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        edSecond.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edSecond, InputMethodManager.SHOW_IMPLICIT);
                    edSecond.setVisibility(View.VISIBLE);
                    imS.setVisibility(View.GONE);
                    edSecond.requestFocus();

                    if(edfirst.getText().toString().length()==1){
                        edfirst.setVisibility(View.INVISIBLE);
                        imFis.setVisibility(View.VISIBLE);
                    }
                    if(edthird.getText().toString().length()==1){
                        edthird.setVisibility(View.INVISIBLE);
                        imth.setVisibility(View.VISIBLE);
                    }
                    if(edFouth.getText().toString().length()==1){
                        edFouth.setVisibility(View.INVISIBLE);
                        imfo.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        edthird.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edthird, InputMethodManager.SHOW_IMPLICIT);

                    edthird.setVisibility(View.VISIBLE);
                    imth.setVisibility(View.GONE);
                    edthird.requestFocus();

                    if(edfirst.getText().toString().length()==1){
                        edfirst.setVisibility(View.INVISIBLE);
                        imFis.setVisibility(View.VISIBLE);
                    }
                    if(edSecond.getText().toString().length()==1){
                        edSecond.setVisibility(View.INVISIBLE);
                        imS.setVisibility(View.VISIBLE);
                    }
                    if(edFouth.getText().toString().length()==1){
                        edFouth.setVisibility(View.INVISIBLE);
                        imfo.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        edFouth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edFouth, InputMethodManager.SHOW_IMPLICIT);

                    edFouth.setVisibility(View.VISIBLE);
                    imfo.setVisibility(View.GONE);
                    edFouth.requestFocus();

                    if(edfirst.getText().toString().length()==1){
                        edfirst.setVisibility(View.INVISIBLE);
                        imFis.setVisibility(View.VISIBLE);
                    }
                    if(edSecond.getText().toString().length()==1){
                        edSecond.setVisibility(View.INVISIBLE);
                        imS.setVisibility(View.VISIBLE);
                    }
                    if(edthird.getText().toString().length()==1){

                        Log.e("edFouth-->","yes");
                        edthird.setVisibility(View.INVISIBLE);
                        imth.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        imS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSecond.setVisibility(View.VISIBLE);
                imS.setVisibility(View.GONE);
                edSecond.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }

            }
        });

        edSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSecond.setVisibility(View.VISIBLE);
                imS.setVisibility(View.GONE);
                edSecond.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }

            }
        });

        imth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edthird.setVisibility(View.VISIBLE);
                imth.setVisibility(View.GONE);
                edthird.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }
            }
        });

        edthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edthird.setVisibility(View.VISIBLE);
                imth.setVisibility(View.GONE);
                edthird.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edFouth.getText().toString().length()==1){
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);
                }
            }
        });

        imfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edFouth.setVisibility(View.VISIBLE);
                imfo.setVisibility(View.GONE);
                edFouth.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){

                    Log.e("edSecond-->","yes");
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
            }
        });

        edFouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("edFouth-->",edthird.getText().toString().length()+"");
                edFouth.setVisibility(View.VISIBLE);
                imfo.setVisibility(View.GONE);
                edFouth.requestFocus();

                if(edfirst.getText().toString().length()==1){
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                }
                if(edSecond.getText().toString().length()==1){
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                }
                if(edthird.getText().toString().length()==1){

                    Log.e("edFouth-->","yes");
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);
                }
            }
        });

        edfirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edfirst.getText().toString().length() == 1)     //size as per your requirement
                {
                    edSecond.requestFocus();
                    edfirst.setVisibility(View.INVISIBLE);
                    imFis.setVisibility(View.VISIBLE);
                    if(edFouth.getText().toString().length() == 1 && edthird.getText().toString().length() == 1 && edSecond.getText().toString().length() == 1 && edfirst.getText().toString().length() == 1) {
                        Constant.hideKeyboard(Login.this,edFouth);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        edSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("beforeTextChanged-->","yes");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edSecond.getText().toString().length() == 1)     //size as per your requirement
                {
                    edthird.requestFocus();
                    edSecond.setVisibility(View.INVISIBLE);
                    imS.setVisibility(View.VISIBLE);
                    Log.e("onTextChanged-->","yes");
                    if(edFouth.getText().toString().length() == 1 && edthird.getText().toString().length() == 1 && edSecond.getText().toString().length() == 1 && edfirst.getText().toString().length() == 1) {
                        Constant.hideKeyboard(Login.this,edFouth);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e("afterTextChanged-->","yes");
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
                    edFouth.requestFocus();
                    edthird.setVisibility(View.INVISIBLE);
                    imth.setVisibility(View.VISIBLE);

                    if(edFouth.getText().toString().length() == 1 && edthird.getText().toString().length() == 1 && edSecond.getText().toString().length() == 1 && edfirst.getText().toString().length() == 1) {
                        Constant.hideKeyboard(Login.this,edFouth);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edFouth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edFouth.getText().toString().length() == 1)     //size as per your requirement
                {
                    edFouth.setVisibility(View.INVISIBLE);
                    imfo.setVisibility(View.VISIBLE);

                    if(edFouth.getText().toString().length() == 1 && edthird.getText().toString().length() == 1 && edSecond.getText().toString().length() == 1 && edfirst.getText().toString().length() == 1) {
                         Constant.hideKeyboard(Login.this,edFouth);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edfirst.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    // this is for backspace
                    Log.e("IME_TEST", "DEL KEY");
                    if (edfirst.getText().length() == 1) {
                        edfirst.setText("");
                        imFis.setVisibility(View.GONE);
                        edfirst.setVisibility(View.VISIBLE);
                        edfirst.requestFocus();
                    }
                }
                return false;
            }
        });

        edSecond.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    // this is for backspace
                    Log.e("IME_TEST", "DEL KEY");
                    if (edSecond.getText().length() == 1) {
                        edSecond.setText("");
                        imS.setVisibility(View.GONE);
                        edSecond.setVisibility(View.VISIBLE);
                        edSecond.requestFocus();
                    }
                    else {
                        edfirst.setText("");
                        imFis.setVisibility(View.GONE);
                        edfirst.setVisibility(View.VISIBLE);
                        edfirst.requestFocus();
                    }
                }
                return false;
            }
        });

        edthird.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    // this is for backspace
                    Log.e("IME_TEST", "DEL KEY");
                if (edthird.getText().length() == 1) {
                    edthird.setText("");
                      imth.setVisibility(View.GONE);
                    edthird.setVisibility(View.VISIBLE);
                    edthird.requestFocus();

                    }else {
                    edSecond.setText("");
                    imS.setVisibility(View.GONE);
                    edSecond.setVisibility(View.VISIBLE);
                    edSecond.requestFocus();
                }
                }
                return false;
            }
        });

        edFouth.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // You can identify which key pressed buy checking keyCode value
                // with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    // this is for backspace
                    Log.e("IME_TEST", "DEL KEY");
                    if (edFouth.getText().length() == 1) {
                        edFouth.setText("");
                        imfo.setVisibility(View.GONE);
                        edFouth.setVisibility(View.VISIBLE);
                        edFouth.requestFocus();

                    }else {
                        edthird.setText("");
                        imth.setVisibility(View.GONE);
                        edthird.setVisibility(View.VISIBLE);
                        edthird.requestFocus();

                    }
                }
                return false;
            }
        });

        edfirst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(Login.this,v);
                    callmethod();
                }

                return false;
            }
        });

        edSecond.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(Login.this,v);
                    callmethod();
                }
                return false;
            }
        });

        edthird.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(Login.this,v);
                    callmethod();
                }
                return false;
            }
        });

        edFouth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Constant.hideKeyboard(Login.this,v);
                    callmethod();
                }
                return false;
            }
        });

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null){

            lnLayerforgot.setVisibility(View.GONE);
        }
        else
        {

           int count = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

            if(count > 0) {

                lnLayerforgot.setVisibility(View.VISIBLE);
                count = 4 - count;

                tvattempts.setText(count + " attempts remaining. ");
            }
        }

      /*  edFouth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if (actionId != 0 || keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {

                    View view=Login.this.getCurrentFocus();
                    Constant.hideKeyboard(Login.this,view);
                    return true;
                }
                return false;
            }
        });
*/

        getLocation();
    }

    void callmethod(){

        confirm_secure_pin="";
        confirm_secure_pin = edfirst.getText().toString() + edSecond.getText().toString() + edthird.getText().toString() + edFouth.getText().toString();

        if(confirm_secure_pin.length()==4) {

            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.secure_pin) != null) {

                if (confirm_secure_pin.equals(PreferenceFile.getInstance().getPreferenceData(this, Constant.secure_pin))) {

                    SharedPreferences sharedPreferences = App.getIdPref();
                    Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                        postParam.put("secure_pin", confirm_secure_pin);
                        postParam.put("lat", String.valueOf(latitude));
                        postParam.put("lng", String.valueOf(longitude));

                        postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                        postParam.put("device_id", Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                        Log.e("postparam--->", postParam.toString());

                        if (Constant.isConnectingToInternet(Login.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    if (PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY) == null) {
                        PreferenceFile.getInstance().saveData(this, Constant.COUNT_SECURITY, "1");

                        int count = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY));

                        if (count > 0) {

                            lnLayerforgot.setVisibility(View.VISIBLE);
                            count = 4 - count;

                            tvattempts.setText(count + " attempts remaining. ");
                        }

                    } else {

                        x = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY));
                        Log.e("x-->", x + "");
                        x++;
                        PreferenceFile.getInstance().saveData(this, Constant.COUNT_SECURITY, String.valueOf(x));

                        int count = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY));

                        if (count > 0) {

                            lnLayerforgot.setVisibility(View.VISIBLE);
                            count = 4 - count;

                            tvattempts.setText(count + " attempts remaining. ");
                        }

                        if (x == 4) {

                            JSONObject postParam = new JSONObject();
                            try {
                                postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                                postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                                Log.e("postparam--->", postParam.toString());

                                if (Constant.isConnectingToInternet(Login.this)) {
                                    Log.e("connect--->", "yes");
                                    new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                                } else {

                                    Log.e("connect--->", "no");
                                    Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Log.e("regenrate secure-->", x + "");
                        }
                    }
                    imFis.setVisibility(View.GONE);
                    imS.setVisibility(View.GONE);
                    imth.setVisibility(View.GONE);
                    imfo.setVisibility(View.GONE);

                    edfirst.setVisibility(View.VISIBLE);
                    edSecond.setVisibility(View.VISIBLE);
                    edthird.setVisibility(View.VISIBLE);
                    edFouth.setVisibility(View.VISIBLE);

                    edfirst.setText("");
                    edSecond.setText("");
                    edthird.setText("");
                    edFouth.setText("");

                    edfirst.requestFocus();

                    if (x < 4) {
                        Constant.alertDialog(this, getResources().getString(R.string.incorrect_secure_pin));
                    }
                }
            }
            else {

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY) == null) {
                    x = 0;
                } else {
                    x = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY));
                }

                if (x < 4) {

                    SharedPreferences sharedPreferences = App.getIdPref();
                    Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                        postParam.put("secure_pin", confirm_secure_pin);
                        postParam.put("lat", String.valueOf(latitude));
                        postParam.put("lng", String.valueOf(longitude));

                        postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                        postParam.put("device_id", Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                        Log.e("postparam--->", postParam.toString());

                        if (Constant.isConnectingToInternet(Login.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (x == 4) {

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                        postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                        Log.e("postparam--->", postParam.toString());

                        if (Constant.isConnectingToInternet(Login.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("regenrate secure-->", x + "");
                }

            }

        }

    }

    public void getLocation() {

        gpsTracker = new GpsTracker(Login.this);

        if (gpsTracker.canGetLocation())
        {
            longitude = gpsTracker.getLongitude();
            latitude = gpsTracker.getLatitude();

        }
        else
        {

            AlertDialog.Builder alertDialog;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_Alert);
            }else {
                alertDialog = new AlertDialog.Builder(this);
            }

            alertDialog.setCancelable(false);
            // Setting Dialog Title
            alertDialog.setTitle(this.getResources().getString(R.string.gps_setting));//gps_setting
            alertDialog.setMessage(this.getResources().getString(R.string.gps_not_enable));
            alertDialog.setPositiveButton(this.getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                }
            });

            alertDialog.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            new CountDownTimer(50000, 2000) {
                public void onTick(long millisUntilFinished) {
                    latitude=GpsTracker.latitude;
                    longitude=GpsTracker.longitude;
                    Log.e("lati-->",latitude+" longi--->"+longitude);
                }
                public void onFinish() {

                }
            }.start();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("onResume-->","yes");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {

            case R.id.txnext:

                Constant.hideKeyboard(this,v);

                confirm_secure_pin="";
                confirm_secure_pin = edfirst.getText().toString() + edSecond.getText().toString() + edthird.getText().toString() + edFouth.getText().toString();

                Log.e("confirm_secure_pin-->",confirm_secure_pin);

                if (confirm_secure_pin.equals("")) {
                    Constant.alertDialog(this, getResources().getString(R.string.please_enter_secure_pin));
                }

               else if(PreferenceFile.getInstance().getPreferenceData(this,Constant.secure_pin)!=null) {

                    if (confirm_secure_pin.equals(PreferenceFile.getInstance().getPreferenceData(this, Constant.secure_pin))) {

                        SharedPreferences sharedPreferences = App.getIdPref();
                        Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

                        JSONObject postParam = new JSONObject();
                        try {
                            postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                            postParam.put("secure_pin", confirm_secure_pin);
                            postParam.put("lat", String.valueOf(latitude));
                            postParam.put("lng", String.valueOf(longitude));

                            postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                            postParam.put("device_id",Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                            Log.e("postparam--->", postParam.toString());

                            if (Constant.isConnectingToInternet(Login.this)) {
                                Log.e("connect--->", "yes");
                                new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
                            } else {

                                Log.e("connect--->", "no");
                                Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null){
                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                            int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                            if(count<4) {

                                lnLayerforgot.setVisibility(View.VISIBLE);
                                count = 4 - count;

                                tvattempts.setText(count + " attempts remaining.");
                            }
                        }
                        else {
                             x= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
                            Log.e("x-->",x+"");
                            x++;
                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,String.valueOf(x));

                            int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                            if(count<4) {

                                lnLayerforgot.setVisibility(View.VISIBLE);
                                count = 4 - count;

                                tvattempts.setText(count + " attempts remaining. ");
                            }
                            if(x==4){

                                JSONObject postParam = new JSONObject();
                                try {
                                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                                    postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                                    Log.e("postparam--->", postParam.toString());

                                    if (Constant.isConnectingToInternet(Login.this)) {
                                        Log.e("connect--->", "yes");
                                        new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                                    }
                                    else {

                                        Log.e("connect--->", "no");
                                        Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.e("regenrate secure-->",x+"");
                            }

                        }

                        edfirst.setText("");
                        edSecond.setText("");
                        edthird.setText("");
                        edFouth.setText("");

                        imFis.setVisibility(View.GONE);
                        imS.setVisibility(View.GONE);
                        imth.setVisibility(View.GONE);
                        imfo.setVisibility(View.GONE);

                        edfirst.setVisibility(View.VISIBLE);
                        edSecond.setVisibility(View.VISIBLE);
                        edthird.setVisibility(View.VISIBLE);
                        edFouth.setVisibility(View.VISIBLE);


                        edfirst.requestFocus();

                        if(x<4) {
                            Constant.alertDialog(this, getResources().getString(R.string.incorrect_secure_pin));
                        }
                    }
                }

                else {

                    if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null) {
                         x =0;
                    }
                    else {
                        x=Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
                    }

                    if(x < 4 ) {

                        SharedPreferences sharedPreferences = App.getIdPref();
                        Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

                            JSONObject postParam = new JSONObject();
                            try {

                                postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                                postParam.put("secure_pin", confirm_secure_pin);
                                postParam.put("lat", String.valueOf(latitude));
                                postParam.put("lng", String.valueOf(longitude));

                                postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                                postParam.put("device_id",Settings.Secure.getString(Login.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                                Log.e("postparam--->", postParam.toString());

                                if (Constant.isConnectingToInternet(Login.this)) {
                                    Log.e("connect--->", "yes");
                                    new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
                                }
                                else {

                                    Log.e("connect--->", "no");
                                    Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    if(x==4) {

                            JSONObject postParam = new JSONObject();
                            try {
                                postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                                postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                                Log.e("postparam--->", postParam.toString());

                                if (Constant.isConnectingToInternet(Login.this)) {
                                    Log.e("connect--->", "yes");
                                    new Retrofit2(Login.this, Login.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                                }
                                else {

                                    Log.e("connect--->", "no");
                                    Constant.alertDialog(Login.this, getResources().getString(R.string.check_connection));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("regenrate secure-->",x+"");
                    }

                }

                break;

            case R.id.ivarrow:

               finish();
                break;


            case R.id.tvforgot:

              Intent intent1=new Intent(Login.this,ForgotPin.class);
                startActivity(intent1);
                break;

            case R.id.edFirst:

               Log.e("edFirst-->","yes");
                break;

            case R.id.edForth:

               Log.e("edForth-->","yes");
                break;
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_LOGIN:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){

                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
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

                                Intent intent = new Intent(Login.this, Dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                            else {

                                Intent intent = new Intent(Login.this, EditProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                        } else {

                            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null) {
                                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
                                int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                                if(count < 4) {

                                    lnLayerforgot.setVisibility(View.VISIBLE);
                                    count = 4 - count;

                                    tvattempts.setText(count + " attempts remaining. ");
                                }
                            }
                            else {
                                 x= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
                                x++;
                                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,String.valueOf(x));
                                Log.e("x-->",x+"");

                                int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                                if(count< 4) {

                                    lnLayerforgot.setVisibility(View.VISIBLE);
                                    count = 4 - count;

                                    tvattempts.setText(count + " attempts remaining. ");
                                }
                            }

                            edfirst.setText("");
                            edSecond.setText("");
                            edthird.setText("");
                            edFouth.setText("");

                            Log.e("invisuble-->","YES");

                            imFis.setVisibility(View.GONE);
                            imS.setVisibility(View.GONE);
                            imth.setVisibility(View.GONE);
                            imfo.setVisibility(View.GONE);

                            edfirst.setVisibility(View.VISIBLE);
                            edSecond.setVisibility(View.VISIBLE);
                            edthird.setVisibility(View.VISIBLE);
                            edFouth.setVisibility(View.VISIBLE);

                            Log.e("data--->", status + "");
                            Constant.alertDialog(Login.this, message);

                        }

                    } catch (JSONException e) {
                        Log.e("exception-->", e.toString());
                    }
                    catch (IOException e) {

                    }
                }
                    break;

                    case Constant.REQ_Block_USER:

                        if (response.isSuccessful()) {

                            JSONObject result1 = null;
                            try {
                                result1 = new JSONObject(response.body().string());
                                Log.e("req_sign_up--->", "yes");
                                Log.e("resultttt-->", result1.toString());
                                String status = result1.getString("response");
                                String message = result1.getString("message");

                                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"4");
                                PreferenceFile.getInstance().saveData(this,Constant.Accunt_status,"Inactive");

                                    Constant.alertWithIntent(this,"Your Account has beed Blocked.",BlockScreen.class);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        break;



        }
    }
    }
