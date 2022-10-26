package com.acepay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.App;
import com.acepay.Util.Constant;
import com.acepay.Util.GpsTracker;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Widget.PinEntryEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class GenrateSecurePin extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    TextView txnext, txSubmit, txEnter, tvGenerate;
    ImageView ivarrow,imFis,imS,imth,imfo;
    EditText edfirst, edSecond, edthird,ed_refral_code, edFouth;
    String classname, phnno,courty_id, secure_code = "",secure_codeok = "", confirm_secure_pin,refer_code="";
    GpsTracker gpsTracker;
    Double longitude,latitude;
    LinearLayout lnLayerforgot;
    String text="";
    int x;
    int flag=1;
    private LinearLayout eight;
    private LinearLayout five;
    private LinearLayout four;
    private LinearLayout ok;
    private LinearLayout nine;
    private StringBuffer num;
    private LinearLayout one;
    private LinearLayout seven;
    private LinearLayout six;
    private LinearLayout back;
    private LinearLayout three;
    private LinearLayout two;
    private LinearLayout zero;
    private PinEntryEditText txtPinEntry;
    private ImageView pin1,pin2,pin3,pin4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_genrate_secure_pin);

        classname = getIntent().getStringExtra("class");
        phnno = getIntent().getStringExtra("no");
        courty_id = getIntent().getStringExtra("id");
        tvGenerate=(TextView)findViewById(R.id.tvGenerate);

        getLocation();
        initializeViews();

    }

    public void getLocation() {

        gpsTracker = new GpsTracker(GenrateSecurePin.this);

        if (gpsTracker.canGetLocation())
        {
            longitude = gpsTracker.getLongitude();
            latitude = gpsTracker.getLatitude();

            Log.e("getLocation-->",latitude+" longii-->"+longitude);

        } else
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

    private void initializeViews()
    {
        txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        pin1 = (ImageView) findViewById(R.id.pin1);
        pin2 = (ImageView) findViewById(R.id.pin2);
        pin3 = (ImageView) findViewById(R.id.pin3);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
        pin4 = (ImageView) findViewById(R.id.pin4);
        if (txtPinEntry != null)
        {
            txtPinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length()==4)
                    {
                        text=str.toString();

                        // norefercode();
                        //callfunction();
                    }
                }

                @Override
                public void onTextChange(CharSequence str) {
                    switch(str.length())
                    {
                        case 0:
                            pin1.setImageResource(R.color.transparent);
                            pin2.setImageResource(R.color.transparent);
                            pin3.setImageResource(R.color.transparent);
                            pin4.setImageResource(R.color.transparent);
                            break;
                        case 1:
                            pin1.setImageResource(R.drawable.password);
                            break;
                        case 2:
                            pin2.setImageResource(R.drawable.password);
                            break;
                        case 3:
                            pin3.setImageResource(R.drawable.password);
                            break;
                        case 4:
                            pin4.setImageResource(R.drawable.password);
                            break;
                    }
                }
            });
        }
        this.num = new StringBuffer();
        this.num.append("");

        this.one = (LinearLayout) findViewById(R.id.one);
        this.two = (LinearLayout) findViewById(R.id.two);
        this.three = (LinearLayout) findViewById(R.id.three);
        this.four = (LinearLayout) findViewById(R.id.four);
        this.five = (LinearLayout) findViewById(R.id.five);
        this.six = (LinearLayout) findViewById(R.id.six);
        this.seven = (LinearLayout) findViewById(R.id.seven);
        this.eight = (LinearLayout) findViewById(R.id.eight);
        this.nine = (LinearLayout) findViewById(R.id.nine);
        this.zero = (LinearLayout)findViewById(R.id.zero);
        this.back = (LinearLayout) findViewById(R.id.back);
        this.ok = (LinearLayout)findViewById(R.id.ok);

        this.one.setOnClickListener(this);
        this.two.setOnClickListener(this);
        this.three.setOnClickListener(this);
        this.four.setOnClickListener(this);
        this.five.setOnClickListener(this);
        this.six.setOnClickListener(this);
        this.seven.setOnClickListener(this);
        this.eight.setOnClickListener(this);
        this.nine.setOnClickListener(this);
        this.zero.setOnClickListener(this);
        this.back.setOnClickListener(this);
        this.ok.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            new CountDownTimer(50000, 2000) {
                public void onTick(long millisUntilFinished) {
                    latitude=GpsTracker.latitude;
                    longitude=GpsTracker.longitude;

                    Log.e("lat-->",latitude+" longii-->"+longitude);
                }
                public void onFinish() {

                }

            }.start();
        }
    }


    public void norefercode()
    {
        SharedPreferences sharedPreferences = App.getIdPref();

        // alertDialog(this);

        JSONObject postParam = new JSONObject();
        try {
            // postParam.put("refer_code",refer_code);
            postParam.put("country_id", courty_id);
            postParam.put("phone", phnno);
            postParam.put("secure_pin", text);
            postParam.put("lat", String.valueOf(latitude));
            postParam.put("lng",String.valueOf(longitude));
            postParam.put("refer_code", getIntent().getStringExtra("refer"));
            postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
            postParam.put("device_id",Settings.Secure.getString(GenrateSecurePin.this.getContentResolver(), Settings.Secure.ANDROID_ID));


            if (Constant.isConnectingToInternet(GenrateSecurePin.this)) {
                new Retrofit2(GenrateSecurePin.this, GenrateSecurePin.this, postParam, Constant.REQ_SignUp, Constant.SignUp, "3").callService(true);
            } else {

                Constant.alertDialog(GenrateSecurePin.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void haverefercode()
    {
        SharedPreferences sharedPreferences = App.getIdPref();

        JSONObject postParam = new JSONObject();
        try {

            if(!ed_refral_code.getText().toString().equals("")) {
                postParam.put("refer_code", ed_refral_code.getText().toString().trim());
            }
            postParam.put("country_id", courty_id);
            postParam.put("phone", phnno);
            postParam.put("secure_pin", text);
            postParam.put("lat", String.valueOf(latitude));
            postParam.put("lng",String.valueOf(longitude));

            postParam.put("refer_code", getIntent().getStringExtra("refer"));

            postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
            postParam.put("device_id",Settings.Secure.getString(GenrateSecurePin.this.getContentResolver(), Settings.Secure.ANDROID_ID));


            if (Constant.isConnectingToInternet(GenrateSecurePin.this)) {
                new Retrofit2(GenrateSecurePin.this, GenrateSecurePin.this, postParam, Constant.REQ_SignUp, Constant.SignUp, "3").callService(true);
            } else {

                Constant.alertDialog(GenrateSecurePin.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(this.txtPinEntry.getText().toString().length()>4)
        {
            txtPinEntry.setText(null);
            num.delete(0, num.length());
        }

        switch (v.getId()) {



            case R.id.ivarrow:

                intent = new Intent(GenrateSecurePin.this, AfterSplash.class);
                startActivity(intent);

                break;

            case R.id.one:
                this.num.append("1");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.two:
                this.num.append("2");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.three:
                this.num.append("3");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.four:
                this.num.append("4");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.five:
                this.num.append("5");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.six:
                this.num.append("6");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.seven:
                this.num.append("7");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.eight:
                this.num.append("8");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.nine:
                this.num.append("9");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.back:
                if(num.length()>0)
                {
                    num.delete(num.length()-1, num.length());
                    this.txtPinEntry.setText(null);
                    for (int i=0;i<num.length();i++)
                    {
                        this.txtPinEntry.setText(num.substring(i));
                    }
                }
                return;
            case R.id.zero:
                this.num.append("0");
                this.txtPinEntry.setText(this.num);
                return;

            case R.id.ok:

                //secure_code = edfirst.getText().toString() + edSecond.getText().toString() + edthird.getText().toString() + edFouth.getText().toString();

                if (text.length() == 4) {
                    // norefercode();
                    if (flag==1)
                    {
                        secure_code = text;

                        txtPinEntry.setText(null);
                        num.delete(0, num.length());
                        //   text.equals("");
                        //  secure_code = secure_codeok;
                        Log.e("if",""+flag);
                        tvGenerate.setText("Confirm your 4-digit Secure PIN");
                        flag=2;
                    }

                    else
                    {
                        if (text.length() == 4) {
                            secure_codeok = text;

                            if(secure_code.equals(secure_codeok)){
                                norefercode();
                            }
                            else {
                                txtPinEntry.setText(null);
                                num.delete(0, num.length());
                                Constant.alertDialog(this, getResources().getString(R.string.secure_pin_do_not_match));
                            }

                            Log.e("else",""+flag);
                        }
                        else {
                            Constant.alertDialog(this, getResources().getString(R.string.please_enter_four_digit_secure_pin));
                        }

                    }
                }
                else {
                    Constant.alertDialog(this, getResources().getString(R.string.please_enter_four_digit_secure_pin));
                }

                break;

        }
    }

    public void alertDialog(Context context) {

        final Dialog dialog=new Dialog(GenrateSecurePin.this, R.style.StatisticsDialog);

        dialog.setTitle("MetaPay");
        dialog.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView2 = li.inflate(R.layout.refral_code_dialog, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button btnok=(Button)promptsView2.findViewById(R.id.btnokk);
        Button btncancel=(Button)promptsView2.findViewById(R.id.btncansel);
        ed_refral_code=(EditText)promptsView2.findViewById(R.id.ed_refral_code);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_refral_code.getText().toString().equals(""))
                {
                    Constant.alertDialog(GenrateSecurePin.this,"Please enter refer code");
                }
                else {
                    dialog.dismiss();
                    haverefercode();

                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                norefercode();

            }
        });
    }
    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

            case Constant.REQ_SignUp:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());

                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")) {

                            JSONObject result = result1.getJSONObject("data");


                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.country_code, result.getString("country_code"));
                            PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                            PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));

                            PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                            PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                            if(!result.getString("first_name").equals("null")){

                                PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name")+" "+result.getString("last_name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));
                                PreferenceFile.getInstance().saveData(this, Constant.City_name, result.getString("city"));


                                JSONObject StateName=result.getJSONObject("StateName");
                                PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));

                                Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.City_name));

                                JSONObject CountryName=result.getJSONObject("CountryName");
                                PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                Intent intent = new Intent(GenrateSecurePin.this, Dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }else {

                                finish();
                                Intent intent = new Intent(GenrateSecurePin.this, EditProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        } else {


                            Constant.alertWithIntent(GenrateSecurePin.this, message,AfterSplash.class);

                        }
                    } catch (JSONException e) {
                        Log.e("exception-->", e.toString());
                    } catch (IOException e) {

                    }

                    break;

                }
        }
    }
}