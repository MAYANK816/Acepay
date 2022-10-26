package com.acepay;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Widget.PinEntryEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener, RetrofitResponse {

    ImageView ivarrow;
    LinearLayout lnLayerforgot;
    String text="";
    TextView tvGenerate,tvattempts;
    int x=0;
    public static Boolean active=false;
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
    int flag=0;
    String text2="",text3="";
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        tvGenerate=(TextView)findViewById(R.id.tvGenerate);
        tvattempts = (TextView) findViewById(R.id.tvattempts);
        tvattempts.setText("3 attempts remaining.");

        ivarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        initializeViews();

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

    private void initializeViews()
    {
        txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        pin1 = (ImageView) findViewById(R.id.pin1);
        pin2 = (ImageView) findViewById(R.id.pin2);
        pin3 = (ImageView) findViewById(R.id.pin3);
        pin4 = (ImageView) findViewById(R.id.pin4);
        if (txtPinEntry != null)
        {
            txtPinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length()==4&flag==0)
                    {
                        text=str.toString();
                        if (text.equals(PreferenceFile.getInstance().getPreferenceData(ChangePassword.this, Constant.secure_pin)))
                        // if (text.equals("1234"))
                        {
                            Log.e("flag",flag+"");
                            flag=1;
                            txtPinEntry.setText(null);
                            num.delete(0, num.length());
                            tvGenerate.setText("Generate your New 4-digit Secure PIN");
                            tvattempts.setVisibility(View.INVISIBLE);


                        }
                        else {

                            x=x+1;
                            txtPinEntry.setText(null);
                            num.delete(0, num.length());
                            Constant.alertDialog(ChangePassword.this,getString(R.string.please_enter_correct_old_secure_pin));
                            if(x==2){
                                Log.e("count-->",x+"yes");
                                Constant.alertDialog(ChangePassword.this,
                                        "Warning! This is your Last attempts otherwise your account has been block.");
                                tvattempts.setText(1 + " attempt remaining. ");
                            }
                            else {
                                int number=3-x;
                                tvattempts.setText(number + " attempts remaining. ");
                            }

                            if (x==3)
                            {
                                JSONObject postParam = new JSONObject();
                                try {
                                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(ChangePassword.this, Constant.ID));
                                    postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(ChangePassword.this, Constant.phone));

                                    if (Constant.isConnectingToInternet(ChangePassword.this)) {
                                        new Retrofit2(ChangePassword.this, ChangePassword.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                                    }
                                    else {
                                        Constant.alertDialog(ChangePassword.this, getResources().getString(R.string.check_connection));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                    }
                    else if (str.length()==4&flag==1){
                        Log.e("flag",flag+"");
                        flag=2;
                        text2=str.toString();
                        txtPinEntry.setText(null);
                        num.delete(0, num.length());
                        tvGenerate.setText("Confirm your New 4-digit Secure PIN");
                        tvattempts.setVisibility(View.INVISIBLE);

                    }
                    else if (str.length()==4&flag==2){
                        Log.e("flag",flag+"");

                        text3=str.toString();

                        if (text3.equals(text2))
                        {
                            changepasswordService();

                        }
                        else {
                            txtPinEntry.setText(null);
                            num.delete(0, num.length());
                            Constant.alertDialog(ChangePassword.this,getString(R.string.secure_pin_do_not_match));
                        }



                    }
                    else
                    {
                        Constant.alertDialog(ChangePassword.this,"you entered wrong pin");
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

    public void onClick(View view) {
        if(this.txtPinEntry.getText().toString().length()>4)
        {
            txtPinEntry.setText(null);
            num.delete(0, num.length());
        }

        switch (view.getId())
        {

          /*  case R.id.tvforgot:

                Intent intent1=new Intent(LoginNew.this,ForgotPin.class);
                startActivity(intent1);
                return;*/

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
                Log.e("flagok",flag+"");
                if(text.length()<4)
                {
                    Constant.alertDialog(this, getResources().getString(R.string.please_enter_four_digit_secure_pin));
                }
                else if (text2.equals(""))
                {
                    Log.e("second","yes");
                    // if (text2.length()<4){
                    Constant.alertDialog(this, getResources().getString(R.string.please_enter_four_digit_secure_pin));
                    Log.e("second","enter");
                    // }

                }
                else if (text3.equals(""))
                {
                    Log.e("third","yes");
                    // if (text3.length()<4){
                    Constant.alertDialog(this, getResources().getString(R.string.please_enter_four_digit_secure_pin));
                    Log.e("third","enter");
                    // }

                }

                return;
            default:
                return;
        }
    }
    public void changepasswordService(){
        JSONObject postParam = new JSONObject();
        try {
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(ChangePassword.this, Constant.ID));
            // postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this,Constant.ID));
            postParam.put("pin", text);
            postParam.put("new_pin", text3);

            if (Constant.isConnectingToInternet(ChangePassword.this)) {
                new Retrofit2(this, ChangePassword.this, postParam, Constant.REQ_EDIT_SECURE_PIN, Constant.EDIT_SECURE_PIN, "3").callService(true);
            } else {

                Constant.alertDialog(ChangePassword.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                        //Constant.alertDialog(this,message);
                        PreferenceFile.getInstance().saveData(this,Constant.secure_pin,text3);
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
            case Constant.REQ_Block_USER:

                if (response.isSuccessful()) {

                    JSONObject result1 = null;
                    try {
                        result1 = new JSONObject(response.body().string());

                        String Bastatus = result1.getString("response");
                        String message = result1.getString("message");

                        PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"4");
                        PreferenceFile.getInstance().saveData(this,Constant.Accunt_status,"Inactive");

                        Constant.alertWithIntent(this,"Your Account has been Blocked.",BlockScreen.class);


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
