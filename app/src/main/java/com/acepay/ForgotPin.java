package com.acepay;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ForgotPin extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {
    DateFormat dateFormatter;
    DatePickerDialog datePickerDialog;
    EditText edpancard,edaccount,edEmail,edDob;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String imagepath="";
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    TextView txSubmit;
    ImageView ivarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);
        TextView txName= (TextView) findViewById(R.id.txName);
        txName.setText("Forgot Secure Pin");
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        edDob= (EditText) findViewById(R.id.dob);
        edpancard= (EditText) findViewById(R.id.edpancard);
        edaccount= (EditText) findViewById(R.id.edbankacc);
        edEmail= (EditText) findViewById(R.id.edmail);
        txSubmit=(TextView)findViewById(R.id.txSubmit);
        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        edDob.setOnClickListener(this);
        txSubmit.setOnClickListener(this);
        ivarrow.setOnClickListener(this);
        edpancard.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void date()
    {
        final Calendar newCalendar = Calendar.getInstance();
        final Date curDate = new Date();

        dateFormatter.format(curDate);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    Constant.alertDialog(ForgotPin.this,getString(R.string.please_select_valid_date));
                }
                else {
                    edDob.setText(dateFormatter.format(userAge.getTime()));
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.dob:
                date();
                break;
            case R.id.txSubmit:
                callmyservice();
                break;
            case R.id.ivarrow:
                finish();
                break;
        }
    }

    public boolean verification(){

        if(edDob.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_dob));
            return false;
        }

        Matcher matcher = pattern.matcher(edpancard.getText().toString());
// Check if pattern matches

        //Log.e("verification--->",imagepath);
/*
        if(imagepath.equals("")){

            Constant.alertDialog(this,getString(R.string.please_select_pan_card_image));

            return false;
        }*/

        if(edpancard.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_pan_card_no));
            return false;
        }

        if (!matcher.matches()) {
            Log.e("Matching","Yes");
            Constant.alertDialog(this,getString(R.string.please_enter_valid_pan_card_no));
            return false;
        }
        if(edEmail.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_email));

            return false;
        }
        if (!edEmail.getText().toString().matches(emailPattern)) {
            Constant.alertDialog(this, getString(R.string.please_enter_valid_email));
            return false;
        }

       if(edaccount.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_account_number));
            return false;
        }
        if(edaccount.getText().toString().length()<14  || edaccount.getText().toString().length()>16){

           Constant.alertDialog(this,getString(R.string.Please_enter_correct_account_number));
           return false;
       }

        return true;
    }

    public void callmyservice(){
        if (verification()) {
            JSONObject postParam = new JSONObject();
            try {
                postParam.put("dob", edDob.getText().toString());
                postParam.put("account_number", edaccount.getText().toString());
                postParam.put("pancard_number", edpancard.getText().toString());
                postParam.put("email", edEmail.getText().toString());
                postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
               // postParam.put("user_id","112");
               // postParam.put("phone","6754345678");
                Log.e("postparam--->", postParam.toString());

                if (Constant.isConnectingToInternet(ForgotPin.this)) {
                    Log.e("connect--->", "yes");
                    new Retrofit2(this, ForgotPin.this, postParam, Constant.REQ_FORGOT_PIN, Constant.FORGOT_PIN, "3").callService(true);
                } else {

                    Log.e("connect--->", "no");
                    Constant.alertDialog(ForgotPin.this, getResources().getString(R.string.check_connection));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {
        Log.e("on service-->", "forgotpin");
        switch (requestCode) {
            case Constant.REQ_FORGOT_PIN:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        Log.e("result-->", result.toString());

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            PreferenceFile.getInstance().saveData(this,Constant.secure_pin,null);
                            Constant.alertWithIntent(this,message,LoginNew.class);

                        } else
                        {
                            Constant.alertDialog(this, message);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }
                break;
        }
    }
}
