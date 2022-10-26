package com.acepay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ChangePin extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow;
    TextView txName,txSubmit;
    LinearLayout lnChangePin;
    String old_pin="",new_pin="",confirm_pin="";
    EditText edfirst,edSecond,edthird,edFouth,edfirstnew,edSecondnew,edthirdnew,edFouthnew,edfirstcnf,edSecondcnf,edthirdcf,edFouthcf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);
        txSubmit= (TextView) findViewById(R.id.txSubmit);

        edfirst= (EditText) findViewById(R.id.edfirst);
        edSecond= (EditText) findViewById(R.id.edSecond);
        edthird= (EditText) findViewById(R.id.edthird);
        edFouth= (EditText) findViewById(R.id.edFouth);
        edfirstnew= (EditText) findViewById(R.id.edfirstnew);
        edSecondnew= (EditText) findViewById(R.id.edSecondnew);
        edthirdnew= (EditText) findViewById(R.id.edthirdnew);
        edFouthnew= (EditText) findViewById(R.id.edFouthnew);
        edfirstcnf= (EditText) findViewById(R.id.edfirstcnf);
        edSecondcnf= (EditText) findViewById(R.id.edSecondcnf);
        edthirdcf= (EditText) findViewById(R.id.edthirdcf);
        edFouthcf= (EditText) findViewById(R.id.edFouthcf);

        ivarrow.setOnClickListener(this);
        txSubmit.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);

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
            public void afterTextChanged(Editable editable) {

            }
        });

        edSecond.addTextChangedListener(new TextWatcher() {
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
            public void afterTextChanged(Editable editable) {

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
                    edfirstnew.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edfirstnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edfirstnew.getText().toString().length() == 1)     //size as per your requirement
                {
                    edSecondnew.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSecondnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edSecondnew.getText().toString().length() == 1)     //size as per your requirement
                {
                    edthirdnew.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edthirdnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edthirdnew.getText().toString().length() == 1)     //size as per your requirement
                {
                    edFouthnew.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edFouthnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edFouthnew.getText().toString().length() == 1)     //size as per your requirement
                {
                    edfirstcnf.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edfirstcnf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edfirstcnf.getText().toString().length() == 1)     //size as per your requirement
                {
                    edSecondcnf.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edSecondcnf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edSecondcnf.getText().toString().length() == 1)     //size as per your requirement
                {
                    edthirdcf.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edthirdcf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edthirdcf.getText().toString().length() == 1)     //size as per your requirement
                {
                    edFouthcf.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.ivarrow:

                intent=new Intent(ChangePin.this,Setting.class);
                startActivity(intent);

                break;


            case R.id.txSubmit:

                old_pin="";
                new_pin="";
                confirm_pin="";

                old_pin = edfirst.getText().toString() + edSecond.getText().toString() + edthird.getText().toString() +
                        edFouth.getText().toString();
                new_pin = edfirstnew.getText().toString() + edSecondnew.getText().toString() +
                        edthirdnew.getText().toString() + edFouthnew.getText().toString();
                confirm_pin = edfirstcnf.getText().toString() + edSecondcnf.getText().toString() +
                        edthirdcf.getText().toString() + edFouthcf.getText().toString();

                if(old_pin.length()< 4){

                    Constant.alertDialog(this,getString(R.string.please_enter_four_digit_old_secure_pin));
                }

                else if(!old_pin.equals(PreferenceFile.getInstance().getPreferenceData(this,Constant.secure_pin))){

                    Constant.alertDialog(this,getString(R.string.please_enter_correct_old_secure_pin));
                }

                else if(new_pin.length()< 4){

                    Constant.alertDialog(this,getString(R.string.please_enter_four_digits_new_secure_pin));
                }

                else if(confirm_pin.length()< 4){

                    Constant.alertDialog(this,getString(R.string.please_enter_four_digits_confirm_secure_pin));
                }

                else if(!new_pin.equals(confirm_pin)){

                    Constant.alertDialog(this,getString(R.string.secure_pin_do_not_match));
                }

                else {

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this,Constant.ID));
                        postParam.put("pin", old_pin);
                        postParam.put("new_pin", new_pin);
                        Log.e("postparam--->", postParam.toString());

                        if (Constant.isConnectingToInternet(ChangePin.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(this, ChangePin.this, postParam, Constant.REQ_EDIT_SECURE_PIN, Constant.EDIT_SECURE_PIN, "3").callService(true);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(ChangePin.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_EDIT_SECURE_PIN:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")) {

                        PreferenceFile.getInstance().saveData(this,Constant.secure_pin,new_pin);
                        Constant.alertWithIntent(this,message,Setting.class);
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

